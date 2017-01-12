package com.framework.common;

import java.io.File;
import java.util.ArrayList;

import com.framework.common.Report.Status;

public class Utils {
    private com.java.common.Utils javaUtils = new com.java.common.Utils();
    private String cp = Res.DIR_PHONE_TMP + "./busybox cp";
    private String tar = Res.DIR_PHONE_TMP + "./busybox tar";

    public String getBackupDir(String versionCode, String classSimpleName) {
        return Res.DIR_PC_BACKUP + versionCode + File.separator + classSimpleName;
    }

    public String getScreenShotDir(int versionCode, String classSimpleName) {
        return Res.DIR_PC_SCREENSHOT + String.valueOf(versionCode) + File.separator
                + classSimpleName;
    }

    public String[] getAdbs(String devices) {
        String[] deviceList = devices.split(",");
        String[] adbs = new String[deviceList.length];
        for (int i = 0; i < deviceList.length; i++) {
            adbs[i] = String.format("adb -s %s", deviceList[i]);
        }
        return adbs;
    }

    public String cmdDelete(String adb, String filePath) {
        return String.format("%s shell rm -rf %s", adb, filePath);
    }

    public String cmdMkdir(String adb, String filePath) {
        return String.format("%s shell mkdir -p %s", adb, filePath);
    }

    public String cmdPush(String adb, String pcPath, String phonePath) {
        return String.format("%s push %s %s", adb, pcPath, phonePath);
    }

    public String cmdPull(String adb, String phonePath, String pcPath) {
        return String.format("%s pull %s %s", adb, phonePath, pcPath);
    }

    public String cmdCpAppData(String adb, String fromPath, String toPath) {
        return String.format("%s shell su -c \"%s -R %s %s\"", adb, cp, fromPath, toPath);
    }

    public String cmdClearAppData(String adb, String packageName) {
        return String.format("%s shell pm clear %s", adb, packageName);
    }

    public String cmdChmod(String adb, String permission, String filePath) {
        return String.format("%s shell chmod %s %s", adb, permission, filePath);
    }

    public String cmdForceStop(String adb, String packageName) {
        return String.format("%s shell am force-stop %s", adb, packageName);
    }

    public String cmdInputKeyEvent(String adb, String keyevent) {
        return String.format("%s shell input keyevent %s", adb, keyevent);
    }

    public String cmdCompress(String adb, String archive, String path, String fileName) {
        return String.format("%s shell %s -czf %s -C %s %s", adb, tar, archive, path, fileName);
    }

    public String cmdExtract(String adb, String archive, String destPath) {
        return String.format("%s shell %s -xzf %s -C %s", adb, tar, archive, destPath);
    }

    public String cmdUninstall(String adb, String packageName) {
        return String.format("%s uninstall %s", adb, packageName);
    }

    public String cmdInstall(String adb, String apkPath) {
        return String.format("%s install %s", adb, apkPath);
    }

    public String cmdUpgrade(String adb, String apkPath) {
        return String.format("%s install -r %s", adb, apkPath);
    }

    public String cmdCatFile(String adb, String filePath) {
        return String.format("%s shell cat %s", adb, filePath);
    }

    public String cmdTop(String adb) {
        return String.format("%s shell top -n 1 -d 0", adb);
    }

    public String cmdInstrument(String adb, String className, String packageName,
            String instrumentName) {
        return String.format("%s shell am instrument -w -e class %s %s/%s", adb, className,
                packageName, instrumentName);
    }

    public String cmdLogcatClear(String adb) {
        return String.format("%s logcat -c", adb);
    }

    public String cmdLogcatDump(String adb) {
        return String.format("%s logcat -d -v time", adb);
    }

    public String cmdStartActivity(String adb, String packageName, String launchableActivity,
            String act, String cat, String dat) {
        String launcherActivity = String.format("%s shell am start -n %s/%s", adb, packageName,
                launchableActivity);
        if (!act.isEmpty()) {
            launcherActivity += " -a " + act;
        }
        if (!cat.isEmpty()) {
            launcherActivity += " -c " + cat;
        }
        if (!dat.isEmpty()) {
            launcherActivity += " -d " + dat;
        }
        return launcherActivity;
    }

    /**
     * 安装apk
     * 
     * @param adb
     *            安装的设备
     * @param apkInfo
     *            apk的基本信息
     * @param reinstall
     *            是否卸载重装
     */
    public void installApk(String adb, final ApkInfo apkInfo, boolean reinstall) {
        ArrayList<String> contents = new ArrayList<String>();
        if (reinstall) {
            // TODO,需要对结果进行判断, 以便能够稳定运行
            javaUtils.runtimeExec(contents, cmdUninstall(adb, apkInfo.packageName), 20);
            javaUtils.printArrayString(contents);
            // TODO,需要对结果进行判断, 以便能够稳定运行
            javaUtils.runtimeExec(contents, cmdInstall(adb, apkInfo.filePath), 40);
            javaUtils.printArrayString(contents);
        } else {
            // TODO,需要对结果进行判断, 以便能够稳定运行
            javaUtils.runtimeExec(contents, cmdUpgrade(adb, apkInfo.filePath), 40);
            javaUtils.printArrayString(contents);
        }
    }

    public void ApksBatch(String adb, ArrayList<ApkInfo> apkinfos, boolean isInstall) {
        ArrayList<String> contents = new ArrayList<String>();
        if (isInstall) {
            for (ApkInfo apkInfos : apkinfos) {
                installApk(adb, apkInfos, true);
            }
        } else {
            for (ApkInfo apkInfos : apkinfos) {
                String uninstall = cmdUninstall(adb, apkInfos.packageName);
                javaUtils.runtimeExec(contents, uninstall, 60);
                javaUtils.printArrayString(contents);
            }
        }
    }

    public void PullScreenShot(String adb, int versionCode, String classSimpleName) {
        ArrayList<String> contents = new ArrayList<String>();
        String ScreenShotPath = getScreenShotDir(versionCode, classSimpleName);
        javaUtils.fileCreate(ScreenShotPath, true, true);
        javaUtils.runtimeExec(contents, cmdPull(adb, Res.DIR_PHONE_SCREENSHOT, ScreenShotPath), 20);
        javaUtils.printArrayString(contents);
    }

    public boolean needScreenShot(String status) {
        boolean isError = Status.ERROR.toString().equals(status);
        boolean isFailure = Status.FAILURES.toString().equals(status);
        return isError || isFailure;
    }
}