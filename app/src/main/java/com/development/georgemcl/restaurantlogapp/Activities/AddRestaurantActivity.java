package com.development.georgemcl.restaurantlogapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.development.georgemcl.restaurantlogapp.Database.RestaurantDbHelper;
import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.R;

public class AddRestaurantActivity extends AppCompatActivity {

    TextView nameTxt;
    Spinner cuisineSpn;
    SeekBar priceSb;
    RatingBar ratingRb;
    Button addBtn;

    private static final String TAG  = "AddRestaurantActivity";

    private Bundle mBundle;
    private RestaurantDbHelper restaurantDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        nameTxt = (TextView) findViewById(R.id.nameTxt);
        cuisineSpn = (Spinner) findViewById(R.id.cuisineSpn);
        priceSb = (SeekBar) findViewById(R.id.priceSb);
        ratingRb = (RatingBar) findViewById(R.id.ratingRb);
        addBtn = (Button) findViewById(R.id.addBtn);

        restaurantDb = new RestaurantDbHelper(this);

        mBundle = getIntent().getExtras();

        ArrayAdapter cuisineAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cuisines));
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpn.setAdapter(cuisineAdapter);

        prepopulateFields();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurantToDb();
            }
        });

    }

    private void prepopulateFields() {

        nameTxt.setText(mBundle.getString(getString(R.string.name)));
        ratingRb.setRating(mBundle.getFloat(getString(R.string.rating)));
    }

    private void addRestaurantToDb() {
        boolean isInserted = restaurantDb.insertData(
                mBundle.getString(getString(R.string.id)),
                mBundle.getString(getString(R.string.name)),
                mBundle.getDouble(getString(R.string.lat)),
                mBundle.getDouble(getString(R.string.lng)),
                cuisineSpn.getSelectedItem().toString(),
                getPrice(),
                ratingRb.getRating()
                );
        if (isInserted){
            Log.d(TAG,"INSERTED");
            Toast.makeText(this, "Restaurant Added", Toast.LENGTH_SHORT).show();
            launchIntent();
        }else{
            Log.d(TAG, "NOT INSERTED");
        }

    }

    private void launchIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public int getPrice() {
        return priceSb.getProgress();
    }
}
