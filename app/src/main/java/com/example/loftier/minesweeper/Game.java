package com.example.loftier.minesweeper;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

public class Game extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

    //****Declaration****
    TextView mines_count;
    Chronometer time;
    Button[][] btn;
    ImageButton smiley, settings_menu;
    GridLayout layout;
    ToggleButton flag;
    MediaPlayer winning_sound, bomb_sound;
    int row = 15, column = 9, no_of_mines = 25;         //  10*6(60)10,    15*9(135)25,     20*12(240)50,     25*15(375)95
    float width_size = 9.7f, height_size = 18.5f;       //  6.5*12,          9.7*18.5,            13*24.5,      16*30

    int [][] value = new int[row][column];
    boolean[][] access = new boolean[row][column];
    Random random;
    private int mine=9, count = 0, flag_count = no_of_mines, flag_display = no_of_mines;
    boolean first=true, flag_on_off=false;
    private long ctime;
    Vibrator vibrator;
    SharedPreferences setting_pref;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //View decorView = getWindow().getDecorView();
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();

        setContentView(R.layout.activity_game);

        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        setting_pref=getSharedPreferences("setting_keys",MODE_PRIVATE);

        //****Initializing Widgets****
        btn = new Button[row][column];
        smiley = findViewById(R.id.idbtnsmiley);
        settings_menu = findViewById(R.id.idibtnsettings);
        flag = findViewById(R.id.idbtnflag);
        random = new Random();
        layout = findViewById(R.id.idglgamelayout);
        layout.setColumnCount(column);
        time = findViewById(R.id.idchtime);
        mines_count = findViewById(R.id.idtvminescount);
        winning_sound = MediaPlayer.create(this,R.raw.gameover);
        bomb_sound = MediaPlayer.create(this, R.raw.bomb);

        //****Adding Properties****
        mines_count.setText(flag_display + "");

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
    /*
                String str = "idbtn"+(i+1);
                int id = getResources().getIdentifier(str,"id",getPackageName());
                btn[i] = findViewById(id);
    */
                btn[i][j] = new Button(this);
                btn[i][j].setOnClickListener(this);
                btn[i][j].setOnLongClickListener(this);
                int width = getWindowManager().getDefaultDisplay().getWidth();
                int height = getWindowManager().getDefaultDisplay().getHeight();
                //btn[i][j].setGravity(Gravity.CENTER);
                btn[i][j].setBackgroundResource(R.drawable.buttonshape);
                btn[i][j].setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                btn[i][j].setTypeface(Typeface.SERIF);
                btn[i][j].setTypeface(btn[i][j].getTypeface(), Typeface.BOLD);
                //btn[i][j].setGravity(Gravity.LEFT);

                layout.addView(btn[i][j], (int) (width/width_size), (int) (height/height_size));
                access[i][j] = true;
            }
        }

        //****Adding OnClickListener****
        flag.setOnCheckedChangeListener(this);
        smiley.setOnClickListener(this);
        settings_menu.setOnClickListener(this);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                btn[i][j].setOnClickListener(this);
            }
        }
    }

    //****OnClick Method****
    @Override
    public void onClick(View view) {
        if(view instanceof Button) {                                //****Operations on Buttons
            if (setting_pref.getBoolean("vibration",false))
                vibrator.vibrate(30);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (view == btn[i][j]) {
                        if (flag_on_off){
                            adding_flag(i,j);
                        }
                        else if (btn[i][j].getBackground().getConstantState() == getResources().getDrawable(R.drawable.buttonshape).getConstantState()){
                            if (first) {
                                minesPosition(i, j);
                                first = false;
                                display_position(i, j);
                                startTimer();
                            }
                            else {
                                if (value[i][j] == mine) {
                                    display_all(i,j);
                                    pauseTimer();
                                    smiley.setImageResource(R.drawable.sad);
                                    bomb_sound.start();
                                    //showMessage("\t\t\t\t\t\t Game Over", "Click Smiley to Start a New Game");
                                }
                                else {
                                    display_position(i, j);
                                }
                            }
                        }

                    }
                }
            }
        }
        else
        if(view == smiley){                                         //****Operations on Smiley****
            smiley.setImageResource(R.drawable.smile);
            reset_game();
            resetTimer();
        }
        else
        if(view == settings_menu){
            startActivity(new Intent(Game.this,Settings.class));
        }
    }

    //****OnCheck Method****
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == flag){
            if(b){
                flag_on_off = true;
            }
            else
                flag_on_off = false;
        }
    }
    //****Adding values to buttons****
    void minesPosition(int a, int b) {
        //****Setting Mines****
        while (count < no_of_mines) {
            int r = random.nextInt(row);
            int c = random.nextInt(column);
            if (value[r][c] == mine || (r==a && c==b))
                continue;
            value[r][c] = mine;
            count++;
        }
        //****Setting neighboring mines count****
        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                if(value[i][j]!=mine){
                    value[i][j] = settingNumbers(i,j);
                }
            }
        }
    }

    //****Counting neighboring mines****
    int settingNumbers(int i, int j) {
        int no_of_mines=0;
        for(int m=i-1;m<i+2;m++){
            for(int n=j-1;n<j+2;n++){
                if(!(m==i && n==j)){
                    if(m>=0 && m<row && n>=0 && n<column){
                        if(value[m][n]==mine)
                            no_of_mines++;
                    }
                }
            }
        }
        return no_of_mines;
    }

    //****Show Board on Game Over****
    void display_all(int m, int n){
        if (setting_pref.getBoolean("vibration",false))
            vibrator.vibrate(100);
        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                if(value[i][j]!=mine){
                    coloringTheNumbers(i,j);
                    //btn[i][j].setBackgroundResource(R.drawable.buttonshapeonclicked);
                }
                else if(i!=m || j!=n){
                    btn[i][j].setBackgroundResource(R.drawable.mine);
                }
                else{
                    btn[i][j].setBackgroundResource(R.drawable.clickedmine);

                }
                btn[i][j].setClickable(false);
            }
        }
    }

    //****Showing the Position Clicked****
    void display_position(int i, int j){
        if(value[i][j]==0){
            showNeighboringPositions(i,j);
        }
        else
        {
            coloringTheNumbers(i,j);
            btn[i][j].setClickable(false);
            //btn[i][j].setBackgroundResource(R.drawable.buttonshapeonclicked);
        }
    }

    //****Giving colors to the different numbers****
    void coloringTheNumbers(int i, int j) {
        if (value[i][j] == 0){
            btn[i][j].setTextColor(Color.parseColor("#00000000"));
            btn[i][j].setBackgroundResource(R.drawable.buttonshapeonclicked);
        }
        else if(value[i][j]==1)
            btn[i][j].setBackgroundResource(R.drawable.one);
        else if(value[i][j]==2)
            btn[i][j].setBackgroundResource(R.drawable.two);
        else if(value[i][j]==3)
            btn[i][j].setBackgroundResource(R.drawable.three);
        else if(value[i][j]==4)
            btn[i][j].setBackgroundResource(R.drawable.four);
        else if(value[i][j]==5)
            btn[i][j].setBackgroundResource(R.drawable.five);
        else if(value[i][j]==6)
            btn[i][j].setBackgroundResource(R.drawable.six);
        else if(value[i][j]==7)
            btn[i][j].setBackgroundResource(R.drawable.seven);
        else
            btn[i][j].setBackgroundResource(R.drawable.eight);
    }

    //****When zero is Pressed****
    void showNeighboringPositions(int i, int j){
        if(value[i][j]!=0){
            return;
        }
        for(int m=i-1; m<i+2; m++) {
            for (int n = j-1; n<j+2; n++) {
                if(m>=0 && m<row && n>=0 && n<column && access[m][n]) {
                    if (btn[m][n].getBackground().getConstantState() == getResources().getDrawable(R.drawable.flag).getConstantState())
                        continue;
                    access[m][n]=false;
                    showNeighboringPositions(m, n);
                    coloringTheNumbers(m, n);
                    btn[m][n].setClickable(false);
                    //btn[m][n].setBackgroundResource(R.drawable.buttonshapeonclicked);
                }
            }
        }
    }

    //****Alert Box when Game is over****
    void showMessage(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.mine_icon);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    //****On Winning the Game****
    void win_game(){
        winning_sound.start();
        pauseTimer();
        smiley.setImageResource(R.drawable.glassessmiley);
        if(setting_pref.getBoolean("vibration",false))
            vibrator.vibrate(100);
        double t = (double)-ctime/1000.0;
        showMessage("\t\t Congratulations!!!", "You Won. \n" + "Time: " + t + "secs");
        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                if(value[i][j] == mine){
                    btn[i][j].setBackgroundResource(R.drawable.flag);
                    btn[i][j].setClickable(false);
                }
                else
                    display_position(i,j);
            }
        }
    }

    //****Reset the Game****
    void reset_game(){
        first = true;
        count = 0;
        flag_count = no_of_mines;
        flag_display = no_of_mines;
        mines_count.setText(flag_display+"");
        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                btn[i][j].setBackgroundResource(R.drawable.buttonshape);
                btn[i][j].setClickable(true);
                btn[i][j].setText("");
                btn[i][j].setTextColor(Color.parseColor("#ff000000"));
                value[i][j] = 0;
                access[i][j] = true;

            }
        }
    }

/*
    void customDialogBox (String title, String message){
        View view = getLayoutInflater().inflate(R.layout.custom_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }
*/

    //****Timer****
    public void startTimer(){
        time.setBase(SystemClock.elapsedRealtime()+ctime);
        time.start();
    }
    public void pauseTimer(){
        ctime=time.getBase()-SystemClock.elapsedRealtime();
        time.stop();
    }
    public void resetTimer(){
        time.setBase(SystemClock.elapsedRealtime());
        ctime=0;
        time.stop();
    }

    //****Setting Flags****
    void adding_flag(int i, int j){
        if (btn[i][j].getBackground().getConstantState() == getResources().getDrawable(R.drawable.flag).getConstantState()) {
            btn[i][j].setBackgroundResource(R.drawable.buttonshape);
            mines_count.setText(++flag_display + "");
            if (value[i][j] == mine) {
                flag_count++;
            }
        }
        else
        if (btn[i][j].getBackground().getConstantState() == getResources().getDrawable(R.drawable.buttonshape).getConstantState() && flag_display>0) {
            btn[i][j].setBackgroundResource(R.drawable.flag);
            mines_count.setText(--flag_display + "");
            if (value[i][j] == mine) {
                flag_count--;
                if (flag_count == 0) {
                    for (int a = 0; a < row; a++)
                        for (int b = 0; b < column; b++) {
                            btn[a][b].setClickable(false);
                        }
                    win_game();
                }
            }
        }
        else{
            Toast.makeText(this, "No flags available", Toast.LENGTH_SHORT).show();
        }
    }

    //****OnLongClick****
    @Override
    public boolean onLongClick(View view) {
        if(view instanceof Button){
            if (setting_pref.getBoolean("vibration",false))
                vibrator.vibrate(100);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    if (view == btn[i][j]) {
                        adding_flag(i,j);
                    }
                }
            }
        }
        return true;
    }
}
