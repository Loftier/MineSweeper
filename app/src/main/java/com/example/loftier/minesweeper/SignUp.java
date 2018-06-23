package com.example.loftier.minesweeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    Button signup, btnView;
    EditText name,email,passwrd,mobile;

    DatabaseHelper databasehelper;
    SQLiteDatabase db;

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
        btnView = findViewById(R.id.idbtnview);

        btnView.setOnClickListener(this);
        signup.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if(view ==signup){
            String username = name.getText().toString();
            String emailid = email.getText().toString();
            String password = passwrd.getText().toString();
            String contact = mobile.getText().toString();

            if(!(username.isEmpty() || emailid.isEmpty() || password.isEmpty() || contact.isEmpty())){
                String query = "INSERT into Data VALUES (null, '"+username+"', '"+emailid+"', '"+password+"', '"+contact+"')";
                db.execSQL(query);
                Toast.makeText(this, "Signed Up", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),GameOption.class));
                finish();
            }
            else if(username.isEmpty()){
                Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            }
            else if(emailid.isEmpty()){
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            }
            else if(password.isEmpty()){
                Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            }
            else{
                String query = "INSERT into Data VALUES (null, '"+username+"', '"+emailid+"', '"+password+"', null)";
                db.execSQL(query);
                Toast.makeText(this, "Signed Up", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),GameOption.class));
                finish();
            }
        }
        else if (view==btnView){
            StringBuffer buffer=new StringBuffer();
            String fetch="SELECT * FROM Data";
            Cursor res=db.rawQuery(fetch,null);
            if (res.getCount()!=0){
                while (res.moveToNext()){
                    buffer.append("id: "+res.getString(0)+"\n");
                    buffer.append("username: "+res.getString(1)+"\n");
                    buffer.append("email: "+res.getString(2)+"\n");
                    buffer.append("MobileNo: "+res.getString(4)+"\n");
                }
                showMessage("Data",buffer.toString());
            }
            else {
                showMessage("Data","empty table");
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
