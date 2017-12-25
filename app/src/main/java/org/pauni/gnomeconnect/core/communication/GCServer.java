package org.pauni.gnomeconnect.core.communication;

import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by roni on 31.10.17.
 */

class GCServer {
    private static ServerSocket server;
    private BufferedReader in;
    private PrintWriter out;


    GCServer() {
        try {
            server = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GCServer(int port) throws IOException {
        server = new ServerSocket();
        server.setReuseAddress(true);
        server.bind(new InetSocketAddress(port));
    }

    void bind(int port) throws IOException {
        Log.d("GCServer","bind()");
        server.setReuseAddress(true);
        server.bind(new InetSocketAddress(port));
    }


    boolean waitForClient() {
        Log.d("GCServer","waitForClient()");

        Socket client;
        try {
            client = server.accept(); // waiting for clients to connect
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // connect a Reader and Writer to the client socket to do I/O
        try {
            in  = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            out = new PrintWriter   (client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    String getInputLine() {

        Log.d("GCServer","getInputLine()");

        try {
            String input = in.readLine();
            return input;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    boolean send(String string) {
        try {
            out.println(string);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    void close() {
        Log.d("GCServer", "connection closed");
        try {
            server.close();
            server = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}