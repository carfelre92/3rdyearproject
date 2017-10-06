package com.google.sample.cloudvision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NutritionInfo extends AppCompatActivity {

    private DatabaseReference dbRef;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private TextView foodInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_info);
        foodInfo = (TextView) findViewById(R.id.foodInfo);


        String message = getIntent().getStringExtra("bestRes");
        TextView infoTitle = (TextView) findViewById(R.id.infoTitle);
        infoTitle.setText("Nutrition facts about: " + message);


        dbRef = database.getReferenceFromUrl("https://nutrition-app-b01d3.firebaseio.com/Food").child(message);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FoodInformation foodInformation = dataSnapshot.getValue(FoodInformation.class);

                    System.out.println("Calories: " + foodInformation.calories);
                    System.out.println("Cholesterol: " + foodInformation.cholesterol);
                    foodInfo.setText("Welcome " + foodInformation.calories + " " + foodInformation.cholesterol);
                } else {
                    foodInfo.setText("No nutrition information found for this food.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
