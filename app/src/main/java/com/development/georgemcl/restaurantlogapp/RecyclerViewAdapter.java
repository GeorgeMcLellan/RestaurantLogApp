package com.development.georgemcl.restaurantlogapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.restaurantlogapp.Activities.ViewRestaurantActivity;
import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.Utils.Utilities;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by George on 16-Feb-18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private LinkedList<Restaurant> restaurantList;
    private Context mContext;

    public RecyclerViewAdapter(Context context, LinkedList<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i(TAG, "onBindViewHolder called");
        final Restaurant restaurant = restaurantList.get(position);
        final String name = restaurant.getName();
        holder.nameTxt.setText(name);
        holder.priceRb.setRating(restaurant.getPriceLevel());
        holder.ratingRb.setRating(restaurant.getRating());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRestaurant(restaurant);
                Snackbar.make(holder.nameTxt, name, Toast.LENGTH_SHORT);
            }
        });
    }

    private void viewRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(mContext, ViewRestaurantActivity.class);
        intent.putExtra(mContext.getString(R.string.id), restaurant.getId());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTxt;
        RatingBar ratingRb;
        RatingBar priceRb;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            priceRb = itemView.findViewById(R.id.priceRb);
            ratingRb = itemView.findViewById(R.id.ratingRb);
            parentLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
