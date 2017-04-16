package com.ambercam.android.camera2basic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ambercam.android.camera2basic.models.CloudImage;
import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.ui.DetailActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private LayoutInflater mLayoutInflater;
    private ImageView mImageView;

    private ArrayList<String> mUrlList = new ArrayList<>();
    private ArrayList<CloudImage> mFireBaseDataSet = new ArrayList<>();

    public ImageAdapter(Context context,
                        final DatabaseReference databaseReference) {
        mContext = context;
        mDatabaseReference = databaseReference;
        mLayoutInflater = LayoutInflater.from(context);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CloudImage cloudImage = dataSnapshot.getValue(CloudImage.class);
                mFireBaseDataSet.add(cloudImage);
                mUrlList.add(cloudImage.getUrl());
                notifyDataSetChanged();
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
        databaseReference.addChildEventListener(childEventListener);
        mChildEventListener = childEventListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater = LayoutInflater.from(mContext);
        final View view = mLayoutInflater.inflate(R.layout.gallery_item, parent, false);
        final ImageViewHolder mImageViewHolder = new ImageViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewPosition = mImageViewHolder.getAdapterPosition();

                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putStringArrayListExtra("list", mUrlList);
                detailIntent.putExtra("position", viewPosition);
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //TODO: shared content transitions

                mContext.startActivity(detailIntent);
            }
        });

        return mImageViewHolder;
    }

    @Override
    public int getItemCount() {
        return mFireBaseDataSet.size();
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        CloudImage cloudImage = mFireBaseDataSet.get(position);

        holder.mImageView.setPadding(2, 2, 2, 2);
        holder.mImageView.setAdjustViewBounds(true);

        Glide.with(mContext)
                .load(cloudImage.getUrl())
                .centerCrop()
                .into(holder.mImageView);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;

        public ImageViewHolder(View itemView){
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.gallery_item);
        }
    }
}
