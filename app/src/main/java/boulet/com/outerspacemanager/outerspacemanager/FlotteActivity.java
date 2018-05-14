package boulet.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlotteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listShips;
    private Button btnAddShip;
    private Button btnReturnMenu;
    private Ship[] flotte;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flotte);

        listShips = (ListView) findViewById(R.id.listViewFlotte);
        listShips.setOnItemClickListener(this);

        btnAddShip = (Button) findViewById(R.id.btnAddShip);
        btnAddShip.setOnClickListener(this);

        btnReturnMenu = findViewById(R.id.btnReturnMenu);
        btnReturnMenu.setOnClickListener(this);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Ships> request = service.GetShips(token);

        request.enqueue(new Callback<Ships>() {
            @Override
            public void onResponse(Call<Ships> call, Response<Ships> response) {
                switch (response.code())
                {
                    case 200:
                        flotte = response.body().getShips();
                        if (flotte.length == 0)
                        {
                            Toast.makeText(getApplicationContext(), "Pas de vaisseaux, pas de chocolat !", Toast.LENGTH_LONG).show();
                        }
                        FlotteAdapter adapter = new FlotteAdapter(getApplicationContext(), flotte );
                        listShips.setAdapter(adapter);
                        break;
                    case 401 :
                        Toast.makeText(getApplicationContext(), "Il va falloir se réauthentifier, désolé ^^'", Toast.LENGTH_SHORT).show();
                        break;
                    case 403 :
                        Toast.makeText(getApplicationContext(), "Veuillez vous réauthentifier s'il vous plait", Toast.LENGTH_LONG).show();
                        settings.edit().remove("token").apply();
                        Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                        startActivity(myIntent);
                        break;
                    case 500 :
                        Toast.makeText(getApplicationContext(), "Problème interne de l'API, réessayez plus tard...", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Ships> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Ship ship = flotte[position];
        ship.setAmount("1");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.CreateShips(token, ship, ship.getShipId());
        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                switch (response.code()) {
                    case 200 :
                        Toast.makeText(getApplicationContext(), "La construction à commencée !", Toast.LENGTH_LONG).show();
                        break;
                    case 401 :
                        String res = "";
                        try {
                            res = response.errorBody().string();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        ErrorResponse er = gson.fromJson(res, ErrorResponse.class);
                        switch (er.getInternalCode())
                        {
                            case "invalid_request":
                                Toast.makeText(getApplicationContext(), "Requête invalide, c'est pas bien de casser le code :c", Toast.LENGTH_LONG).show();
                                break;
                            case "not_enough_resources":
                                Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources pour construire le vaisseau demandé", Toast.LENGTH_LONG).show();
                                break;
                            case "insufficient_spaceport_level":
                                Toast.makeText(getApplicationContext(), "Il va falloir augmenter le niveau du bâtiment spacioport pour avoir un vaisseau comme cela", Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;
                    case 404 :
                        Toast.makeText(getApplicationContext(), "N'essaie pas de casser mon code pls :'(", Toast.LENGTH_SHORT).show();
                        break;
                    case 500 :
                        Toast.makeText(getApplicationContext(), "Erreur interne à l'API. Réessayez plus tard...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAddShip.getId())
        {
            Intent addShipIntent = new Intent(getApplicationContext(), VaisseauActivity.class);
            startActivity(addShipIntent);
        }
        else if (v.getId() == btnReturnMenu.getId())
        {
            Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MainIntent);
        }
    }
}