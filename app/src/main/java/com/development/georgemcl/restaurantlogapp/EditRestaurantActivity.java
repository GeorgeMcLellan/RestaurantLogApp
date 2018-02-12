package com.development.georgemcl.restaurantlogapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

import com.development.georgemcl.restaurantlogapp.Activities.MainActivity;
import com.development.georgemcl.restaurantlogapp.Database.RestaurantDbHelper;

public class EditRestaurantActivity extends AppCompatActivity {

    TextView nameTxt;
    Spinner cuisineSpn;
    SeekBar priceSb;
    RatingBar ratingRb;
    Button saveBtn, removeBtn;

    private static final String TAG  = "EditRestaurantActivity";

    private Bundle mBundle;
    private String placeId;
    private RestaurantDbHelper restaurantDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);

        nameTxt = (TextView) findViewById(R.id.nameTxt);
        cuisineSpn = (Spinner) findViewById(R.id.cuisineSpn);
        priceSb = (SeekBar) findViewById(R.id.priceSb);
        ratingRb = (RatingBar) findViewById(R.id.ratingRb);
        saveBtn = (Button) findViewById(R.id.addBtn);
        removeBtn = findViewById(R.id.removeBtn);

        restaurantDb = new RestaurantDbHelper(this);

        mBundle = getIntent().getExtras();

        placeId = mBundle.getString(getString(R.string.id));

        ArrayAdapter cuisineAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cuisines));
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineSpn.setAdapter(cuisineAdapter);

        populateFields();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modifyRestaurantInDb())
                {
                    Log.d(TAG,"Modified");
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    launchIntent();
                }
                else{
                    Log.d(TAG, "NOT Modified");
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog();

            }
        });
    }

    private void buildDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        dialogBuilder.setTitle("Remove Restaurant")
                .setMessage("Remove this restaurant from your eats?")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (restaurantDb.deleteData(placeId)){
                            Log.d(TAG,"Removed restaurant");
                            Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                            launchIntent();
                        }else{
                            Log.d(TAG, "NOT Removed");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void launchIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean modifyRestaurantInDb() {

        return restaurantDb.updateData(placeId,
                mBundle.getString(getString(R.string.name)),
                cuisineSpn.getSelectedItem().toString(),
                priceSb.getProgress(),
                ratingRb.getRating()
                );

    }

    private void populateFields() {
        nameTxt.setText(mBundle.getString(getString(R.string.name)));
        cuisineSpn.setSelection(getPosition());
        priceSb.setProgress(mBundle.getInt(getString(R.string.priceLevel)));
        ratingRb.setRating(mBundle.getFloat(getString(R.string.rating)));
    }

    public int getPosition() {
        String cuisine = mBundle.getString(getString(R.string.cuisine));
        String[] cuisinesArray = getResources().getStringArray(R.array.cuisines);
        for(int i = 0; i < cuisinesArray.length; i++){
            if (cuisinesArray[i].equals(cuisine)){
                Log.d(TAG,"Pos = "+i);
                return i;
            }
        }
        return 0;
    }
}
