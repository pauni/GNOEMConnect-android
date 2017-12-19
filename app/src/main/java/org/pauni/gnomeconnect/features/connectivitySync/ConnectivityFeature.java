package org.pauni.gnomeconnect.features.connectivitySync;


import android.content.Context;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCFeature;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 *  Detect changes of the connectivity state and send them to the gnome desktops
 */

public class ConnectivityFeature implements GCFeature, Protocol {
    private ConnectivityChangedListener conChangedListener;
    private Context context;

    @Override
    public void handle(JSONObject data) {

    }

    @Override
    public void enable(Context context) {
        this.context = context;
        if (conChangedListener != null) {
            conChangedListener.start(context);
        } else {
            conChangedListener = new ConnectivityChangedListener();
            conChangedListener.start(context);
        }
    }

    @Override
    public void disable() {
        if (conChangedListener != null) {
            conChangedListener.stop(context);
        }
    }



}
