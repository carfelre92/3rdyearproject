package com.google.sample.cloudvision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
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

import java.io.File;
import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.StrictMath.round;


public class NutritionInfo extends AppCompatActivity {

    private DatabaseReference dbRef;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;

    //Reccomended daily intake of certain nutrients
    //This info is based on the guide provided by the australia new zealand food standards code (FSC)
    //Guide for an average adult
    final static double FAT_DI = 70.0; //g
    final static int CHO_DI = 300; //mg
    final static int SOD_DI = 2300; //mg
    final static int POT_DI = 4700; //mg
    final static int CAR_DI = 310; //g
    final static double PRO_DI = 50.0; //g

    private TextView foodNotFoundText;
    private ProgressBar loadingIcon;

    private TextView caloriesText;
    private TextView fatText;
    private TextView cholesterolText;
    private TextView sodiumText;
    private TextView potassiumText;
    private TextView carbohydratesText;
    private TextView proteinText;

    private TextView caloriesVal;
    private TextView fatVal;
    private TextView cholesterolVal;
    private TextView sodiumVal;
    private TextView potassiumVal;
    private TextView carbohydratesVal;
    private TextView proteinVal;

    private TextView caloriesDI;
    private TextView fatDI;
    private TextView cholesterolDI;
    private TextView sodiumDI;
    private TextView potassiumDI;
    private TextView carbohydratesDI;
    private TextView proteinDI;

    //Contains result
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_info);

        //Get all components that will be changed at runtime
        foodNotFoundText = (TextView) findViewById(R.id.foodNotFoundText);
        loadingIcon = (ProgressBar) findViewById(R.id.loadingIcon);

        caloriesText = (TextView) findViewById(R.id.caloriesText);
        fatText = (TextView) findViewById(R.id.fatText);
        cholesterolText = (TextView) findViewById(R.id.cholesterolText);
        sodiumText = (TextView) findViewById(R.id.sodiumText);
        potassiumText = (TextView) findViewById(R.id.potassiumText);
        carbohydratesText = (TextView) findViewById(R.id.carbohydratesText);
        proteinText = (TextView) findViewById(R.id.proteinText);

        caloriesVal = (TextView) findViewById(R.id.caloriesVal);
        fatVal = (TextView) findViewById(R.id.fatVal);
        cholesterolVal = (TextView) findViewById(R.id.cholesterolVal);
        sodiumVal = (TextView) findViewById(R.id.sodiumVal);
        potassiumVal = (TextView) findViewById(R.id.potassiumVal);
        carbohydratesVal = (TextView) findViewById(R.id.carbohydratesVal);
        proteinVal = (TextView) findViewById(R.id.proteinVal);

        caloriesDI = (TextView) findViewById(R.id.caloriesDI);
        fatDI = (TextView) findViewById(R.id.fatDI);
        cholesterolDI = (TextView) findViewById(R.id.cholesterolDI);
        sodiumDI = (TextView) findViewById(R.id.sodiumDI);
        potassiumDI = (TextView) findViewById(R.id.potassiumDI);
        carbohydratesDI = (TextView) findViewById(R.id.carbohydratesDI);
        proteinDI = (TextView) findViewById(R.id.proteinDI);

        foodNotFoundText.setVisibility(View.GONE);

        caloriesText.setVisibility(View.GONE);
        fatText.setVisibility(View.GONE);
        cholesterolText.setVisibility(View.GONE);
        sodiumText.setVisibility(View.GONE);
        potassiumText.setVisibility(View.GONE);
        carbohydratesText.setVisibility(View.GONE);
        proteinText.setVisibility(View.GONE);

        caloriesVal.setVisibility(View.GONE);
        fatVal.setVisibility(View.GONE);
        cholesterolVal.setVisibility(View.GONE);
        sodiumVal.setVisibility(View.GONE);
        potassiumVal.setVisibility(View.GONE);
        carbohydratesVal.setVisibility(View.GONE);
        proteinVal.setVisibility(View.GONE);

        caloriesDI.setVisibility(View.GONE);
        fatDI.setVisibility(View.GONE);
        cholesterolDI.setVisibility(View.GONE);
        sodiumDI.setVisibility(View.GONE);
        potassiumDI.setVisibility(View.GONE);
        carbohydratesDI.setVisibility(View.GONE);
        proteinDI.setVisibility(View.GONE);


        message = getIntent().getStringExtra("bestRes");
        TextView infoTitle = (TextView) findViewById(R.id.infoTitle);
        infoTitle.setText("Nutrition facts about: " + message);


        dbRef = database.getReferenceFromUrl("https://nutrition-app-b01d3.firebaseio.com/Food").child(message);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    FoodInformation foodInformation = dataSnapshot.getValue(FoodInformation.class);

                    caloriesVal.setText(foodInformation.calories);
                    fatVal.setText(foodInformation.fat);
                    cholesterolVal.setText(foodInformation.cholesterol);
                    sodiumVal.setText(foodInformation.sodium);
                    potassiumVal.setText(foodInformation.potassium);
                    carbohydratesVal.setText(foodInformation.carbohydrates);
                    proteinVal.setText(foodInformation.protein);

                    //Calc may not be working correctly
                    //Or figures may be incorrect

                    //Sets rounding format
                    DecimalFormat df = new DecimalFormat("#0.00");
                    df.setRoundingMode(RoundingMode.HALF_UP);

                    caloriesDI.setText("N/A");
                    fatDI.setText((df.format(((Integer.parseInt(foodInformation.fat.replaceAll("[^0-9]", ""))) / FAT_DI) * 100)) + "%");
                    cholesterolDI.setText((df.format(((Integer.parseInt(foodInformation.cholesterol.replaceAll("[^0-9]", ""))) / CHO_DI) * 100)) + "%");
                    sodiumDI.setText((df.format(((Integer.parseInt(foodInformation.sodium.replaceAll("[^0-9]", ""))) / SOD_DI) * 100)) + "%");
                    potassiumDI.setText((df.format(((Integer.parseInt(foodInformation.potassium.replaceAll("[^0-9]", ""))) / POT_DI) * 100)) + "%");
                    carbohydratesDI.setText((df.format(((Integer.parseInt(foodInformation.carbohydrates.replaceAll("[^0-9]", ""))) / CAR_DI) * 100)) + "%");
                    proteinDI.setText((df.format(((Integer.parseInt(foodInformation.protein.replaceAll("[^0-9]", ""))) / PRO_DI) * 100)) + "%");

                    loadingIcon.setVisibility(View.GONE);
                    caloriesText.setVisibility(View.VISIBLE);
                    fatText.setVisibility(View.VISIBLE);
                    cholesterolText.setVisibility(View.VISIBLE);
                    sodiumText.setVisibility(View.VISIBLE);
                    potassiumText.setVisibility(View.VISIBLE);
                    carbohydratesText.setVisibility(View.VISIBLE);
                    proteinText.setVisibility(View.VISIBLE);
                    caloriesVal.setVisibility(View.VISIBLE);
                    fatVal.setVisibility(View.VISIBLE);
                    cholesterolVal.setVisibility(View.VISIBLE);
                    sodiumVal.setVisibility(View.VISIBLE);
                    potassiumVal.setVisibility(View.VISIBLE);
                    carbohydratesVal.setVisibility(View.VISIBLE);
                    proteinVal.setVisibility(View.VISIBLE);
                    caloriesDI.setVisibility(View.VISIBLE);
                    fatDI.setVisibility(View.VISIBLE);
                    cholesterolDI.setVisibility(View.VISIBLE);
                    sodiumDI.setVisibility(View.VISIBLE);
                    potassiumDI.setVisibility(View.VISIBLE);
                    carbohydratesDI.setVisibility(View.VISIBLE);
                    proteinDI.setVisibility(View.VISIBLE);

                    //foodInfo.setText("Welcome " + foodInformation.calories + " " + foodInformation.cholesterol);
                } else {
                    loadingIcon.setVisibility(View.GONE);
                    foodNotFoundText.setVisibility(View.VISIBLE);
                    foodNotFoundText.setText("No nutrition information found for this food.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //call save search
        saveSearch();

    }

    //save search
    private void saveSearch(){
        //get date/time
        String time = new SimpleDateFormat("ddMMyy_HHmmss").format(new Date());

        //get item searched
        String foodSearched = message;

        //Check if folder is already created, if not, create one with userID
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uID;
        if(Profile.getCurrentProfile() != null) {
            uID = Profile.getCurrentProfile().getId();
        } else {
            uID = user.getUid();
        }
        String path = "data/data/com.google.sample.cloudvision/" + uID;
        File mydir = new File(path); //Creating an internal dir inside phone;
        if (!mydir.exists())
        {
            mydir.mkdirs();
        }

        //Save new file with the name <food item>_<date and time>
        FileOutputStream fos;
        String filename = time + "_" + foodSearched;
        String string = "empty";
        try {
            File file = new File(path,filename);
            fos = new FileOutputStream(file);
            fos.write(string.getBytes());
            fos.close();
            //Toast for confirmation when debugging
            //Toast.makeText(getApplicationContext(), "File name with "+filename+" has been saved to path: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
