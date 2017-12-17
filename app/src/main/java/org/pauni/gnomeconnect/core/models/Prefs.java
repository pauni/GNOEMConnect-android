package org.pauni.gnomeconnect.core.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.pauni.gnomeconnect.core.network.CommunicationManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Store stuff
 */

public class Prefs {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public final static String KEY_CONNECTED_COMPUTERS = "connected_computers";



    public Prefs(Context context) {
        prefs = context.getSharedPreferences("generalPrefs", 0);
    }







    /*
    *   Save and get Strings
    */
    public static void saveString(String key, String value) {
        editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
        Log.i("Prefs", "saved: key=" + key + " string=" + value);
    }

    public static String getString(String key) {
        return prefs.getString(key, "");
    }



    /*
    *   Save and get Set<String>
    */
    private static boolean saveStringSet(String key, Set<String> set) {
        try {
            editor = prefs.edit();
            editor.putStringSet(key, set);
            editor.apply();
            Log.i("Prefs", "saved: key=" + key + " value=" + set);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Set<String> getStringSet(String key) {
        return prefs.getStringSet(key, new HashSet<String>());
    }



    /*
    *   Save and get Computers
    */
    public static boolean saveComputerConnection(ConnectedComputer connectedComputer) {
        try {
            Set<String> set = Prefs.getStringSet(Prefs.KEY_CONNECTED_COMPUTERS); // get the existing set
            String jackson = new ObjectMapper().writeValueAsString(connectedComputer);    // convert Computer to JSONString
            set.add(jackson);                                                    // add it to the set

            CommunicationManager.updateList();
            return saveStringSet(Prefs.KEY_CONNECTED_COMPUTERS, set);            // save the set
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Computer> getConnectedComputers() {
        ArrayList<Computer> connectedComputers = new ArrayList<>();
        Set<String> set = Prefs.getStringSet(Prefs.KEY_CONNECTED_COMPUTERS);
        ObjectMapper mapper = new ObjectMapper();

        for (String computerJackson : set) {
            try {
                ConnectedComputer computer = mapper.readValue(computerJackson, ConnectedComputer.class);
                connectedComputers.add(computer);
            } catch (Exception e) { e.printStackTrace(); }
        }

        return connectedComputers;
    }


    public static boolean removeComputer() {
        CommunicationManager.updateList();
        return false;
    }

}
