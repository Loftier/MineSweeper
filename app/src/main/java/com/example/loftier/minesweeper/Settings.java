package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    //****Declaration****
    Switch vibrate;
    Switch sound;
    Button ok;
    BackgroundAudioService music;
    Spinner difficulty_level;
    Intent i;
    SharedPreferences setting_pref;
    Vibrator vibrator;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //****Preference****
        setting_pref=getSharedPreferences("setting_keys",MODE_PRIVATE);
        editor=setting_pref.edit();

        i = new Intent(getApplicationContext(),BackgroundAudioService.class);

        //****Initialization****
        vibrate = findViewById(R.id.idswitchvibrate);
        sound = findViewById(R.id.idswitchsound);
        ok = findViewById(R.id.idbtnoksettings);
        difficulty_level = findViewById(R.id.idspinnerlevels);
        vibrate.setChecked(setting_pref.getBoolean("vibration",false));
        sound.setChecked(setting_pref.getBoolean("volume",false));
        difficulty_level.setSelection(setting_pref.getInt("level",0));

        //****OnClick Listener****
        vibrate.setOnCheckedChangeListener(this);
        sound.setOnCheckedChangeListener(this);
        ok.setOnClickListener(this);
        difficulty_level.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View view) {
         if(view == ok){
             i = new Intent(Settings.this, Game.class);
             //i.putExtra("Name:",Game.class);
             //String s = getIntent().getStringExtra("Name");
             startActivity(i);
             finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton == vibrate){
            if(b){
                vibrator.vibrate(100);
                editor.putBoolean("vibration",true);
                editor.apply();
            }
            else{
                editor.putBoolean("vibration",false);
                editor.apply();
            }
        } else if (compoundButton == sound){
            Intent i = new Intent(getApplicationContext(),BackgroundAudioService.class);
            if(b){
                startService(i);
                editor.putBoolean("volume",true);
                editor.apply();
            }
            else{
                stopService(i);
                editor.putBoolean("volume",false);
                editor.apply();
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        editor.putInt("level",i);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
