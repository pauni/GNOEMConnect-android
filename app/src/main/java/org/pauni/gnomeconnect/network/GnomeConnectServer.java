package org.pauni.gnomeconnect.network;

import android.util.Log;

import org.pauni.gnomeconnect.models.Packet;

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

class GnomeConnectServer {
    private static ServerSocket server;
    private BufferedReader in;
    private PrintWriter out;


    GnomeConnectServer() {
        try {
            server = new ServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    GnomeConnectServer(int port) throws IOException {
        server = new ServerSocket();
        server.setReuseAddress(true);
        server.bind(new InetSocketAddress(port));
    }

    void bind(int port) throws IOException {
        Log.d("GnomeConnectServer","bind()");
        server.setReuseAddress(true);
        server.bind(new InetSocketAddress(port));
    }


    boolean waitForClient() {
        Log.d("GnomeConnectServer","waitForClient()");

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


    Packet getInput() {

        Log.d("GnomeConnectServer","getInputLine()");

        try {
            Packet packet  = new Packet();
            String input = in.readLine();
            return new Packet(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    boolean send (Packet packet) {
        try {
            out.println(packet.toJsonString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    void close() {
        Log.d("GnomeConnectServer", "connection closed");
        try {
            server.close();
            server = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}