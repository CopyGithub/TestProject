package com.framework.tools.apkinfocompare;

import com.java.common.Utils;

public class RunApkInfoCompare {

    public static void main(String[] args) throws Exception {
        Utils javaUtils = new Utils();
        String target = "";
        String compare = "";
        if (args.length < 2) {
            System.out.println("请输入目标APK的路径");
            target = javaUtils.getStringOfSystemIn();
            System.out.println("请输入对比APK的路径");
            compare = javaUtils.getStringOfSystemIn();
        } else {
            target = args[0];
            compare = args[1];
        }
        new ApkInfoCompare().execute(target, compare);
        System.out.println("对比完成, 请查看result目录下的apkInfo.txt文件");
    }
}
