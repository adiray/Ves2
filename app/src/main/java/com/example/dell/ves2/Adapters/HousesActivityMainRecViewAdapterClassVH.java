package com.example.dell.ves2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.ves2.R;


//this is the view holder class for the first recycler view in the houses activity


public class HousesActivityMainRecViewAdapterClassVH extends RecyclerView.ViewHolder {

//declaring the views

    TextView title_vh, location_vh, rent_vh, price_vh;
    ImageView house_main_image_vh;


    public HousesActivityMainRecViewAdapterClassVH(View itemView) {

        //calling the super constructor
        super(itemView);

        //assigning the previously declared views
        title_vh = itemView.findViewById(R.id.house_title);
        location_vh = itemView.findViewById(R.id.house_location);
        price_vh = itemView.findViewById(R.id.house_price);
        rent_vh = itemView.findViewById(R.id.house_transaction_type);
        house_main_image_vh = itemView.findViewById(R.id.house_main_image);


    }
}







