package org.pauni.gnomeconnect.core.interfaces;

import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.utils.Utils;

import java.security.Key;

/**
 *      Contains all keys/field names we use. It's all in one file to make it
 *      easier to maintain, make changes etc.
 */

public interface Protocol {


    /**
     *      JSON KEYS. NAMES OF THE FIELDS USED IN GNOME CONNECT
     */

    class Commands {
        public static String buildConnectionRequest(String type) throws JSONException {
            JSONObject connectionRequest = new JSONObject();
            connectionRequest.put(Keys.ConnectionRequest.FINGERPRINT, Utils.getFingerprint());
            connectionRequest.put(Keys.ConnectionRequest.TYPE, type);
            return connectionRequest.toString();
        }
    }

    class Keys {
        public class ConnectionRequest {
            public static final String FINGERPRINT   = "fingerprint";
            public static final String TYPE          = "type";
            public static final String AUTHENTICATED = "authenticated";
        }

        public class Packet {
            public static final String SRC_FINGERPRINT = "src_fingerprint";
            public static final String DST_FINGERPRINT = "dst_fingerprint";
            public static final String VERSION         = "version";
            public static final String PAYLOAD         = "payload";
        }

        public class Pairing {
            public static final String STEP = "step";
            public static final String DATA = "data";
        }

        public class Device {
            public static final String FINGERPRINT  = "fingerprint";
            public static final String PUBLIC_KEY   = "public_key";
            public static final String DEVICENAME   = "devicename";
            public static final String OS           = "os";

        }

        public class Report {
            public static final String BATTERY_LEVEL       = "battery_level";
            public static final String BATTERY_CHARGING    = "battery_charging";
            public static final String BATTERY_TEMPERATURE = "battery_temperature";

            public static final String WIFI_ENABLED  = "wifi_enabled";
            public static final String WIFI_BSSID    = "wifi_bssid";
            public static final String WIFI_STRENGTH = "wifi_strength";
            public static final String CELL_NETWORK  = "cell network";

            public static final String AIRPLANE_MODE        = "airplane_mode";
            public static final String HEADSET_PLUGGED_IN   = "headset_plugged_in";
            public static final String HEADSET_HAS_MIC      = "headset_has_mic";
            public static final String HEADSET_NAME         = "headset_name";
            public static final String PHONE_UNLOCKED       = "phone_unlocked";
            public static final String SCREEN_ON            = "screen_on";
        }

    }


    /**
     *      JSON VALUES. VALUES OF CERTAIN FIELDS USED IN GNOME CONNECT
     */

    class Values {
        public class Packet {

        }

        public class ConnectionRequest {
            public static final String TYPE_PAIRING   = "pairing";
            public static final String TYPE_ENCRYPTED = "encrypted";
        }

        public class Payload {
            public static final String TYPE_USERDATA = "userdata";
            public static final String TYPE_PAIRING  = "pairing";
        }

        public class Pairing {
            public static final int STEP_1 = 1;
            public static final int STEP_2 = 2;
            public static final int STEP_3 = 3;
            public static final int STEP_4 = 4;
            public static final int STEP_5 = 5;
            public static final int STEP_6 = 6;
            public static final int STEP_7 = 7;
            public static final int STEP_8 = 8;
        }

        // PAYLOAD TYPE VALUES
        public static final String TYPE_MISC_REPORT         = "misc_report";


        // POWER REPORT VALUES
        public static final String BATTERY_NOT_CHARGING = "not_charging";
        public static final String BATTERY_CHARGING_AC  = "charging_ac";
        public static final String BATTERY_CHARGING_USB = "charging_usb";
        public static final String BATTERY_CHARGING_WIRELESS = "charging_wireless";

    }
    /**
     *      PORT
     */

    int TCP_PORT = 4112;
}
