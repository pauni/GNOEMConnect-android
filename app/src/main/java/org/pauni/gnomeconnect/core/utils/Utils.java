package org.pauni.gnomeconnect.core.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

/**
 * Outsourcing some methods to keep the code clean, readable, short etc.
 */

public class Utils {

    Utils() {}







    /*
    *   INFORMATION ABOUT THE PHONE
    */


    public static String getModelName() {
        return Build.MANUFACTURER + " " + Build.PRODUCT;
    }
    // returns true, if phone is ready for user-interactions
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm != null && pm.isInteractive();
    }

    // returns true, if lock screen is passed
    public static boolean isPhoneUnlocked(Context context) {
        KeyguardManager mgr = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        return !(mgr != null && mgr.isKeyguardSecure());
    }


    /*
    *   CONVERTING, GENERATE
    */
    public static String convertIpAddressToString(int ip4addr) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ip4addr = Integer.reverseBytes(ip4addr);
        }
        byte[] ipByteArray = BigInteger.valueOf(ip4addr).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            ipAddressString = "not connected";
        }
        return ipAddressString;
    }



}
