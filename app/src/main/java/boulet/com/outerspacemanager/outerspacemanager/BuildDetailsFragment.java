package boulet.com.outerspacemanager.outerspacemanager;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by aboulet on 05/03/2018.
 */

public class BuildDetailsFragment extends Fragment implements View.OnClickListener {
    private TextView tvNameBuilding;
    private TextView tvLevelBuilding;
    private TextView tvMineralCost;
    private TextView tvGasCost;
    private TextView tvTimeToBuild;
    private TextView tvAmountOfEffect;
    private TextView tvEffect;
    private TextView tvCost;
    private ImageView ivBuildingDetails;
    private Button btnBuild;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private Building building;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_build_details,container);
        tvNameBuilding = v.findViewById(R.id.tvNameBuilding);
        tvLevelBuilding = v.findViewById(R.id.tvLevelBuilding);
        tvMineralCost = v.findViewById(R.id.tvMineralCost);
        tvGasCost = v.findViewById(R.id.tvGasCost);
        tvTimeToBuild = v.findViewById(R.id.tvTimeToBuild);
        tvAmountOfEffect = v.findViewById(R.id.tvAmountOfEffect);
        tvEffect = v.findViewById(R.id.tvEffect);
        tvCost = v.findViewById(R.id.tvCost);
        ivBuildingDetails = v.findViewById(R.id.ivBuildingDetails);
        btnBuild = v.findViewById(R.id.btnBuild);
        btnBuild.setOnClickListener(this);
        SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        return v;
    }

    public void fillTextView(String text)
    {
        tvNameBuilding.setText(text);
    }

    public void fillContent(Building building)
    {
        this.building = building;
        DownLoadImageTask dl = new DownLoadImageTask(ivBuildingDetails);
        dl.execute(building.getImageUrl());
        tvNameBuilding.setText(building.getName());
        tvLevelBuilding.setText("Level : " + building.getLevel());
        tvTimeToBuild.setText("Temps de build : " + building.getTimeBuildingMin().toString() + ":" + building.getTimeBuildingSec().toString() + " minutes");
        if (building.isBuilding())
        {
            tvTimeToBuild.append(" (en construction)");
        }
        tvMineralCost.setText(building.getMineralCost().toString() + " minéraux");
        tvGasCost.setText(building.getGasCost().toString() + " gaz");
        tvAmountOfEffect.setText("Bonus : +" + building.getAmountEffect() + " ");
        tvEffect.setText(building.getEffect());
        if (building.isBuilding())
        {
            btnBuild.setText("En construction");
            btnBuild.setEnabled(false);
        }
        else
        {
            btnBuild.setText("Construire");
            btnBuild.setEnabled(true);
        }
        btnBuild.setVisibility(View.VISIBLE);
        tvCost.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.CreateBuilding(token, building.getBuildingId());
        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if(response.code() != 200){
                    if(building.getBuilding().equals("true")) {
                        Toast.makeText(getContext(), "La construction n'est pas terminée !", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Vous n'avez pas assez de ressources !", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "La construction à commencé !", Toast.LENGTH_LONG).show();
                    building.setBuilding("true");
                }
            }
            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(getContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
