package com.ambercam.android.camera2basic.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.models.FirebaseData;
import com.ambercam.android.camera2basic.presenters.DetailPresenter;
import com.ambercam.android.camera2basic.util.OnSwipeTouchListener;
import com.ambercam.android.camera2basic.util.Util;
import com.ambercam.android.camera2basic.views.DetailView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailView {

    DetailPresenter mDetailPresenter;

    private ImageView mDetailImageView;
    private RelativeLayout mRelativeLayout;
    private Button mDetailDeleteButton;
    private FloatingActionButton mCameraFab;
    private Toolbar mToolbar;
    final int VIBRATE_DURATION = 200;

    private ArrayList<String> mImageUrlList;
    private int mPosition;

    final String SWIPEDIRECTIONLEFT = "LEFT";
    final String SWIPEDIRECTIONRIGHT = "RIGHT";
    final int VIEWSLIDEDURATION = 300;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private FirebaseUser mActiveUser;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseData mFirebaseData;

    private SharedPreferences mUserPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailPresenter = new DetailPresenter(getApplicationContext());
        mDetailPresenter.attachView(this);

        initializeViews();
        initializeToolbarBehavior(mToolbar);
        returnIntentExtras();

        mAuth = FirebaseAuth.getInstance();

        setAuthStateListener();

        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        setDetailImageViewLongClickListener();
        setDetailDeleteButtonListener();
        setDetailImageViewSwipeListener();
        setCameraFabListener();

        returnUserPreferences();

        updateUI(mImageUrlList, mPosition);
    }

    /**
     * listener for firebase authentication
     */
    public void setAuthStateListener(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                mActiveUser = firebaseAuth.getCurrentUser();
                Log.i("Current User: ", mActiveUser + "");
                if(mActiveUser!=null){
                    mDatabaseReference = FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child(Util.returnSplitEmail(mActiveUser.getEmail().toString()) + "_count");
                    mDetailPresenter.setChildEventListener(mDatabaseReference, mChildEventListener);
                    mFirebaseData = setFirebaseData();
                }
            }
        };
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

    /**
     * initialize all views in activity
     */
    public void initializeViews(){
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        mDetailImageView = (ImageView)findViewById(R.id.detail_imageview);
        mDetailDeleteButton = (Button)findViewById(R.id.detail_delete_button);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.detail_relative_layout);
        mCameraFab = (FloatingActionButton)findViewById(R.id.detail_camera_fab);
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

    /**
     * menu methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_forever:{
                //if delete button is already visible it is hidden
                if(mRelativeLayout.getVisibility() == View.VISIBLE){
                    mRelativeLayout.setVisibility(View.GONE);
                    mCameraFab.setVisibility(View.VISIBLE);
                }
                else{
                    //if button is not visible it is shown
                    mRelativeLayout.setVisibility(View.VISIBLE);
                    handleDeviceVibration();
                    mCameraFab.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.share:{
                shareImage();
                break;
            }
            default: return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * retrieve intent extras from previous activity
     */
    @Override
    public void returnIntentExtras() {
        Bundle bundle = getIntent().getExtras();
        mDetailPresenter.returnIntentExtras(bundle);
        mPosition = bundle.getInt("position");
        mImageUrlList = bundle.getStringArrayList("list");
    }

    /**
     * update user interface
     */
    public void updateUI(ArrayList<String> list, int position){
        Glide.with(getApplicationContext())
                .load(list.get(position))
                .into(mDetailImageView);
        handleFabBehavior();
    }

    /**
     * long click listener for the detail ImageView
     */
    public void setDetailImageViewLongClickListener(){
        mDetailImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    /**
     * listener for detail delete button
     */
    public void setDetailDeleteButtonListener(){
        mDetailDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirebaseData();
                mDetailPresenter.detailDeleteClick(mFirebaseData);
            }
        });
    }

    /**
     * stores all current firebase data in a single object
     */
    public FirebaseData setFirebaseData(){
        FirebaseData firebaseData = new FirebaseData();
        firebaseData.setAuth(mAuth);
        firebaseData.setAuthStateListener(mAuthStateListener);
        firebaseData.setDatabaseReference(mDatabaseReference);
        firebaseData.setFirebaseDatabase(mFirebaseDatabase);
        firebaseData.setFirebaseStorage(mFirebaseStorage);
        firebaseData.setFirebaseUser(mActiveUser);
        Log.i("Active User: ", mActiveUser.getEmail());
        return firebaseData;
    }

    /**
     * swipe listener for detail image view
     */
    public void setDetailImageViewSwipeListener() {
        mDetailImageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

            @Override
            public void onSwipeLeft() {

                if (mImageUrlList.size() - 1 == mPosition) {
                    Toast.makeText(getApplicationContext(), "There's no more pictures!", Toast.LENGTH_SHORT).show();
                } else {
                    mPosition = mPosition + 1;
                    updateUI(mImageUrlList, mPosition);
                }

                SlideImageViewAnimation setSlide = new SlideImageViewAnimation(
                        getApplicationContext(),
                        mDetailImageView,
                        SWIPEDIRECTIONLEFT);
                setSlide.run();
            }

            @Override
            public void onSwipeRight() {

                if (mPosition == 0) {
                    Toast.makeText(getApplicationContext(), "That's your first picture!", Toast.LENGTH_SHORT).show();
                } else {
                    mPosition = mPosition - 1;
                    updateUI(mImageUrlList, mPosition);
                }

                SlideImageViewAnimation setSlide = new SlideImageViewAnimation(
                        getApplicationContext(),
                        mDetailImageView,
                        SWIPEDIRECTIONRIGHT);
                setSlide.run();
            }
        });
    }

    /**
     * listener for camera floating action button
     */
    public void setCameraFabListener() {
        mCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraIntent);
                overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
            }
        });
    }

    /**
     * enables device vibration
     */
    @Override
    public void setDeviceVibration() {
        Vibrator vibrator = (Vibrator) getApplicationContext()
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_DURATION);
    }

    /**
     * share image url using intent
     */
    @Override
    public void shareImage() {
        String url = mImageUrlList.get(mPosition);
        Log.i("Url: ", url);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(shareIntent, "Share using"));
    }

    /**
     * retrieves shared preferences for user preferences
     */
    @Override
    public void returnUserPreferences() {
        mUserPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
    }

    /**
     * handles fab behavior according to user preferences
     */
    public void handleFabBehavior(){
        boolean isVisible = mUserPreferences
                .getBoolean(getString(R.string.preference_key_hide), false);

        if(isVisible == true){
            mCameraFab.setVisibility(View.GONE);
        }
    }

    /**
     * handles device vibration behavior according to user preferences
     */
    @Override
    public void handleDeviceVibration() {
        boolean vibrationEnable = mUserPreferences
                .getBoolean(getString(R.string.preference_key_vibration), false);
        if(vibrationEnable == false){
            setDeviceVibration();
        }
    }

    /**
     * handles detail ImageView slide animation off thread
     */
    public class SlideImageViewAnimation implements Runnable{

        private ImageView mImageView;
        private Context mContext;
        private String mDirection;

        public SlideImageViewAnimation(Context context, ImageView imageView, String direction) {
            this.mContext = context;
            this.mImageView = imageView;
            this.mDirection = direction;
        }

        @Override
        public void run() {
            setSlideTransition(mDirection, mImageView);
        }

        /**
         * slide transition for touch swipe listener
         */
        public void setSlideTransition(String direction, ImageView image){
            if(direction.equals(SWIPEDIRECTIONLEFT)){
                ObjectAnimator mover = ObjectAnimator.ofFloat(image,
                        "translationX", Util.returnScreenWidth(mContext), 0f);
                AnimatorSet animatorSet = new AnimatorSet();
                mover.setDuration(VIEWSLIDEDURATION);
                animatorSet.play(mover);
                animatorSet.start();
            }
            else if(direction.equals(SWIPEDIRECTIONRIGHT)){
                ObjectAnimator mover = ObjectAnimator.ofFloat(image,
                        "translationX", -(Util.returnScreenWidth(mContext)), 0f);
                AnimatorSet animatorSet = new AnimatorSet();
                mover.setDuration(VIEWSLIDEDURATION);
                animatorSet.play(mover);
                animatorSet.start();
            }
        }
    }
}
