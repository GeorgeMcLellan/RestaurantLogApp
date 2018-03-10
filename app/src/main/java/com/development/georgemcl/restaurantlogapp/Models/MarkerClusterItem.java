package com.development.georgemcl.restaurantlogapp.Models;


import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by George on 5/03/2018.
 */

public class MarkerClusterItem implements ClusterItem{
    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private Object mTag;

    public MarkerClusterItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public MarkerClusterItem(double lat, double lng, String title, String snippet, Object tag) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mTag = tag;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() { return mTitle; }

    @Override
    public String getSnippet() { return mSnippet; }

    public Object getTag() { return mTag; }

    /**
     * Set the title of the marker
     * @param title string to be set as title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Set the description of the marker
     * @param snippet string to be set as snippet
     */
    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }

    public void setTag(Object tag){ mTag = tag; }
}
