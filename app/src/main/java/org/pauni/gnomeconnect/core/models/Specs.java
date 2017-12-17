package org.pauni.gnomeconnect.core.models;

/**
 *      Contains all keys/field names we use. It's all in one file to make it
 *      easier to maintain, make changes etc.
 */

public class Specs {


    /**
     *      JSON KEYS. NAMES OF THE FIELDS USED IN GNOME CONNECT
     */


    public class Packet {
        // PACKET KEYS
        public static final String FINGERPRINT = "fingerprint";
        public static final String VERSION     = "version";
        public static final String PAYLOAD     = "payload";
    }

    public class Payload {
        // PAYLOAD KEYS
        public static final String TYPE = "payload_type";
        public static final String DATA = "payload_data";
    }

    public class Device {
        // PAIR REQUEST
        public static final String FINGERPRINT  = "fingerprint";
        public static final String PUBLIC_KEY   = "public_key";
        public static final String MODEL        = "device_model";
        public static final String NAME         = "device_name";
        public static final String OS           = "device_os";
    }

    public class PowerReport {
        // POWER REPORT KEYS
        public static final String REPORT_BATTERY_LEVEL       = "battery_level";
        public static final String REPORT_BATTERY_CHARGING    = "battery_charging";
        public static final String REPORT_BATTERY_TEMPERATURE = "battery_temperature";
    }


    // CONNECTIVITY REPORT KEYS
    public static final String REPORT_WIFI_ENABLED   = "wifi_enabled";
    public static final String REPORT_WIFI_BSSID     = "wifi_bssid";
    public static final String REPORT_WIFI_STRENGTH  = "wifi_strength";
    public static final String REPORT_CELL_NETWORK   = "cell network";


    // MISC REPORT KEYS
    public static final String AIRPLANE_MODE        = "airplane_mode";
    public static final String HEADSET_PLUGGED_IN   = "headset_plugged_in";
    public static final String HEADSET_HAS_MIC      = "headset_has_mic";
    public static final String HEADSET_NAME         = "headset_name";
    public static final String PHONE_UNLOCKED       = "phone_unlocked";
    public static final String SCREEN_ON            = "screen_on";





    /**
     *      JSON VALUES. VALUES OF CERTAIN FIELDS USED IN GNOME CONNECT
     */

    // PAYLOAD TYPE VALUES
    public static final String TYPE_NOTIFICATION_REPORT = "notification_report";
    public static final String TYPE_CONNECTIVITY_REPORT = "connectivity_report";
    public static final String TYPE_POWER_REPORT        = "power_report";
    public static final String TYPE_MISC_REPORT         = "misc_report";
    public static final String TYPE_PAIRREQUEST         = "pairrequest";


    // POWER REPORT VALUES
    public static final String BATTERY_NOT_CHARGING = "not_charging";
    public static final String BATTERY_CHARGING_AC  = "charging_ac";
    public static final String BATTERY_CHARGING_USB = "charging_usb";
    public static final String BATTERY_CHARGING_WIRELESS = "charging_wireless";


    /**
     *      PORT
     */

    public static final int NETWORK_PORT = 4112;
}
