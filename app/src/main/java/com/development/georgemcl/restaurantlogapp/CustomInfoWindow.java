package com.development.georgemcl.restaurantlogapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.R;
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
        TextView ratingTxt = view.findViewById(R.id.ratingTxt);
        TextView priceLevelTxt = view.findViewById(R.id.priceLevelTxt);

        Restaurant restaurant = (Restaurant) marker.getTag();

        nameTxt.setText(restaurant.getName());
        cuisineTxt.setText(restaurant.getCuisine());
        ratingTxt.setText(restaurant.getRating()+"");
        priceLevelTxt.setText(String.valueOf(restaurant.getPriceLevel()));


        return view;
    }
}
