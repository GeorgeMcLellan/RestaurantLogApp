package com.development.georgemcl.restaurantlogapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class FindRestaurantsFragment extends Fragment {

    private static final String TAG = "FindRestaurantsFragment";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private View mView;

    public FindRestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_find_restaurants, container, false);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
        LatLngBounds userLatLng = getUserLocationBounds();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(userLatLng)
                            .setFilter(typeFilter)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "GooglePlayServicesRepairableException : " + e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "GooglePlayServicesNotAvailableException : " + e.getMessage());
        }

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "OnActivityResult: Place : " + place.getName());

                Bundle placeData = new Bundle();
                placeData.putString("PlaceId", place.getId());

                MapFragment mapFragment = new MapFragment();
                mapFragment.setArguments(placeData);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mapFragment).commit();
                /*
                ((MainActivity)getActivity()).setViewAsSelectedNav();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, new ClockedOnEmployeeListFrag()).commit();
                 */

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.e(TAG, "OnActivityResult: PlaceAutocomplete error : " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public LatLngBounds getUserLocationBounds() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = String.valueOf(manager.getBestProvider(new Criteria(), true));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
