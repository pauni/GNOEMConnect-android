package org.pauni.gnomeconnect.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.activities.MainActivity;
import org.pauni.gnomeconnect.models.BatteryStatus;
import org.pauni.gnomeconnect.models.reports.MiscReport;
import org.pauni.gnomeconnect.models.PhoneStatus;
import org.pauni.gnomeconnect.models.reports.PowerReport;
import org.pauni.gnomeconnect.network.CommunicationManager;
import org.pauni.gnomeconnect.utils.Utils;

import java.io.ByteArrayOutputStream;

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
    String TYPE_NOTIFICATION  = "notification";
    BatteryStatus currentStatus = null;
    CommunicationManager comMgr;

    public ReportService() {

    }


    /**
     *      Override methods
     *
     * */
    @Override
    public void onCreate() {
        comMgr = new CommunicationManager();


        IntentFilter filters = new IntentFilter();
        filters.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filters.addAction(Intent.ACTION_HEADSET_PLUG);
        filters.addAction(Intent.ACTION_BATTERY_CHANGED);
        filters.addAction(Intent.ACTION_POWER_CONNECTED);
        filters.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filters.addAction(Intent.ACTION_USER_PRESENT);
        filters.addAction(Intent.ACTION_SHUTDOWN);
        filters.addAction(Intent.ACTION_SCREEN_ON);
        filters.addAction(Intent.ACTION_SCREEN_OFF);
        filters.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filters.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        registerReceiver(receiver, filters);


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
                    case Intent.ACTION_BATTERY_CHANGED:
                        onActionBatteryChanged(intent);
                        break;
                    case Intent.ACTION_USER_PRESENT:
                        onActionUserPresent(intent);
                        break;
                    case Intent.ACTION_SHUTDOWN:
                        break; // maybe later ...
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
        try {
            JSONObject report      = new JSONObject();  // root JSONObject
            JSONArray  reportArray = new JSONArray();   // JSONArray holding reports
            JSONObject notificationReport = createNotificationReport(sbn); // actual report

            reportArray.put(notificationReport);        // put the report into the array
            report.put("reports", reportArray);   // put the array into the root JSONObject

            // sendToAll notificationReport immediatly with out any delay

            // logging
            print(now()+"new notification from "+sbn.getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onActionAirplaneModeChanged(Intent i) {
        boolean isAirplaneModeOn = i.getBooleanExtra("state", false);
        PhoneStatus.setAirplaneMode(isAirplaneModeOn);

        // logging
        Log.i("ReportService", "onActionAirplaneModeChanged: " + isAirplaneModeOn);
        print(now()+"airplane-mode enabled="+isAirplaneModeOn);
    }

    private void onActionHeadsetPlug(Intent i) {
        boolean isPlugged    = 1==i.getIntExtra("state", 0);
        boolean isMicrophone = 1==i.getIntExtra("microphone", 0);
        String  headset      = i.getStringExtra("name");

        PhoneStatus.setHeadsetPluggedIn(isPlugged);
        PhoneStatus.setHeadsetHasMic(isMicrophone);
        PhoneStatus.setHeadsetName(headset);

        // logging
        print(now()+"headset is plugged="+isPlugged+" has microphone="+isMicrophone);
    }

    private void onActionBatteryChanged(Intent i) {
        // get the information from the intent
        int level       = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int temperature = i.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)-273;
        String charging = Utils.getChargingStateString(i.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1));

        BatteryStatus status = new BatteryStatus(level, temperature, charging);

        if (status.equals(currentStatus)) // compare currentStatus(old) with status(new)
            return;

        PowerReport powerReport = new PowerReport();
        comMgr.sendToAll(powerReport);
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

        // logging
        print(now()+"screen on="+isScreenOn);
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
        print(now()+"wifi has been enabled/disabled");
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

        // logging
        print(now()+"wifi-connection changed");
    }



    /**
     *      METHODS FOR THIS CLASSES METHODS
     */

    private JSONObject createNotificationReport(StatusBarNotification sbn) {
        Notification nfc = sbn.getNotification();
        Bundle extras    = nfc.extras;

        String title     = extras.getString(Notification.EXTRA_TITLE);
        String text      = ""+extras.getCharSequence(Notification.EXTRA_TEXT);
        String program   = getApplicationName(sbn.getPackageName());
        boolean ongoing  = sbn.isOngoing();
        Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
        Bitmap smallIcon = Utils.getSmallIcon(this, sbn);
        Bitmap extraImg  = Utils.getExtraImg(sbn);


        JSONObject notificationReport = new JSONObject();
        JSONObject information        = new JSONObject();

        try {
            information.put("title",     title);
            information.put("text",      text);
            information.put("program",   program);
            information.put("ongoing",   ongoing);
            information.put("largeIcon", compressAndEncode(largeIcon));
            information.put("smallIcon", compressAndEncode(smallIcon));
            information.put("extraImg",  compressAndEncode(extraImg));

            notificationReport.put("report_type", TYPE_NOTIFICATION);
            notificationReport.put("report",      information);
            return notificationReport;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getApplicationName(String packageName) {
        PackageManager packageManager= getApplicationContext().getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String compressAndEncode(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        // create outputstream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // compress data
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        // return compressed data as byteArray
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


    // print string on main activity layout
    private void print(final String txt) {
        MainActivity.log += txt+"\n";
        sendBroadcast(new Intent("logthecat"));
    }
    String now() {
        Time now = new Time();
        now.setToNow();

        return now.format("%H:%M")+"    ";
    }







}
