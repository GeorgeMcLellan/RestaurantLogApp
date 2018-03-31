package com.development.georgemcl.restaurantlogapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.Utils.Utilities;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by George on 04-Feb-18.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Context mContext;

    public CustomInfoWindow(Context context) {
        mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)mContext).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView nameTxt = view.findViewById(R.id.nameTxt);
        TextView cuisineTxt = view.findViewById(R.id.cuisineTxt);
        RatingBar ratingBar = view.findViewById(R.id.ratingRb);
        RatingBar priceRb = view.findViewById(R.id.priceRb);

        Restaurant restaurant = (Restaurant) marker.getTag();

        nameTxt.setText(restaurant.getName());
        cuisineTxt.setText(restaurant.getCuisine());
        ratingBar.setRating(restaurant.getRating());
        priceRb.setRating(restaurant.getPriceLevel());

        return view;
    }
}
