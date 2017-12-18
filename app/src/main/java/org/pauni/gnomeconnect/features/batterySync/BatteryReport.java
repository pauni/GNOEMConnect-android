package org.pauni.gnomeconnect.features.batterySync;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 *      Reports have static attributes, that are updated
 *      by the ReportService that detects all the phone's changes.
 *
 */

public class BatteryReport implements GCPackageData, Protocol {
    private static final String TYPE = Protocol.TYPE_POWER_REPORT;

    private int level;
    private int temperature;
    private String charging;


    public BatteryReport(int level, int temperature, String charging) {
        this.level       = level;
        this.temperature = temperature;
        this.charging    = charging;
    }




    /**
     *      OVERRIDE METHODS
     */
    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject powerReport = new JSONObject();
            powerReport.put(Report.BATTERY_LEVEL, level);
            powerReport.put(Report.BATTERY_CHARGING, charging);
            powerReport.put(Report.BATTERY_TEMPERATURE, temperature);
            return powerReport;
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