package org.pauni.gnomeconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import org.pauni.gnomeconnect.core.models.Specs;

/**
 *      The BatteryWatcher replaces the Broadcasts from the BatteryManager.
 *      Since the BatteryManager sends broadcasts for various changes that are
 *      not needed by this program, the BatteryWatcher will only send a
 *      broadcast if relevant changes occured.
 *
 *      It's initialized, started/stopped by the ReportService
 */

public class BatteryWatcher {
    public static final String ACTION_BATTERY_CHANGED_RELEVANT = "battery_changed_relevant";
    public static String EXTRA_LEVEL        = "extra_level";
    public static String EXTRA_CHARGING     = "extra_charging";
    public static String EXTRA_TEMPERATURE  = "extra_temperature";

    static int lvl  = 0;
    static int tmp  = 0;
    static String chrg = "";

    private BroadcastReceiver receiver;


    public BatteryWatcher() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int lvl  = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int tmp  = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                String chrg = getChargingStateString(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1));

                if (BatteryWatcher.lvl == lvl
                        && BatteryWatcher.tmp == tmp
                        && BatteryWatcher.chrg.equals(chrg)) {
                    return; // none of these states changed, do nothing.
                }

                // update the classes variables to be checked at the next onReceive
                BatteryWatcher.lvl = lvl;
                BatteryWatcher.tmp = tmp;
                BatteryWatcher.chrg = chrg;


                Intent i = new Intent(ACTION_BATTERY_CHANGED_RELEVANT);
                i.putExtra(EXTRA_LEVEL, BatteryWatcher.lvl);
                i.putExtra(EXTRA_TEMPERATURE, BatteryWatcher.tmp);
                i.putExtra(EXTRA_CHARGING, BatteryWatcher.chrg);
                context.sendBroadcast(i);
            }
        };
    }

    public void start(Context context) {
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void stop(Context context) {
        context.unregisterReceiver(receiver);
    }


    private static String getChargingStateString(int state) {
        /*
         * Returns a human readable String for each charging state(int)
         */

        switch (state) {
            case 0:
                return Specs.BATTERY_NOT_CHARGING;
            case BatteryManager.BATTERY_PLUGGED_AC:
                return Specs.BATTERY_CHARGING_AC;
            case BatteryManager.BATTERY_PLUGGED_USB:
                return Specs.BATTERY_CHARGING_USB;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return Specs.BATTERY_CHARGING_WIRELESS;
            default:
                return Specs.BATTERY_NOT_CHARGING;
        }
    }

}
