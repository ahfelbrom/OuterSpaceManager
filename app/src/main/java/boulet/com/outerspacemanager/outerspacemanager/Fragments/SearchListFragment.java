package boulet.com.outerspacemanager.outerspacemanager.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import boulet.com.outerspacemanager.outerspacemanager.Activity.SearchActivity;
import boulet.com.outerspacemanager.outerspacemanager.R;


public class SearchListFragment extends Fragment {
    private ListView listViewSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_search_list,container);
        listViewSearch = v.findViewById(R.id.listViewSearch);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        listViewSearch.setOnItemClickListener((SearchActivity)getActivity());
    }
}
