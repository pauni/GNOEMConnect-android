package org.pauni.gnomeconnect.core.encryption;

import android.util.Base64;



import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by roni on 22.12.17.
 */

public class SymCipher {


    public String encrypt(String text, String sharedSecret) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SecretKey key = generateKey(sharedSecret);
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(text.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String text, String sharedSecret) throws
            UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        SecretKey key = generateKey(sharedSecret);

        // convert base64 String to byte-array
        byte[] encrypted = Base64.decode(text, Base64.DEFAULT);

        // decrypt
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] cipherData = cipher.doFinal(encrypted);

        return new String(cipherData);

    }

    private SecretKey generateKey(String sharedSecret) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key;
        MessageDigest sha;

        key = sharedSecret.getBytes("UTF-8");

        sha = MessageDigest.getInstance("SHA-256");

        key = sha.digest(key);

        key = Arrays.copyOf(key, 16);

        return new SecretKeySpec(key, "AES");
    }

}
