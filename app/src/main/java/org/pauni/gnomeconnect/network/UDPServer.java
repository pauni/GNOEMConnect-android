package org.pauni.gnomeconnect.network;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;


import org.pauni.gnomeconnect.models.Computer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import static android.content.Context.WIFI_SERVICE;


/**
 *  UDP Server only for listening
 */

class UDPServer {
    private DatagramSocket server;
    private String lastIpAddress = null;
    private Context c;

    UDPServer(int port, Context c) throws SocketException {
        server = new DatagramSocket(port);
        this.c = c;
    }

    String receive() throws IOException {
        byte[] buffer  = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        do {
            server.receive(packet);
        } while (packet.getAddress().getHostAddress().equals(getOwnIP()));



        try {
            lastIpAddress = packet.getAddress().getHostAddress();
        } catch (Exception ignored) {}

        Log.i("UDPServer", "received: "+new String(packet.getData()));
        return new String(packet.getData(), 0, packet.getLength());
    }

    private String getOwnIP() {
        WifiManager wm = (WifiManager) c.getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

    }

    String getLastIpAddress() {
        return lastIpAddress;
    }

    void close() {
        server.close();
        Log.i("UDPServer", "closed");
    }

}
