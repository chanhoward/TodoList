package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EncryptionService extends DataIO {

    private static final Logger LOGGER = LogManager.getLogger(EncryptionService.class);
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_FILE = "key.bin";
    private static final String IV_FILE = "iv.bin";
    // Cache for Cipher instances with expiration
    private static final ConcurrentMap<String, Cipher> cipherCache = new ConcurrentHashMap<>(100);
    private static SecretKey key;
    private static byte[] iv;

    static void initializeKeyAndIv() {
        LOGGER.info("Initializing key and IV...");
        File keyFile = new File(KEY_FILE);
        File ivFile = new File(IV_FILE);

        if (checkExistingFiles()) {
            loadKeyAndIv(keyFile, ivFile);
        } else if (keyFile.exists() || ivFile.exists()) {
            if (keyFile.exists()) {
                throw new RuntimeException("IV file is missing or corrupt.");
            } else {
                throw new RuntimeException("Key file is missing or corrupt.");
            }
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
                throw new RuntimeException("Invalid key or IV length detected.");
            }

            key = new SecretKeySpec(keyBytes, "AES");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load key or IV.", e);
        }
    }

    private static boolean checkExistingFiles() {
        LOGGER.info("Checking if key and IV files exist...");
        File keyFile = new File(KEY_FILE);
        File ivFile = new File(IV_FILE);

        if (keyFile.exists() && ivFile.exists()) {
            LOGGER.info("Key and IV files exist.");
            return true;
        } else {
            LOGGER.warn("Key or IV file does not exist.");
            return false;
        }
    }

    public static Cipher getEncryptCipher() {
        LOGGER.debug("Getting encrypt cipher...");
        return getCipher(Cipher.ENCRYPT_MODE);
    }

    public static Cipher getDecryptCipher() {
        LOGGER.debug("Getting decrypt cipher...");
        return getCipher(Cipher.DECRYPT_MODE);
    }

    private static Cipher getCipher(int mode) {
        String cacheKey = (mode == Cipher.ENCRYPT_MODE ? "ENCRYPT" : "DECRYPT");
        return cipherCache.computeIfAbsent(cacheKey, key -> {
            try {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(mode, EncryptionService.key, new IvParameterSpec(EncryptionService.iv));
                LOGGER.debug("Cipher initialized in {} mode.", cacheKey.toLowerCase());
                return cipher;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                throw new IllegalArgumentException("Failed to initialize the cipher due to algorithm/padding issue", e);
            } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
                throw new IllegalArgumentException("Failed to initialize the cipher due to invalid key/parameter", e);
            }
        });
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
            LOGGER.info("Key and IV generated successfully.");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate the key and IV: ", e);
        }
    }

    private static void saveKeyAndIv(File keyFile, File ivFile) {
        LOGGER.info("Saving key and IV to files...");
        try (FileOutputStream keyOut = new FileOutputStream(keyFile);
             FileOutputStream ivOut = new FileOutputStream(ivFile)) {
            keyOut.write(key.getEncoded());
            ivOut.write(iv);
            LOGGER.info("Key and IV saved successfully.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save key or IV: ", e);
        }
    }
}
