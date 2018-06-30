package com.example.loftier.minesweeper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ControlFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View controleView= inflater.inflate(R.layout.fragment_control, container, false);
        // Inflate the layout for this fragment
       // TextView tv=controleView.findViewById();
        return controleView;
    }

}
