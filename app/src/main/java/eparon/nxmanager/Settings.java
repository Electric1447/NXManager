package eparon.nxmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    public String PREFS = "PrefsFile";

    String firmwarename = Build.DISPLAY; //Getting Device Build Number.

    int romInt;

    //Reloading MainActivity when back is pressed.
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Settings.this, MainActivity.class);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences rom = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        romInt = rom.getInt("romInt", romInt);
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
                    SharedPreferences rom = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = rom.edit();
                    editor.putInt("romInt", romInt);
                    editor.apply();
                }
            }
        });
        builder.show();
    }

}
