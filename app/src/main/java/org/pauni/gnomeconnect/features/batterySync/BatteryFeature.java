package org.pauni.gnomeconnect.features.batterySync;

import android.content.Context;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCFeature;

/**
 *  Detect changes in battery's tmp, lvl and charging-state (chrg)
 *  and send them to the gnome desktops
 */

public class BatteryFeature implements GCFeature {
    private BatteryChangedListener batteryChangedListener = null;
    private Context context;

    @Override
    public void handle(JSONObject data) {
        // Handle battery updates from gnome desktop
    }

    @Override
    public void enable(Context context) {
        this.context = context;
        if (batteryChangedListener != null) {
            batteryChangedListener.start(context);
        }
    }

    @Override
    public void disable() {
        if (batteryChangedListener != null)
            batteryChangedListener.stop(context);
    }
}
