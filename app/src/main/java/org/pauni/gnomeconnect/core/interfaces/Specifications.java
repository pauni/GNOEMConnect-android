package org.pauni.gnomeconnect.core.interfaces;

/**
 *      Contains all keys/field names we use. It's all in one file to make it
 *      easier to maintain, make changes etc.
 */

public interface Specifications {


    /**
     *      JSON KEYS. NAMES OF THE FIELDS USED IN GNOME CONNECT
     */


    class Packet {
        // PACKET KEYS
        public static final String FINGERPRINT = "fingerprint";
        public static final String VERSION     = "version";
        public static final String PAYLOAD     = "payload";
    }

    class Payload {
        // PAYLOAD KEYS
        public static final String TYPE = "payload_type";
        public static final String DATA = "payload_data";
    }

    class Device {
        // PAIR REQUEST
        public static final String FINGERPRINT  = "fingerprint";
        public static final String PUBLIC_KEY   = "public_key";
        public static final String MODEL        = "device_model";
        public static final String NAME         = "device_name";
        public static final String OS           = "device_os";
    }

    class PowerReport {
        // POWER REPORT KEYS
        public static final String REPORT_BATTERY_LEVEL       = "battery_level";
        public static final String REPORT_BATTERY_CHARGING    = "battery_charging";
        public static final String REPORT_BATTERY_TEMPERATURE = "battery_temperature";
    }


    // CONNECTIVITY REPORT KEYS
    String REPORT_WIFI_ENABLED   = "wifi_enabled";
    String REPORT_WIFI_BSSID     = "wifi_bssid";
    String REPORT_WIFI_STRENGTH  = "wifi_strength";
    String REPORT_CELL_NETWORK   = "cell network";


    // MISC REPORT KEYS
    String AIRPLANE_MODE        = "airplane_mode";
    String HEADSET_PLUGGED_IN   = "headset_plugged_in";
    String HEADSET_HAS_MIC      = "headset_has_mic";
    String HEADSET_NAME         = "headset_name";
    String PHONE_UNLOCKED       = "phone_unlocked";
    String SCREEN_ON            = "screen_on";





    /**
     *      JSON VALUES. VALUES OF CERTAIN FIELDS USED IN GNOME CONNECT
     */

    // PAYLOAD TYPE VALUES
    String TYPE_NOTIFICATION_REPORT = "notification_report";
    String TYPE_CONNECTIVITY_REPORT = "connectivity_report";
    String TYPE_POWER_REPORT        = "power_report";
    String TYPE_MISC_REPORT         = "misc_report";
    String TYPE_PAIRREQUEST         = "pairrequest";


    // POWER REPORT VALUES
    String BATTERY_NOT_CHARGING = "not_charging";
    String BATTERY_CHARGING_AC  = "charging_ac";
    String BATTERY_CHARGING_USB = "charging_usb";
    String BATTERY_CHARGING_WIRELESS = "charging_wireless";


    /**
     *      PORT
     */

    int NETWORK_PORT = 4112;
}
