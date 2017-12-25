package org.pauni.gnomeconnect.features.notificationsSync;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

import java.io.ByteArrayOutputStream;

/**
 *  Contains notification data
 */

public class NotificationReport implements Protocol {
    private static final String TYPE = Values.Payload.TYPE_USERDATA;

    private StatusBarNotification sbn;
    private Context context;




    public NotificationReport(StatusBarNotification sbn, Context context) {
        this.sbn = sbn;
        this.context = context;
    }




    /**
     *      PRIVATE METHODS
     */
    private static Bitmap getSmallIcon(Context c, StatusBarNotification sbn) {
        // returns the small icon of a notification
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

    private static Bitmap getExtraImg(StatusBarNotification sbn) {
        // returns the notification attached image
        Bundle extras = sbn.getNotification().extras;

        if (extras.containsKey(Notification.EXTRA_PICTURE)) {
            // this bitmap contain the picture attachment
            return (Bitmap) extras.get(Notification.EXTRA_PICTURE);
        }
        // if there was no attached extr. img. just return null
        return null;
    }

    private String getApplicationName(String packageName) {
        PackageManager packageManager= context.getApplicationContext().getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String compressAndEncode(Bitmap bitmap) {
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



    /**
     *      OVERRIDE METHODS
     */
    
    public JSONObject toJsonObject() {
        Notification nfc = sbn.getNotification();
        Bundle extras    = nfc.extras;

        String title     = (String) extras.getCharSequence(Notification.EXTRA_TITLE);
        String text      = ""+extras.getCharSequence(Notification.EXTRA_TEXT);
        String program   = getApplicationName(sbn.getPackageName());
        boolean ongoing  = sbn.isOngoing();
        Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
        Bitmap smallIcon = getSmallIcon(context, sbn);
        Bitmap extraImg  = getExtraImg(sbn);


        JSONObject notificationReport = new JSONObject();

        try {
            notificationReport.put("title",     title);
            notificationReport.put("text",      text);
            notificationReport.put("program",   program);
            notificationReport.put("ongoing",   ongoing);
            notificationReport.put("largeIcon", compressAndEncode(largeIcon));
            notificationReport.put("smallIcon", compressAndEncode(smallIcon));
            notificationReport.put("extraImg",  compressAndEncode(extraImg));

            return notificationReport;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
