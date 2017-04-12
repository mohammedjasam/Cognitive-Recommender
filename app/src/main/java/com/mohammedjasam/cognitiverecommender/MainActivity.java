package com.mohammedjasam.cognitiverecommender;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {
    static int counter=0;
    private FloatingActionButton uploadFABBtn;

    final int PICK_IMAGE = 1;
    final int REQUEST_IMAGE_CAPTURE = 2;

    final static String clientId = "21IgMMOfoj5kD4gAOUtKjJN3sNWbwpkCxpzwRH3b";//"TWLwx0Svio0V5WKuRZ1HejSVyYZtYTu8MydOT3yI";
    final static String clientSecret = "JHFF2_adodG9-Av7AJEk4vyBKnPu3e-rZ_S0cTh0";//"1Lc3sttcMfHXzUcgZe3HVxkyWLHo77C3H6LmqxNs";


    static public Bitmap imageBitmap = null;
    static public byte[] imageInBytes = null;

    private final predictions_adapter adapter = new predictions_adapter();
    private ImageView imageView;
    private RecyclerView recyclerView;


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    static public double currentLatitude;
    static public double currentLongitude;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("User");

    @Override
    protected void onStart() {
        super.onStart();

        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i=new Intent(MainActivity.this,LoadImages.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


        TextView hintTv = (TextView) findViewById(R.id.hint_tv);
        hintTv.setText("Please click on Action Button to continue!");


        uploadFABBtn = (FloatingActionButton) findViewById(R.id.uploadFAB);

        uploadFABBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), PICK_IMAGE);

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                ++counter;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = (ImageView) findViewById(R.id.main_image_view);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

            // Log.d("harsimarSingh", data.getData().toString());


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            if (byteArray != null) {
                imageInBytes = byteArray;
                onImagePicked(byteArray);
            }

        }
        switch (requestCode) {
            case PICK_IMAGE:
                Log.d("mohammedjasam", "picked");

                //ProgressDialog pd=new ProgressDialog(MainActivity.this);
                final ProgressDialog pd = ProgressDialog.show(this, "Working..", "Image Conversion", true, false);

                imageView = (ImageView) findViewById(R.id.main_image_view);
                Uri uri = data.getData();
                imageView.setImageURI(uri);
                Handler handler = new Handler(Looper.getMainLooper());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final byte[] imageBytes = getImageByte(MainActivity.this, data);
                        if (imageBytes != null) {
                            onImagePicked(imageBytes);
                        }
                        Log.d("mohammedjasam", "inside");
                        pd.dismiss();
                    }
                }, 100);
                break;
        }
    }


    private byte[] getImageByte(Context context, Intent data) {
        InputStream inStream = null;
        Bitmap bitmap = null;

        try {
            inStream = context.getContentResolver().openInputStream(data.getData());
            bitmap = BitmapFactory.decodeStream(inStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return outStream.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }


    private void onImagePicked(final byte[] imageBytes) {

        final ProgressDialog mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setTitle("Contacting API");
        mProgress.setMessage("Please Wait...");
        mProgress.show();
        imageView = (ImageView) findViewById(R.id.main_image_view);
        final TextView hint = (TextView) findViewById(R.id.hint_tv);
        uploadFABBtn.setEnabled(false);
        adapter.setData(Collections.<Concept>emptyList());

        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {

            @Override
            protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                Log.d("mohammedjasam", "inside Async");

                ClarifaiClient client = new ClarifaiBuilder(clientId, clientSecret)
                        .client(new OkHttpClient()).buildSync();
                return client.getDefaultModels().generalModel().predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes))
                        ).executeSync();


                // Working on Custom models!
//                return client.predict("ProfileGen").withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes))
//                        ).executeSync();
//                return  client.getModelByID("ProfileGen").executeSync().get().predict().withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)));
            }

            @Override
            protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                super.onPostExecute(response);
                mProgress.dismiss();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                uploadFABBtn.setEnabled(true);

                if (!response.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "Problem Contacting API", Toast.LENGTH_SHORT).show();
                    adapter.setData(Collections.<Concept>emptyList());
                    uploadFABBtn.setEnabled(true);
                    return;
                }
                hint.setText("Select Appropriate Label : ");
                final List<ClarifaiOutput<Concept>> clarifaiOutputs = response.get();
                Log.d("            Clarify  op", clarifaiOutputs.toString());
                if (clarifaiOutputs.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty Response From API", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Log.d("mohammedjasam", clarifaiOutputs.get(0).data().toString());
                adapter.setData(clarifaiOutputs.get(0).data());
//                mConditionRef.setValue();

                recyclerView.setAdapter(adapter);

            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

         /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();


    }

}
