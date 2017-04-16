package com.ambercam.android.camera2basic.models;

/**
 * Created by brianroper on 7/26/16.
 */
public class User {

    private String mEmail;
    private String mPassword;

    public User() {
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
