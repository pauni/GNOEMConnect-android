package org.pauni.gnomeconnect.models;

/**
 *      Contains all keys/field names we use.
 */

public class Specs {


    /**
     *      JSON KEYS. NAMES OF THE FIELDS USED IN GNOME CONNECT
     */

    public static final String HOSTNAME    = "hostname";
    public static final String FINGERPRINT = "fingerprint";
    public static final String VERSION     = "version";
    public static final String PAYLOAD     = "payload";


    public static final String PAYLOAD_TYPE       = "payload_type";
    public static final String PAYLOAD_DATA       = "payload_data";


    public static final String PUBLIC_KEY = "public_key";
    public static final String MODEL      = "model";
    public static final String OS         = "os";


    // Power
    public static final String REPORT_BATTERY_LEVEL        = "battery_level";
    public static final String REPORT_BATTERY_CHARGING     = "battery_charging";
    public static final String REPORT_BATTERY_TEMPERATURE  = "battery_temperature";


    // Wifi
    public static final String REPORT_WIFI_ENABLED         = "wifi_enabled";
    public static final String REPORT_WIFI_CONNECTED       = "wifi_connected";
    public static final String REPORT_WIFI_BSSID           = "wifi_bssid";
    public static final String REPORT_WIFI_STRENGTH        = "wifi_strength";
    public static final String REPORT_CELL_NETWORK         = "cell network";


    // Miscellanous
    public static final String AIRPLANE_MODE = "airplane_mode";
    public static final String HEADSET_PLUGGED_IN = "headset_plugged_in";
    public static final String HEADSET_HAS_MIC = "headset_has_mic";
    public static final String HEADSET_NAME = "headset_name";
    public static final String PHONE_UNLOCKED = "phone_unlocked";
    public static final String SCREEN_ON = "screen_on";





    /**
     *      JSON VALUES. VALUES OF CERTAIN FIELDS USED IN GNOME CONNECT
     */

    public static final String TYPE_PAIRREQUEST     = "pairrequest";
    public static final String TYPE_REPORT          = "report";

    public static final String MISC_REPORT         = "misc_report";
    public static final String POWER_REPORT        = "power_report";
    public static final String CONNECTIVITY_REPORT = "connectivity_report";


    /**
     *      PORT
     */

    public static final int NETWORK_PORT = 4112;
}
