package com.example.loftier.minesweeper;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Beginner extends Fragment{


    ListView listView;
    SharedPreferences setting_pref;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    ArrayList<String> arrName,arrTime;
    View viewB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewB=inflater.inflate(R.layout.fragment_beginner, container, false);
        databaseHelper = new DatabaseHelper(getContext());
        db = databaseHelper.getWritableDatabase();
        arrName=new ArrayList<>();
        arrTime=new ArrayList<>();
        String getData = "SELECT * FROM Time";
        Cursor cs = db.rawQuery(getData,null);
        if (cs.getCount()!=0) {
            while (cs.moveToNext()) {
                arrName.add(cs.getString(1));
                if (!cs.isNull(2))
                    arrTime.add(cs.getString(2) + " (B)");
                else if(!cs.isNull(3))
                    arrTime.add(cs.getString(3) + " (S)");
                else if(!cs.isNull(4))
                    arrTime.add(cs.getString(4) + " (E)");
                else if(!cs.isNull(5))
                    arrTime.add(cs.getString(5) + " (GM)");
            }
        }
        listView =viewB.findViewById(R.id.idLvAllRecord);

        listView.setAdapter(new CustomAdaptor());
        return viewB;
    }

    class CustomAdaptor extends BaseAdapter {

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
            col3.setText(arrTime.get(i));
            return view;
        }
    }

}
