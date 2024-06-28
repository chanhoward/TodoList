import org.ToDoList.FileEncryption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class FileEncryptionTest {

    private static final String KEY_FILE = "key.bin";
    private static final String IV_FILE = "iv.bin";

    @BeforeEach
    public void setUp() throws IOException, NoSuchAlgorithmException {
        FileEncryption.initializeKeyAndIv();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(KEY_FILE));
        Files.deleteIfExists(Paths.get(IV_FILE));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello, World!", "", "VeryLongStringVeryLongStringVeryLongStringVeryLongString", "包含特殊字元~!@#$%^&*()_+=-`", "包含中文測試"})
    public void testEncryptAndDecrypt(String testData) {
        try {
            byte[] encryptedData = FileEncryption.encrypt(testData);
            assertNotNull(encryptedData, "加密後的資料不應為 null");

            String decryptedData = FileEncryption.decrypt(encryptedData);
            assertNotNull(decryptedData, "解密後的資料不應為 null");
            assertEquals(testData, decryptedData, "解密後的資料應與原始資料相同");

        } catch (Exception e) {
            fail("加密或解密過程中發生錯誤: " + e.getMessage());
        }
    }

    @Test
    public void testEncryptAndDecryptEmptyData() {
        try {
            String testData = "";
            byte[] encryptedData = FileEncryption.encrypt(testData);
            assertNotNull(encryptedData, "加密後的資料不應為 null");
            assertTrue(encryptedData.length > 0, "加密後的資料長度應大於 0");

            String decryptedData = FileEncryption.decrypt(encryptedData);
            assertNotNull(decryptedData, "解密後的資料不應為 null");
            assertEquals(testData, decryptedData, "解密後的資料應與原始資料相同");

        } catch (Exception e) {
            fail("加密或解密過程中發生錯誤: " + e.getMessage());
        }
    }

    @Test
    public void testEncryptAndDecryptLargeData() {
        try {
            // 生成 1MB 的測試資料
            byte[] largeData = new byte[1024 * 1024];
            Arrays.fill(largeData, (byte) 'A');
            String testData = new String(largeData, StandardCharsets.UTF_8);

            byte[] encryptedData = FileEncryption.encrypt(testData);
            assertNotNull(encryptedData, "加密後的資料不應為 null");
            assertTrue(encryptedData.length > testData.length(), "加密後的資料長度應大於原始資料長度");

            String decryptedData = FileEncryption.decrypt(encryptedData);
            assertNotNull(decryptedData, "解密後的資料不應為 null");
            assertEquals(testData, decryptedData, "解密後的資料應與原始資料相同");

        } catch (Exception e) {
            fail("加密或解密過程中發生錯誤: " + e.getMessage());
        }
    }
}