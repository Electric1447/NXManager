package eparon.nxmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Changelog extends AppCompatActivity {

    String soldierFile = "http://www.renovate-ice.com/svn/soldier9312-g95x/trunk/META-INF/com/google/android/aroma/changelog.txt";

    int lines = 0;
    String finalstr = "";

    TextView Changelog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelog);

        GetChangelog();

        Changelog = findViewById(R.id.changelog);

    }

    public void GetChangelog() {
        new Thread(new Runnable() {

            public void run() {

                try {
                    URL url = new URL(soldierFile);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String str;

                    while ((str = in.readLine()) != null) {
                        if (lines == 0){finalstr = str;}
                        else {
                            finalstr = finalstr + System.lineSeparator() + str;
                        }
                        lines = 1;
                    }
                    in.close();
                } catch (Exception e) {
                    Log.d("MyTag", e.toString());
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        Changelog.setText(finalstr);
                    }
                });

            }
        }).start();
    }

}
