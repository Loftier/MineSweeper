package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayAsGuest extends AppCompatActivity implements View.OnClickListener {

    EditText guest_name;
    Button start_game;
    String name;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_as_guest);
        start_game = findViewById(R.id.idbtnstart);
        guest_name = findViewById(R.id.idetguestname);
        start_game.setOnClickListener(this);
        pref=getSharedPreferences("login_values",MODE_PRIVATE);


    }

    @Override
    public void onClick(View view) {
        if(view == start_game){
            name = guest_name.getText().toString();
            if(name.length()<4){
                Snackbar.make(view,"Min 5 characters", Snackbar.LENGTH_SHORT).setAction("action",null).show();
            }
            else{
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("email",name);
                editor.putBoolean("guest_status",true);
                editor.apply();
                Intent i = new Intent(PlayAsGuest.this, GameOption.class);
                startActivity(i);
                finish();
                Snackbar.make(view,"Welcome "+name, Snackbar.LENGTH_SHORT).setAction("action",null).show();

            }
        }
    }
}
