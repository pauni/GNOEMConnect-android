package org.pauni.gnomeconnect.interfaces;


import org.json.JSONObject;

/**
 *        This interface ensures that everything that is being set
 *        as the packet's "data" comes with a "type", describing
 *        the data.
 */

public interface DataPackage {
     String getType();
     JSONObject toJsonObject();
}
