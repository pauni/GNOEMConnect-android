package org.pauni.gnomeconnect.core.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Base64;
import android.util.Log;

import org.pauni.gnomeconnect.core.encryption.Encryption;
import org.pauni.gnomeconnect.core.models.Prefs;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * Outsourcing some methods to keep the code clean, readable, short etc.
 */

public class Utils {

    Utils() {}







    /*
    *   INFORMATION ABOUT THE PHONE
    */


    public static String getDeviceName() {
        return "MANUFACTURER=" + Build.MANUFACTURER + " BRAND=" + Build.BRAND + " BOARD="
                + " DEVICE=" + Build.DEVICE + " MODEL=" + Build.MODEL
                + " RELEASE=" + Build.VERSION.RELEASE;
    }

    public static PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String key64 = Prefs.getString("private_key");

        if (key64 == null) {
            savePrivateKey(getKeyPair().getPrivate());
            key64 = Prefs.getString("private_key");

            if (key64 == null) {
                // if it's still null, throw exception and tell user he got a problem!
                throw new RuntimeException("PrivateKey either couldn't be generated or not saved");
            }
        }

        try {
            byte[] clear = Base64.decode(key64, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
            KeyFactory fact = KeyFactory.getInstance("DSA");
            PrivateKey priv = fact.generatePrivate(keySpec);
            Arrays.fill(clear, (byte) 0);

            return priv;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String key64 = Prefs.getString("public_key");

        if (key64 == null) {
            savePublicKey(getKeyPair().getPublic());
            key64 = Prefs.getString("public_key");

            if (key64 == null) {
                // if it's still null, throw exception and tell user he got a problem!
                throw new RuntimeException("PublicKey either couldn't be generated or not saved");
            }
        }

        try {
            byte[] data = Base64.decode(key64, Base64.DEFAULT);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("DSA");

            return fact.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void savePrivateKey(PrivateKey priv) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory fact = KeyFactory.getInstance("DSA");
        PKCS8EncodedKeySpec spec = fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
        byte[] packed = spec.getEncoded();
        String key64 = Base64.encodeToString(packed, Base64.DEFAULT);

        Arrays.fill(packed, (byte) 0);
        Prefs.saveString("private_key", key64);

    }

    private static void savePublicKey(PublicKey publ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory fact = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec spec = fact.getKeySpec(publ, X509EncodedKeySpec.class);
        String keyString = Base64.encodeToString(spec.getEncoded(), Base64.DEFAULT);

        Prefs.saveString("public_key", keyString);
    }

    private static KeyPair getKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFingerprint() {
        PublicKey key;
        try {
            key = getPublicKey();

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(key.getEncoded());
            return new String(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





    // returns true, if phone is ready for user-interactions
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm != null && pm.isInteractive();
    }

    // returns true, if lock screen is passed
    public static boolean isPhoneUnlocked(Context context) {
        KeyguardManager mgr = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        return !(mgr != null && mgr.isKeyguardSecure());
    }


    /*
    *   CONVERTING, GENERATE
    */
    public static String convertIpAddressToString(int ip4addr) {
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ip4addr = Integer.reverseBytes(ip4addr);
        }
        byte[] ipByteArray = BigInteger.valueOf(ip4addr).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            ipAddressString = "not connected";
        }
        return ipAddressString;
    }



}
