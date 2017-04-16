package com.ambercam.android.camera2basic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ambercam.android.camera2basic.R;
import com.ambercam.android.camera2basic.models.PurchaseItem;
import com.ambercam.android.camera2basic.util.Util;

import java.util.ArrayList;

/**
 * Created by brianroper on 10/3/16.
 */
public class PurchaseAdapter extends ArrayAdapter {

    private Button mPurchaseButton;
    private TextView mAmountTextView;
    private ArrayList<PurchaseItem> mPurchaseList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public PurchaseAdapter(Context context, ArrayList<PurchaseItem> purchaseList) {
        super(context, R.layout.purchase_list, purchaseList);
        this.mPurchaseList = purchaseList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.purchase_item, parent, false);
        }

        mPurchaseButton = (Button)convertView.findViewById(R.id.purchase_button);
        mAmountTextView = (TextView)convertView.findViewById(R.id.purchase_textview);

        mAmountTextView.setText(mPurchaseList.get(position).getAmount());
        mPurchaseButton.setText(mPurchaseList.get(position).getPrice());

        setPurchaseButtonListener();

        return convertView;
    }

    /**
     * listener for purchase button
     */
    public void setPurchaseButtonListener(){
        mPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.incompleteToast(mContext);
            }
        });
    }
}
