package com.example.dell.ves2.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dell.ves2.R;
import com.example.dell.ves2.Utility.HousesResponse;

import java.util.ArrayList;

public class HousesActivityMainRecViewAdapterClass extends RecyclerView.Adapter<HousesActivityMainRecViewAdapterClassVH> {

    ArrayList<HousesResponse> dataSource;
    Context context;


    public HousesActivityMainRecViewAdapterClass(ArrayList<HousesResponse> dataSource, Context context) {
        this.dataSource = dataSource;
        this.context = context;
    }

    @NonNull
    @Override
    public HousesActivityMainRecViewAdapterClassVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.houses_activity_rec_view_single_object, parent, false);
        return new HousesActivityMainRecViewAdapterClassVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HousesActivityMainRecViewAdapterClassVH holder, int position) {

        //set the text that shows the title of the house
        holder.title_vh.setText(dataSource.get(position).getTitle());

        //set the text that describes whether the item is for sale or for rent. Use the resource ids instead of string
        //literals to make it easier to translate
        if (dataSource.get(position).isRent()) {

            holder.rent_vh.setText(R.string.ForRent);

        } else {

            holder.rent_vh.setText(R.string.ForSale);

        }

        //set the text that shows the price of the house
        holder.price_vh.setText(dataSource.get(position).getPrice());

        //set the text that shows the location of the house
        holder.location_vh.setText(dataSource.get(position).getLocation());

        //set the image of the house using the glide image library
        Glide.with(context).load(dataSource.get(position).getMianImageReference()).into(holder.house_main_image_vh);


    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }




}
