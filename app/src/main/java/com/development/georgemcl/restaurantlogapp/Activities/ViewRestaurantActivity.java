package com.development.georgemcl.restaurantlogapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.development.georgemcl.restaurantlogapp.Database.RestaurantDbHelper;
import com.development.georgemcl.restaurantlogapp.Utils.Utilities;

import com.development.georgemcl.restaurantlogapp.R;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


/**
 * Created by George on 05-Feb-18.
 */

public class ViewRestaurantActivity extends AppCompatActivity {
    private static final String TAG  = "ViewRestaurantActivity";

    private String mPlaceId;

    TextView nameTxt;
    TextView cuisineTxt;
    TextView ratingTxt;
    RatingBar priceRb;
    RatingBar ratingRb;
    ImageView imageView;
    FloatingActionButton editFab;
    RestaurantDbHelper restaurantDb;
    GeoDataClient mGeoDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_restaurant);

        nameTxt = (TextView) findViewById(R.id.nameTxt);
        cuisineTxt = (TextView) findViewById(R.id.cuisineTxt);
        ratingTxt = (TextView) findViewById(R.id.ratingTxt);

        ratingRb = findViewById(R.id.ratingRb);
        priceRb = findViewById(R.id.priceRb);
        editFab = findViewById(R.id.editFab);
        imageView = findViewById(R.id.placeIv);

        restaurantDb = new RestaurantDbHelper(this);

        final Bundle bundle = getIntent().getExtras();

        mPlaceId = bundle.getString(getString(R.string.id));

        Cursor cursor = restaurantDb.getRowById(mPlaceId);
        populateView(cursor);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        getPhotos();

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
            priceRb.setRating(cursor.getFloat(5));
            ratingRb.setRating(cursor.getFloat(6));
        }
    }


    public void getPhotos() {
        try {
            final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(mPlaceId);
            photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                    // Get the list of photos.
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    // Get the first photo in the list.
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                    // Get the attribution text.
                    CharSequence attribution = photoMetadata.getAttributions();
                    // Get a full-size bitmap for the photo.
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            });
        }
        catch(IllegalStateException e){
            Log.d(TAG, "IllegalStateException in getPhotos(): " + e.getMessage());
        }
    }
}
