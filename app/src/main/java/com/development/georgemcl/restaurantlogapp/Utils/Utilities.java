package com.development.georgemcl.restaurantlogapp.Utils;

/**
 * Created by George on 24/02/2018.
 */

public class Utilities {

    private static final String TAG = "Utilities";


    public static String convertPriceLevelToDollarSign(int priceLevel){

        StringBuilder priceAsDollars = new StringBuilder();
        for (int i = 0; i <= priceLevel; i++){
            priceAsDollars.append("$");
        }
        return priceAsDollars.toString();

    }
}
