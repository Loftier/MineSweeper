package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserRegistration extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref=getSharedPreferences("login_values",MODE_PRIVATE);
        if (pref.getBoolean("status",false)){
            startActivity(new Intent(getApplicationContext(),GameOption.class));
            finish();
        }

        setContentView(R.layout.activity_user_registration);
        Button login = findViewById(R.id.idbtnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserRegistration.this,LoginActivity.class);
                startActivity(i);
            }
        });
        Button playasguest = findViewById(R.id.idbtnplayasguest);
        playasguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserRegistration.this, PlayAsGuest.class);
                startActivity(i);
            }
        });

    }
}
