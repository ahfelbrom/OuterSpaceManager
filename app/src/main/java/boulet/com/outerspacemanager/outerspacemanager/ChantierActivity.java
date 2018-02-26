package boulet.com.outerspacemanager.outerspacemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChantierActivity extends AppCompatActivity {

    private TextView txtChantier;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chantier);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_chantier);
    }
}