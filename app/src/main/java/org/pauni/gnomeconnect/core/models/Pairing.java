package org.pauni.gnomeconnect.core.models;



import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

/**
 *    Yeah, I should describe what it does, but it's reallyobvious. It builds packets
 *    for request stuff thing
 */

public abstract class Pairing implements Protocol, GCPackageData  {
    @Override
    public String getType() {
        return Values.Payload.TYPE_PAIRING;
    }



    /**
     *  Different packets (inside the Payload.data field)
     *  for requesting, accepting, denying pairing.
     */

    public static GCPackageData getRequestPacket() {
        return new RequestPacket();
    }

    public static GCPackageData getAcceptPacket() {
        return new AcceptPacket();
    }

    public static GCPackageData getDenyPacket() {
        return new DenyPacket();
    }


    private static class RequestPacket extends Pairing {
        @Override
        public String getType() {
            return super.getType();
        }

        @Override
        public JSONObject toJsonObject() {
            try {
                JSONObject request = new JSONObject();
                JSONObject device  = new JSONObject();

                device.put(Keys.Device.FINGERPRINT,"daumen, rechts");
                device.put(Keys.Device.HOSTNAME,   "Pauls iPhone..");
                device.put(Keys.Device.MODEL,      "model..");
                device.put(Keys.Device.PUBLIC_KEY, "keeey..");
                device.put(Keys.Device.OS,         "iOS 5..");

                request.put(Keys.Pairing.ACTION, Values.Pairing.ACTION_REQUEST);
                request.put(Keys.Pairing.DEVICE, device);

                return request;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }        }
    }

    private static class AcceptPacket extends Pairing {
            @Override
            public String getType() {
                return super.getType();
            }

            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject request = new JSONObject();
                    JSONObject device  = new JSONObject();

                    request.put(Keys.Pairing.ACTION, Values.Pairing.ACTION_ACCCEPTED);
                    request.put(Keys.Pairing.DEVICE, device);

                    return request;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }        }
        }

    private static class DenyPacket extends Pairing {
            @Override
            public String getType() {
                return super.getType();
            }

            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject request = new JSONObject();
                    JSONObject device  = new JSONObject();

                    request.put(Keys.Pairing.ACTION, Values.Pairing.ACTION_DENIED);
                    request.put(Keys.Pairing.DEVICE, device);

                    return request;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }        }
        }

}
