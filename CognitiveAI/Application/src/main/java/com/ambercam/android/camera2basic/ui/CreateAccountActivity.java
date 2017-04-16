package com.ambercam.android.camera2basic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ambercam.android.camera2basic.models.CountData;
import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.models.User;
import com.ambercam.android.camera2basic.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private EditText mEmailEditText;
    private EditText mConfirmEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mCreateButton;
    private User mUser;
    final int BASE_IMAGE_MAX = 100000000;
    final int BASE_IMAGE_COUNT = 0;
    private Toolbar mToolbar;

    /**
     * LIFE CYCLE methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initializeViews();
        initializeToolbarBehavior(mToolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        setFirebaseAuthListener();

        createButtonListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * initialize the views for the activity
     */
    public void initializeViews(){
        mEmailEditText = (EditText)findViewById(R.id.create_account_email);
        mConfirmEmailEditText = (EditText)findViewById(R.id.create_account_confirm_email);
        mPasswordEditText = (EditText)findViewById(R.id.create_account_password);
        mConfirmPasswordEditText = (EditText)findViewById(R.id.create_account_confirm_password);
        mCreateButton = (Button)findViewById(R.id.create_account_create);
        mToolbar = (Toolbar) findViewById(R.id.create_toolbar);
    }

    /**
     * handles toolbar behavior
     */
    public void initializeToolbarBehavior(Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.app_title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_left, R.transition.fade_out);
    }

    /**
     * listener for firebase authentication
     */
    public void setFirebaseAuthListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
    }

    /**
     * creates a new user in firebase
     */
    public void createNewUser(User user, FirebaseAuth auth){

        final User newUser = user;

        auth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.authentication_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.authentication_success),
                                    Toast.LENGTH_LONG).show();

                            initializeCountData(mFirebaseDatabase, newUser.getEmail());

                            Intent loginIntent = new Intent(getApplicationContext(), CameraActivity.class);
                            startActivity(loginIntent);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * sets the values for the users count data which is used to keep track of
     * images stored and the users personal limit within firebase
     */
    public void initializeCountData(FirebaseDatabase database, String user){
        String[] userRoot = mUser.getEmail().split("@");

        //reference to the users image count
        DatabaseReference reference = database.getReference(userRoot[0] + "_count");

        //reference to the users count data
        DatabaseReference subReference = reference.child("countData");

        //all accounts start with 0 stored images and a 100 image max
        CountData countData = new CountData(BASE_IMAGE_COUNT, BASE_IMAGE_MAX);

        //sets the initialized values to the newly created firebase reference
        subReference.setValue(countData);
    }

    /**
     * returns text from views and puts data into new user object
     */
    public User returnNewUser() {

        User user = new User();

        //email tests
        if (!mEmailEditText.getText().toString()
                .equals(mConfirmEmailEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "emails do not match",
                    Toast.LENGTH_SHORT).show();
        } else if (mEmailEditText.getText().toString().equals(null)) {
            Toast.makeText(getApplicationContext(), "Enter a valid email address",
                    Toast.LENGTH_SHORT).show();
        } else if (!Util.isValidEmail(mEmailEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter a valid email address",
                    Toast.LENGTH_SHORT).show();
        } else {
            user.setEmail(mEmailEditText.getText().toString());
        }

        //password tests
        if (!mPasswordEditText.getText().toString()
                .equals(mConfirmPasswordEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
        }
        else if (mPasswordEditText.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            user.setPassword(mPasswordEditText.getText().toString());
        }

        return user;
    }

    /**
     * listener for create account button
     */
    public void createButtonListener(){
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = returnNewUser();
                if(Util.activeNetworkCheck(getApplicationContext()) == true){
                    createNewUser(mUser, mFirebaseAuth );
                }
                else{
                    Util.noActiveNetworkToast(getApplicationContext());
                }
            }
        });
    }
}

