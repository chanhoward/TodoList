package org.ToDoList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
        try {
            initializeKeyAndIv();
        } catch (Exception e) {
            LOGGER.error("An error occurred while initializing the key and IV", e);
        }
    }

    public static void initializeKeyAndIv() throws NoSuchAlgorithmException, IOException {
        if (new File(KEY_FILE).exists() && new File(IV_FILE).exists()) {
            loadKeyAndIv();
        } else {
            generateKeyAndIv();
            saveKeyAndIv();
        }
    }

    private static void generateKeyAndIv() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // 使用 128 位元金鑰
        key = keyGenerator.generateKey();

        iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
    }

    private static void saveKeyAndIv() throws IOException {
        try (FileOutputStream keyOut = new FileOutputStream(KEY_FILE);
             FileOutputStream ivOut = new FileOutputStream(IV_FILE)) {
            keyOut.write(key.getEncoded());
            ivOut.write(iv);
        }
    }

    private static void loadKeyAndIv() {
        byte[] keyBytes = new byte[16];
        iv = new byte[16];

        /*
        try (FileInputStream keyIn = new FileInputStream(KEY_FILE);
             FileInputStream ivIn = new FileInputStream(IV_FILE)) {
            keyIn.read(keyBytes);
            ivIn.read(iv);
        }
        */
        key = new SecretKeySpec(keyBytes, "AES");
    }

    public static byte[] encrypt(String data) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return new String(cipher.doFinal(encryptedData));
    }
}