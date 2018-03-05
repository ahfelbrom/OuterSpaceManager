package boulet.com.outerspacemanager.outerspacemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aboulet on 05/03/2018.
 */

public class BuildingDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_details_activity);
        String texteAAfficher= getIntent().getStringExtra("monTextAAfficher");
        BuildDetailsFragment fragB = (BuildDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragDetails);
        fragB.fillTextView(texteAAfficher); }
}
