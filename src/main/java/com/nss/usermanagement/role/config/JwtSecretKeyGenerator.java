package com.nss.usermanagement.role.config;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JwtSecretKeyGenerator {
    public static void main(String[] args) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
            keyGen.init(512); // You can choose the key size (e.g., 256, 384, 512)
            SecretKey secretKey = keyGen.generateKey();
            String base64Secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Secret Key: " + base64Secret);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
