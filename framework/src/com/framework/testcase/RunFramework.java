package com.framework.testcase;

public class RunFramework {

    public static void main(String[] args) throws Exception {
        Task task = new Task();
        com.java.common.Utils javaUtils = new com.java.common.Utils();
        String taskPath;
        if (args.length > 0) {
            taskPath = args[0];
        } else {
            System.out.println("请输入Excel任务的路径");
            taskPath = javaUtils.getStringOfSystemIn();
        }
        String devices = args.length == 2 ? args[1] : javaUtils.selectInput(
                javaUtils.getAllDevices(), false);
        System.out.println("开始");
        long time = System.currentTimeMillis();
        task.executeExcel(taskPath, devices);
        time = System.currentTimeMillis() - time;
        System.out.println("完成,耗时:" + time);
    }
}