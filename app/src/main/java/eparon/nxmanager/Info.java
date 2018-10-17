package eparon.nxmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    String versionName = BuildConfig.VERSION_NAME; //Get App version.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Set app version.
        TextView VersionLine = findViewById(R.id.version);
        VersionLine.setText(versionName);
    }
}
