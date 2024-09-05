package com.codebydaud.training.banking_app.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
public class EncryptionUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";

    public static String decrypt(String encryptedPassword) throws Exception {
        String[] parts = encryptedPassword.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encryptedPassword format. Must be IV:encrypted");
        }

        String iv = parts[0];
        String encrypted = parts[1];

        byte[] ivBytes = hexToBytes(iv);
        byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey().getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        byte[] original = cipher.doFinal(encryptedBytes);

        log.info(new String(original));
        return new String(original, "UTF-8");
    }

    private static String getSecretKey() {
        String secretKey = System.getProperty("SECRET_KEY");
        if (secretKey == null || secretKey.length() != 32) {
            throw new IllegalStateException("Secret key must be exactly 16 bytes long");
        }
        return secretKey;
    }

    // Convert hexadecimal string to byte array
    private static byte[] hexToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
