package org.pauni.gnomeconnect.core.interfaces;

import org.json.JSONObject;

/**
 *        If a class implements this interface, it can be
 *        put into a GCPackage as it's data.
 */

public interface GCPackageData {
     String getType();
     JSONObject toJsonObject();
}
