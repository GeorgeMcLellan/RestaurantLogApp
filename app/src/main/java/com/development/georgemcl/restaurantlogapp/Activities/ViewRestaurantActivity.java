package com.development.georgemcl.restaurantlogapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_restaurant);

        nameTxt = (TextView) findViewById(R.id.nameTxt);
        cuisineTxt = (TextView) findViewById(R.id.cuisineTxt);
        ratingTxt = (TextView) findViewById(R.id.ratingTxt);
        priceLevelTxt = (TextView) findViewById(R.id.priceLevelTxt);
        ratingRb = findViewById(R.id.ratingRb);

        Bundle bundle = getIntent().getExtras();

        mPlaceId = bundle.getString(getString(R.string.id));

        nameTxt.setText(bundle.getString(getString(R.string.name)));
        cuisineTxt.setText(bundle.getString(getString(R.string.cuisine)));
        ratingRb.setRating(bundle.getFloat(getString(R.string.rating)));
        int priceLevel = bundle.getInt(getString(R.string.priceLevel));
        String priceAsDollars;
        switch (priceLevel){
            case 1: priceAsDollars = "$";
                break;
            case 2: priceAsDollars = "$$";
                break;
            case 3: priceAsDollars = "$$$";
                break;
            case 4: priceAsDollars = "$$$$";
                break;
            default: priceAsDollars = "";
        }
        priceLevelTxt.setText(priceAsDollars);


    }
}
