package com.mohammedjasam.cognitiverecommender;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CloudUpload extends AppCompatActivity {

    private static final int MAX_LENGTH = 30;
    Bitmap image=null;
    private double latitude;
    private double longitude;
    private String label;
    private String uploadAddress;
    private List<Address> addresses= Collections.emptyList();

    private ImageView imageView;
    private EditText labelText;
    private EditText gpsText;
    private Button uploadBtn;
    private Uri mImageUri;

    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        Firebase.setAndroidContext(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_cloud_upload);
        Bundle activitySentData= getIntent().getExtras();
        image=activitySentData.getParcelable(predictions_adapter.IMAGE_KEY);
        latitude=activitySentData.getDouble(predictions_adapter.LAT_KEY);
        longitude=activitySentData.getDouble(predictions_adapter.LON_KEY);
        label=activitySentData.getString(predictions_adapter.LABEL_KEY);

        imageView=(ImageView)findViewById(R.id.imageView);
        labelText=(EditText)findViewById(R.id.label_et);
        gpsText=(EditText)findViewById(R.id.gps_et);
        uploadBtn=(Button)findViewById(R.id.fireabaseUpload_button);

        labelText.setText(label);
        imageView.setImageBitmap(image);

        try {
            addresses=gpsConverter(latitude,longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        gpsText.setText(address+", "+city+", "+state);

        uploadAddress=address+", "+city+", "+state;
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFirebaseUpload();
            }

            
        });
        
        
    }
    private List<Address> gpsConverter(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return addresses;

    }

    private void startFirebaseUpload() {

        final ProgressDialog mProgress=new ProgressDialog(CloudUpload.this);
        mProgress.setMessage("Uploading To Firebase...");
        mProgress.setTitle("Please Wait");
        mProgress.show();
        firebaseInit();

        mStorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("ProcessedImages");

        StorageReference filepath=
                mStorage.child("MobileCaptures").child(random());
        filepath.putBytes(converBitmap(image)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri downloadUri = taskSnapshot.getDownloadUrl();

                DatabaseReference newPost = mDatabase.push();
                Log.d("harsimarSINGH", newPost.toString());

                newPost.child("imageLink").setValue(downloadUri.toString().trim());
                newPost.child("label").setValue(label);
                newPost.child("location").setValue(uploadAddress);

                mProgress.dismiss();
                Toast.makeText(CloudUpload.this, "Done Uploading.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }

    private byte[] converBitmap(Bitmap imageBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void firebaseInit() {
        Firebase.setAndroidContext(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }


}
