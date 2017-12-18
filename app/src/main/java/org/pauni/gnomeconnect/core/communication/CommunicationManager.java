package org.pauni.gnomeconnect.core.communication;


import android.util.Log;

import org.pauni.gnomeconnect.core.interfaces.GCPackageData;
import org.pauni.gnomeconnect.core.models.Computer;
import org.pauni.gnomeconnect.core.models.ConnectedComputer;
import org.pauni.gnomeconnect.core.models.Packet.GCPackage;
import org.pauni.gnomeconnect.core.models.Prefs;

import java.util.ArrayList;
import java.util.List;

/**
 *      The CommunicationManager is responsible for sending
 *      and receiving data to/from the connected computers.
 *
 *      The ReportService detects a change and tells the
 *      CommunicationManager to sendToAll it. It will then sendToAll it
 *      to all computers currently connected with the phone.
 */


public class CommunicationManager {
    private static List<ConnectedComputer> connectedComputers = new ArrayList<>(); // connected computers


    public CommunicationManager() {
        updateList();
    }

    public static void sendToAll(GCPackageData data) {
        Log.i("sendToAll", "called()");
        try {
            // Send the report to every connected computer
            for (ConnectedComputer computer : connectedComputers) {
                GCPackage output = new GCPackage(data);
                Log.i("sendToAll", output.getData().toString());
                // TODO: computer.send(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean sendToComputer(Computer computer, GCPackageData data) {
        // TODO: build method.
        return false;
    }

    // Adds newly added computers to the list
    public static void updateList() {
        for (Computer computer : Prefs.getConnectedComputers()) {
            connectedComputers.add((ConnectedComputer) computer);
        }
    }

}
