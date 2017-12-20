package org.pauni.gnomeconnect.core.models;


import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.json.JSONException;
import org.json.JSONObject;
import org.pauni.gnomeconnect.core.interfaces.Protocol;


/**
 *      This represents a connected computer with GNOME Connect Desktop.
 *      It holds every needed information about the computer and acts
 *      kinda as a data model.
 */

public class Computer implements Protocol {
    private String publicKey   = null;
    private String fingerprint = null;
    private String ipAddress   = null;
    private String hostname    = null;
    private String model       = null;
    private String os          = null;
    private String version     = null;


    /*
    *       CONSTRUCTORS
    */

    public Computer() {}

    public Computer(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public Computer(JSONObject json) {
        String info = "";   // Short info if failed,  instead of huge stacktraces

        // Json obj don't necessarily have all fields. So each is checked individually



        try { // fingerprint
            this.fingerprint = json.getString(Keys.Packet.SRC_FINGERPRINT);
        } catch (JSONException e) {info += "field "+ Keys.Packet.SRC_FINGERPRINT + " not found";}

        try { // version
            this.version     = json.getString(Keys.Packet.VERSION);
        } catch (JSONException e) {info += "field "+ Keys.Packet.VERSION + " not found";}

        try { // os
            this.os          = json.getString(Keys.Device.OS);
        } catch (JSONException e) {info += "field "+ Keys.Device.OS + " not found";}

        try { // hostname
            this.hostname = json.getString(Keys.Device.HOSTNAME);
        } catch (JSONException e) {info += "field "+ Keys.Device.HOSTNAME + " not found";}

        try { // model
            this.model       = json.getString(Keys.Device.MODEL);
        } catch (JSONException e) {info += "field "+ Keys.Device.MODEL + " not found";}

        try { // public key
            this.publicKey   = json.getString(Keys.Device.PUBLIC_KEY);
        } catch (JSONException e) {info += "field "+ Keys.Device.PUBLIC_KEY + " not found";}

        Log.i("Computer", "Constructor from Json Errors: " + info);

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

    public String getHostname() {
        return hostname;
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
                && hostname    != null
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

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
