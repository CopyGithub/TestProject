package com.framework.tools.apkinfocompare;

import java.util.ArrayList;

import com.framework.common.ApkInfo;
import com.framework.common.Res;
import com.java.common.Utils;

public class ApkInfoCompare {

    /**
     * 比较两个apk的基本信息
     * 
     * @param target
     *            目标apk路径
     * @param compare
     *            对比apk路径
     * @throws Exception
     */
    public void execute(String target, String compare) throws Exception {
        Utils javaUtils = new Utils();
        javaUtils.fileCreate(Res.DIR_RESULT, true, true);
        String filePath = Res.DIR_RESULT + "apkInfo.txt";
        ApkInfo targetInfo = new ApkInfo(target);
        ApkInfo compareInfo = new ApkInfo(compare);
        javaUtils.writeText("版本的不同之处:\r\n", filePath, true);
        javaUtils.writeText(compareApkInfo(targetInfo, compareInfo), filePath, true);
        javaUtils.writeText("\r\n新版本的信息:\r\n", filePath, true);
        javaUtils.writeText(targetInfo.toString(), filePath, true);
        javaUtils.writeText("\r\n老版本的信息:\r\n", filePath, true);
        javaUtils.writeText(compareInfo.toString(), filePath, true);
    }

    private String compareApkInfo(ApkInfo target, ApkInfo compare) {
        String content = "";
        String difference = "  %s不一致--旧版本为:%s, 新版本为:%s.\r\n";
        if (target.fileSize != compare.fileSize) {
            content += String.format(difference, "文件大小", compare.fileSize + " Byte",
                    target.fileSize + " Byte");
        }
        if (target.versionCode != compare.versionCode) {
            content += String.format(difference, "版本号", compare.versionCode, target.versionCode);
        }
        if (!target.versionName.equals(compare.versionName)) {
            content += String.format(difference, "版本名", compare.versionName, target.versionName);
        }
        if (!target.packageName.equals(compare.packageName)) {
            content += String.format(difference, "包名", compare.packageName, target.packageName);
        }
        if (target.minSdkVersion != compare.minSdkVersion) {
            content += String.format(difference, "最低sdk版本", compare.minSdkVersion,
                    target.minSdkVersion);
        }
        if (target.targetSdkVersion != compare.targetSdkVersion) {
            content += String.format(difference, "目标sdk版本", compare.targetSdkVersion,
                    target.targetSdkVersion);
        }
        ArrayList<String> targetList = new ArrayList<String>();
        targetList.addAll(target.usesPermissions);
        ArrayList<String> compareList = new ArrayList<String>();
        compareList.addAll(compare.usesPermissions);
        for (int i = 0; i < targetList.size(); i++) {
            String usesPermission = targetList.get(i);
            if (compareList.contains(usesPermission)) {
                compareList.remove(usesPermission);
                targetList.remove(usesPermission);
                i--;
            }
        }
        content += "  新版本多余的权限:\r\n";
        for (String usesPermission : targetList) {
            content += "    " + usesPermission + "\r\n";
        }
        content += "  旧版本多余的权限:\r\n";
        for (String usesPermission : compareList) {
            content += "    " + usesPermission + "\r\n";
        }
        return content;
    }
}
