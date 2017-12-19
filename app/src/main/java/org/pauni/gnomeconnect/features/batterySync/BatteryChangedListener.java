package org.pauni.gnomeconnect.features.batterySync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import org.pauni.gnomeconnect.core.interfaces.Protocol;
import org.pauni.gnomeconnect.core.communication.CommunicationManager;

/**
 *      The BatteryChangedListener replaces the Broadcasts from the BatteryManager.
 *      Since the BatteryManager sends broadcasts for various changes that are
 *      not needed by this program, the BatteryChangedListener will only send a
 *      broadcast if relevant changes occured.
 *
 *      It's initialized, started/stopped by the ReportService
 */

public class BatteryChangedListener {
    private int lvl  = 0;       // These are the values we care about
    private int tmp  = 0;       // These are the values we care about
    private String chrg = "";   // These are the values we care about

    private BroadcastReceiver receiver;


    public BatteryChangedListener() {
        createReceiver();
    }


    public void start(Context context) {
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void stop(Context context) {
        context.unregisterReceiver(receiver);
    }

    private void createReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int lvl  = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int tmp  = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                String chrg = getChargingStateString(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1));

                // Check if the values we care about changed
                if (BatteryChangedListener.this.lvl == lvl
                        && BatteryChangedListener.this.tmp == tmp
                        && BatteryChangedListener.this.chrg.equals(chrg)) {
                    return; // none of these values changed, leave onReceive().
                }

                // update the classes variables to be checked at the next onReceive()
                BatteryChangedListener.this.lvl = lvl;
                BatteryChangedListener.this.tmp = tmp;
                BatteryChangedListener.this.chrg = chrg;

                BatteryReport report = new BatteryReport(lvl, tmp, chrg);
                CommunicationManager.sendToAll(report);
            }
        };
    }

    private static String getChargingStateString(int state) {
        /*
         * Returns a human readable String for each charging state(int)
         */

        switch (state) {
            case 0:
                return Protocol.BATTERY_NOT_CHARGING;
            case BatteryManager.BATTERY_PLUGGED_AC:
                return Protocol.BATTERY_CHARGING_AC;
            case BatteryManager.BATTERY_PLUGGED_USB:
                return Protocol.BATTERY_CHARGING_USB;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return Protocol.BATTERY_CHARGING_WIRELESS;
            default:
                return Protocol.BATTERY_NOT_CHARGING;
        }
    }

}
