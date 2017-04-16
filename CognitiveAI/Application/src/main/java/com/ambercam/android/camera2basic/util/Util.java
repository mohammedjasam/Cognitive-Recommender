package com.ambercam.android.camera2basic.util;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

/**
 * Created by brianroper on 8/4/16.
 */
public class Util {

    /**
     * initializes and shows a progress dialog
     */
    public static MaterialDialog showProgressDialog(Context context){
        MaterialDialog builder = new MaterialDialog.Builder(context)
                .title("Uploading...")
                .content("please wait")
                .progress(true, 0)
                .cancelable(false)
                .show();
        return builder;
    }

    /**
     * gets the devices screen height
     */
    public static int returnScreenHeight(Context context){
        WindowManager windowManager = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    /**
     * gets the devices screen width
     */
    public static int returnScreenWidth(Context context){
        WindowManager windowManager = (WindowManager)context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    /**
     * check the device for an active network connection
     */
    static public boolean activeNetworkCheck(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * check for valid email format
     */
    static public boolean isValidEmail(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * returns the first half of an email address
     */
    static public String returnSplitEmail(String email){
        String[] splitEmail = email.split("@");
        return splitEmail[0];
    }

    /**
     * toast that displays for unfinished features
     */
    static public void incompleteToast(Context context){
        Toast.makeText(context, "This feature isn't finished yet", Toast.LENGTH_SHORT).show();
    }

    /**
     * toast displayed to user when there is no available network
     */
    static public void noActiveNetworkToast(Context context){
        Toast.makeText(context, "There is currently no active network connection."
                + " Cognitive Recommender requires a network connection.", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param array
     * @param message
     * easy logging for String arrays
     */
    public static void logArray(String[] array, String message){
        for(int i = 0; i <= array.length; i++){
            Log.i(message, array[i]);
        }
    }

    /**
     * @param list
     * @param message
     * easy logging for String array lists
     */
    public static void logList(ArrayList<String> list, String message){
        for(int i = 0; i <= list.size(); i++){
            Log.i(message, list.get(i));
        }
    }
}
