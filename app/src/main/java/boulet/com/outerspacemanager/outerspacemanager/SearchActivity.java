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
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Searches> request = service.GetSearchesForUser(token);

        request.enqueue(new Callback<Searches>() {
            @Override
            public void onResponse(Call<Searches> call, Response<Searches> response) {
                if(response.code() != 200){
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    searches = response.body().getSearches();
                    //Toast.makeText(getApplicationContext(), buildings.toString(), Toast.LENGTH_LONG).show();

                    //listSearch.setAdapter(new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_list_item_1, searches));

                    SearchAdpater adapter = new SearchAdpater(getApplicationContext(), searches );
                    listSearch.setAdapter(adapter);
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