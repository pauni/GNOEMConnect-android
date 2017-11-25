package org.pauni.gnomeconnect.models;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.network.GnomeConnectClient;

import java.net.Socket;

/**
 *      Special class of Computer for all connected computers.
 *      This class implements special methods to communicate with
 *      an actual physical computer.
 */

public class ConnectedComputer extends Computer {
    GnomeConnectClient client;


    public ConnectedComputer(@NonNull Computer computer) throws Exception{
        if (!computer.isComplete()) {
            throw new Exception("Computer is not complete. Please make sure, all fields of the passed computer are initialized with @NonNull");
        }

        setFingerprint(computer.getFingerprint());
        setIpAddress(computer.getIpAddress());
        setModel(computer.getModel());
        setName(computer.getName());
        setOs(computer.getOs());
        setPublicKey(computer.getPublicKey());
        setVersion(computer.getVersion());

        client = new GnomeConnectClient(getIpAddress());
    }

    public boolean isReachable() {
        try {
            Socket client = new Socket(this.getIpAddress(), Specs.NETWORK_PORT);
            client.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean send(Packet packet) {
        return client.send(packet);
    }
}
