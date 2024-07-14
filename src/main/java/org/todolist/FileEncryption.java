package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class FileEncryption {

    private static final Logger LOGGER = LogManager.getLogger(FileEncryption.class);
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_FILE = "key.bin";
    private static final String IV_FILE = "iv.bin";
    private static SecretKey key;
    private static byte[] iv;

    static {
        initializeKeyAndIv();
    }

    static void initializeKeyAndIv() {
        File keyFile = new File(KEY_FILE);
        File ivFile = new File(IV_FILE);

        if (keyFile.exists() && ivFile.exists()) {
            loadKeyAndIv(keyFile, ivFile);
        } else {
            generateKeyAndIv();
            saveKeyAndIv(keyFile, ivFile);
        }
    }

    private static void loadKeyAndIv(File keyFile, File ivFile) {
        LOGGER.info("Loading key and IV from files...");
        try (FileInputStream keyIn = new FileInputStream(keyFile);
             FileInputStream ivIn = new FileInputStream(ivFile)) {
            byte[] keyBytes = keyIn.readAllBytes();
            iv = ivIn.readAllBytes();

            if (keyBytes.length != 32 || iv.length != 16) { // 32 bytes for 256-bit key
                LOGGER.error("Incomplete key or IV data");
                throw new IOException("Failed to read the complete key or IV");
            }

            key = new SecretKeySpec(keyBytes, "AES");
        } catch (IOException e) {
            LOGGER.error("An error occurred while loading the key and IV", e);
            throw new RuntimeException("Failed to load key and IV", e);
        }
    }

    private static void generateKeyAndIv() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // 使用 256 位元金鑰
            key = keyGenerator.generateKey();

            iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("An error occurred while generating the key and IV", e);
            throw new RuntimeException("Failed to generate key and IV", e);
        }
    }

    private static void saveKeyAndIv(File keyFile, File ivFile) {
        try (FileOutputStream keyOut = new FileOutputStream(keyFile);
             FileOutputStream ivOut = new FileOutputStream(ivFile)) {
            keyOut.write(key.getEncoded());
            ivOut.write(iv);
        } catch (IOException e) {
            LOGGER.error("An error occurred while saving the key and IV", e);
            throw new RuntimeException("Failed to save key and IV", e);
        }
    }

    public static byte[] encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            LOGGER.error("An error occurred while encrypting data", e);
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    public static String decrypt(byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] decryptedBytes = cipher.doFinal(encryptedData);
            return new String(decryptedBytes);
        } catch (Exception e) {
            LOGGER.error("An error occurred while decrypting data", e);
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }
}