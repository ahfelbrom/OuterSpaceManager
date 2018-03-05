package boulet.com.outerspacemanager.outerspacemanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by aboulet on 05/03/2018.
 */

public class BuildDetailsFragment extends Fragment {
    private TextView tvNameBuilding;
    private TextView tvLevelBuilding;
    private TextView tvIsBuilding;
    private TextView tvMineralCost;
    private TextView tvGasCost;
    private TextView tvTimeToBuild;
    private TextView tvAmountOfEffect;
    private TextView tvEffect;
    private ImageView ivBuildingDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_build_details,container);
        tvNameBuilding = v.findViewById(R.id.tvNameBuilding);
        tvLevelBuilding = v.findViewById(R.id.tvLevelBuilding);
        tvIsBuilding = v.findViewById(R.id.tvIsBuilding);
        tvMineralCost = v.findViewById(R.id.tvMineralCost);
        tvGasCost = v.findViewById(R.id.tvGasCost);
        tvTimeToBuild = v.findViewById(R.id.tvTimeToBuild);
        tvAmountOfEffect = v.findViewById(R.id.tvAmountOfEffect);
        tvEffect = v.findViewById(R.id.tvEffect);
        ivBuildingDetails = v.findViewById(R.id.ivBuildingDetails);
        return v;
    }

    public void fillTextView(String text)
    {
        tvNameBuilding.setText(text);
    }

    public void toastBuilding(Building building)
    {
        Toast.makeText(getContext(), building.toCompleteString(), Toast.LENGTH_LONG).show();
        // DownLoadImageTask dl = new DownLoadImageTask();
    }
}
