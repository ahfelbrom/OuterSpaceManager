package boulet.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VaisseauActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listShips;
    private Ship[] flotte;
    private TextView tvTitleFlotte;
    private Button btnReturnFleet;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ship);

        btnReturnFleet = findViewById(R.id.btnReturnFleet);
        btnReturnFleet.setOnClickListener(this);

        tvTitleFlotte = findViewById(R.id.tvTitleFlotte);
        tvTitleFlotte.setText("Ajouter un vaisseau");

        listShips = findViewById(R.id.listViewFlotte);
        listShips.setOnItemClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Ships> request = service.GetAllShips(token);

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
        Intent intentToFleet = new Intent(getApplicationContext(), FlotteActivity.class);
        startActivity(intentToFleet);
    }
}
