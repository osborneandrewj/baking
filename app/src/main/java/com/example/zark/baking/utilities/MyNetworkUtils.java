package com.example.zark.baking.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Andrew Osborne on 7/10/17.
 *
 */

public class MyNetworkUtils {

    private MyNetworkUtils() {
        // Never needs to be created
    }

    /**
     * Check for internet connection.
     *
     * @param context The context.
     * @return The boolean "true" if internet connection exists.
     */
    public static boolean doesNetworkConnectionExist(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
