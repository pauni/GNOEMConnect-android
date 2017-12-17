package org.pauni.gnomeconnect.core.models.reports;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Specifications;

/**
 *      Reports consists of their static attributes, that are updated
 *      by the ReportService that detects all the phone's changes.
 *
 *      Reports have never non-initialized fields.
 *      If a report is instantiated the first time (so every attribute
 *      is null) the constructor automatically initializes every attribute.
 */

public class ConnectivityReport implements GCPackageData {
    private static final String TYPE = Specifications.TYPE_CONNECTIVITY_REPORT;


    private static String wifiEnabled   = null;
    private static String wifiStrength  = null;
    private static String BSSID         = null;
    private static String SSID          = null;
    private static String cellNetwork   = null;


    public ConnectivityReport() {
        if (!isInitialized())
            initialize();
    }




    /**
     *      PRIVATE METHODS
     */
    private boolean isInitialized() {
        return wifiEnabled != null
                && wifiStrength  != null
                && BSSID         != null
                && SSID          != null
                && cellNetwork   != null;
    }

    private void initialize() {
        wifiEnabled   = "";
        wifiStrength  = "";
        BSSID         = "";
        SSID          = "";
        cellNetwork   = "";
    }

    public JSONObject toJsonObject() {
        try {
            JSONObject cntReport = new JSONObject();
            cntReport.put(Specifications.REPORT_WIFI_ENABLED, wifiEnabled);
            cntReport.put(Specifications.REPORT_WIFI_STRENGTH, wifiStrength);
            cntReport.put(Specifications.REPORT_WIFI_BSSID, BSSID);
            cntReport.put(Specifications.REPORT_WIFI_BSSID, SSID);
            cntReport.put(Specifications.REPORT_CELL_NETWORK, cellNetwork);
            return cntReport;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
