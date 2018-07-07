package com.example.loftier.minesweeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameOption extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //****Declaration****
    Button new_game, user_score, game_settings, quit;
    DrawerLayout drawer;
    Toolbar toolbar;
    FloatingActionButton about;
    NavigationView navigationView;
    SharedPreferences pref, setting_pref;
    SharedPreferences.Editor editor, editor_loginvalues;
    ConstraintLayout layout;
    TextView show_email;
    CircleImageView user_image;
    CircleImageView[] image;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onStart() {
        super.onStart();
        if(setting_pref.getBoolean("volume",false)){
            startService(new Intent(getApplicationContext(),BackgroundAudioService.class));
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        stopService(new Intent(getApplicationContext(),BackgroundAudioService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(),BackgroundAudioService.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option);

        //****Initialization****
        new_game = findViewById(R.id.idbtnnewgame);
        user_score = findViewById(R.id.idbtnscore);
        game_settings = findViewById(R.id.idbtnsetting);
        quit = findViewById(R.id.idbtnquit);
        image = new CircleImageView[12];
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pref=getSharedPreferences("login_values",MODE_PRIVATE);
        setting_pref=getSharedPreferences("setting_keys",MODE_PRIVATE);
        editor=setting_pref.edit();
        editor_loginvalues=pref.edit();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        layout = findViewById(R.id.idconslayoutgameoptn);
        setSupportActionBar(toolbar);
        show_email = v.findViewById(R.id.idtvemailgameoptionheader);
        user_image = v.findViewById(R.id.id_profile_image);
        about = (FloatingActionButton) findViewById(R.id.idfababout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogBox();
            }
        });
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        //****Adding Listener****
        new_game.setOnClickListener(this);
        user_score.setOnClickListener(this);
        game_settings.setOnClickListener(this);
        quit.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //****Setting User Details in Drawer****
        if (pref.getBoolean("status",false)){
            String mailid = pref.getString("email","");
            String im = "SELECT * FROM Data WHERE EMail = '"+mailid+"'";
            Cursor cr;
            cr = db.rawQuery(im,null);
            cr.moveToFirst();
            int var = cr.getInt(5);
            if(var==0){
                user_image.setImageResource(R.drawable.mine_icon);
            }
            else {
                user_image.setImageResource(var);
            }
            show_email.setText(mailid);
        }
        else if (pref.getBoolean("guest_status",false)){
            show_email.setText(pref.getString("email",""));
            user_image.setImageResource(R.drawable.mine_icon);
        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("EXIT");
            builder.setMessage("Are you sure, you want to quit?");
            builder.setIcon(R.drawable.mine_icon);
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(),HelpMenu.class));
            
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Menu menu = navigationView.getMenu();
        MenuItem itemImage = menu.findItem(R.id.idchangeimage);
        MenuItem itemUpdate = menu.findItem(R.id.idupdate);
        MenuItem itemLogout = menu.findItem(R.id.idlogout);
        MenuItem itemShare = menu.findItem(R.id.nav_share);
        MenuItem itemSend = menu.findItem(R.id.nav_send);

        if (id == R.id.idchangeimage) {
            itemImage.setCheckable(false);
            if (pref.getBoolean("guest_status",false)){
                login_dialog_box();
            }
            else
                change_image();
        } else if (id == R.id.idupdate) {
            itemUpdate.setCheckable(false);
            if (pref.getBoolean("guest_status",false)){
                login_dialog_box();
            }
            else{
                startActivity(new Intent(getApplicationContext(),SignUp.class));
                finish();
            }

        } else if (id == R.id.idlogout) {
            itemLogout.setCheckable(false);
            pref = getSharedPreferences("login_values", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            if (pref.getBoolean("status",false)) {
                editor.putBoolean("status", false);
            }
            else if (pref.getBoolean("guest_status",false)){
                editor.putBoolean("guest_status",false);
            }
            editor.apply();
            startActivity(new Intent(GameOption.this, UserRegistration.class));
            finish();
        } else if (id == R.id.nav_share) {
            itemShare.setCheckable(false);

        } else if (id == R.id.nav_send) {
            itemSend.setCheckable(false);

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void login_dialog_box() {
        View v = getLayoutInflater().inflate(R.layout.login_dialog_box,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    AlertDialog dialog;
    private void change_image() {
        View v = getLayoutInflater().inflate(R.layout.images_collection,null);
        for (int i=0; i<12; i++){
            String s = "imageView"+(i+1);
            String t = "i"+(i+1);
            int id = getResources().getIdentifier(s,"id",getPackageName());
            int tag = getResources().getIdentifier(t,"drawable",getPackageName());
            image[i] = v.findViewById(id);
            image[i].setTag(tag);
            image[i].setOnClickListener(this);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == new_game) {
            startActivity(new Intent(GameOption.this, Game.class));
        }
        else if (view == user_score) {
            startActivity(new Intent(GameOption.this, Scores.class));
        }
        else if (view == game_settings) {
            editor.putBoolean("fromGameOption",true);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), Settings.class));
        }
        else if (view == quit) {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("EXIT");
            builder.setMessage("Are you sure, you want to quit?");
            builder.setIcon(R.drawable.mine_icon);
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finishAffinity();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
        else if (view instanceof CircleImageView){
            for (int i=0; i<12; i++){
                if (view == image[i]){
                    int var = (int) image[i].getTag();
                    user_image.setImageResource(var);
                    String mail = pref.getString("email","");
                    String str = "SELECT * FROM Data WHERE EMail = '"+mail+"'";
                    Cursor cur;
                    cur=db.rawQuery(str,null);
                    cur.moveToFirst();
                    String updt = "UPDATE Data SET image_source='"+var+"' WHERE EMail = '"+mail+"'";
                    db.execSQL(updt);
                    dialog.dismiss();
                }
            }
        }
    }

    void customDialogBox (){
        View view = getLayoutInflater().inflate(R.layout.custom_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.setCancelable(false);
        dialog.show();
        Button btn=view.findViewById(R.id.idbtncloseabout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}