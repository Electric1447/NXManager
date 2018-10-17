package eparon.nxmanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class InternetCheck extends AppCompatActivity {

    //Checking if network is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netcheck);

        deleteCache(this); //Deleting cache on app startup to avoid bugs.
        CheckConnection(); //Checking if the device has Internet Connectivity.
    }

    public void CheckConnection() {
        if (isNetworkAvailable()) {
            Intent a = new Intent(InternetCheck.this, MainActivity.class);
            startActivity(a);
        } else {
            Toast.makeText(this, R.string.icError, Toast.LENGTH_SHORT).show();
        }
    }

    public void CheckConnectionButton(View view) { CheckConnection();} //Rechecking Internet Connectivity on button click.

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir(); //Getting cache file.
            deleteDir(dir); //Deleting cache file function.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Deleting cache file function (I did not write it).
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
