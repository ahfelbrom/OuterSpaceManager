package boulet.com.outerspacemanager.outerspacemanager.Activity;

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

import boulet.com.outerspacemanager.outerspacemanager.Model.Api;
import boulet.com.outerspacemanager.outerspacemanager.Model.Search;
import boulet.com.outerspacemanager.outerspacemanager.R;
import boulet.com.outerspacemanager.outerspacemanager.Adapter.SearchAdpater;
import boulet.com.outerspacemanager.outerspacemanager.Fragments.SearchDetailsFragment;
import boulet.com.outerspacemanager.outerspacemanager.Fragments.SearchListFragment;
import boulet.com.outerspacemanager.outerspacemanager.Model.Searches;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listSearch;
    private Button btnReturnMenu;
    private Search[] searches;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listSearch = (ListView) findViewById(R.id.listViewSearch);
        listSearch.setOnItemClickListener(this);

        btnReturnMenu = findViewById(R.id.btnReturnMenu);
        btnReturnMenu.setOnClickListener(this);
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Searches> request = service.GetSearchesForUser(token);

        request.enqueue(new Callback<Searches>() {
            @Override
            public void onResponse(Call<Searches> call, Response<Searches> response) {
                switch (response.code())
                {
                    case 200:
                        searches = response.body().getSearches();
                        SearchAdpater adapter = new SearchAdpater(getApplicationContext(), searches );
                        listSearch.setAdapter(adapter);
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
            public void onFailure(Call<Searches> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchListFragment fragA = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.fragSearchList);
        SearchDetailsFragment fragB = (SearchDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.fragSearchDetails);
        if(fragB == null || !fragB.isInLayout())
        {
            Intent i = new Intent(getApplicationContext(), SearchDetailsActivity.class);
            Gson json = new Gson();
            String jsonSearch = json.toJson(searches[position]);
            i.putExtra("search", jsonSearch);
            i.putExtra("index", position);
            startActivity(i);
        }
        else
        {
            fragB.fillContent(searches[position], position);
        }
    }

    @Override
    public void onClick(View v) {
        Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainIntent);
    }
}