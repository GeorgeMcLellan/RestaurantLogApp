package com.development.georgemcl.restaurantlogapp.Activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.development.georgemcl.restaurantlogapp.Fragments.FindRestaurantsFragment;
import com.development.georgemcl.restaurantlogapp.Fragments.MapFragment;
import com.development.georgemcl.restaurantlogapp.Fragments.RestaurantListFrag;
import com.development.georgemcl.restaurantlogapp.Fragments.ShareFragment;
import com.development.georgemcl.restaurantlogapp.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        replaceFragment(new MapFragment());
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new MapFragment());
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



}
