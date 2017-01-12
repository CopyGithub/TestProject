package com.framework.testcase;

import com.framework.common.ApkInfo;
import com.framework.common.Report;
import com.framework.common.Res;
import com.framework.common.Utils;
import com.framework.common.ApkInfo.TestClass;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

abstract class TestCase {
    ApkInfo targetApk;// 测试目标apk, 如果是升级,则表示安装的第一个apk
    ApkInfo updateApk;// 升级目标apk
    ApkInfo caseApk;// 执行的Case apk
    Utils utils = new Utils();
    com.java.common.Utils javaUtils = new com.java.common.Utils();
    Communication communication = new Communication();

    TestCase(final ApkInfo targetApk, final ApkInfo updateApk, final ApkInfo caseApk) {
        this.targetApk = targetApk;
        this.updateApk = updateApk;
        this.caseApk = caseApk;
    }

    abstract void execute(Report report, String adb, TestClass testClass);

    void executeCase(Report report, int reinstall, String adb, TestClass testClass) {
        switch (reinstall) {
        case 0:// 重装
            utils.installApk(adb, targetApk, true);
            break;
        case 1:// 升级
            utils.installApk(adb, updateApk, false);
            skipGuide(adb);// 用于消除升级后弹出的SonarGuide,11.4.8版本需求
            break;
        default:
            // NOTHING
            break;
        }
        executeCase(report, adb, testClass);
    }

    private void skipGuide(String adb) {
        for (int i = 0; i < 2; i++) {
            javaUtils.runtimeExec(null, utils.cmdForceStop(adb, targetApk.packageName), 10);
            javaUtils.sleep(1000);
            javaUtils.runtimeExec(null, utils.cmdStartActivity(adb, targetApk.packageName,
                    targetApk.launchableActivity, "", "", ""), 10);
            javaUtils.sleep(10 * 1000);
            javaUtils.runtimeExec(null, utils.cmdInputKeyEvent(adb, "KEYCODE_BACK"), 10);
            javaUtils.sleep(2 * 1000);
            javaUtils.runtimeExec(null, utils.cmdInputKeyEvent(adb, "KEYCODE_BACK"), 10);
            javaUtils.sleep(2 * 1000);
        }
    }

    void executeCase(Report report, String adb, TestClass testClass) {
        javaUtils.fileCreate(communication.getJsonFolder(adb), true, false);
        Timer timer = new Timer();
        if (!testClass.className.equals(Res.CASE_REPLACE_DATA)) {
            timer.schedule(new MyTimerTask(adb, testClass.classSimpleName), 1000, 1000);
        }

        ArrayList<String> result = new ArrayList<String>();
        javaUtils.runtimeExec(result, utils.cmdInstrument(adb, testClass.className,
                caseApk.packageName, caseApk.instrumentName), 20 * 60);

        javaUtils.printArrayString(result);
        report.status = report.analysis(result);
        report.formatReportContent(result);
        timer.cancel();
    }

    private class MyTimerTask extends TimerTask {
        private String adb;
        private String className;

        public MyTimerTask(String adb, String classname) {
            this.adb = adb;
            this.className = classname;
        }

        public void run() {
            // JSON文件pull到电脑
            communication.pullJsonToPC(adb);
            communication.classname = className;
            communication.communicateWithCase(adb);
        }
    }
}