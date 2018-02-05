package com.development.georgemcl.restaurantlogapp.Models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by George on 09-Nov-17.
 */

public class Restaurant {
    //Default
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private Uri websiteUri;
    private LatLng latLng;
    private float rating;
    private String attributions;

    //Added
    private String cuisine;
    private int priceLevel;

    public Restaurant(String id, String name, String address, String phoneNumber, Uri websiteUri, LatLng latLng, String attributions, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;
        this.attributions = attributions;
    }

    public Restaurant(String id, String name, String address, String phoneNumber, Uri websiteUri, LatLng latLng, String attributions, float rating, String cuisine, int priceLevel) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.websiteUri = websiteUri;
        this.latLng = latLng;
        this.rating = rating;
        this.attributions = attributions;
        this.cuisine = cuisine;
        this.priceLevel = priceLevel;
    }

    public Restaurant(String id, String name, LatLng latLng, float rating,  String cuisine, int priceLevel) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
        this.rating = rating;
        this.cuisine = cuisine;
        this.priceLevel = priceLevel;
    }

    public Restaurant(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                " id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", websiteUri=" + websiteUri +
                ", latLng='" + latLng +
                ", rating='" + rating +
                ", attributions='" + attributions + '\'' +
                ", cuisine='" + cuisine + '\''+
                ", priceLevel='" + priceLevel +'\''+
                '}';
    }
}
