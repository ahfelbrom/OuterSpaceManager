package boulet.com.outerspacemanager.outerspacemanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuildingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {


    private ListView listBuilding;
    private Button btnMenuBat;
    private Building[] buildings;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        listBuilding = (ListView) findViewById(R.id.lvFragList);
        btnMenuBat = findViewById(R.id.btnMenuBat);
        btnMenuBat.setOnClickListener(this);

        settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        this.loadBuildings();
    }

    private void loadBuildings() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Buildings> request = service.GetBuildings(token);

        request.enqueue(new Callback<Buildings>() {
            @Override
            public void onResponse(Call<Buildings> call, Response<Buildings> response) {
                if(response.code() != 200){
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                }
                switch (response.code())
                {
                    case 200:
                        buildings = response.body().getBuildings();
                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, buildings);
                        listBuilding.setAdapter(adapter);
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
                        Toast.makeText(getApplicationContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<Buildings> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BuildListFragment fragA = (BuildListFragment) getSupportFragmentManager().findFragmentById(R.id.fragList);
        BuildDetailsFragment fragB = (BuildDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.fragDetails);
        if(fragB == null || !fragB.isInLayout())
        {
            Intent i = new Intent(getApplicationContext(), BuildingDetailsActivity.class);
            i.putExtra("monTextAAfficher","coucou");
            Gson json = new Gson();
            String jsonBuilding = json.toJson(buildings[position]);
            i.putExtra("building", jsonBuilding);
            startActivity(i);
        }
        else
        {
            fragB.fillContent(buildings[position]);
            fragB.updateProgressBar();
        }
    }

    @Override
    public void onClick(View v) {
        Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainIntent);
    }
}