package org.pauni.gnomeconnect.core.models;



import android.content.Context;
import android.os.Build;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.Protocol;
import org.pauni.gnomeconnect.core.utils.Utils;

/**
 *    Yeah, I should describe what it does, but it's reallyobvious. It builds packets
 *    for request stuff thing
 */

public abstract class Pairing implements Protocol {


    /**
     *  Different packets (inside the Payload.data field)
     *  for requesting, accepting, denying pairing.
     */

    public static JSONObject buildDeviceInfo() {
        JSONObject device = new JSONObject();

        try {
            device.put(Keys.Device.FINGERPRINT, Utils.getFingerprint());
            device.put(Keys.Device.PUBLIC_KEY, Utils.getPublicKey());
            device.put(Keys.Device.DEVICENAME, Utils.getDeviceName());
            device.put(Keys.Device.OS, Build.VERSION.CODENAME + " " + Build.VERSION.SDK_INT);

            return device;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
