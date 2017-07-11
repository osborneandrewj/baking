package com.example.zark.baking.utilities;

import java.text.DecimalFormat;

/**
 * Created by Andrew Osborne on 7/11/17.
 *
 */

public class MyNumberUtils {

    public static String formatQuantityToString(Double unformattedQuantity) {
        String formattedValue = new DecimalFormat("0.####").format(unformattedQuantity);
        return formattedValue;
    }
}
