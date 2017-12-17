package org.pauni.gnomeconnect.core.network;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.pauni.gnomeconnect.core.models.Computer;
import org.pauni.gnomeconnect.core.models.Specs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 *      GnomeSpotter broadcasts search for GNOMEConnect Desktop programs.
 *      If he found a sever, he broacasts ACTION_FOUND_SERVER with String extra
 *      containing an JSONObject of the found server (ip, priv & pub key etc.)
 */

public class GnomeSpotter {
    public final static String ACTION_DESKTOP_DISCOVERED = "org.pauni.gnomeconnect.COMPUTER_DISCOVERED";
    public final static String EXTRA_COMPUTER_INFO = "computerInfo";
    private int broadcastInterval = 60 * 1000;


    private Context context;


    // Thread for broadcasting GNOMEConnect Desktop searches and receiving answers
    private Thread broadcasterThread;
    private Thread responseListenerThread;



    public GnomeSpotter(Context context) {
        this.context = context;
        initThreads();
    }

    public void startSpotting() {
        broadcasterThread.start();
        responseListenerThread.start();
    }

    public void stopSpotting() {
        broadcasterThread.interrupt();
        responseListenerThread.interrupt();
    }



    private void initThreads() {
        /*
         *  Sends broadcast every broadcastInterval milliseconds to find
         *  computers in the lan running GNOMEConnect Desktop
         */
        broadcasterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!broadcasterThread.isInterrupted()) {
                        sendDiscoveryBroadcast();
                        Thread.sleep(broadcastInterval);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        /*
         *  Listens for responses to that (udp) broadcast and
         *  sends a (android) broadcast with the found computer
         */
        responseListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UDPServer server = new UDPServer(Specs.NETWORK_PORT, context);
                    while (!responseListenerThread.isInterrupted()) {
                        String input        = server.receive();         //wait4input
                        Computer computer   = new Computer(input);      //json2comp

                        computer.setIpAddress(server.getLastIpAddress());
                        sendComputerFoundBroadcast(computer);
                    }
                    server.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void sendComputerFoundBroadcast(Computer computer) throws JsonProcessingException {
        // Convert the computer to a jsonstring and attach it to the intent. Broadcast the intent
        String computerJackson = new ObjectMapper().writeValueAsString(computer);
        Intent broadcastIntent = new Intent(ACTION_DESKTOP_DISCOVERED);
        broadcastIntent.putExtra(EXTRA_COMPUTER_INFO, computerJackson);
        context.sendBroadcast(broadcastIntent);
    }


    private void sendDiscoveryBroadcast() {
        try {
            String msg = "Hello someone there?";
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), getBroadcastAddr(), Specs.NETWORK_PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            Log.i("Utils", "sendBroadcast()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InetAddress getBroadcastAddr() throws IOException {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi == null)
            return null;

        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

}
