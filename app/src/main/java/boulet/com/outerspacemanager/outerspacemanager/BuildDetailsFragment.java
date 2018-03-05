package boulet.com.outerspacemanager.outerspacemanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aboulet on 05/03/2018.
 */

public class BuildDetailsFragment extends Fragment {
    private TextView tvNameBuilding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_build_details,container);
        tvNameBuilding = v.findViewById(R.id.tvNameBuilding);
        return v;
    }

    public void fillTextView(String text)
    {
        tvNameBuilding.setText(text);
    }
}
