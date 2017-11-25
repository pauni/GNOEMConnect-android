package org.pauni.gnomeconnect.models;



import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.interfaces.DataPackage;

/**
 *      Contains header and payload of packets sent with
 *      gnome connect
 */

public class Packet {
    private String hostname;
    private String fingerprint;
    private String version;
    private JSONObject payload = new JSONObject();


    public Packet() {}

    // For creating a Packet that should be sendToAll. The header is generated automatically
    public Packet(DataPackage data) throws JSONException {
        hostname = "getHostname()";
        fingerprint = "getFingerprint()";
        version = "getVersion()";

        payload.put(Specs.PAYLOAD_TYPE, data.getType());
        payload.put(Specs.PAYLOAD_DATA, data.toJsonObject());

    }


    // For converting a received JSONString to a Packet object.
    public Packet(String jsonstring) throws JSONException {
        JSONObject json = new JSONObject(jsonstring);
        hostname    = json.getString(Specs.HOSTNAME);
        fingerprint = json.getString(Specs.FINGERPRINT);
        version     = json.getString(Specs.VERSION);
        payload     = json.getJSONObject(Specs.PAYLOAD);
    }

    // For converting a received JSONObject to a Packet object.
    public Packet (JSONObject json) throws JSONException {
        hostname    = json.getString(Specs.HOSTNAME);
        fingerprint = json.getString(Specs.FINGERPRINT);
        version     = json.getString(Specs.VERSION);
        payload     = json.getJSONObject(Specs.PAYLOAD);
    }


    public String toJsonString() {
        JSONObject o = new JSONObject();
        try {
            o.put(Specs.HOSTNAME, hostname);
            o.put(Specs.FINGERPRINT, fingerprint);
            o.put(Specs.VERSION, version);
            o.put(Specs.PAYLOAD, payload);
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

    public String getHostname() {
        return hostname;
    }

    public String getVersion() {
        return version;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public JSONObject getData() {
        try {
            return payload.getJSONObject(Specs.PAYLOAD_DATA);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
