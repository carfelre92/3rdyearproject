package com.google.sample.cloudvision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class OtherResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_results);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        ArrayList<String> results = getIntent().getStringArrayListExtra("results");



    }
}
