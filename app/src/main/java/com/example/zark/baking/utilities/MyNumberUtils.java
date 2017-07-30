package com.example.zark.baking.utilities;

import java.text.DecimalFormat;

/**
 * Created by Andrew Osborne on 7/11/17.
 *
 */

public class MyNumberUtils {

    /**
     * Takes an unformatted double (i.e. 1.0) and transforms this into a format appropriate for recipes (i.e. 1)
     *
     * @param unformattedQuantity that will be converted to a formatted number
     * @return the double into a format appropriate for recipe ingredients
     */
    public static String formatQuantityToString(Double unformattedQuantity) {
        String formattedValue = new DecimalFormat("0.####").format(unformattedQuantity);
        return formattedValue;
    }
}
