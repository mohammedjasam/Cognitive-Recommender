package com.mohammedjasam.cognitiverecommender;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import clarifai2.dto.prediction.Concept;

import static com.mohammedjasam.cognitiverecommender.MainActivity.counter;

public class predictions_adapter extends RecyclerView.Adapter<predictions_adapter.ViewHolder> {

    public List<Concept> list;
    static public String IMAGE_KEY ="IMAGE";
    static public String LABEL_KEY ="LABEL";
    static public String LAT_KEY="Latitude";
    static public String LON_KEY="Longitude";
    private List<String> values = new ArrayList<String>();

    public predictions_adapter setData(List<Concept> concepts) {
        list=concepts;
        return this;
    }
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("User");
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       ViewHolder holder= new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_predictions, parent, false));

        /*return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_predictions, parent, false));*/
        return holder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Concept concept = list.get(position);
        holder.predictions.setText(concept.name() != null ? concept.name() : concept.id());
        holder.percentage.setText(String.valueOf(concept.value()));

        for(int i=0; i<list.size();i++)
        {
            final Concept c = list.get(i);
            Log.d("      Property      ", c.name());
            Log.d("      Percentage      ", String.valueOf(c.value()));

            String a=c.name();
            String b=String.format("%f",  c.value()* 100);
            String fin="{"+a+":"+b+"}";
//            String c = String.valueOf("{"a,":"b""}")";
//            mConditionRef.setValue(c.name(),String.valueOf(c.value()));
            values.add(fin);

        }
        String holly = "User #"+String.valueOf(counter);
        mConditionRef.child(String.valueOf(counter)).setValue(values);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context=v.getContext();
                Bitmap image=MainActivity.imageBitmap;
                String label =concept.name();
                if(!label.isEmpty()&& image!=null){

                    Intent i=new Intent(context,CloudUpload.class);
                    i.putExtra(IMAGE_KEY,image);
                    i.putExtra(LABEL_KEY,label);
                    i.putExtra(LAT_KEY,MainActivity.currentLatitude);
                    i.putExtra(LON_KEY,MainActivity.currentLongitude);
                    context.startActivity(i);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
              return list.size();
        //return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView predictions;
        TextView percentage;
        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            predictions = (TextView) itemView.findViewById(R.id.predictions_tv);
            percentage = (TextView) itemView.findViewById(R.id.predictions_percentage_tv);

//            Log.d("Predictions is ", predictions.toString());
//            Log.d("percentage is ", percentage.toString());

//            Log.d("      Property      ", concept.name());
//            Log.d("      Percentage      ", String.valueOf(concept.value()));

        }
    }
}