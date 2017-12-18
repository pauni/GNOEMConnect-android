package org.pauni.gnomeconnect.features.notificationsSync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCFeature;

/**
 * Created by roni on 18.12.17.
 */

public class NotificationsFeature implements GCFeature {
    private Context context;

    @Override
    public void handle(JSONObject data) {

    }

    @Override
    public void enable(Context context) {
        Log.i("NotiFeature", "enable");
        this.context = context;
        context.startService(new Intent(context, NotificationListenerSv.class));
    }

    @Override
    public void disable() {
        context.stopService(new Intent(context, NotificationListenerSv.class));
    }
}
