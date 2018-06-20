package com.development.georgemcl.restaurantlogapp.Presentation.FindRestaurants;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.restaurantlogapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindRestaurantsFragment extends Fragment {

    private static final String TAG = "FindRestaurantsFragment";

    private View mView;

    public FindRestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_find_restaurants, container, false);



        return mView;
    }

}
