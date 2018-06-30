package com.example.loftier.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button signup;
    EditText name,email,passwrd,mobile;

    DatabaseHelper databasehelper;
    SQLiteDatabase db;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databasehelper = new DatabaseHelper(this);
        db = databasehelper.getWritableDatabase();

        signup = findViewById(R.id.idbtnsignup);
        name = findViewById(R.id.idetusernamesignup);
        email = findViewById(R.id.idetemailsignup);
        passwrd = findViewById(R.id.idetpasswordsignup);
        mobile = findViewById(R.id.idetnumbersignup);
        pref = getSharedPreferences("login_values", MODE_PRIVATE);


        signup.setOnClickListener(this);

        if (pref.getBoolean("status",false)){
            String mail = pref.getString("email","");
            email.setText(mail);
            email.setEnabled(false);
            //email.setTextColor(Color.parseColor("#000"));
            String data = "SELECT * FROM Data WHERE EMail = '"+mail+"'";
            Cursor c = db.rawQuery(data,null);
            c.moveToFirst();
            String number=c.getString(4);
            name.setText(c.getString(1));
            passwrd.setText(c.getString(3));
            if(number.length()>0)
                mobile.setText(number);
            signup.setText("UPDATE");
        }
    }



    @Override
    public void onClick(View view) {
        if(view ==signup){
            if (pref.getBoolean("status",false)){
                String update_values = "UPDATE Data SET Username='"+name.getText().toString()+"' , Password='"+passwrd.getText().toString()+"' , MobileNo='"+mobile.getText().toString()+"'";
                db.execSQL(update_values);
                Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
                final Intent i = new Intent(getApplicationContext(),GameOption.class);

                Thread thread=new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        }
                        finally {
                            startActivity(i);
                            finish();
                        }
                    }
                };
                thread.start();

            }
            else {
                String username = name.getText().toString();
                String emailid = email.getText().toString();
                String password = passwrd.getText().toString();
                String contact = mobile.getText().toString();

                if (!(username.isEmpty() || emailid.isEmpty() || password.isEmpty() || contact.isEmpty())) {
                    String exist = "SELECT * FROM Data WHERE EMail = '" + emailid + "'";
                    Cursor r = db.rawQuery(exist, null);
                    if (r.getCount() != 0) {
                        Snackbar.make(view, "Email Already Exist", Snackbar.LENGTH_LONG).setAction("action", null).show();
                    } else {
                        if (contact.length() != 10) {
                            Toast.makeText(this, "Invalid Mobile No", Toast.LENGTH_SHORT).show();
                        } else {
                            String query = "INSERT into Data VALUES (null, '" + username + "', '" + emailid + "', '" + password + "', '" + contact + "')";
                            db.execSQL(query);
                            Toast.makeText(this, "Signing Up", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", emailid);
                            editor.putBoolean("status", true);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), GameOption.class));
                            finish();
                        }
                    }
                } else if (username.isEmpty()) {
                    Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
                } else if (emailid.isEmpty()) {
                    Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
                } else {
                    String exist = "SELECT * FROM Data WHERE EMail = '" + emailid + "'";
                    Cursor r = db.rawQuery(exist, null);
                    if (r.getCount() != 0) {
                        Snackbar.make(view, "Email Already Exist", Snackbar.LENGTH_LONG).setAction("action", null).show();
                    } else {
                        String query = "INSERT into Data VALUES (null, '" + username + "', '" + emailid + "', '" + password + "', '" + contact + "')";
                        db.execSQL(query);
                        Toast.makeText(this, "Signing Up", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", emailid);
                        editor.putBoolean("status", true);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), GameOption.class));
                        finish();
                    }
                }
            }
        }
    }
    void showMessage(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }
}
