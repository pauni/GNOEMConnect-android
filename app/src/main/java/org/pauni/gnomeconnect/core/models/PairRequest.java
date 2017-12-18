package org.pauni.gnomeconnect.core.models;


import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 *      When created, a jsonobj is created dynamically with all
 *      required information.
 */

public class PairRequest implements GCPackageData {
    private static final String TYPE = Protocol.TYPE_PAIRREQUEST;

    public  PairRequest() {
    }



    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject pairRequest = new JSONObject();
            pairRequest.put(Protocol.Device.FINGERPRINT,"daumen, rechts");
            pairRequest.put(Protocol.Device.MODEL,      "model..");
            pairRequest.put(Protocol.Device.PUBLIC_KEY, "keeey..");
            pairRequest.put(Protocol.Device.OS,         "iOS 5..");
            pairRequest.put(Protocol.Device.NAME,       "Pauls iPhone..");
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
