package com.development.georgemcl.restaurantlogapp.Presentation.ViewRestaurantsFragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.development.georgemcl.restaurantlogapp.Activities.ViewRestaurantActivity;
import com.development.georgemcl.restaurantlogapp.Database.RestaurantDbHelper;
import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.R;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapDisplayFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private static final String TAG = "MapDisplayFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int MAP_ZOOM = 15;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    private Marker mCurrentMarker;

    private SharedPreferences mSharedPreferences;
    private RestaurantDbHelper restaurantDb;

    private View mView;
    private MapView mMapView;


    public MapDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_map, container, false);

        restaurantDb = new RestaurantDbHelper(getContext());

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        getLocationPermission();

        mSharedPreferences = getActivity().getSharedPreferences("CameraPosition",Context.MODE_PRIVATE);

        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady() called");
        mMap = googleMap;
        CustomInfoWindow customInfoWindow = new CustomInfoWindow(getContext());
        mMap.setInfoWindowAdapter(customInfoWindow);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setPadding(0,0,50,270);
        moveCameraToUserLocation();
        if (!moveCameraToSavedPosition())
            moveCameraToUserLocation();
        populateMap();
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        Restaurant restaurant = (Restaurant)marker.getTag();
        Log.d(TAG, "Price level = " +restaurant.getPriceLevel());
        Intent intent = new Intent(getContext(), ViewRestaurantActivity.class);
        intent.putExtra(getString(R.string.id),restaurant.getId());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause Called");
        if (mMap != null){
            Log.d(TAG, "OnPause - saving camera position");
            saveCameraPosition();
        }
    }

    private void saveCameraPosition() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        //LatLng cameraPosition =  mMap.getCameraPosition().target;
        mSharedPreferences.edit().putFloat(getString(R.string.lat),(float) cameraPosition.target.latitude)
                .putFloat(getString(R.string.lng), (float) cameraPosition.target.longitude)
                .putFloat(getString(R.string.Zoom),cameraPosition.zoom)
                .apply();
    }

    private boolean moveCameraToSavedPosition() {
        LatLng targetPosition = new LatLng(
                mSharedPreferences.getFloat(getString(R.string.lat), -1),
                mSharedPreferences.getFloat(getString(R.string.lng), -1)
        );
        if (targetPosition.latitude == -1 || targetPosition.longitude == -1){
            return false;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPosition, mSharedPreferences.getFloat(getString(R.string.Zoom), -1));
        mMap.moveCamera(cameraUpdate);
        return true;


    }

    private void moveCameraToUserLocation() {
        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //mMap.setOnMarkerClickListener(this);

            //Move camera to device location
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            String bestProvider = String.valueOf(manager.getBestProvider(new Criteria(), true));

            Location location = manager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.d(TAG, "onMapReady : Moving to users location (lat: "+location.getLatitude()+" long: "+location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), MAP_ZOOM));
            }
        }
    }


    private void populateMap() {
        Cursor cursor = restaurantDb.getAllData();
        Restaurant currentRestaurant;
        while(cursor.moveToNext()){
            currentRestaurant = new Restaurant();
            currentRestaurant.setId(cursor.getString(0));
            currentRestaurant.setName(cursor.getString(1));
            LatLng latlng = new LatLng(cursor.getDouble(2), cursor.getDouble(3));
            currentRestaurant.setLatLng(latlng);
            currentRestaurant.setCuisine(cursor.getString(4));
            currentRestaurant.setPriceLevel(cursor.getFloat(5));
            currentRestaurant.setRating(cursor.getFloat(6));

            MarkerOptions options = new MarkerOptions()
                    .position(latlng);
            Marker marker = mMap.addMarker(options);
            marker.setTag(currentRestaurant);
        }
    }


    private void initMap() {
        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null){
            Log.d(TAG, "initMap : Loading Map");
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    /*****************
    ------------------------------------- Permissions ----------------------------------------------
     *****************/

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission : getting location permissions");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG,"onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG,"onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }



}
