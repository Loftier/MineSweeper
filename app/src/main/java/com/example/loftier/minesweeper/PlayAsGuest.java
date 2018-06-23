package com.example.loftier.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayAsGuest extends AppCompatActivity implements View.OnClickListener {

    EditText guest_name;
    Button start_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_as_guest);
        start_game = findViewById(R.id.idbtnstart);
        guest_name = findViewById(R.id.idetguestname);
        start_game.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == start_game){
            Intent i = new Intent(PlayAsGuest.this, GameOption.class);
            startActivity(i);
        }
    }
}
