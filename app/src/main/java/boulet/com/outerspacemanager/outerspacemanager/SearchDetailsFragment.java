package boulet.com.outerspacemanager.outerspacemanager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchDetailsFragment extends Fragment implements View.OnClickListener {
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private Search search;
    private Integer index;
    private Button btnBuildSearch;
    private TextView tvName;
    private TextView tvAmountOfEffect;
    private TextView tvEffect;
    private TextView tvGazCost;
    private TextView tvMineralCost;
    private TextView tvTimeToBuild;
    private TextView tvCost;
    private TextView tvLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_search_details,container);
        SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        btnBuildSearch = v.findViewById(R.id.btnBuildSearch);
        btnBuildSearch.setOnClickListener(this);
        tvName = v.findViewById(R.id.tvName);
        tvLevel = v.findViewById(R.id.tvLevel);
        tvAmountOfEffect = v.findViewById(R.id.tvAmountOfEffect);
        tvEffect = v.findViewById(R.id.tvEffect);
        tvGazCost = v.findViewById(R.id.tvGazCost);
        tvCost = v.findViewById(R.id.tvCost);
        tvMineralCost = v.findViewById(R.id.tvMineralCost);
        tvTimeToBuild = v.findViewById(R.id.tvTimeToBuild);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public void fillContent(Search search, Integer index) {
        this.search = search;
        this.index = index;
        tvName.setText(search.getName());
        tvEffect.setText(search.getEffect());
        tvLevel.setText("level " + search.getLevel());
        tvAmountOfEffect.setText(search.getAmountEffect().toString() + " ");
        tvGazCost.setText(search.getGasCost().toString() + " gaz");
        tvMineralCost.setText(search.getMineralCost().toString() + " minéraux");
        tvTimeToBuild.setText(search.getTimeBuildingMin() + ":" + search.getTimeBuildingSec() + "min");
        if (search.isBuilding())
        {
            tvTimeToBuild.append(" (en recherche)");
            btnBuildSearch.setEnabled(false);
            btnBuildSearch.setText("En recherche");
        }
        else
        {
            btnBuildSearch.setEnabled(true);
            btnBuildSearch.setText("Rechercher");
        }
        btnBuildSearch.setVisibility(View.VISIBLE);
        tvCost.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        final Search search = this.search;
        //Toast.makeText(getApplicationContext(), search.getName(), Toast.LENGTH_LONG).show();

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.StartSearchesForUser(token, Integer.toString(this.index));

        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if(response.code() != 200){
                    if(search.getBuilding().equals("true"))
                        Toast.makeText(getContext(), "La recherche n'est pas terminée !", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Vous n'avez pas assez de ressources !", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "La recherche à commencé !", Toast.LENGTH_LONG).show();

                    Double time =  Double.parseDouble(search.getTimeToBuildLevel0()) + ( Double.parseDouble(search.getTimeToBuildByLevel()) * Double.parseDouble(search.getLevel()));
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
