package eparon.nxmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public String PREFS = "PrefsFile";

    int COLOR_WHITE = 0xFFFFFFFF;
    int COLOR_BLACK = 0xFF000000;

    String firmwarename = Build.DISPLAY; //Getting Device Build Number

    int romInt;
    boolean darkTheme = false;

    Switch DarkThemeSwitch;

    CoordinatorLayout scl;

    TextView STitle;
    TextView Settings1;
    TextView Settings2;

    //Reloading MainActivity when back is pressed.
    @Override
    public void onBackPressed() {
        //Checking if Dark Theme switch is checked.
        darkTheme = DarkThemeSwitch.isChecked(); //Setting the Dark Theme of the switch case.
        SharedPreferences settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("darkTheme", darkTheme);
        editor.apply();

        //Going to MainActivity.
        Intent a = new Intent(Settings.this, MainActivity.class);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Get SharedPreferences.
        SharedPreferences settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        romInt = settings.getInt("romInt", romInt);
        darkTheme = settings.getBoolean("darkTheme", darkTheme);

        DarkThemeSwitch = findViewById(R.id.darkthemeswitch);
        DarkThemeSwitch.setChecked(darkTheme);

        scl = findViewById(R.id.settingslayout);

        STitle = findViewById(R.id.sTitle);
        Settings1 = findViewById(R.id.settings1);
        Settings2 = findViewById(R.id.settings2);

        //Set Dark Theme (if enabled).
        if (darkTheme) {
            scl.setBackgroundColor(COLOR_BLACK);
            STitle.setTextColor(COLOR_WHITE);
            Settings1.setTextColor(COLOR_WHITE);
            Settings2.setTextColor(COLOR_WHITE);
        }
    }

    public void selectRom(View view) {
        String[] roms = {"Stock", "Soldier9312s ROM"}; //Setting AlertDialog options.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(roms, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //If pressed SoLdieR9312's Rom, it will check if the device is running it.
                if (which == 1 && firmwarename.length() != 33 || !firmwarename.substring(0, 12).equals("SoLdieR9312s")){
                    Toast.makeText(getApplicationContext(), "SoLdieR9312s ROM Version 10.0+ Not Found.", Toast.LENGTH_LONG).show();
                } else {
                    //Sets the rom to the selected rom.
                    romInt = which;
                    SharedPreferences settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("romInt", romInt);
                    editor.apply();
                }
            }
        });
        builder.show();
    }

}
