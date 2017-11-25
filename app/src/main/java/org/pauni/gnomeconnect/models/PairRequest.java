package org.pauni.gnomeconnect.models;


import org.json.JSONObject;
import org.pauni.gnomeconnect.interfaces.DataPackage;

/**
 *      When created, a jsonobj is created dynamically with all
 *      required information.
 */

public class PairRequest implements DataPackage {
    private static final String TYPE = Specs.TYPE_PAIRREQUEST;

    public  PairRequest() {
    }



    @Override
    public JSONObject toJsonObject() {
        try {
            JSONObject pairRequest = new JSONObject();
            pairRequest.put(Specs.PUBLIC_KEY, "keeey");
            pairRequest.put(Specs.OS,         "iOS 5");
            pairRequest.put(Specs.MODEL,      "Pauls iPhone");
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
