package org.pauni.gnomeconnect.core.interfaces;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by roni on 18.12.17.
 */

public interface GCFeature {

    // pass data from GCServer to the feature (e.g. a gnome-notification)
    void handle(JSONObject data);

    // enable a feature
    void enable(Context context);

    // disable a feature
    void disable();
}
