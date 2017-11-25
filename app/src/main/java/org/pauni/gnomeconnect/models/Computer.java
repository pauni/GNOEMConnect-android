package org.pauni.gnomeconnect.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import org.json.JSONException;
import org.json.JSONObject;


/**
 *      This represents a connected computer with GNOME Connect Desktop.
 *      It holds every needed information about the computer and acts
 *      kinda as a data model.
 */

public class Computer {
    private String publicKey   = null;
    private String fingerprint = null;
    private String ipAddress   = null;
    private String model       = null;
    private String name        = null;
    private String os          = null;
    private String version     = null;


    /*
    *       CONSTRUCTORS
    */

    public Computer() {}

    // i should really create a constructor where i can just pass jsons...

    public static Computer fromJson(String json) {
        try {
            return  fromJson(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Computer fromJson(JSONObject json) {
        Computer computer = new Computer();
        int initFields = 0; // Counter for num of fields that have been init.

        // Usually "hostname", "fingerprint" and "version" are only in the header.
        // But the GnomeSpotter receives a plain JSON obj with all these info as
        // a broadcast response and no packet yet.



        // hostname (readable string/name of sender)
        try {
            computer.setName(json.getString(Specs.HOSTNAME));
            initFields++;
        } catch (JSONException e) {e.printStackTrace();}


        // fingerprint
        try {
            computer.setFingerprint(json.getString(Specs.FINGERPRINT));
            initFields++;
        } catch (JSONException e) {e.printStackTrace();}


        // version
        try {
            computer.setVersion(json.getString(Specs.VERSION));
            initFields++;
        } catch (JSONException e) {e.printStackTrace();}


        // os
        try {
            computer.setOs(json.getString(Specs.OS));
            initFields++;
        } catch (JSONException e) {e.printStackTrace();}


        // model
        try {
            computer.setModel(json.getString(Specs.MODEL));
            initFields++;
        } catch (JSONException e) {e.printStackTrace();}


        // public key
        try {
            computer.setPublicKey(json.getString(Specs.PUBLIC_KEY));
            initFields++;
        } catch (JSONException e) {e.printStackTrace();}



        if (initFields == 0)
            return null;

        return computer;
    }


    /*
     *      GETTER
     */
    public String getPublicKey() {
        return publicKey;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getOs() {
        return os;
    }

    public String getVersion() {
        return version;
    }


    @JsonIgnore
    public boolean isComplete() {
        return publicKey != null
                && fingerprint != null
                && ipAddress   != null
                && model       != null
                && name        != null
                && os          != null
                && version     != null;
    }




    /*
     *      SETTER
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public  void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
