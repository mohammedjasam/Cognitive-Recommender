package com.ambercam.android.camera2basic.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.models.User;
import com.ambercam.android.camera2basic.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class GetStartedActivity extends Activity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private String mEmail;
    private String mPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mCreateTextView;
    private ImageView mIconImageView;
    public static int UID = 1152;



    /**
     * LIFE CYCLE methods
     */
    SharedPreferences staticUserCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        initializeViews();
        setViewTransitionAnimations();

        mAuth = FirebaseAuth.getInstance();
        setFirebaseAuthListener();

        setLoginButtonListener();
        setCreateTextViewListener();

        staticUserCount = PreferenceManager.getDefaultSharedPreferences(this);
        UID = staticUserCount.getInt("Count",0);
//        staticUserCount.edit();
        
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        staticUserCount.edit().putInt("Count", UID);
        staticUserCount.edit().apply();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * disables device and toolbar back buttons
     */
    @Override
    public void onBackPressed() {}

    /**
     * initialize the activities views
     */
    public void initializeViews(){
        mEmailEditText = (EditText)findViewById(R.id.get_started_email);
        mPasswordEditText = (EditText)findViewById(R.id.get_started_password);
        mLoginButton = (Button)findViewById(R.id.get_started_login);
        mCreateTextView = (TextView)findViewById(R.id.get_started_create);
        mIconImageView = (ImageView)findViewById(R.id.get_started_icon);
    }

    /**
     * retrieves text from user input views
     */
    public User getTextFromViews(){

        mEmail = mEmailEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();

        User user = new User();

        if(!mEmail.equals(null) && Util.isValidEmail(mEmail) == true){
            user.setEmail(mEmail);
            Log.i("Email: ", mEmail);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter a valid email",
                    Toast.LENGTH_LONG).show();
        }

        if(mPassword.length() >= 6){
            user.setPassword(mPassword);
        }
        else{
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters",
                    Toast.LENGTH_LONG).show();
        }
        return user;
    }

    /**
     * listener for login button
     */
    public void setLoginButtonListener(){
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = getTextFromViews();
                if(Util.activeNetworkCheck(getApplicationContext()) == true){
                    loginUserWithFirebase(user, mAuth);
                }
                else{
                    Util.noActiveNetworkToast(getApplicationContext());
                }
            }
        });
    }

    /**
     * initialize the listener for firebase authentication
     */
    public void setFirebaseAuthListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent autoLoginIntent = new Intent(getApplicationContext(),
                            CameraActivity.class);
                    startActivity(autoLoginIntent);
                    overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                }
            }
        };
    }

    /**
     * login an existing user using email and password
     */
    public void loginUserWithFirebase(User user, FirebaseAuth auth){
        auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent cameraIntent = new Intent(getApplicationContext(),
                                    CameraActivity.class);
                            startActivity(cameraIntent);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.get_started_error),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    /**
     * listener for create account clickable text view
     */
    public void setCreateTextViewListener(){
        mCreateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(createIntent);
                overridePendingTransition(R.transition.slide_right, R.transition.fade_out);
            }
        });
    }

    public void setViewTransitionAnimations(){
        ObjectAnimator iconMover = ObjectAnimator.ofFloat(
                mIconImageView,
                "translationY",
                (Util.returnScreenHeight(getApplicationContext())),
                0f);
        iconMover.setDuration(700);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(iconMover);
        animatorSet.start();
    }
}
















