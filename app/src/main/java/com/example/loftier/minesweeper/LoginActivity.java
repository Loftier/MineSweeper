package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref;
    Button signin, createnewaccount;
    EditText mail, paswrd;

    DatabaseHelper databasehelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databasehelper = new DatabaseHelper(this);
        db = databasehelper.getWritableDatabase();

        pref=getSharedPreferences("login_values",MODE_PRIVATE);

        signin = findViewById(R.id.idbtnsignin);
        signin.setOnClickListener(this);

        createnewaccount = findViewById(R.id.idbtnnewaccount);
        createnewaccount.setOnClickListener(this);

        mail = findViewById(R.id.idetemail);
        paswrd = findViewById(R.id.idetpassword);
    }

    @Override
    public void onClick(View view) {
        if(view == signin) {
            String email = mail.getText().toString();
            String pswrd = paswrd.getText().toString();
            if (email.isEmpty() || pswrd.isEmpty()) {
                Toast.makeText(this, "Enter Details", Toast.LENGTH_SHORT).show();
            }
            else {
                String query = "SELECT * FROM Data WHERE EMail = '" +email+ "' AND Password = '"+pswrd+"'";
                Cursor res = db.rawQuery(query, null);
                if (res.getCount() != 0) {
                    //SharedPreference
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("email",email);
                    editor.putBoolean("status",true);
                    editor.apply();
                    Intent i = new Intent(LoginActivity.this, GameOption.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(view == createnewaccount) {
            Intent i = new Intent(LoginActivity.this, SignUp.class);
            startActivity(i);
        }
    }
}
