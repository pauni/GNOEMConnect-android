package org.pauni.gnomeconnect.core.models.reports;

import android.content.Intent;

import org.json.JSONObject;
import org.pauni.gnomeconnect.core.utils.BatteryWatcher;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Specifications;

/**
 *      Reports have static attributes, that are updated
 *      by the ReportService that detects all the phone's changes.
 *
 */

public class PowerReport implements GCPackageData, Specifications {
    private static final String TYPE = Specifications.TYPE_POWER_REPORT;

    private int level;
    private int temperature;
    private String charging;


    public PowerReport(Intent intent) {
        level       = intent.getIntExtra(BatteryWatcher.EXTRA_LEVEL, -1);
        temperature = intent.getIntExtra(BatteryWatcher.EXTRA_TEMPERATURE, -1)-273;
        charging = (intent.getStringExtra(BatteryWatcher.EXTRA_CHARGING));
    }




    /**
     *      OVERRIDE METHODS
     */
    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject powerReport = new JSONObject();
            powerReport.put(Report.REPORT_BATTERY_LEVEL, level);
            powerReport.put(Report.REPORT_BATTERY_CHARGING, charging);
            powerReport.put(Report.REPORT_BATTERY_TEMPERATURE, temperature);
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
