package org.ToDoList;

import java.util.List;
import java.util.Scanner;

public class ToDoList_Main {
    private final FileAccess fileAccess;
    Scanner input = new Scanner(System.in);

    public ToDoList_Main(FileAccess fileAccess) {
        this.fileAccess = fileAccess;
    }

    public static void main(String[] args) {
        // 創建 FileAccess 實例並注入到 ToDoList_Main
        FileAccess fileAccess = new FileAccess();
        ToDoList_Main main = new ToDoList_Main(fileAccess);
        main.run();
    }

    public final void run() {
        TaskClass taskClass = input();
        fileAccess.accessFile(taskClass);
        output();
    }

    public final TaskClass input() {
        System.out.println("請輸入內容: ");
        String inputContent = input.nextLine();

        System.out.println("請輸入作者: ");
        String inputAuthor = input.nextLine();

        CustomLocalTime customLocalTime = new CustomLocalTime();
        String currentTime = customLocalTime.getTime();

        return new TaskClass(currentTime, inputContent, inputAuthor);

    }

    public final void output() {
        List<TaskClass> lists = fileAccess.readFile();
        for (TaskClass taskClass : lists) {

            System.out.println(taskClass.content());
            System.out.print("by " + taskClass.author());
            System.out.printf("\t(%s)\n", taskClass.time());
            System.out.println("-------------------------------------------------------------------------");
        }

    }
}
