package boulet.com.outerspacemanager.outerspacemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalaxyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private UserResponse[] listUsers;
    private ListView viewUsers;
    private Button btnBackMenu;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy);
        viewUsers = (ListView) findViewById(R.id.listViewGalaxy);
        viewUsers.setOnItemClickListener(this);

        btnBackMenu = findViewById(R.id.btnBackMenu);
        btnBackMenu.setOnClickListener(this);
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<UserTable> request = service.GetUsers(token);

        request.enqueue(new Callback<UserTable>() {
            @Override
            public void onResponse(Call<UserTable> call, Response<UserTable> response) {
                if(response.code() != 200){
                        Toast.makeText(getApplicationContext(), "Veuillez vous réauthentifier s'il vous plait", Toast.LENGTH_LONG).show();
                        settings.edit().remove("token").apply();
                        Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                        startActivity(myIntent);
                }else{
                    listUsers = response.body().getUsers();

                    UserAdapter adapter = new UserAdapter(getApplicationContext(), listUsers );
                    viewUsers.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<UserTable> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent MainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final UserResponse userToAttack = listUsers[position];
        AlertDialog.Builder builder = new AlertDialog.Builder(GalaxyActivity.this);
        builder.setMessage("Voulez-vous attaquer " + listUsers[position].getUsername() + " ?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent myIntent = new Intent(getApplicationContext(), AttackActivity.class);
                        Gson json = new Gson();
                        String jsonString = json.toJson(userToAttack);
                        myIntent.putExtra("USER_TO_ATTACK", jsonString);
                        startActivity(myIntent);
                    }
                });

        builder.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}