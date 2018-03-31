package com.development.georgemcl.restaurantlogapp.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.development.georgemcl.restaurantlogapp.Fragments.FindRestaurantsFragment;
import com.development.georgemcl.restaurantlogapp.Fragments.MapFragment;
import com.development.georgemcl.restaurantlogapp.Fragments.RestaurantListFrag;
import com.development.georgemcl.restaurantlogapp.Fragments.ShareFragment;
import com.development.georgemcl.restaurantlogapp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private BottomNavigationView navigation;

    private SharedPreferences mSharedPref;
    private FloatingActionButton addFab;
    private FloatingActionButton changeViewFab;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int TYPE_RESTAURANT_FILTER = 79;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        addFab = findViewById(R.id.addFab);
        changeViewFab = findViewById(R.id.changeViewFab);
        addFab.setOnClickListener(this);
        changeViewFab.setOnClickListener(this);
        mSharedPref = getSharedPreferences("MapOrList", MODE_PRIVATE);
        launchChosenDisplayFragment();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFab : {
                launchPlaceAutoComplete();
                break;
            }
            case R.id.changeViewFab : {
                if (mSharedPref.getBoolean("isUsingMap", true)){
                    replaceFragment(new RestaurantListFrag());
                    setMapSharedPref(false);
                }else{
                    replaceFragment(new MapFragment());
                    setMapSharedPref(true);
                }
                break;
            }
        }
    }



    public void replaceFragment(Fragment fragment){
        if (fragment instanceof MapFragment || fragment instanceof  RestaurantListFrag) {
            setButtonVisibility(View.VISIBLE);
        }
        else {
            setButtonVisibility(View.INVISIBLE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment).commit();
    }

    private void launchChosenDisplayFragment(){
        if (mSharedPref.getBoolean("isUsingMap", true)){
            replaceFragment(new MapFragment());
        }else{
            replaceFragment(new RestaurantListFrag());
        }
    }

    private void setMapSharedPref(boolean isUsingMap) {
        mSharedPref.edit().putBoolean("isUsingMap", isUsingMap).apply();
    }

    private void setButtonVisibility(int buttonVisibility){
        addFab.setVisibility(buttonVisibility);
        changeViewFab.setVisibility(buttonVisibility);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    launchChosenDisplayFragment();
                    return true;
                case R.id.navigation_find:
                    replaceFragment(new FindRestaurantsFragment());
                    return true;
                case R.id.navigation_share:
                    replaceFragment(new ShareFragment());
                    return true;
            }
            return false;
        }
    };

    public void setHomeAsSelectedNav(){
        navigation.getMenu().getItem(0).setChecked(true);
    }

    //Adding Restaurant

    private void launchPlaceAutoComplete() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
            .setTypeFilter(TYPE_RESTAURANT_FILTER)
                .setTypeFilter(34)
            .build();
        LatLngBounds userLatLng = getUserLocationBounds();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setBoundsBias(userLatLng)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "GooglePlayServicesRepairableException : " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "GooglePlayServicesNotAvailableException : " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "OnActivityResult: Place : " + place.getName());
                Log.i(TAG, "Place Types: " + place.getPlaceTypes());

                Bundle placeData = new Bundle();
                placeData.putString("PlaceId", place.getId());

                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(placeData);
                replaceFragment(mapFragment);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e(TAG, "OnActivityResult: PlaceAutocomplete error : " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public LatLngBounds getUserLocationBounds() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = String.valueOf(manager.getBestProvider(new Criteria(), true));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getUserLocationBounds : Permission has not been granted");
            return new LatLngBounds(
                    new LatLng(0, 0),
                    new LatLng(0, 0));
        }
        Location location = manager.getLastKnownLocation(bestProvider);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(TAG, "getUserLocationBounds : Acquired user location at : "+latitude+" "+longitude );
            return new LatLngBounds(
                    new LatLng(latitude - 0.03, longitude - 0.03),
                    new LatLng(latitude + 0.03, longitude + 0.03)
            );
        }
        Log.d(TAG, "getUserLocationBounds : User location is null");
        return new LatLngBounds(
                new LatLng(0, 0),
                new LatLng(0, 0));
    }



}
