package org.pauni.gnomeconnect.core.models.Packet;



import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.encryption.Encryption;
import org.pauni.gnomeconnect.core.interfaces.GCPacketData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 *      Contains header and payload of packets sent with
 *      gnome connect
 */

public class GCPacket implements Protocol {

    // Elements of the header
    private String src_fingerprint;
    private String dst_fingerprint;
    private String version;
    private Payload payload = new Payload();


    public GCPacket() {}


    // Create GCPacket with data
    public static GCPacket buildGCPacket(GCPacketData data) throws JSONException {
        GCPacket packet = new GCPacket();

        packet.src_fingerprint = "getSrcFingerprint()";
        packet.dst_fingerprint = "getSrcFingerprint()";
        packet.version = "getVersion()";
        packet.payload.setType(data.getType());
        packet.payload.setData(data.toJsonObject());

        return packet;
    }


    // Convert JSONString to GCPacket

    public static GCPacket fromJsonString(String jsonstring) throws Exception {

        GCPacket packet = new GCPacket();
        JSONObject json = new JSONObject(jsonstring);

        JSONObject payload = json.getJSONObject(Keys.Packet.PAYLOAD);

        String datatype = payload.getString(Keys.Payload.TYPE);

        if (datatype.equals(Values.Payload.TYPE_USERDATA)) {
            json = decryptPayloadData(json);
        }


        packet.src_fingerprint = json.getString(Keys.Packet.SRC_FINGERPRINT);
        packet.version         = json.getString(Keys.Packet.VERSION);
        packet.payload         = new Payload(json.getJSONObject(Keys.Packet.PAYLOAD));

        return packet;
    }

    private static JSONObject decryptPayloadData(JSONObject packet) throws Exception {
        JSONObject payload = packet.getJSONObject(Keys.Packet.PAYLOAD);

        String encrypted   = payload.getString(Keys.Payload.DATA);

        String decrypted   = Encryption.getSymCipher().decrypt(encrypted, null);

        payload.put(Keys.Payload.DATA, new JSONObject(decrypted));

        packet.put(Keys.Packet.PAYLOAD, payload);

        return packet;
    }


    // For sending the packet over tcp

    public String toJsonString() {
        try {
            return new JSONObject()
                    .put(Keys.Packet.SRC_FINGERPRINT, src_fingerprint)
                    .put(Keys.Packet.DST_FINGERPRINT, dst_fingerprint)
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


    public String getSrc_fingerprint() {
        return src_fingerprint;
    }

    public String getDst_fingerprint() {
        return dst_fingerprint;
    }

    public String getVersion() {
        return version;
    }

    public JSONObject getData() {
        return payload.getData();

    }

}

