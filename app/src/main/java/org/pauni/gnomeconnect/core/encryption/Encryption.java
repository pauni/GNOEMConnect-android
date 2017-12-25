package org.pauni.gnomeconnect.core.encryption;

import android.util.Base64;

import org.pauni.gnomeconnect.core.models.Prefs;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by roni on 22.12.17.
 */

public class Encryption {
    private static KeyPair pair;
    public static final String KEY_SHARED_SECRET = "shared_secret";

    public static AsymCipher getAsymCipher() {
        return new AsymCipher();
    }

    public static SymCipher getSymCipher() {
        return new SymCipher();
    }

    public static void createSharedSecret(String preSharedSecret) {
        byte[] key;
        MessageDigest sha;

        try {
            //
            key = preSharedSecret.getBytes("UTF-8");

            sha = MessageDigest.getInstance("SHA-256");

            key = sha.digest(key);

            key = Arrays.copyOf(key, 16);

            SecretKey secretKey = new SecretKeySpec(key, "AES");

            String stringKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);

            Prefs.saveString(KEY_SHARED_SECRET, stringKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
