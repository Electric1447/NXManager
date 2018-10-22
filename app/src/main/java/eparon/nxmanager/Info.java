package eparon.nxmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    public String PREFS = "PrefsFile";

    int COLOR_WHITE = 0xFFFFFFFF;
    int COLOR_BLACK = 0xFF000000;

    String githubURL = "https://github.com/Electric1447/NXManager";

    boolean darkTheme;

    String versionName = BuildConfig.VERSION_NAME; //Get App version.

    TextView VersionLine;

    CoordinatorLayout icl;

    TextView SNT;
    TextView SNT2;
    TextView SDT;
    TextView SDT2;
    TextView SVT;
    TextView CT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Get SharedPreferences.
        SharedPreferences settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        darkTheme = settings.getBoolean("darkTheme", darkTheme);

        //Set app version.
        VersionLine = findViewById(R.id.version);
        VersionLine.setText(versionName);

        icl = findViewById(R.id.cl);
        SNT = findViewById(R.id.staticNT);
        SNT2 = findViewById(R.id.staticNT2);
        SDT = findViewById(R.id.staticDT);
        SDT2 = findViewById(R.id.staticDT2);
        SVT = findViewById(R.id.staticVT);
        CT = findViewById(R.id.companytext);

        //Set Dark Theme (if enabled).
        if (darkTheme) {
            icl.setBackgroundColor(COLOR_BLACK);
            SNT.setTextColor(COLOR_WHITE);
            SNT2.setTextColor(COLOR_WHITE);
            SDT.setTextColor(COLOR_WHITE);
            SDT2.setTextColor(COLOR_WHITE);
            SVT.setTextColor(COLOR_WHITE);
            CT.setTextColor(COLOR_WHITE);
        }

    }

    //Opens GitHub repo
    public void GotoGithub(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubURL));
        startActivity(browserIntent);
    }
}
