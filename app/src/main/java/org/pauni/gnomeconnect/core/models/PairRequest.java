package org.pauni.gnomeconnect.core.models;


import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;

/**
 *      When created, a jsonobj is created dynamically with all
 *      required information.
 */

public class PairRequest implements GCPackageData {
    private static final String TYPE = Specs.TYPE_PAIRREQUEST;

    public  PairRequest() {
    }



    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject pairRequest = new JSONObject();
            pairRequest.put(Specs.Device.FINGERPRINT, "daumen, rechts");
            pairRequest.put(Specs.Device.MODEL, "model..");
            pairRequest.put(Specs.Device.PUBLIC_KEY, "keeey..");
            pairRequest.put(Specs.Device.OS,         "iOS 5..");
            pairRequest.put(Specs.Device.NAME,      "Pauls iPhone..");
            return pairRequest;
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
