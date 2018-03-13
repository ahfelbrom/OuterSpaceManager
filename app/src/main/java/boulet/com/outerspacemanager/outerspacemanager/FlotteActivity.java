package boulet.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Ships> request = service.GetShips(token);

        request.enqueue(new Callback<Ships>() {
            @Override
            public void onResponse(Call<Ships> call, Response<Ships> response) {
                if(response.code() != 200){
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    flotte = response.body().getShips();
                    if (flotte.length == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Pas de ships :'(", Toast.LENGTH_LONG).show();
                    }
                    FlotteAdapter adapter = new FlotteAdapter(getApplicationContext(), flotte );
                    listShips.setAdapter(adapter);
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

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
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
                        Toast.makeText(getApplicationContext(), er.getMessage(), Toast.LENGTH_LONG).show();
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