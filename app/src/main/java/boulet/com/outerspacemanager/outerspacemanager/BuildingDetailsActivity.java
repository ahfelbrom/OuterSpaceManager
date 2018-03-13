package boulet.com.outerspacemanager.outerspacemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

/**
 * Created by aboulet on 05/03/2018.
 */

public class BuildingDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_details_activity);
        String texteAAfficher = getIntent().getStringExtra("monTextAAfficher");
        String jsonBuilding = getIntent().getStringExtra("building");
        Gson json = new Gson();
        Building building = json.fromJson(jsonBuilding, Building.class);
        BuildDetailsFragment fragB = (BuildDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragDetails);
        fragB.fillTextView(texteAAfficher);
        fragB.fillContent(building);
    }
}
