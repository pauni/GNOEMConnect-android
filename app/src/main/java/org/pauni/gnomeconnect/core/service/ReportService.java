package org.pauni.gnomeconnect.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


import org.pauni.gnomeconnect.BatteryWatcher;
import org.pauni.gnomeconnect.core.models.reports.MiscReport;
import org.pauni.gnomeconnect.core.models.PhoneStatus;
import org.pauni.gnomeconnect.core.models.reports.NotificationReport;
import org.pauni.gnomeconnect.core.models.reports.PowerReport;
import org.pauni.gnomeconnect.core.network.CommunicationManager;
import org.pauni.gnomeconnect.core.utils.Utils;


/**
 *      The ReportService detects various events (e.g. Wifi enabled)
 *      and updates the PhoneStatus.class accordingly, which is a
 *      representation of all information about the phone, we want
 *      to sync with the computer.
 *      Also the ReportService notifies the CommunicationManager about
 *      the changes, which will then take care of building a report and
 *      sendToAll it over to the computer.
 */

public class ReportService extends NotificationListenerService {
    BroadcastReceiver receiver;
    CommunicationManager comMgr;
    BatteryWatcher batteryWatcher;
    Context context;


    public ReportService() {
    }

    public ReportService(Context context) {
        this.context = context;
    }


    /**
     *      Override methods
     *
     * */
    @Override
    public void onCreate() {
        IntentFilter filters = new IntentFilter();
        filters.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filters.addAction(Intent.ACTION_HEADSET_PLUG);
        filters.addAction(BatteryWatcher.ACTION_BATTERY_CHANGED_RELEVANT);
        filters.addAction(Intent.ACTION_POWER_CONNECTED);
        filters.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filters.addAction(Intent.ACTION_USER_PRESENT);
        filters.addAction(Intent.ACTION_SHUTDOWN);
        filters.addAction(Intent.ACTION_SCREEN_ON);
        filters.addAction(Intent.ACTION_SCREEN_OFF);
        filters.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filters.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        registerReceiver(receiver, filters);

        comMgr = new CommunicationManager();
        batteryWatcher = new BatteryWatcher();
        batteryWatcher.start(this);

        Log.i("ReportService", "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    /*
     *      Broadcast receiver
     */
    {
         receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }

                switch (action) {
                    case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                        onActionAirplaneModeChanged(intent);
                        break;
                    case Intent.ACTION_HEADSET_PLUG:
                        onActionHeadsetPlug(intent);
                        break;
                    case BatteryWatcher.ACTION_BATTERY_CHANGED_RELEVANT:
                        comMgr.sendToAll(new PowerReport(intent));
                        break;
                    case Intent.ACTION_USER_PRESENT:
                        onActionUserPresent(intent);
                        break;
                    case Intent.ACTION_SHUTDOWN:
                        // maybe later ...
                        break;
                    case Intent.ACTION_SCREEN_ON:
                        onActionScreenOnOfChanged();
                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        onActionScreenOnOfChanged();
                        break;
                    case WifiManager.WIFI_STATE_CHANGED_ACTION:
                        onWifiStateChangedAction(intent);
                        break;
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                        onNetworkStateChangedAction(intent);
                        break;
                }
            }
        };
    }


    /**
     *      METHODS FOR HANDLING EACH EVENT FROM  THE
     *      BROADCAST RECEIVER (and any notifications)
     **/

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        comMgr.sendToAll(new NotificationReport(sbn, context));
    }

    private void onActionAirplaneModeChanged(Intent i) {
        boolean isAirplaneModeOn = i.getBooleanExtra("state", false);
        PhoneStatus.setAirplaneMode(isAirplaneModeOn);

        // logging
        Log.i("ReportService", "onActionAirplaneModeChanged: " + isAirplaneModeOn);
    }

    private void onActionHeadsetPlug(Intent i) {
        boolean isPlugged = 1==i.getIntExtra("state", 0);
        boolean hasMic    = 1==i.getIntExtra("microphone", 0);
        String  headset   = i.getStringExtra("name");

        MiscReport miscReport = new MiscReport();
        miscReport.setHeadsetPluggedIn(isPlugged);
        miscReport.setHeadsetHasMic(hasMic);
        miscReport.setHeadsetName(headset);

        comMgr.sendToAll(miscReport);
    }

    private void onActionUserPresent(Intent i) {
        MiscReport miscReport = new MiscReport();
        miscReport.setPhoneUnlocked(true);
        comMgr.sendToAll(miscReport);
    }

    private void onActionScreenOnOfChanged() {
        // See android documentation:
        // https://developer.android.com/reference/android/content/Intent.html#ACTION_SCREEN_ON
        boolean isScreenOn = Utils.isScreenOn(this);
        PhoneStatus.setScreenOn(isScreenOn);
        PhoneStatus.setPhoneUnlocked(Utils.isPhoneUnlocked(getApplicationContext()));

    }

    private void onWifiStateChangedAction(Intent i) {
        int state = i.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        switch (state) {
            case WifiManager.WIFI_STATE_ENABLED:
                PhoneStatus.setWifiEnabled(true);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                PhoneStatus.setWifiEnabled(false);
                break;
        }

        // logging
        Log.i("WifiStateChanged", "wifistate="+state );
    }

    private void onNetworkStateChangedAction(Intent i) {
        NetworkInfo nInfo = i.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if (nInfo.isConnected()) {
            WifiInfo wInfo =  i.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            PhoneStatus.setWifiConnected(true);
            PhoneStatus.setBSSID(wInfo.getBSSID());
            PhoneStatus.setSSID (wInfo.getSSID());
            PhoneStatus.setWifiSignalStrength(wInfo.getRssi());
            PhoneStatus.setLocalIP(String.valueOf(
                    Utils.convertIpAddressToString(wInfo.getIpAddress())));
        } else {
            PhoneStatus.setWifiConnected(false);
            PhoneStatus.setBSSID(null);
            PhoneStatus.setSSID (null);
            PhoneStatus.setWifiSignalStrength(-9000);
            PhoneStatus.setLocalIP(null);
        }
    }




}
