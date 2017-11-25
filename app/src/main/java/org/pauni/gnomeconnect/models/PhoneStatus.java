package org.pauni.gnomeconnect.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *      This class represents the phone by having all
 *      attributes which are synched with the computer. So if
 *      something on the phone changes, the changes are immediately
 *      applied to this class.
 */

public class PhoneStatus {
    public static final String TYPE_POWER         = "power";
    public static final String TYPE_CONNECTIVITY  = "connectivity";
    public static final String TYPE_MISCELLANEOUS = "miscellaneous";


    // all variables have default values to prevent unnecessary JSONExceptions
    private static boolean airplaneMode     = false;
    private static boolean headsetPluggedIn = false;
    private static boolean headsetHasMic    = false;
    private static boolean phoneUnlocked    = false;
    private static boolean shuttingDown     = false;
    private static boolean screenOn         = false;
    private static boolean wifiEnabled      = false;
    private static boolean wifiConnected    = false;


    private static int  batteryLevel        = 0;
    private static int  batteryTemperature  = 0;
    private static int  wifiSignalStrength  = 0;


    private static String BSSID             = "none";
    private static String SSID              = "none";
    private static String localIP           = "none";
    private static String cellNetwork       = "none";
    private static String headsetName       = "none";
    private static String charging          = "none";




    /************************************
     ↓           POWER REPORT           ↓
     ************************************/

    public static JSONObject getPowerReport() {
        // powerReport consists of the header-field 'report_type' and the field 'report'
        // which contains the actual information about the power state
        JSONObject powerReport = new JSONObject();
        JSONObject information = new JSONObject();

        try {
            information.put("level",      batteryLevel);
            information.put("charging",   charging);
            information.put("temperatur", batteryTemperature);

            powerReport.put("report_type", TYPE_POWER);
            powerReport.put("report",      information);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // returning the powerReport JSONObject
        return powerReport;
    }



    /************************************
     ↓       CONNECTIVITY REPORT        ↓
     ************************************/

    public static JSONObject getConnectivityReport() {
        JSONObject connectivityReport = new JSONObject();
        JSONObject information        = new JSONObject();

        try {
            information.put("wifi_enabled",   wifiEnabled);
            information.put("wifi_connected", wifiConnected);
            information.put("wifi_bssid",     BSSID);
            information.put("wifi_ssid",      SSID);
            information.put("wifi_strength",  wifiSignalStrength);
            information.put("local_ip",       localIP);
            information.put("cell_network",   cellNetwork);

            connectivityReport.put("report_type", TYPE_CONNECTIVITY);
            connectivityReport.put("report",      information);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // returning the connectivityReport JSONObject
        return connectivityReport;
    }



    /************************************
     ↓       MISCELLANEOUS REPORT       ↓
     ************************************/

    public static JSONObject getMiscellaneousReport() {
        JSONObject miscellaneousReport = new JSONObject();
        JSONObject information         = new JSONObject();

        try {
            information.put("airplane_mode",    airplaneMode);
            information.put("headset_plugged_in", headsetPluggedIn);
            information.put("headset_has_mic",  headsetHasMic);
            information.put("headset_name",     headsetName);
            information.put("phone_unlocked",    phoneUnlocked);
            information.put("screen_on",         screenOn);

            miscellaneousReport.put("report_type", TYPE_MISCELLANEOUS);
            miscellaneousReport.put("report",      information);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // returning the miscellaneousReport JSONObject
        return miscellaneousReport;
    }



    /************************************
     ↓          SETTER METHODS          ↓
     ************************************/

    // boolean setter
    public static void setAirplaneMode     (boolean airplaneMode) {
        PhoneStatus.airplaneMode = airplaneMode;
    }
    public static void setHeadsetPluggedIn (boolean headsetPluggedIn) {
        PhoneStatus.headsetPluggedIn = headsetPluggedIn;
    }
    public static void setHeadsetHasMic    (boolean headsetHasMic) {
        PhoneStatus.headsetHasMic = headsetHasMic;
    }
    public static void setPhoneUnlocked    (boolean phoneUnlocked) {
        PhoneStatus.phoneUnlocked = phoneUnlocked;
    }
    public static void setShuttingDown     (boolean shuttingDown) {
        PhoneStatus.shuttingDown = shuttingDown;
    }
    public static void setScreenOn         (boolean screenOn) {
        PhoneStatus.screenOn = screenOn;
    }
    public static void setWifiEnabled      (boolean wifiEnabled) {
        Log.d("PhoneStatus", "setting: wifiEnabled="+wifiEnabled);
        PhoneStatus.wifiEnabled = wifiEnabled;
    }
    public static void setWifiConnected    (boolean wifiConnected) {
        PhoneStatus.wifiConnected = wifiConnected;
    }

    // String setter
    public static void setBSSID        (String BSSID) {
        PhoneStatus.BSSID = BSSID;
    }
    public static void setSSID         (String SSID) {
        PhoneStatus.SSID = SSID;
    }
    public static void setLocalIP      (String localIP) {
        PhoneStatus.localIP = localIP;
    }
    public static void setCellNetwork  (String cellNetwork) {
        PhoneStatus.cellNetwork = cellNetwork;
    }
    public static void setBatteryState (BatteryStatus status) {
        batteryLevel        = status.getLevel();
        batteryTemperature  = status.getTemperature();
        charging            = status.getCharging();
    }
    public static void setHeadsetName  (String headsetName) {
        headsetName = headsetName;
    }

    // int setter
    public static void setWifiSignalStrength (int wifiSignalStrength) {
        PhoneStatus.wifiSignalStrength = wifiSignalStrength;
    }

}
