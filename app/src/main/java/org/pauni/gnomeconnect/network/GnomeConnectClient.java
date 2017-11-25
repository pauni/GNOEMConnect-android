package org.pauni.gnomeconnect.network;

import android.util.Log;

import org.pauni.gnomeconnect.models.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * TCP client to sendToAll data to a TCP server and receive an answer ...
 */


public class GnomeConnectClient {

    private Socket          client;
    private PrintWriter     out;
    private static final int PORT = 4112;

    public GnomeConnectClient(String ip) {
        try {
            Log.i("JUST NOW", ip);
            client = null;
            client = new Socket(ip, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isBound() {
        return client.isBound();
    }

    public boolean send(final Packet packet) {
         // DataPackage will be wrapped in payload. Payload is part of Packet.
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            out.println(packet.toJsonString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Packet getInput() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String input = in.readLine();
            return new Packet(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void close() {
        if (out != null) {
            out.close();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}