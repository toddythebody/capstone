package org.launchcode.capstone.models.faces;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public class Encoder {

    private static String makeHash(String password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = factory.generateSecret(spec);
        Byte hash = new Byte(String.valueOf(key));
        return String.valueOf(key);
    }

    private static byte[] makeSalt(String name) {
        byte[] salt = new byte[name.length()];
        return salt;
    }

    public static String encode(String name, String password) throws Exception {
        return makeHash(password, makeSalt(name));
    }

    public static boolean match(String name, String password, String hash) throws Exception {
        return encode(name, password).equals(hash);
    }

}
