package org.pauni.gnomeconnect.core.models;


import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Specifications;

/**
 *      When created, a jsonobj is created dynamically with all
 *      required information.
 */

public class PairRequest implements GCPackageData {
    private static final String TYPE = Specifications.TYPE_PAIRREQUEST;

    public  PairRequest() {
    }



    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject pairRequest = new JSONObject();
            pairRequest.put(Specifications.Device.FINGERPRINT,"daumen, rechts");
            pairRequest.put(Specifications.Device.MODEL,      "model..");
            pairRequest.put(Specifications.Device.PUBLIC_KEY, "keeey..");
            pairRequest.put(Specifications.Device.OS,         "iOS 5..");
            pairRequest.put(Specifications.Device.NAME,       "Pauls iPhone..");
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
