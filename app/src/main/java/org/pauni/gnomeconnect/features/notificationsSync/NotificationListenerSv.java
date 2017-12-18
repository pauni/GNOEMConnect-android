package org.pauni.gnomeconnect.features.notificationsSync;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.pauni.gnomeconnect.core.models.Prefs;
import org.pauni.gnomeconnect.core.communication.CommunicationManager;

/**
 * Created by roni on 18.12.17.
 */

public class NotificationListenerSv extends NotificationListenerService {
    CommunicationManager comMgr;

    String TAG = "NotificationListenerSv";

    @Override
    public void onCreate() {
        new Prefs(this);
        comMgr = new CommunicationManager();
        Log.i("NotificationListenerSv", "onCreate()");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent mIntent) {
        return super.onBind(mIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        log("onNotiicationpostered");
        comMgr.sendToAll(new NotificationReport(sbn, this));
    }

    private void log(String string) {
        Log.i("NotificationListenerSv", string);
    }
}
