package boulet.com.outerspacemanager.outerspacemanager;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bouleta on 26/02/2018.
 */
public class UserAdapter extends ArrayAdapter<UserResponse> {

    private final Context context;
    private final UserResponse[] values;
    public UserAdapter(Context context, UserResponse[] values) {
        super(context,R.layout.activity_galaxy, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_user, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelUser);

        textView.setText(values[position].toString());

        return rowView;
    }
}