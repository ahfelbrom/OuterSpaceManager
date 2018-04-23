package boulet.com.outerspacemanager.outerspacemanager;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
    private ProgressBar pbPercentBuild;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private Building building;
    private Long timeStartBuilding;
    private SharedPreferences settings;
    private Timer time;
    private boolean canceled = false;

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
        pbPercentBuild = v.findViewById(R.id.pbPercentBuild);
        btnBuild.setOnClickListener(this);
        settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        time = new Timer();

        return v;
    }

    public void fillTextView(String text)
    {
        tvNameBuilding.setText(text);
    }

    public void fillContent(Building building)
    {
        this.building = building;
        timeStartBuilding = settings.getLong("timeStartConstruction"+building.getName(), 0);

        DownLoadImageTask dl = new DownLoadImageTask(ivBuildingDetails);
        dl.execute(building.getImageUrl());
        tvNameBuilding.setText(building.getName());
        tvLevelBuilding.setText("Level : " + building.getLevel());
        tvTimeToBuild.setText("Temps de build : " + building.getTimeBuildingMin().toString() + ":" + building.getTimeBuildingSec().toString() + " minutes");
        tvMineralCost.setText(building.getMineralCost().toString() + " minéraux");
        tvGasCost.setText(building.getGasCost().toString() + " gaz");
        tvAmountOfEffect.setText("Bonus : +" + building.getAmountEffect() + " ");
        if (building.getEffect() != null && building.getEffect() != "") {
            String resource = getResources().getString( getResources().getIdentifier(building.getEffect(), "string", getActivity().getPackageName()));
            tvEffect.setText(resource);
        } else {
            tvEffect.setText("Ressources");
        }
        if (building.isBuilding())
        {
            tvTimeToBuild.append(" (en construction)");
            btnBuild.setText("En construction");
            btnBuild.setEnabled(false);
            if (timeStartBuilding != 0) {
                this.updateProgressBar();
            }
        }
        else
        {
            btnBuild.setText("Construire");
            btnBuild.setEnabled(true);
        }
        btnBuild.setVisibility(View.VISIBLE);
        tvCost.setVisibility(View.VISIBLE);
    }

    public void updateProgressBar()
    {
        if (this.building.isBuilding())
        {
            if (canceled) {
                time = new Timer();
            }
            time.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    if (getActivity()!=null){
                        Log.d("dsfds", "Timer execution" + time.toString());
                        pbPercentBuild.setMax(BuildDetailsFragment.this.building.getTimeBuilding());
                        Long timeNow = new Date().getTime() / 1000;
                        final Long difference = timeNow - timeStartBuilding;
                        Double percent = Double.parseDouble(difference + "") / Double.parseDouble(BuildDetailsFragment.this.building.getTimeBuilding() + "");
                        if (percent < 1) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbPercentBuild.setProgress(Integer.parseInt(difference + ""));
                                    tvTimeToBuild.setText("Temps restant : " + (building.getTimeBuilding() - difference)/60 + ":" + (building.getTimeBuilding() - difference)%60 + " minutes");
                                    pbPercentBuild.setVisibility(View.VISIBLE);
                                }
                            });

                        } else {
                            time.cancel();
                            canceled = true;
                            time.purge();
                            Log.d("dsfds", "sfdsffdssfdsfddsf");
                            BuildDetailsFragment.this.refreshBuild();
                            settings.edit().remove("timeStartConstruction"+building.getName()).apply();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BuildDetailsFragment.this.fillContent(BuildDetailsFragment.this.building);
                                    pbPercentBuild.setVisibility(View.INVISIBLE);
                                    btnBuild.setText("Construire");
                                    btnBuild.setEnabled(true);
                                }
                            });
                        }
                    }
                }
            },0,1000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        time.cancel();
        canceled = true;
    }

    private void refreshBuild()
    {
        Integer lvl = Integer.parseInt(this.building.getLevel())+1;
        this.building.setLevel(lvl+"");
        this.building.setBuilding(false+"");
    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.CreateBuilding(token, building.getBuildingId());
        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                switch (response.code())
                {
                    case 200:
                        Toast.makeText(getContext(), "La construction à commencé !", Toast.LENGTH_LONG).show();
                        Date dateCreation = new Date();
                        Long timeConstruct = dateCreation.getTime()/1000;
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putLong("timeStartConstruction"+building.getName(), timeConstruct);
                        editor.commit();
                        building.setBuilding("true");
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
                        Toast.makeText(getContext(), er.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    case 403 :
                        Toast.makeText(getContext(), "Veuillez vous réauthentifier s'il vous plait", Toast.LENGTH_LONG).show();
                        settings.edit().remove("token").apply();
                        Intent myIntent = new Intent(getContext(), SignUpActivity.class);
                        startActivity(myIntent);
                        break;
                    case 404 :
                        Toast.makeText(getContext(), "Comment as tu fait pour changer mes valeurs ? Oo", Toast.LENGTH_LONG).show();
                        break;
                    case 500 :
                        Toast.makeText(getContext(), "Problème interne de l'API, réessayez plus tard...", Toast.LENGTH_LONG).show();
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
