package com.example.loftier.minesweeper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Specialist extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    View viewB;
    Spinner selected_level;
    Button go;
    ListView listView;
    FrameLayout fl;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    ArrayList<String> arrName;
    ArrayList<Double> arrTime;
    int level;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewB =inflater.inflate(R.layout.fragment_specialist, container, false);

        go = viewB.findViewById(R.id.idbtngo);
        go.setOnClickListener(this);
        selected_level = viewB.findViewById(R.id.idSpinnerSelectLevel);
        selected_level.setOnItemSelectedListener(this);
        databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getWritableDatabase();
        arrName=new ArrayList<>();
        arrTime=new ArrayList<>();
        listView =viewB.findViewById(R.id.idLvLevelWiseRecord);
        fl=viewB.findViewById(R.id.idfl);
        return viewB;
    }

    @Override
    public void onClick(View view) {
        if (view == go){
            arrName.removeAll(arrName);
            arrTime.removeAll(arrTime);
            String getData = "";
            if (level == 0){
                getData = "SELECT * FROM Time WHERE beginner !=' '";
            }
            else if (level ==1){
                getData = "SELECT * FROM Time WHERE specialist !=' '";
            }
            else if (level ==2){
                getData = "SELECT * FROM Time WHERE expert !=' '";
            }
            else if (level ==3){
                getData = "SELECT * FROM Time WHERE grandmaster !=' '";
            }
            Cursor cs = db.rawQuery(getData,null);
            int i=0;
            if (cs.getCount()!=0) {
                while (cs.moveToNext()) {
                    arrName.add(cs.getString(1));
                    if (level == 0)
                        arrTime.add(Double.parseDouble(cs.getString(2)));
                    else if(level == 1)
                        arrTime.add(Double.parseDouble(cs.getString(3)));
                    else if(level == 2)
                        arrTime.add(Double.parseDouble(cs.getString(4)));
                    else if(level == 3)
                        arrTime.add(Double.parseDouble(cs.getString(5)));
                    i++;
                }
            }
            bubbleSort();
            listView.setAdapter(new CustomAdaptor());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        level = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    final class CustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return arrName.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.activity_card,null);
            TextView col1 = view.findViewById(R.id.idtvrank);
            TextView col2 = view.findViewById(R.id.idtvnameuser);
            TextView col3 = view.findViewById(R.id.idtvtimemin);

            col1.setText((i+1)+"");
            col2.setText(arrName.get(i));
            col3.setText(arrTime.get(i)+"");
            return view;
        }
    }

    void bubbleSort()
    {
        int n = arrTime.size();
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (arrTime.get(j) > arrTime.get(j+1))
                {
                    String tempname = arrName.get(j);
                    double temptime = arrTime.get(j);
                    arrName.set(j,arrName.get(j+1));
                    arrTime.set(j,arrTime.get(j+1));
                    arrName.set(j+1,tempname);
                    arrTime.set(j+1,temptime);
                }
    }
}
