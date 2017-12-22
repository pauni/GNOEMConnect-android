package org.pauni.gnomeconnect.core.models;



import org.json.JSONObject;
import org.pauni.gnomeconnect.core.encryption.Encryption;
import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;
import org.pauni.gnomeconnect.core.utils.Utils;

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

    public static Pairing Step1() {
        return new Pairing() {
            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject pairingPacket = new JSONObject();

                    pairingPacket.put(Keys.Pairing.STEP, Values.Pairing.STEP_1);
                    pairingPacket.put(Keys.Pairing.DATA, Encryption.getPublicKey());

                    return pairingPacket;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Pairing Step2() {
        return new Pairing() {
            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject pairingPacket = new JSONObject();

                    pairingPacket.put(Keys.Pairing.STEP, Values.Pairing.STEP_2);
                    pairingPacket.put(Keys.Pairing.DATA, Encryption.getPublicKey());

                    return pairingPacket;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Pairing Step3() {
        return new Pairing() {
            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject pairingPacket = new JSONObject();

                    String name = Utils.getModelName();
                    pairingPacket.put(Keys.Pairing.STEP, Values.Pairing.STEP_3);
                    pairingPacket.put(Keys.Pairing.DATA, name);

                    return pairingPacket;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Pairing Step4() {
        return new Pairing() {
            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject pairingPacket = new JSONObject();


                    pairingPacket.put(Keys.Pairing.STEP, Values.Pairing.STEP_4);
                    pairingPacket.put(Keys.Pairing.DATA, true);

                    return pairingPacket;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Pairing Step5(final String randomString) {
        return new Pairing() {
            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject pairingPacket = new JSONObject();


                    pairingPacket.put(Keys.Pairing.STEP, Values.Pairing.STEP_5);
                    pairingPacket.put(Keys.Pairing.DATA, randomString);

                    return pairingPacket;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    public static Pairing Step6(final String randomString) {
        return new Pairing() {
            @Override
            public JSONObject toJsonObject() {
                try {
                    JSONObject pairingPacket = new JSONObject();


                    pairingPacket.put(Keys.Pairing.STEP, Values.Pairing.STEP_5);
                    pairingPacket.put(Keys.Pairing.DATA, randomString);

                    return pairingPacket;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}
