package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.reflect.GenericArrayType;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences setting_pref;
    MediaPlayer music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setting_pref = getSharedPreferences("setting_keys",MODE_PRIVATE);
        music = MediaPlayer.create(this,R.raw.splashmusic);
        final Intent i = new Intent(getApplicationContext(),UserRegistration.class);

        CircleImageView logo = findViewById(R.id.profile_image);
        ImageView minesweeper = findViewById(R.id.minesweeper);
        Animation alpha = AnimationUtils.loadAnimation(this,R.anim.alpha);
        Animation rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);
        minesweeper.startAnimation(alpha);
        logo.startAnimation(rotate);
        music.start();
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        thread.start();
    }
}
