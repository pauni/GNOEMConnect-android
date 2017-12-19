package org.pauni.gnomeconnect.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.pauni.gnomeconnect.features.batterySync.BatteryFeature;
import org.pauni.gnomeconnect.features.connectivitySync.ConnectivityFeature;
import org.pauni.gnomeconnect.features.notificationsSync.NotificationsFeature;

/**
 * Created by roni on 18.12.17.
 */

public class GCBackgroundService extends Service {
    NotificationsFeature notificationsFeature;
    ConnectivityFeature connectivityFeature;
    BatteryFeature batteryFeature;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeFeatures();
        enableFeatures();
    }

    private void initializeFeatures() {
        notificationsFeature = new NotificationsFeature();
        connectivityFeature = new ConnectivityFeature();
        batteryFeature = new BatteryFeature();
    }

    private void enableFeatures() {
        notificationsFeature.enable(this);
        batteryFeature.enable(this);
    }

    private void disableFeatures() {
        notificationsFeature.disable();
        batteryFeature.disable();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
