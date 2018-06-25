package com.example.loftier.minesweeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class GameOption extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //****Declaration****
    Button new_game, user_score, game_settings, quit;
    DrawerLayout drawer;
    Toolbar toolbar;
    FloatingActionButton about;
    NavigationView navigationView;
    SharedPreferences pref;
    ConstraintLayout layout;
    TextView show_email;


    DatabaseHelper databasehelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option);

        //****Initialization****
        new_game = findViewById(R.id.idbtnnewgame);
        user_score = findViewById(R.id.idbtnscore);
        game_settings = findViewById(R.id.idbtnsetting);
        quit = findViewById(R.id.idbtnquit);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        layout = findViewById(R.id.idconslayoutgameoptn);
        setSupportActionBar(toolbar);
        show_email = findViewById(R.id.idtvemailgameoptionheader);

        about = (FloatingActionButton) findViewById(R.id.idfababout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogBox("","");
            }
        });

        //****Adding Listener****
        new_game.setOnClickListener(this);
        user_score.setOnClickListener(this);
        game_settings.setOnClickListener(this);
        quit.setOnClickListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.idchangeimage) {
            // Handle the camera action
        } else if (id == R.id.idupdate) {

        } else if (id == R.id.idlogout) {
            pref = getSharedPreferences("login_values", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("status", false);
            editor.apply();
            startActivity(new Intent(GameOption.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == new_game) {
            startActivity(new Intent(GameOption.this, Game.class));
        } else if (view == user_score) {
            startActivity(new Intent(GameOption.this, Scores.class));
        } else if (view == game_settings) {
            startActivity(new Intent(GameOption.this, Settings.class));
        } else if (view == quit) {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("EXIT");
            builder.setMessage("Are you sure, you want to quit?");
            builder.setIcon(R.drawable.mine_icon);
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    void customDialogBox (String title, String message){
        View view = getLayoutInflater().inflate(R.layout.custom_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.setCancelable(true);
        dialog.show();
        Button btn=view.findViewById(R.id.idbtncloseabout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GameOption.this, "Button is clicked", Toast.LENGTH_SHORT).show();            }
        });
    }
}