package com.cc.io;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleOperation {

    /**
     * 打印文本列表中的内容到控制台
     * 
     * @param contents
     */
    public void printArrayString(ArrayList<String> contents) {
        for (String string : contents) {
            System.out.println(string);
        }
    }

    /**
     * 从列表中获取用户选择
     * 
     * @param contents
     * @param all
     *            添加所有选项
     * @return 返回选择的文本内容
     */
    public String selectInput(ArrayList<String> contents, boolean all) {
        while (true) {
            int length = contents.size();
            if (length == 0) {
                System.out.println("当前没有可选项, 请确认后输入任意整数重试");
            } else {
                for (int i = 0; i < length; i++) {
                    String content = contents.get(i);
                    System.out.println((i + 1) + ":" + content);
                }
                if (all) {
                    System.out.println((length + 1) + ":all");
                }
            }
            int input = getIntOfSystemIn();
            if (input < 1 || input > length + 1) {
                continue;
            } else if (input == length + 1) {
                if (all) {
                    return "all";
                } else {
                    continue;
                }
            } else {
                return contents.get(input - 1);
            }
        }
    }

    /**
     * 从控制台获取一个{@code int}类型的值输入
     * 
     * @return 返回输入的{@code int}值
     */
    public int getIntOfSystemIn() {
        int input = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextInt();
            scanner.close();
        } catch (Exception e) {
        }
        return input;
    }

    /**
     * 从控制台获取一个{@link String}类型的值输入
     * 
     * @return 返回输入的{@link String}值
     */
    public String getStringOfSystemIn() {
        String input = "";
        try {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            scanner.close();
        } catch (Exception e) {
        }
        return input;
    }
}
