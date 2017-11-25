package org.pauni.gnomeconnect.models.reports;



import org.json.JSONObject;
import org.pauni.gnomeconnect.interfaces.DataPackage;
import org.pauni.gnomeconnect.models.Specs;

/**
 *      Reports consists of their static attributes, that are updated
 *      by the ReportService that detects all the phone's changes.
 *
 *      Reports have never non-initialized fields.
 *      If a report is instantiated the first time (so every attribute
 *      is null) the constructor automatically initializes every attribute.
 */


public class PowerReport implements DataPackage {
    private static final String TYPE = Specs.POWER_REPORT;


    private static String level       = null;
    private static String charging    = null;
    private static String temperature = null;





    public PowerReport() {
        if (!isInitialized())
            initialize();
    }




    /**
     *      PRIVATE METHODS
     */
    private boolean isInitialized() {
        return level != null
                && charging    != null
                && temperature != null;
    }

    private void initialize() {
        level       = "";
        charging    = "";
        temperature = "";
    }

    /**
     *      OVERRIDE METHODS
     */
    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject powerReport = new JSONObject();
            powerReport.put(Specs.REPORT_BATTERY_LEVEL,       "getlvl");
            powerReport.put(Specs.REPORT_BATTERY_CHARGING,    "getlvl");
            powerReport.put(Specs.REPORT_BATTERY_TEMPERATURE, "getlvl");
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
