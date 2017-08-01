package com.example.zark.baking.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Andrew Osborne on 7/29/2017.
 *
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new IngredientsRemoteViewsFactory(this.getApplicationContext(),
                intent));
    }
}
