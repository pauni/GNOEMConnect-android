package org.pauni.gnomeconnect.core.models.Packet;



import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

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
        fingerprint = json.getString(Protocol.Packet.FINGERPRINT);
        version     = json.getString(Protocol.Packet.VERSION);
        payload     = new Payload(json.getJSONObject(Protocol.Packet.PAYLOAD));
    }

    // For converting a received JSONObject to a GCPackage object.
    public GCPackage(JSONObject json) throws JSONException {
        fingerprint = json.getString(Protocol.Packet.FINGERPRINT);
        version     = json.getString(Protocol.Packet.VERSION);
        payload     = new Payload(json.getJSONObject(Protocol.Packet.PAYLOAD));
    }


    public String toJsonString() {
        try {
            return new JSONObject()
                    .put(Protocol.Packet.FINGERPRINT, fingerprint)
                    .put(Protocol.Packet.VERSION, version)
                    .put(Protocol.Packet.PAYLOAD, new JSONObject()
                            .put(Protocol.Payload.TYPE, payload.getType())
                            .put(Protocol.Payload.DATA, payload.getData())).toString();
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
