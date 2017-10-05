package com.google.sample.cloudvision;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class OtherResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_results);

        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add((Button)findViewById(R.id.b1));
        buttons.add((Button)findViewById(R.id.b2));
        buttons.add((Button)findViewById(R.id.b3));
        buttons.add((Button)findViewById(R.id.b4));
        buttons.add((Button)findViewById(R.id.b5));
        buttons.add((Button)findViewById(R.id.b6));
        buttons.add((Button)findViewById(R.id.b7));
        buttons.add((Button)findViewById(R.id.b8));
        buttons.add((Button)findViewById(R.id.b9));
        buttons.add((Button)findViewById(R.id.b10));
        buttons.add((Button)findViewById(R.id.b11));
        buttons.add((Button)findViewById(R.id.b12));
        buttons.add((Button)findViewById(R.id.b13));
        buttons.add((Button)findViewById(R.id.b14));
        buttons.add((Button)findViewById(R.id.b15));
        buttons.add((Button)findViewById(R.id.b16));
        buttons.add((Button)findViewById(R.id.b17));
        buttons.add((Button)findViewById(R.id.b18));
        buttons.add((Button)findViewById(R.id.b19));
        buttons.add((Button)findViewById(R.id.b20));

        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setVisibility(View.GONE);
        }

        System.out.println("Made it past the first loop!");

        // Get the Intent that started this activity and extract the string
        ArrayList<String> results = getIntent().getStringArrayListExtra("results");

        //Dynamically show the buttons at runtime
        for(int i = 0; i < results.size(); i++) {
            Button b = buttons.get(i);
            b.setText(results.get(i));
            b.setVisibility(View.VISIBLE);
        }

    }

    public void takeMeHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
