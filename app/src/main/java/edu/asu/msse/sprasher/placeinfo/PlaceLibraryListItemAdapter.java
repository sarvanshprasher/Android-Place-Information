package edu.asu.msse.sprasher.placeinfo;

/*
Copyright 2020 Sarvansh Prasher

Rights to use this code given to Arizona State University
& Professor Timothy Lindquist (Tim.Lindquist@asu.edu) for course SER 423

@author Sarvansh Prasher mailto:sprasher@asu.edu

@version 4th February ,2020

*/


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlaceLibraryListItemAdapter extends RecyclerView.Adapter<PlaceLibraryListItemAdapter.ViewHolder> {

    private static final String TAG = "PlaceLibraryListItemAda";

    private List<PlaceDescription> places = new ArrayList<>();
    private Context mContext;
    private ListClickListener listClickListener;

    public PlaceLibraryListItemAdapter(List<PlaceDescription> places, Context context) {

        this.places = places;
        mContext = context;
        listClickListener = (ListClickListener)context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_place_libray_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder : called");
        final PlaceDescription pd = places.get(position);
        holder.name.setText(places.get(position).getName());
        holder.categoryName.setText(places.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.onItemClicked(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            categoryName = itemView.findViewById(R.id.itemCategoryName);

        }
    }
}
