package org.pauni.gnomeconnect.core.communication;

import org.pauni.gnomeconnect.core.models.Computer;
import org.pauni.gnomeconnect.core.models.ComputerConnection;
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
    private static List<ComputerConnection> computerConnections = new ArrayList<>(); // connected computers


    public CommunicationManager() {
        updateList();
    }

    public static void sendToAll(String output) {
        try {
            // Send the report to every connected computer
            for (ComputerConnection computer : computerConnections) {
                // TODO: build method
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Adds newly added computers to the list
    public static void updateList() {
        for (Computer computer : Prefs.getConnectedComputers()) {
            computerConnections.add((ComputerConnection) computer);
        }
    }

}
