import java.io.*;

public class checkFile {
    private static final String FILE_NAME = "document.json";

    public static void main(String[] args) {

        try {
            FileReader reader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("未讀取到文件");
        } catch (IOException e) {
            System.out.println("讀取文件時發生錯誤。");
        }
        removeFile();

    }

    public static void removeFile() {
        File file = new File(FILE_NAME);
        if (file.delete()) {
            System.out.printf("文件已刪除: %s", file.getName());
        } else {
            System.out.println("刪除文件失敗。");
        }
    }
}
