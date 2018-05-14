package boulet.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aboulet on 05/03/2018.
 */

public class BuildingDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRetourBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_details_activity);

        btnRetourBuilding = findViewById(R.id.btnRetourBuilding);
        btnRetourBuilding.setOnClickListener(this);

        String jsonBuilding = getIntent().getStringExtra("building");
        Gson json = new Gson();
        Building building = json.fromJson(jsonBuilding, Building.class);
        final BuildDetailsFragment fragB = (BuildDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragDetails);
        fragB.fillContent(building);
    }

    @Override
    public void onClick(View v) {
        Intent BuildingIntent = new Intent(getApplicationContext(), BuildingActivity.class);
        startActivity(BuildingIntent);
    }
}
