package org.pauni.gnomeconnect.core.models.Packet;

import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 * Created by roni on 17.12.17.
 */

public class Payload {
    private JSONObject data = null;
    private String type = null;

    public Payload() {}

    public Payload(JSONObject payload) throws JSONException {
        this.data = payload.getJSONObject(Protocol.Payload.DATA);
        this.type = payload.getString(Protocol.Payload.TYPE);
    }


    /**
     * SETTERS
     */
    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * GETTERS
     */
    public JSONObject getData() {
        return data;
    }

    public String getType() {
        return type;
    }
}
