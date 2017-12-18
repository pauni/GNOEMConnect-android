package org.pauni.gnomeconnect.core.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.pauni.gnomeconnect.features.notificationsSync.NotificationsFeature;

/**
 * Created by roni on 18.12.17.
 */

public class GCBackgroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        startFeatures();
    }

    private void startFeatures() {
        NotificationsFeature notifyFeature = new NotificationsFeature();
        notifyFeature.enable(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
