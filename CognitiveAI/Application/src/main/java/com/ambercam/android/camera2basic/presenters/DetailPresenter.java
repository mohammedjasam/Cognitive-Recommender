package com.ambercam.android.camera2basic.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.models.CountData;
import com.ambercam.android.camera2basic.models.FirebaseData;
import com.ambercam.android.camera2basic.ui.GalleryActivity;
import com.ambercam.android.camera2basic.util.Util;
import com.ambercam.android.camera2basic.views.DetailView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by brianroper on 12/15/16.
 */

public class DetailPresenter implements Presenter<DetailView> {

    private Context mContext;
    private DetailView mDetailView;
    private CountData mCountData;
    private String mFinalUrl;
    private ArrayList<String> mImageUrlList;
    private int mPosition;

    final String FIREBASE_BUCKET = "gs://cameraapp-6a5ab.appspot.com";
    private FirebaseUser mActiveUser;

    public DetailPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(DetailView detailView) {
        this.mDetailView = detailView;
    }

    public void returnIntentExtras(Bundle bundle){
        mPosition = bundle.getInt("position");
        mImageUrlList = bundle.getStringArrayList("list");
    }

    /**
     * child event listener that retrieves the current count data for the user
     */
    public void setChildEventListener(DatabaseReference reference,
                                      ChildEventListener childEventListener){

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mCountData = dataSnapshot.getValue(CountData.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        reference.addChildEventListener(childEventListener);
    }

    /**
     * deletes image data from firebase
     */
    public void detailDeleteClick(FirebaseData firebaseData){

        SplitUrl splitUrl = new SplitUrl(mPosition, mImageUrlList);
        splitUrl.run();

        mActiveUser = firebaseData.getFirebaseUser();
        if (mActiveUser != null) {
            String user = Util.returnSplitEmail(firebaseData.getFirebaseUser().getEmail());

            deleteImageFromDatabase(firebaseData.getDatabaseReference(),
                    Util.returnSplitEmail(user));
            deleteImageFromStorage(firebaseData.getFirebaseStorage(),
                    Util.returnSplitEmail(user));

            updateDatabaseImageCounter(firebaseData.getFirebaseDatabase(), user);
        }
    }

    /**
     * removes the image from firebase storage
     */
    public void deleteImageFromStorage(FirebaseStorage storage, String user){

        //reference to the projects storage bucket
        StorageReference reference = storage.getReferenceFromUrl(FIREBASE_BUCKET);

        //reference to the users storage folder
        StorageReference subReference = reference.child(user);

        //reference to the individual image stored in firebase
        StorageReference timestampReference = subReference
                .child(mFinalUrl);

        //deletes the image
        timestampReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, mContext.getString(R.string.detail_delete_success_toast),
                        Toast.LENGTH_SHORT).show();
                Intent galleryIntent = new Intent(mContext, GalleryActivity.class);
                galleryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(galleryIntent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
               /* Toast.makeText(getApplicationContext(), getString(R.string.detail_delete_failed_toast),
                        Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    /**
     * removes image data from firebase realtime database
     */
    public void deleteImageFromDatabase(DatabaseReference databaseReference, String user){

        //reference to the users data in firebase
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Util.returnSplitEmail(mActiveUser.getEmail().toString()));

        //reference to the individual image data stored in firebase
        DatabaseReference subReference = databaseReference
                .child(mFinalUrl);

        //removes the image data from firebase
        subReference
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.i("Realtime Delete: ", "Failed");
                        e.printStackTrace();
                    }
                });
    }

    /**
     * updates the image count when a photo is deleted
     */
    public void updateDatabaseImageCounter(FirebaseDatabase database,
                                           String user){

        //decrements the image count when a photo is deleted
        mCountData.setImageCount(mCountData.getImageCount()-1);

        //reference to the users image count
        DatabaseReference reference = database.getReference(user + "_count");

        //reference to the users count data
        DatabaseReference subReference = reference.child("countData");

        subReference.setValue(mCountData);
    }

    /**
     * returns the timestamp from the url off thread
     */
    public class SplitUrl implements Runnable{

        private int position;
        private ArrayList<String> list;

        public SplitUrl() {
        }

        public SplitUrl(int position, ArrayList<String> list) {
            this.position = position;
            this.list = list;
        }

        @Override
        public void run() {
            returnImageTimeStamp(position, list);
        }

        /**
         * returns the timestamp from the url
         */
        public String returnImageTimeStamp(int position, ArrayList<String> list){
            String url = list.get(position);
            String[] splitUrl = url.split("%2F");
            String secondUrl = splitUrl[1];
            String[] secondSplitUrl = secondUrl.split("\\?");
            mFinalUrl = secondSplitUrl[0];
            return mFinalUrl;
        }
    }
}
