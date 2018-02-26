package com.development.georgemcl.restaurantlogapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.development.georgemcl.restaurantlogapp.Database.RestaurantDbHelper;
import com.development.georgemcl.restaurantlogapp.Utils.Utilities;

import com.development.georgemcl.restaurantlogapp.R;


/**
 * Created by George on 05-Feb-18.
 */

public class ViewRestaurantActivity extends AppCompatActivity {
    private static final String TAG  = "ViewRestaurantActivity";

    private String mPlaceId;

    TextView nameTxt;
    TextView cuisineTxt;
    TextView ratingTxt;
    TextView priceLevelTxt;
    RatingBar ratingRb;
    FloatingActionButton editFab;
    RestaurantDbHelper restaurantDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_restaurant);

        nameTxt = (TextView) findViewById(R.id.nameTxt);
        cuisineTxt = (TextView) findViewById(R.id.cuisineTxt);
        ratingTxt = (TextView) findViewById(R.id.ratingTxt);
        priceLevelTxt = (TextView) findViewById(R.id.priceLevelTxt);
        ratingRb = findViewById(R.id.ratingRb);
        editFab = findViewById(R.id.editFab);

        restaurantDb = new RestaurantDbHelper(this);

        final Bundle bundle = getIntent().getExtras();

        mPlaceId = bundle.getString(getString(R.string.id));

        Cursor cursor = restaurantDb.getRowById(mPlaceId);
        populateView(cursor);

        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditRestaurantActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void populateView(Cursor cursor) {
        while (cursor.moveToNext()){
            nameTxt.setText(cursor.getString(1));
            cuisineTxt.setText(cursor.getString(4));
            priceLevelTxt.setText(Utilities.convertPriceLevelToDollarSign(cursor.getInt(5)));
            ratingRb.setRating(cursor.getFloat(6));
        }
    }

}
