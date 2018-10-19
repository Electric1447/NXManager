package eparon.nxmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Info extends AppCompatActivity {

    String githubURL = "https://github.com/Electric1447/NXManager";

    String versionName = BuildConfig.VERSION_NAME; //Get App version.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //Set app version.
        TextView VersionLine = findViewById(R.id.version);
        VersionLine.setText(versionName);
    }

    public void GotoGithub(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubURL));
        startActivity(browserIntent);
    }
}
