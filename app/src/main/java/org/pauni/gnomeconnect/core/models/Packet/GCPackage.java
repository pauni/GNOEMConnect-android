package org.pauni.gnomeconnect.core.models.Packet;



import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 *      Contains header and payload of packets sent with
 *      gnome connect
 */

public class GCPackage implements Protocol {
    private String fingerprint;
    private String version;
    private Payload payload = new Payload();


    public GCPackage() {}

    public GCPackage(GCPackageData data) throws JSONException {
        // Create a GCPackage that should be sendToAll. The header is generated automatically
        fingerprint = "getFingerprint()";
        version = "getVersion()";

        payload.setType(data.getType());
        payload.setData(data.toJsonObject());

    }

    public GCPackage(String jsonstring) throws JSONException {
        // Create GCPackage from JSON Object

        JSONObject json = new JSONObject(jsonstring);
        fingerprint = json.getString(Keys.Packet.SRC_FINGERPRINT);
        version     = json.getString(Keys.Packet.VERSION);
        payload     = new Payload(json.getJSONObject(Keys.Packet.PAYLOAD));
    }

    public GCPackage(JSONObject json) throws JSONException {
        // Convert a received JSONObject to a GCPackage object.

        fingerprint = json.getString(Keys.Packet.SRC_FINGERPRINT);
        version     = json.getString(Keys.Packet.VERSION);
        payload     = new Payload(json.getJSONObject(Keys.Packet.PAYLOAD));
    }


    public String toJsonString() {
        try {
            return new JSONObject()
                    .put(Keys.Packet.SRC_FINGERPRINT, fingerprint)
                    .put(Keys.Packet.VERSION, version)
                    .put(Keys.Packet.PAYLOAD, new JSONObject()
                            .put(Keys.Payload.TYPE, payload.getType())
                            .put(Keys.Payload.DATA, payload.getData())).toString();
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
