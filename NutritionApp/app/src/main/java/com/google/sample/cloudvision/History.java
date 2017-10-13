package com.google.sample.cloudvision;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SuKim on 15/08/2017.
 */

public class History extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.history, container, false);

        Toolbar toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);

        //set toolbar appearance
        toolbar.setTitle("History");

        return fragmentView;

    }
}
