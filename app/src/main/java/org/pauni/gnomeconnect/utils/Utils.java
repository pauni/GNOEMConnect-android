package org.pauni.gnomeconnect.utils;

import android.app.KeyguardManager;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import org.pauni.gnomeconnect.models.BatteryStatus;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

/**
 * Outsourcing some methods to keep the code clean, readable, short etc.
 */

public class Utils {

    Utils() {}


    /*
    *   IMAGE METHODS
    */

    // returns the small icon of a notification
    public static Bitmap getSmallIcon(Context c, StatusBarNotification sbn) {
        // because .getSmallIcon() was introduced with API23 we have to use our own method

        /*
        *       THANK YOU VERY MUCH MASOUD VALI FOR THIS SOLUTION:
        *       https://stackoverflow.com/a/40691763/8008892
        */

        try {
            Bitmap bmp = null;

            // get the id of the small icon
            int id = sbn.getNotification().extras.getInt(Notification.EXTRA_SMALL_ICON);

            // get the package-name of the application that published the notification,
            // get access to this app's package and finally get the drawable
            String pack = sbn.getPackageName();
            Context remotePackageContext;
            remotePackageContext = c.createPackageContext(pack, 0);
            Drawable icon = remotePackageContext.getResources().getDrawable(id);

            // convert it to bmp
            bmp = ((BitmapDrawable) icon).getBitmap();
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if something went wrong, return null
        return null;
    }

    // returns the notification attached image
    public static Bitmap getExtraImg(StatusBarNotification sbn) {
        Bundle extras = sbn.getNotification().extras;

        if (extras.containsKey(Notification.EXTRA_PICTURE)) {
            // this bitmap contain the picture attachment
            return (Bitmap) extras.get(Notification.EXTRA_PICTURE);
        }
        // if there was no attached extr. img. just return null
        return null;
    }





    /*
    *   INFORMATION ABOUT THE PHONE
    */

    // returns a human readable String for each charging state(int)
    public static String getChargingStateString(int state) {
        switch (state) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                return BatteryStatus.CHARGING_CHARGING_AC;
            case BatteryManager.BATTERY_PLUGGED_USB:
                return BatteryStatus.CHARGING_CHARGING_USB;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return BatteryStatus.CHARGING_CHARGING_WIRELESS;
            default:
                return BatteryStatus.CHARGING_NOT_CHARGING;
        }
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
