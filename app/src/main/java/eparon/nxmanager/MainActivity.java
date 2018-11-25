package eparon.nxmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.support.v4.widget.SwipeRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public String PREFS = "PrefsFile";

    int COLOR_GREEN = 0xFF00FF00;
    int COLOR_RED = 0xFFFF0000;
    int COLOR_YELLOW = 0xFFFFCC00;
    int COLOR_WHITE = 0xFFFFFFFF;
    int COLOR_BLACK = 0xFF000000;

    int romInt;
    boolean darkTheme;

    String nxURL = "https://forum.xda-developers.com/galaxy-s8/samsung-galaxy-s8--s8-cross-device-development/kernel-nox-kernel-v1-t3721469";
    String soldierXDAURL = "https://forum.xda-developers.com/galaxy-s8/samsung-galaxy-s8--s8-cross-device-development/g95xf-fd-n-oreo-beta-2-soldier9312-zqk4-t3703183";
    String soliderSiteURL = "http://soldier9312.de/";
    String soldierFile = "http://www.renovate-ice.com/svn/soldier9312-g95x/trunk/META-INF/com/google/android/aroma/changelog.txt";
    String downloadURL = "http://www.renovate-ice.com/soldier9312/release/g95x/json.php?download=latest";

    TextView latestVersion;
    TextView currentVersion;
    TextView cFirmwareVersion;
    TextView lFirmwareVersion;

    TextView fTitle;
    TextView cfText;
    TextView lfText;

    TextView soldiersiteButton;
    TextView soldierxdaButton;
    TextView dlromButton;

    TextView changelogLink;

    TextView KernelTitle;
    TextView StaticCV;
    TextView StaticLV;

    int ckernel = 0;
    int lkernel;

    double cFirmware = 0;
    double lFirmware;

    String kernelname = System.getProperty("os.version"); //Getting Kernel name.
    String firmwarename = Build.DISPLAY; //Getting Device Build Number.
    String kernelversion;
    String firmwareVersion;
    String pageTitle;
    String latestKernel;
    String currentFirmware;
    String latestFirmware;

    SwipeRefreshLayout srl;

    //Making that you can't press the back button.
    @Override
    public void onBackPressed() { }

    //Checking if network is available.
    private boolean isNetworkAvailable2() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get SharedPreferences.
        SharedPreferences settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        romInt = settings.getInt("romInt", romInt);
        darkTheme = settings.getBoolean("darkTheme", darkTheme);

        ArrayList<String> fileArray = new ArrayList<>();

        latestVersion = findViewById(R.id.latestver);
        currentVersion = findViewById(R.id.currentver);
        cFirmwareVersion = findViewById(R.id.currentFirmware);
        lFirmwareVersion = findViewById(R.id.latestFirmware);

        fTitle = findViewById(R.id.fTitle);
        cfText = findViewById(R.id.cfText);
        lfText = findViewById(R.id.lfText);
        lfText = findViewById(R.id.lfText);

        soldiersiteButton = findViewById(R.id.soldier_site);
        soldierxdaButton = findViewById(R.id.xda_rom);
        dlromButton = findViewById(R.id.download);

        changelogLink = findViewById(R.id.clhl);

        KernelTitle = findViewById(R.id.kerneltitle);
        StaticCV = findViewById(R.id.staticCV);
        StaticLV = findViewById(R.id.staticLV);

        getNXCurrentVersion(); //Getting current NX kernel version (dah)

        getNXVersionFromURL(); //Getting latest NX version from url title.
        soldierRomCheck(fileArray); //Checking if SoLdieR's rom is selected.

        //SwipeRefreshLayout function
        srl = findViewById(R.id.swipelayout);
        srl.setColorSchemeResources(R.color.refresh1, R.color.refresh2, R.color.refresh3, R.color.refresh4);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Making sure network is still available.
                if (isNetworkAvailable2()) {
                    //Reloading the latest kernel and rom.
                    ArrayList<String> fileArray = new ArrayList<>();
                    srl.setRefreshing(true);
                    getNXVersionFromURL();
                    soldierRomCheck(fileArray);
                } else {
                    //If there is no Internet Connection, sending you back to InternetCheck activity.
                    Intent a = new Intent(MainActivity.this, InternetCheck.class);
                    startActivity(a);
                }
            }
        });

        //Set Dark Theme (if enabled).
        if (darkTheme){
            srl.setBackgroundColor(COLOR_BLACK);
            KernelTitle.setTextColor(COLOR_WHITE);
            StaticCV.setTextColor(COLOR_WHITE);
            StaticLV.setTextColor(COLOR_WHITE);
            latestVersion.setTextColor(COLOR_WHITE);
            currentVersion.setTextColor(COLOR_WHITE);
            cFirmwareVersion.setTextColor(COLOR_WHITE);
            lFirmwareVersion.setTextColor(COLOR_WHITE);
            fTitle.setTextColor(COLOR_WHITE);
            cfText.setTextColor(COLOR_WHITE);
            lfText.setTextColor(COLOR_WHITE);
        }
    }

    //Getting latest NX version from url title.
    public void getNXVersionFromURL (){
        new Thread(new Runnable() {
            public void run() {
                try {

                    //Getting page title from URL using Jsoup.
                    Document document = Jsoup.connect(nxURL).followRedirects(false).timeout(60000).get();
                    pageTitle = document.title();

                } catch (Exception e) { Log.d("MyTag", e.toString()); }

                //On finish
                runOnUiThread(new Runnable() {
                    public void run() {
                        InternetCheck.deleteCache(getApplicationContext()); ///Deleting cache on to avoid bugs.
                        srl.setRefreshing(false); //Stop Refreshing animation.
                        latestKernel = pageTitle.substring(24, 26); //Shorting page title to the latest version.
                        lkernel = Integer.parseInt(latestKernel);
                        latestVersion.setText("R" + latestKernel);
                        nxColor(); //Colorizing the kernel text.
                    }
                });

            }
        }).start();
    }

    //Checking if SoLdieR's rom is selected.
    public void soldierRomCheck(final ArrayList<String> urls) {
        if (romInt == 1) {
            firmwareVersion = firmwarename.substring(26); //Shorting the Build Number to only show the firmware version.
            cFirmwareVersion.setText(firmwareVersion);
            cFirmware = Double.parseDouble(firmwareVersion); //Making the firmware version a double.
            new Thread(new Runnable() {

                public void run() {

                    try {
                        //Getting Changelog file from URL
                        URL url = new URL(soldierFile);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(60000);

                        //Defining the BufferedReader to read from the file
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        String str;
                        //Reading the entire text file.
                        while ((str = in.readLine()) != null) {
                            urls.add(str);
                        }
                        in.close();
                    } catch (Exception e) {
                        Log.d("MyTag", e.toString());
                    }

                    //On finish
                    runOnUiThread(new Runnable() {
                        public void run() {
                            lFirmwareVersion.setText(urls.get(0)); //Setting the latest firmware version to be the first line of changelog file.
                            lFirmware = Double.parseDouble(lFirmwareVersion.getText().toString());
                            fwColor(); //Colorizing the firmware text.
                        }
                    });

                }
            }).start();
        } else {
            //If SoLdieR's rom isn't selected, making everything disappear.
            cFirmwareVersion.setText("");
            lFirmwareVersion.setText("");
            fTitle.setText("");
            cfText.setText("");
            lfText.setText("");
            soldierxdaButton.setVisibility(View.GONE);
            soldiersiteButton.setVisibility(View.GONE);
            dlromButton.setVisibility(View.GONE);//
            changelogLink.setClickable(false);
            changelogLink.setText("");
        }
    }

    //Getting current NX kernel version.
    public void getNXCurrentVersion() {
        //Validating if NX kernel is installed.
        if (kernelname.length() > 11 && kernelname.length() < 14 && kernelname.substring(7, 9).equals("NX")) {
            kernelversion = kernelname.substring(11); //Shorting the kernel to only show the version.
            ckernel = Integer.parseInt(kernelversion);
            currentVersion.setText("R" + kernelversion);
        } else {
            currentVersion.setText(R.string.cvError);
            currentVersion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    //Setting the kernel text color.
    public void nxColor() {
        if (ckernel == 0) {
            //Nx kernel not found.
            currentVersion.setTextColor(COLOR_YELLOW);
            latestVersion.setTextColor(COLOR_YELLOW);
        } else {
            if (ckernel == lkernel) {
                //Latest NX kernel version is installed.
                currentVersion.setTextColor(COLOR_GREEN);
                latestVersion.setTextColor(COLOR_GREEN);
            } else {
                //Latest NX kernel version isn't installed.
                currentVersion.setTextColor(COLOR_RED);
                latestVersion.setTextColor(COLOR_RED);
            }
        }
    }

    //Setting the firmware text color.
    public void fwColor() {
        if (cFirmware == lFirmware) {
            //Latest firmware version is installed.
            cFirmwareVersion.setTextColor(COLOR_GREEN);
            lFirmwareVersion.setTextColor(COLOR_GREEN);
        } else {
            //Latest firmware version isn't installed.
            cFirmwareVersion.setTextColor(COLOR_RED);
            lFirmwareVersion.setTextColor(COLOR_RED);
        }
    }

    //All the buttons that send you somewhere (kinda obvious).
    public void GoInfo(View view) {
        Intent a = new Intent(MainActivity.this, Info.class);
        startActivity(a);
    }

    public void GoSettings(View view) {
        Intent a = new Intent(MainActivity.this, Settings.class);
        startActivity(a);
    }

    public void GotoXDA(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nxURL));
        startActivity(browserIntent);
    }

    public void GotoSoldierXDA(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(soldierXDAURL));
        startActivity(browserIntent);
    }

    public void GotoSoldierSite(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(soliderSiteURL));
        startActivity(browserIntent);
    }

    public void GotoChangelog(View view) {
        Intent a = new Intent(MainActivity.this, Changelog.class);
        startActivity(a);
    }

    public void DownloadRom(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadURL));
        startActivity(browserIntent);
    }

}