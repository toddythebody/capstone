package org.launchcode.capstone.models.faces;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

public interface Encoder {

    static String makeHash(String password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return String.valueOf(hash);
    }

    static byte[] makeSalt(String name) {
        Byte bigByte = new Byte(name);
        byte[] salt = new byte[bigByte];
        return salt;
    }

    static String encode(String name, String password) throws Exception {
        return makeHash(password, makeSalt(name));
    }

    static boolean match(String name, String password, String hash) throws Exception {
        return encode(name, password).equals(hash);
    }

}
