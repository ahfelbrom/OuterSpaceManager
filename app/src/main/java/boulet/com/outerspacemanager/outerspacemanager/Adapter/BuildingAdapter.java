package boulet.com.outerspacemanager.outerspacemanager.Adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import boulet.com.outerspacemanager.outerspacemanager.Model.Building;
import boulet.com.outerspacemanager.outerspacemanager.R;


/**
 * Created by atison on 23/01/2018.
 */

public class BuildingAdapter extends ArrayAdapter<Building> {

    private final Context context;
    private final Building[] values;
    public BuildingAdapter(Context context, Building[] values) {
        super(context, R.layout.activity_galaxy, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_building, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelBuilding);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconBuilding);

        textView.setText(values[position].toString());

        new DownLoadImageTask(imageView).execute(values[position].getImageUrl());

        return rowView;
    }
}