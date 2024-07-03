package org.todolist;

public class Main {

    public static void main(String[] args) {
        FileAccess.readDataFile();
        Menu.commandMain();
    }

}