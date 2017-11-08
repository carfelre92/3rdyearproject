package com.google.sample.cloudvision;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SuKim on 15/08/2017.
 */

public class History extends Fragment {

    private FirebaseAuth firebaseAuth;
    private ListView listView1;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.history, container, false);

        view = fragmentView;
        listView1 = (ListView) fragmentView.findViewById(R.id.listView1);

        Toolbar toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);

        //set toolbar appearance
        toolbar.setTitle("History");

        fetchHistory();

        return fragmentView;

    }

    //get previous search
    private void fetchHistory(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uID = "";
        Boolean loggedIn = true;
        if(Profile.getCurrentProfile() != null) {
            uID = Profile.getCurrentProfile().getId();
        } else if (user != null){
            uID = user.getUid();
        } else {
            loggedIn = false;
        }
        String path = "data/data/com.google.sample.cloudvision/" + uID;

        File directory = new File(path);
        File[] files = directory.listFiles();

        if (files != null && loggedIn) {
            List<String> filesNameList = new ArrayList<>();

            String rawName;
            String[] bits;
            String niceName;
            String date;
            String time;
            for (int i = 0; i < files.length; i++) {
                //processing data
                rawName = files[i].getName();
                bits = rawName.split("_");
                date = bits[0];
                time = bits[1];

                //Add in the punctuation for formatting
                date = new StringBuilder(date).insert(date.length()-4, "/").toString();
                date = new StringBuilder(date).insert(date.length()-2, "/").toString();
                time = time.substring(0, 4);
                time = new StringBuilder(time).insert(time.length()-2, ":").toString();


                niceName = "Food: " + bits[2] + "\nDate: " + date + " Time: " + time;

                filesNameList.add(niceName);

            }
            Collections.reverse(filesNameList);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, filesNameList);

            listView1.setAdapter(arrayAdapter);
        }
    }

    //create textview for each previous search

}
