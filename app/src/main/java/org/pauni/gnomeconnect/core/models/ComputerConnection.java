package org.pauni.gnomeconnect.core.models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.pauni.gnomeconnect.core.interfaces.Protocol;
import org.pauni.gnomeconnect.core.communication.GCClient;

import java.net.Socket;

/**
 *      Special class of Computer for all connected computers.
 *      This class implements special methods to communicate with
 *      an actual physical computer.
 */

public class ComputerConnection extends Computer {
    @JsonIgnore
    GCClient client;


    public ComputerConnection() {
    }

    public ComputerConnection(@NonNull Computer computer) throws Exception{
        if (!computer.isComplete()) {
            throw new Exception("Computer is not complete. Please make sure, all fields of the passed computer are initialized with @NonNull");
        }

        setFingerprint(computer.getFingerprint());
        setIpAddress(computer.getIpAddress());
        setModel(computer.getModel());
        setDevicename(computer.getDevicename());
        setOs(computer.getOs());
        setSharedSecret(computer.getSharedSecret());
        setVersion(computer.getVersion());

        client = new GCClient(getIpAddress());
    }

    @JsonIgnore
    public boolean isReachable() {
        try {
            Socket client = new Socket(this.getIpAddress(), Protocol.TCP_PORT);
            client.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @JsonIgnore
    public boolean send(String string) {
        return client.send(string);
    }
}
