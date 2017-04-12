package com.mohammedjasam.cognitiverecommender;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LoadImages extends AppCompatActivity {
    private RecyclerView loadRecycler;
    private DatabaseReference databaseReference;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<imageData,ImageViewHolder> firebaseRecyclerAdapter=new
                FirebaseRecyclerAdapter<imageData, ImageViewHolder>(
                        imageData.class,
                        R.layout.load_image_row_layout,
                        ImageViewHolder.class,
                        databaseReference)
                {
                    @Override
                    protected void populateViewHolder(ImageViewHolder viewHolder, imageData model, int position) {
                        viewHolder.setImageLabel(model.getLabel());
                        viewHolder.setImage(getApplicationContext(),model.getImageLink());
                        viewHolder.setLocation(model.getLocation());

                    }
                };
        loadRecycler.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setImageLabel(String label){
            TextView imageLabel =(TextView)mView.findViewById(R.id.label_tv);
            imageLabel.setText(label);
        }
        public void setLocation(String location){
            TextView imageLocation =(TextView)mView.findViewById(R.id.location_tv);
            imageLocation.setText(location);
        }

        public void setImage(Context context, String imageUri){
            final ImageView photo=(ImageView)mView.findViewById(R.id.image_load_iv);
            Picasso.with(context).load(imageUri).into(photo);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_images);

        ActionBar bar= getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("ProcessedImages");

        loadRecycler=(RecyclerView) findViewById(R.id.load_recycler);
        loadRecycler.setHasFixedSize(true);
        loadRecycler.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private List<Address> gpsConverter(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        return addresses;

    }
}