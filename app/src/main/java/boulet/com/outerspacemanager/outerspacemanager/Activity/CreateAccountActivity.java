package boulet.com.outerspacemanager.outerspacemanager.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import boulet.com.outerspacemanager.outerspacemanager.Model.ErrorResponse;
import boulet.com.outerspacemanager.outerspacemanager.Model.Api;
import boulet.com.outerspacemanager.outerspacemanager.Model.AuthResponse;
import boulet.com.outerspacemanager.outerspacemanager.R;
import boulet.com.outerspacemanager.outerspacemanager.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnValider;
    private EditText inputEmail;
    private EditText inputUsername;
    private EditText inputPassword;
    private Button btnConnect;
    private Activity thisActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnValider = (Button) findViewById(R.id.btnValider);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputEmail = (EditText) findViewById(R.id.inputEmail);

        btnValider.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnValider.getId())
        {
            Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
            Api service = retrofit.create(Api.class);
            Call<AuthResponse> request = service.CreateAccount(new User(inputUsername.getText().toString(), inputPassword.getText().toString(), inputEmail.getText().toString()));
            request.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    switch (response.code())
                    {
                        case 200:
                            Toast.makeText(getApplicationContext(), "Votre compte a bien été créé !", Toast.LENGTH_LONG).show();
                            break;
                        case 400 :
                            String raiponce = "";
                            try {
                                raiponce = response.errorBody().string();
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            Gson gson = new Gson();
                            ErrorResponse er = gson.fromJson(raiponce, ErrorResponse.class);
                            switch (er.getInternalCode())
                            {
                                case "already_registered_username":
                                    Toast.makeText(getApplicationContext(), "L'identifiant existe déjà, il va falloir être un peu plus créatif l'ami", Toast.LENGTH_SHORT).show();
                                    break;
                                case "already_registered_email":
                                    Toast.makeText(getApplicationContext(), "L'email entré existe déjà, tu as peut-être déjà un compte chez OSMEntreprises", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            break;
                        case 401 :
                            String message;
                            if (inputPassword.getText().length() == 0 && inputEmail.getText().length() != 0 && inputUsername.getText().length() != 0)
                            {
                                message = "Problème interne à l'application, veuileez réessayer plus tard...";
                            }
                            else
                            {
                                message = "Il manque ";
                                if (inputUsername.getText().length() == 0)
                                {
                                    message += "votre nom d'utilisateur ";
                                }
                                if (inputEmail.getText().length() == 0)
                                {
                                    message += "votre mail ";
                                }
                                if (inputPassword.getText().length() == 0)
                                {
                                    message += "votre mot de passe";
                                }
                            }
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            break;
                        case 500 :
                            Toast.makeText(getApplicationContext(), "Problème interne de l'API, réessayez plus tard...", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(myIntent);
        }
    }
}