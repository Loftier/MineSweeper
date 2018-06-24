package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    //****Declaration****
    Switch vibrate;
    SeekBar sound;
    Button ok;
    BackgroundAudioService music;
    Spinner difficulty_level;
    Intent i;
    SharedPreferences setting_pref;
    Vibrator vibrator;
    SharedPreferences.Editor editor;
    int vol = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //Preference
        setting_pref=getSharedPreferences("setting_keys",MODE_PRIVATE);
        editor=setting_pref.edit();

        i = new Intent(getApplicationContext(),BackgroundAudioService.class);

        //****Initialization****
        vibrate = findViewById(R.id.idswitchvibrate);
        sound = findViewById(R.id.idseeksound);
        ok = findViewById(R.id.idbtnoksettings);
        difficulty_level = findViewById(R.id.idspinner);

        //OnClick Listener
        vibrate.setOnCheckedChangeListener(this);
        sound.setOnSeekBarChangeListener(this);
        ok.setOnClickListener(this);

        vibrate.setChecked(setting_pref.getBoolean("vibration",false));

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
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(i>0)
            editor.putBoolean("volume",true);
        else
            editor.putBoolean("volume",false);
        editor.apply();
        vol = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        music.music_player.setVolume(vol,vol);
        Toast.makeText(this, seekBar.getProgress()+"%", Toast.LENGTH_SHORT).show();

    }
}
