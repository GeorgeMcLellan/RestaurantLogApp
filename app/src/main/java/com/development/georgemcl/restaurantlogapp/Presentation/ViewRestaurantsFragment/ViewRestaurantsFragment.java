package com.development.georgemcl.restaurantlogapp.Presentation.ViewRestaurantsFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.restaurantlogapp.Presentation.AddRestaurantActivity.AddRestaurantActivity;
import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ViewRestaurantsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ViewRestaurantsFragment";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int TYPE_RESTAURANT_FILTER = 79;

    private SharedPreferences mSharedPref;

    private Context mContext;

    View mView;

    FloatingActionButton buttonAddRestaurant;
    FloatingActionButton buttonChangeView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_view_restaurants, container, false);
        mContext = getContext();
        mSharedPref = mContext.getSharedPreferences("MapOrList", MODE_PRIVATE);
        buttonAddRestaurant = mView.findViewById(R.id.view_restaurants_add_fab);
        buttonChangeView = mView.findViewById(R.id.view_restaurants_change_view_fab);
        buttonAddRestaurant.setOnClickListener(this);
        buttonChangeView.setOnClickListener(this);

        launchChosenDisplayFragment();

        return mView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                Log.i(TAG, "OnActivityResult: Place : " + place.getName());
                Log.i(TAG, "Place Types: " + place.getPlaceTypes());
                buildDialog(convertPlaceToRestaurant(place));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, data);
                // TODO: Handle the error.
                Log.e(TAG, "OnActivityResult: PlaceAutocomplete error : " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_restaurants_add_fab: {
                launchPlaceAutoComplete();
                break;
            }
            case R.id.view_restaurants_change_view_fab: {
                toggleFragmentInView();
                break;
            }
        }
    }

    private void toggleFragmentInView() {
        if (mSharedPref.getBoolean("isUsingMap", true)){
            buttonChangeView.setImageResource(R.drawable.ic_map_black_24dp);
            setMapSharedPref(false);
            replaceFragment(new ListDisplayFragment());
        }else{
            buttonChangeView.setImageResource(R.drawable.ic_list_black_24dp);
            setMapSharedPref(true);
            replaceFragment(new MapDisplayFragment());
        }
    }

    private void setMapSharedPref(boolean isUsingMap) {
        mSharedPref.edit().putBoolean("isUsingMap", isUsingMap).apply();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_restaurants_fragment_container, fragment).commit();
    }

    private void launchChosenDisplayFragment() {
        if (mSharedPref.getBoolean("isUsingMap", true)){
            buttonChangeView.setImageResource(R.drawable.ic_list_black_24dp);
            replaceFragment(new MapDisplayFragment());
        }else{
            buttonChangeView.setImageResource(R.drawable.ic_map_black_24dp);
            replaceFragment(new ListFragment());
        }
    }

    public void launchPlaceAutoComplete() {
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
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "GooglePlayServicesRepairableException : " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "GooglePlayServicesNotAvailableException : " + e.getMessage());
        }
    }

    private void buildDialog(final Restaurant restaurant){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Dialog_Alert);
        dialogBuilder.setTitle("Add Restaurant")
                .setMessage("Add this restaurant to your eats?")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Add the restaurant to db & map
                        //addRestaurantToDb(restaurant);
                        addRestaurant(restaurant);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void addRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(getContext(), AddRestaurantActivity.class);
        intent.putExtra(getString(R.string.id), restaurant.getId())
                .putExtra(getString(R.string.name), restaurant.getName())
                .putExtra(getString(R.string.lat), restaurant.getLatLng().latitude)
                .putExtra(getString(R.string.lng), restaurant.getLatLng().longitude)
                .putExtra(getString(R.string.rating), restaurant.getRating());
        startActivity(intent);
    }

    private Restaurant convertPlaceToRestaurant(Place place) {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(place.getId());
        restaurant.setName(place.getName().toString());
        restaurant.setAddress(place.getAddress().toString());
        restaurant.setPhoneNumber(place.getPhoneNumber().toString());
        restaurant.setWebsiteUri(place.getWebsiteUri());
        restaurant.setLatLng(place.getLatLng());
        restaurant.setRating(place.getRating());
        // restaurant.setAttributions(place.getAttributions().toString());
        Log.i(TAG, "onComplete: Restaurant details : " + restaurant.toString());
        return restaurant;
    }

    public LatLngBounds getUserLocationBounds() {
        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = String.valueOf(manager.getBestProvider(new Criteria(), true));

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getUserLocationBounds : Permission has not been granted");
            return new LatLngBounds(
                    new LatLng(0, 0),
                    new LatLng(0, 0));
        }
        Location location = manager.getLastKnownLocation(bestProvider);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(TAG, "getUserLocationBounds : Acquired user location at : " + latitude + " " + longitude);
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
