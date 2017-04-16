package com.ambercam.android.camera2basic.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by brianroper on 12/16/16.
 */

public class FirebaseData {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setAuth(FirebaseAuth auth) {
        mAuth = auth;
    }

    public FirebaseAuth.AuthStateListener getAuthStateListener() {
        return mAuthStateListener;
    }

    public void setAuthStateListener(FirebaseAuth.AuthStateListener authStateListener) {
        mAuthStateListener = authStateListener;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase;
    }

    public void setFirebaseDatabase(FirebaseDatabase firebaseDatabase) {
        mFirebaseDatabase = firebaseDatabase;
    }

    public FirebaseStorage getFirebaseStorage() {
        return mFirebaseStorage;
    }

    public void setFirebaseStorage(FirebaseStorage firebaseStorage) {
        mFirebaseStorage = firebaseStorage;
    }

    public DatabaseReference getDatabaseReference() {
        return mDatabaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        mDatabaseReference = databaseReference;
    }

    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mFirebaseUser = firebaseUser;
    }
}
