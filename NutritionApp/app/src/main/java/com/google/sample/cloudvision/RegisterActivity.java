package com.google.sample.cloudvision;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.provider.ContactsContract;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextfName;
    private EditText editTextlName;
    private EditText editTextGender;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;

    //firebaseAuth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //Initializing views
        progressDialog = new ProgressDialog(this);

        dbRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://nutrition-app-b01d3.firebaseio.com/user");

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);
        editTextfName = (EditText) findViewById(R.id.editTextfName);
        editTextlName = (EditText) findViewById(R.id.editTextlName);
        editTextGender = (EditText) findViewById(R.id.editTextGender);

        //attaching listener to button
        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //if email is empty, return short toast
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            //if password is empty, return short toast
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }


        //if validation are ok it will show progressDialog

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInformation();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            //user registered successfully
                            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        } else {
                            //user registration failed
                            Toast.makeText(RegisterActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });
    }

    private void saveUserInformation() {
        String fname = editTextfName.getText().toString().trim();
        String lname = editTextlName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        int auth = 0;

        UserInformation userInformation = new UserInformation(fname, lname, gender, auth);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        dbRef.child(user.getUid()).setValue(userInformation);
    }

    @Override
    public void onClick(View view) {
        if(view ==buttonRegister){
            //register activity
            registerUser();
        }
        if(view == textViewSignIn){
            //Login
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
