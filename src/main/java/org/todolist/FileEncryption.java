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

public class FileEncryption extends FileAccess {

    private static final Logger LOGGER = LogManager.getLogger(FileEncryption.class);
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_FILE = "key.bin";
    private static final String IV_FILE = "iv.bin";
    private static SecretKey key;
    private static byte[] iv;

    static void initializeKeyAndIv() {
        LOGGER.info("Initializing key and IV...");
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
            }

            key = new SecretKeySpec(keyBytes, "AES");
        } catch (IOException e) {
            LOGGER.error("An error occurred while loading the key and IV: ", e);
        }
    }

    private static void generateKeyAndIv() {
        LOGGER.info("Generating key and IV...");
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            key = keyGenerator.generateKey();

            iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("An error occurred while generating the key and IV: ", e);
        }
    }

    private static void saveKeyAndIv(File keyFile, File ivFile) {
        LOGGER.info("Building key and IV files...");
        try (FileOutputStream keyOut = new FileOutputStream(keyFile);
             FileOutputStream ivOut = new FileOutputStream(ivFile)) {
            keyOut.write(key.getEncoded());
            ivOut.write(iv);
        } catch (IOException e) {
            LOGGER.error("An error occurred while saving the key and IV: ", e);
        }
    }

    public static Cipher getEncryptCipher() {
        LOGGER.info("Getting encrypt cipher...");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher;
        } catch (Exception e) {
            LOGGER.error("An error occurred while initializing the encrypt cipher", e);
            throw new IllegalArgumentException("Failed to initialize the encrypt cipher");
        }
    }

    public static Cipher getDecryptCipher() {
        LOGGER.info("Getting decrypt cipher...");
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher;
        } catch (Exception e) {
            LOGGER.error("An error occurred while initializing the decrypt cipher", e);
            throw new IllegalArgumentException("Failed to initialize the decrypt cipher");
        }
    }
}
