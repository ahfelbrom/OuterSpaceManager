package boulet.com.outerspacemanager.outerspacemanager.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import boulet.com.outerspacemanager.outerspacemanager.Adapter.RecyclerViewAdapter;
import boulet.com.outerspacemanager.outerspacemanager.Model.Api;
import boulet.com.outerspacemanager.outerspacemanager.R;
import boulet.com.outerspacemanager.outerspacemanager.Model.Reports;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChantierActivity extends AppCompatActivity {

    private TextView txtChantier;
    private RecyclerView lvReports;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chantier);
      
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        lvReports = findViewById(R.id.lvReports);

        lvReports.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        lvReports.setLayoutManager(mLayoutManager);


        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Reports> request = service.GetReports(token, 0, 20);
        request.enqueue(new Callback<Reports>() {
            @Override
            public void onResponse(Call<Reports> call, Response<Reports> response) {
                switch (response.code())
                {
                    case 200:
                        Reports reports = response.body();
                        // specify an adapter (see also next example)
                        mAdapter = new RecyclerViewAdapter(reports);
                        lvReports.setAdapter(mAdapter);
                        //lvReports.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, reports.getReports()));
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
            public void onFailure(Call<Reports> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}