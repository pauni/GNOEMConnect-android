package org.pauni.gnomeconnect.core.interfaces;

import org.json.JSONObject;

/**
 *        If a class implements this interface, it can be
 *        put into a GCPacket as it's data.
 */

public interface GCPacketData {
     String getType();
     JSONObject toJsonObject();
}
