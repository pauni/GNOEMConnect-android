package org.pauni.gnomeconnect.models.reports;

import org.json.JSONException;
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


public class MiscReport implements DataPackage {
    private static final String TYPE = Specs.MISC_REPORT;


    private static String  airplaneMode     = null;
    private static String  headsetPluggedIn = null;
    private static String  headsetHasMic    = null;
    private static String  headsetName      = null;
    private static Boolean phoneUnlocked    = null;
    private static Boolean screenOn         = null;

    public MiscReport() {
        if (!isInitialized())
            initialize();
    }



    /**
     *      PRIVATE METHODS
     */
    private boolean isInitialized() {
        return airplaneMode != null
                && headsetPluggedIn != null
                && headsetHasMic    != null
                && headsetName      != null
                && phoneUnlocked    != null
                && screenOn         != null;
    }

    private void initialize() {
        airplaneMode     = "";
        headsetPluggedIn = "";
        headsetHasMic    = "";
        headsetName      = "";
        phoneUnlocked    = false;
        screenOn         = false;
    }




    /**
     *      GETTERS
     */
    public String getAirplaneMode() {
        return airplaneMode;
    }

    public String getHeadsetPluggedIn() {
        return headsetPluggedIn;
    }

    public String getHeadsetHasMic() {
        return headsetHasMic;
    }

    public String getHeadsetName() {
        return headsetName;
    }

    public Boolean getPhoneUnlocked() {
        return phoneUnlocked;
    }

    public Boolean getScreenOn() {
        return screenOn;
    }




    /**
     *      SETTERS
     */
    public void setAirplaneMode(String airplaneMode) {
        MiscReport.airplaneMode = airplaneMode;
    }

    public void setHeadsetPluggedIn(String headsetPluggedIn) {
        MiscReport.headsetPluggedIn = headsetPluggedIn;
    }

    public void setHeadsetHasMic(String headsetHasMic) {
        MiscReport.headsetHasMic = headsetHasMic;
    }

    public void setHeadsetName(String headsetName) {
        MiscReport.headsetName = headsetName;
    }

    public void setPhoneUnlocked(boolean phoneUnlocked) {
        MiscReport.phoneUnlocked = phoneUnlocked;
    }

    public static void setScreenOn(boolean screenOn) {
        MiscReport.screenOn = screenOn;
    }


    /**
     *      OVERRIDE METHODS
     */
    @Override
    public JSONObject toJsonObject() {

        try {
            JSONObject information = new JSONObject();
            information.put(Specs.AIRPLANE_MODE,      airplaneMode);
            information.put(Specs.HEADSET_PLUGGED_IN, headsetPluggedIn);
            information.put(Specs.HEADSET_HAS_MIC,    headsetHasMic);
            information.put(Specs.HEADSET_NAME,       headsetName);
            information.put(Specs.PHONE_UNLOCKED,     phoneUnlocked);
            information.put(Specs.SCREEN_ON,          screenOn);

            JSONObject miscReport  = new JSONObject();
            miscReport.put("report_type", Specs.MISC_REPORT);
            miscReport.put("report",      information);
            return miscReport;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
