package com.framework.performance;

public class RunPerformance {

    public static void main(String[] args) throws Exception {
        System.out.println("开始执行");
        Task task = new Task();
        if (args.length == 0) {
            task.execute(null);
        } else {
            task.execute(args[0]);
        }
        System.out.println("执行完成");
    }
}
