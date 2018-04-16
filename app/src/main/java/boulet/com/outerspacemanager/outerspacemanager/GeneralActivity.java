package boulet.com.outerspacemanager.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeneralActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtInfos;
    private TextView tvGasShow;
    private TextView tvMineralShow;
    private Button btnMenuGeneral;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private Timer timer;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        txtInfos = (TextView) findViewById(R.id.txtInfos);
        tvGasShow = findViewById(R.id.tvGasShow);
        tvMineralShow = findViewById(R.id.tvMineralShow);
        btnMenuGeneral = findViewById(R.id.btnMenuGeneral);
        btnMenuGeneral.setOnClickListener(this);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        timer = new Timer();
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service = retrofit.create(Api.class);
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Call<UserResponse> request = service.GetUserInfo(token);

                request.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.code() != 200){
                            Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            UserResponse user = response.body();
                            String toDisplay = user.getUsername() + " (" + user.getPoints() + " points)\n";
                            tvGasShow.setText(Math.round(Double.parseDouble(user.getGas()))+"");
                            tvMineralShow.setText(Math.round(Double.parseDouble(user.getMinerals()))+"");
                            txtInfos.setText(toDisplay);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }},0,1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
    }

    @Override
    public void onClick(View v) {
        Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainIntent);
    }
}