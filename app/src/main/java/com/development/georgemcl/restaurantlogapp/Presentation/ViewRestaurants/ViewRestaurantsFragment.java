package com.development.georgemcl.restaurantlogapp.Presentation.ViewRestaurants;

import android.Manifest;
import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.restaurantlogapp.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ViewRestaurantsFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ViewRestaurantsFragment";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int TYPE_RESTAURANT_FILTER = 79;

    private SharedPreferences mSharedPref;

    private Context mContext;

    View mView;

    @BindView(R.id.view_restaurants_add_fab)
    FloatingActionButton buttonAddRestaurant;

    @BindView(R.id.view_restaurants_change_view_fab)
    FloatingActionButton buttonChangeView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_view_restaurants, container, false);
        mContext = getContext();
        mSharedPref = mContext.getSharedPreferences("MapOrList", MODE_PRIVATE);
        launchChosenDisplayFragment();

        buttonAddRestaurant.setOnClickListener(this);
        buttonChangeView.setOnClickListener(this);

        return mView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addFab : {
                launchPlaceAutoComplete();
                break;
            }
            case R.id.changeViewFab : {
                toggleFragmentInView();
                break;
            }
        }
    }

    private void toggleFragmentInView() {
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.view_restaurants_fragment_container, fragment).commit();
    }

    private void launchChosenDisplayFragment(){
        if (mSharedPref.getBoolean("isUsingMap", true)){
            replaceFragment(new MapFragment());
        }else{
            replaceFragment(new RestaurantListFrag());
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mContext, data);
                Log.i(TAG, "OnActivityResult: Place : " + place.getName());
                Log.i(TAG, "Place Types: " + place.getPlaceTypes());

                Bundle placeData = new Bundle();
                placeData.putString("PlaceId", place.getId());

                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(placeData);
                replaceFragment(mapFragment);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, data);
                // TODO: Handle the error.
                Log.e(TAG, "OnActivityResult: PlaceAutocomplete error : " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
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
