package org.pauni.gnomeconnect.features.connectivitySync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.pauni.gnomeconnect.core.models.reports.ConnectivityReport;

/**
 * Created by roni on 18.12.17.
 */

public class ConnectivityChangedListener {
    private BroadcastReceiver receiver = null;
    private IntentFilter filter = null;

    public ConnectivityChangedListener() {
        createReceiver();
    }

    void start(Context context) {
        context.registerReceiver(receiver, filter);
    }

    void stop(Context context) {
        context.unregisterReceiver(receiver);
    }

    private void createReceiver() {
        filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null)
                    return;

                switch (action) {
                    case WifiManager.WIFI_STATE_CHANGED_ACTION:
                        onWifiStateChangedAction(intent);
                        break;
                    case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                        onNetworkStateChangedAction(intent);
                        break;
                }
            }
        };
    }

    private void onWifiStateChangedAction(Intent i) {
        int state = i.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
        ConnectivityReport report = new ConnectivityReport();

        switch (state) {
            case WifiManager.WIFI_STATE_ENABLED:
                // TODO: report.setWifiEnabled(Values.Wifi.enabled);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                // TODO: report.setWifiEnabled(Values.Wifi.disabled);
                break;
        }

        // logging
        Log.i("WifiStateChanged", "wifistate="+state );
    }

    private void onNetworkStateChangedAction(Intent i) {
        NetworkInfo nInfo = i.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        if (nInfo.isConnected()) {
            WifiInfo wInfo = i.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            // TODO: report.set...()    ->    setWifiConnected(true);
            // TODO: report.set...()    ->    setBSSID(wInfo.getBSSID());
            // TODO: report.set...()    ->    setSSID (wInfo.getSSID());
            // TODO: report.set...()    ->    setWifiSignalStrength(wInfo.getRssi());
            // TODO: report.set...()    ->    setLocalIP(String.valueOf(Utils.convertIpAddressToString(wInfo.getIpAddress())));
        }
    }
}
