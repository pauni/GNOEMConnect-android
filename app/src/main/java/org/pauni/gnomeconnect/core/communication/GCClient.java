package org.pauni.gnomeconnect.core.communication;

import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * TCP client to sendToAll data to a TCP server and receive an answer ...
 */


public class GCClient implements Protocol {

    private Socket         client;
    private PrintWriter    out = null;
    private BufferedReader in;

    public GCClient(String ip) throws Exception {
        client = new Socket(ip, TCP_PORT);

        Log.i("GCClient", "Constructor  successful");
    }


    public boolean connect(String type) {
        try {
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            out.println(Commands.buildConnectionRequest(type));

            String input = in.readLine();

            return (new JSONObject(input)).getBoolean(Keys.ConnectionRequest.AUTHENTICATED);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean send(String output) {
        /*
         * Sends packets. Go to models.GCPacket for more details
         */

        try {
            out.println(output);

            Log.i("GCClient", "send(): " + output);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    String getInput() {
        /*
         *  Waits for 30 seconds for input
         */

        try {
            Future<String> input = Executors.newSingleThreadExecutor().submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String input = in.readLine();
                    Log.i("GCClient", "getInputLine(): "+input);

                    return input;
                }
            });
            // Abort after 30 seconds
            return input.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
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