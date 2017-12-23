package org.pauni.gnomeconnect.core.communication;

import android.util.Log;

import org.pauni.gnomeconnect.core.interfaces.GCPacketData;
import org.pauni.gnomeconnect.core.interfaces.Protocol;
import org.pauni.gnomeconnect.core.models.Packet.GCPacket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * TCP client to sendToAll data to a TCP server and receive an answer ...
 */


public class GCClient implements Protocol {

    private Socket          client;
    private PrintWriter     out;
    private static final int PORT = 4112;

    public GCClient(String ip) throws Exception {
        client = new Socket(ip, PORT);
        Log.i("GCClient", "Constructor  successful");
    }


    public boolean isBound() {
        return client.isBound();
    }

    public boolean send(final GCPacketData data) {
        /*
         * Sends packets. Go to models.GCPacket for more details
         */


        try {
            GCPacket gcPacket = GCPacket.buildGCPacket(data);

            out = new PrintWriter(client.getOutputStream(), true);

            out.println(gcPacket.toJsonString());

            Log.i("GCClient", "send=" + gcPacket.toJsonString());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public GCPacket getInput() {
        /*
         *  Waits for 30 seconds for input
         */

        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            Future<GCPacket> input = Executors.newSingleThreadExecutor().submit(new Callable<GCPacket>() {
                @Override
                public GCPacket call() throws Exception {
                    String input = in.readLine();
                    Log.i("GCClient", "getInputLine="+input);

                    return GCPacket.fromJsonString(input);
                }
            });

            // Abort after 30 seconds
            return input.get(30, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void close() {
        /*
         *  Closes the PrintWriter and the client connection
         */
        if (out != null) {
            out.close();
            Log.i("GCClient", "closed PrintWriter >out<");
        }

        try {
            client.close();
            Log.i("GCClient", "closed tcp socket >client<");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("GCClient", "did NOT close tcp socket >client<");
        }
    }

}