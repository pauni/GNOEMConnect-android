package org.pauni.gnomeconnect.core.encryption;

import android.util.Base64;

import org.pauni.gnomeconnect.core.models.Prefs;
import org.pauni.gnomeconnect.core.utils.Utils;

import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 * Created by roni on 22.12.17.
 */

public class AsymCipher {


    public String encrypt(String unencryptedText, PublicKey key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return Base64.encodeToString(cipher.doFinal(unencryptedText.getBytes()), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, Utils.getPrivateKey());

            return new String(cipher.doFinal(Base64.decode(encryptedText, Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
