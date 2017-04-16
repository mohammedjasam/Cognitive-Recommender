package com.ambercam.android.camera2basic.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.adapter.ImageAdapter;
import com.ambercam.android.camera2basic.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private ImageButton mCameraButton;
    private ImageButton mMenuButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mActiveUser;
    private DatabaseReference mDatabaseReference;
    private ArrayList<String> mImageUrlList = new ArrayList<>();
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences mUserPreferences;
    private RecyclerView mGalleryRecycler;

    private ImageAdapter mImageAdapter;

    /**
     * Life Cycle methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if(savedInstanceState == null){
            initializeViews();

            setDrawerLayout(mNavigationView);

            mAuth = FirebaseAuth.getInstance();

            //firebase listeners
            setAuthStateListener();

            //button listeners
            setCameraButtonListener();
            setMenuButtonListener();
            //setGalleryGridViewListener();

            returnUserPreferences();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.transition.slide_right, R.transition.fade_in);
    }

    /**
     * initialize the views for the activity
     */
    public void initializeViews(){
        mCameraButton = (ImageButton)findViewById(R.id.gallery_picture);
        mMenuButton = (ImageButton)findViewById(R.id.gallery_menu);
        mNavigationView = (NavigationView)findViewById(R.id.gallery_nav_view);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.gallery_drawer_layout);
        mGalleryRecycler = (RecyclerView)findViewById(R.id.gallery_recycler);

        mGalleryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
}

    /**
     * returns the users preferences
     */
    public void returnUserPreferences(){
        mUserPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
    }

    /**
     * listener for camera button
     */
    public void setCameraButtonListener(){
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraIntent);
                overridePendingTransition(R.transition.slide_left, R.transition.fade_out);
            }
        });
    }

    /**
     * listener for menu button
     */
    public void setMenuButtonListener(){
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    /**
     * listener for firebase authentication
     */
    public void setAuthStateListener(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                //gets the active user
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                //if the user is not null the database reference is retrieved
                if(firebaseUser != null){
                    mActiveUser = firebaseUser;
                    mDatabaseReference = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(Util.returnSplitEmail(mActiveUser.getEmail().toString()));

                    updateUI();
                }
            }
        };
    }

    /**
     * updates the GridView to contain the most recent images
     */
    public void updateUI(){
        mImageAdapter = new ImageAdapter(getApplicationContext(), mDatabaseReference);
        mGalleryRecycler.setAdapter(mImageAdapter);
    }

    /**
     * changes the column count depending on user preferences
     */
    public void handleGalleryGridViewPreferenceBehavior(){
        String columnCount = mUserPreferences
                .getString(getString(R.string.preference_key_gallery), "3");

        if(columnCount == "1"){
            mGalleryRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        else if(columnCount == "2"){
            mGalleryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        else if(columnCount == "3"){
            mGalleryRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        }

       // mGalleryGridView.setNumColumns(Integer.parseInt(columnCount));
    }

    /**
     * selection for for gallery images
     * created by tsparrow
     */
    public static String[] selectionSort(ArrayList mList) {

        String[] list = new String[mList.size()];

        /**
         * take ArrayList mList and transfer contents into String[] list;
         */
        for (int i = 0; i < mList.size(); i++) {
            list[i] = mList.get(i).toString();
        }

        /**
         * arrays to store the urls and the timestamps separately.
         * both half the size of the original array.
         */
        String[] urlArray = new String[list.length / 2];
        Integer[] timeStampArray = new Integer[list.length / 2];

        /**
         * counters for the two separate arrays.
         */
        int urlCounter = 0;
        int timeCounter = 0;

        /**
         * split the list by urls and by timestamps
         */
        for (int i = 0; i < list.length; i+=2) {

            /**
             * if i is an even number
             * (these are the urls)
             */
            if (i == 0 || i % 2 == 0) {
                urlArray[urlCounter++] = list[i];
            }

            /**
             * if i is an odd number
             * (these are the timeStamps)
             */
            else {
                timeStampArray[timeCounter++] = Integer.valueOf(list[i]);
            }
        }

        /**
         * selection sort the arrays based on the timestamps
         */
        for (int i = 0; i < timeStampArray.length; i++) {

            int index = 1;

            for (int j = i + 1; j < timeStampArray.length; j++) {

                if (timeStampArray[j] < timeStampArray[index]) {
                    index = j;
                }

                int smallerNumber = timeStampArray[index];
                String smallerUrl = urlArray[index];

                /**
                 * sorting the timestamps.
                 */
                timeStampArray[index] = timeStampArray[i];
                timeStampArray[i] = smallerNumber;

                /**
                 * sorting the urls.
                 */
                urlArray[index] = urlArray[i];
                urlArray[i] = smallerUrl;
            }

            /**
             * return the sorted array of urls.
             */
            return urlArray;
        }
        return null;
    }

    /**
     * sorts the image url list off thread
     */
    public class GallerySort implements Runnable{

        private ArrayList<String> mList;

        public GallerySort() {
        }

        public GallerySort(ArrayList<String> list) {
            mList = list;
        }

        @Override
        public void run() {
            String[] urlArray = selectionSort(mList);

            for (int i = 0; i < urlArray.length; i++) {

                mImageUrlList.add(urlArray[i]);
            }
        }
    }

    /**
     * methods for nav drawer
     */

    /**
     * opens the play store using an intent so users can rate the app
     */
    public void openPlayStore(){
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, uri);

        //allows for us to return to app from play store using back button
        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try{
            startActivity(playStoreIntent);
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
        }
    }

    /**
     * allows the user to chose there favorite email app and send feedback to the development team
     */
    public void sendFeedBack(){
        Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("mnqnd@mst.edu") +
                "?subject=" + Uri.encode("Cognitive Recommender Feedback");

        Uri uri = Uri.parse(uriText);
        feedbackIntent.setData(uri);

        //allows for us to return to app from play store using back button
        feedbackIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        startActivity(Intent.createChooser(feedbackIntent, "Send mail......"));
    }

    /**
     * opens the cloud usage activity
     */
    public void cloudUsageIntent() {
        Intent usageIntent = new Intent(getApplicationContext(), UsageActivity.class);
        startActivity(usageIntent);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    /**
     * opens the preference activity
     */
    public void preferenceIntent(){
        Intent preferenceIntent = new Intent(getApplicationContext(), PreferenceActivity.class);
        startActivity(preferenceIntent);
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    /**
     * logs the user out of firebase and returns the user to the get started activity
     */
    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent logoutActivity = new Intent(getApplicationContext(), GetStartedActivity.class);
        startActivity(logoutActivity);
    }

    /**
     * set listener for nav drawer items
     */
    public void setDrawerLayout(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_cloud_usage: {
                        cloudUsageIntent();
                        break;
                    }
                    case R.id.menu_feedback: {
                        sendFeedBack();
                        break;
                    }
                    case R.id.menu_rate: {
                        openPlayStore();
                        break;
                    }
                    case R.id.menu_settings: {
                        preferenceIntent();
                        break;
                    }
                    case R.id.menu_logout: {
                        logOut();
                        break;
                    }
                }
                return true;
            }
        });
    }
}