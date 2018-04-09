package boulet.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

/**
 * Created by aboulet on 05/03/2018.
 */

public class SearchDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRetourSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_details_activity);

        btnRetourSearch = findViewById(R.id.btnRetourSearch);
        btnRetourSearch.setOnClickListener(this);

        String jsonSearch = getIntent().getStringExtra("search");
        Integer index = getIntent().getIntExtra("index", 0);
        Gson json = new Gson();
        Search search = json.fromJson(jsonSearch, Search.class);
        SearchDetailsFragment fragB = (SearchDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragSearchDetails);
        fragB.fillContent(search, index);
    }

    @Override
    public void onClick(View v) {
        Intent SearchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(SearchIntent);
    }
}
