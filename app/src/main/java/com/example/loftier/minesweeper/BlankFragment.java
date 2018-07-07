package com.example.loftier.minesweeper;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BlankFragment extends Fragment {

    View v;
    TextView[] t;
    Typeface tf;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_blank, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/DejaVuSansMono-Bold.ttf");
        t = new TextView[6];
        for(int i=0; i<6; i++){
            String s = "tv"+(i+1);
            int id = v.getResources().getIdentifier(s,"id",getActivity().getPackageName());
            t[i] = v.findViewById(id);
            t[i].setTypeface(tf);
        }
        return v;
    }
}
