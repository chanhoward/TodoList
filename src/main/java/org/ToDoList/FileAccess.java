package org.ToDoList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final String Data_FILE = "Data.dat";
    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final FileEncryption fileEncryption = new FileEncryption();

    static {
        try {
            FileEncryption.initializeKeyAndIv();
        } catch (IOException | NoSuchAlgorithmException e) {
            LOGGER.error("初始化金鑰和 IV 時發生錯誤：", e);
        }
    }

    public void accessFile(TaskClass newList) {
        List<TaskClass> tasks = readFile();
        tasks.add(newList);
        writeFile(tasks);
    }

    private void writeFile(List<TaskClass> tasks) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(tasks);
            byte[] encryptedData = fileEncryption.encrypt(json);
            try (FileOutputStream fos = new FileOutputStream(Data_FILE)) {
                fos.write(encryptedData);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                 | BadPaddingException | InvalidAlgorithmParameterException | IOException e) {
            LOGGER.error("寫入文件時發生錯誤：", e);
        }
    }

    public List<TaskClass> readFile() {
        List<TaskClass> tasks = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(Data_FILE)) {
            File DataFile = new File(Data_FILE);

            if (!DataFile.exists()) {
                boolean created = DataFile.createNewFile();
                if (created) {
                    LOGGER.info("文件創建成功：" + Data_FILE);
                } else {
                    LOGGER.warn("文件已存在，未創建：" + Data_FILE);
                }
                return tasks; // 返回空列表，因為文件是新建的
            }

            byte[] fileData = new byte[(int) DataFile.length()];
            int bytesRead = fis.read(fileData);
            if (bytesRead != fileData.length) {
                throw new IOException("無法讀取完整的檔案資料");
            }

            String decryptedData = fileEncryption.decrypt(fileData);
            ObjectMapper objectMapper = new ObjectMapper();
            tasks = objectMapper.readValue(decryptedData, new TypeReference<>() {
            });

        } catch (Exception e) { // ERROR: no find files(iv.bin, key.bin, Data.dat) will crash.
            LOGGER.error("讀取文件時發生錯誤：", e);
        }
        return tasks;
    }
}