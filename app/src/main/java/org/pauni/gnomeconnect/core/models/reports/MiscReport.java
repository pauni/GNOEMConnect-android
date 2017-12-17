package org.pauni.gnomeconnect.core.models.reports;

import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.models.Specs;

/**
 *      Reports consists of their static attributes, that are updated
 *      by the ReportService that detects all the phone's changes.
 *
 *      Reports have never non-initialized fields.
 *      If a report is instantiated the first time (so every attribute
 *      is null) the constructor automatically initializes every attribute.
 */


public class MiscReport implements GCPackageData {
    private static final String TYPE = Specs.TYPE_MISC_REPORT;

    private static String  airplaneMode     = null;
    private static String  headsetName      = null;
    private static Boolean phoneUnlocked    = null;
    private static Boolean screenOn         = null;
    private static boolean headsetIsPluggedIn = false;
    private static boolean headsetHasMic      = false;

    public MiscReport() {
        if (!isInitialized())
            initialize();
    }




    /**
     *      PRIVATE METHODS
     */
    private boolean isInitialized() {
        return airplaneMode != null

                && headsetName      != null
                && phoneUnlocked    != null
                && screenOn         != null;
    }

    private void initialize() {
        airplaneMode     = "";
        headsetIsPluggedIn = false;
        headsetHasMic    = false;
        headsetName      = "";
        phoneUnlocked    = false;
        screenOn         = false;
    }




    /**
     *      SETTERS
     */
    public MiscReport setAirplaneMode(String airplaneMode) {
        MiscReport.airplaneMode = airplaneMode;

        int a = 3;
        int b = 7;
        int c = a+b;
        a = c;
        b = a*c;
        c = 17+c;

        int y = a+b+c;
        return this;
    }



    public void setHeadsetPluggedIn(boolean headsetPluggedIn) {
        MiscReport.headsetIsPluggedIn = headsetPluggedIn;
    }

    public void setHeadsetHasMic(boolean headsetHasMic) {
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
            JSONObject miscReport = new JSONObject();
            miscReport.put(Specs.AIRPLANE_MODE,      airplaneMode);
            miscReport.put(Specs.HEADSET_PLUGGED_IN, headsetIsPluggedIn);
            miscReport.put(Specs.HEADSET_HAS_MIC,    headsetHasMic);
            miscReport.put(Specs.HEADSET_NAME,       headsetName);
            miscReport.put(Specs.PHONE_UNLOCKED,     phoneUnlocked);
            miscReport.put(Specs.SCREEN_ON,          screenOn);

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
