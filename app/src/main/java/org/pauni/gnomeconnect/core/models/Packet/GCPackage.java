package org.pauni.gnomeconnect.core.models.Packet;



import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.models.Specs;

/**
 *      Contains header and payload of packets sent with
 *      gnome connect
 */

public class GCPackage {
    private String fingerprint;
    private String version;
    private Payload payload = new Payload();


    public GCPackage() {}

    // For creating a GCPackage that should be sendToAll. The header is generated automatically
    public GCPackage(GCPackageData data) throws JSONException {
        fingerprint = "getFingerprint()";
        version = "getVersion()";

        payload.setType(data.getType());
        payload.setData(data.toJsonObject());

    }


    // Create GCPackage from JSON Object
    public GCPackage(String jsonstring) throws JSONException {
        JSONObject json = new JSONObject(jsonstring);
        fingerprint = json.getString(Specs.Packet.FINGERPRINT);
        version     = json.getString(Specs.Packet.VERSION);
        payload     = new Payload(json.getJSONObject(Specs.Packet.PAYLOAD));
    }

    // For converting a received JSONObject to a GCPackage object.
    public GCPackage(JSONObject json) throws JSONException {
        fingerprint = json.getString(Specs.Packet.FINGERPRINT);
        version     = json.getString(Specs.Packet.VERSION);
        payload     = new Payload(json.getJSONObject(Specs.Packet.PAYLOAD));
    }


    public String toJsonString() {
        JSONObject o = new JSONObject();
        try {
            o.put(Specs.Packet.FINGERPRINT, fingerprint);
            o.put(Specs.Packet.VERSION, version);
            o.put(Specs.Packet.PAYLOAD, payload);
            return o.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     *      GETTERS
     */
    public String getFingerprint() {
        return fingerprint;
    }


    public String getVersion() {
        return version;
    }

    public Payload getPayload() {
        return payload;
    }

    public JSONObject getData() {
        return payload.getData();

    }
}
