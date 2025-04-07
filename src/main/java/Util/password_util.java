package Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class password_util {

    public static String generateSalt()  {
        byte[] salt = new byte[16]; // 16 bytes (128 bits)
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }

    public static boolean verifyPassword(String storedHash, String salt, String inputPassword) {
        String inputHash = hashPassword(inputPassword, salt);
        return storedHash.equals(inputHash);
    }
}
