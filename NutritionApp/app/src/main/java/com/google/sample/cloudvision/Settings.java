package com.google.sample.cloudvision;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by SuKim on 15/08/2017.
 */

public class Settings extends Fragment implements View.OnClickListener {

    private Button logoutButton;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.settings, container, false);

        Toolbar toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);

        //set toolbar appearance
        toolbar.setTitle("Settings");

        logoutButton = (Button) fragmentView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://nutrition-app-b01d3.firebaseio.com/user").child(user.getUid());

        return fragmentView;
    }

    @Override
    public void onClick(View view) {
        if (view == logoutButton) {
            firebaseAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }
}
