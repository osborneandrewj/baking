package com.example.zark.baking.utilities;

import com.squareup.otto.Bus;

/**
 * Created by Andrew Osborne on 7/11/17.
 *
 */

public class RecipeBus {

    private static Bus sBus;

    public static Bus getBus() {

        if (sBus == null) {
            sBus = new Bus();
        }

        return sBus;
    }
}
