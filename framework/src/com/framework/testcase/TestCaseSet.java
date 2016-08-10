package com.framework.testcase;

import com.framework.common.ApkInfo;
import com.framework.common.Report;
import com.framework.common.Res;
import com.framework.common.Utils;
import com.framework.common.ApkInfo.TestClass;
import com.framework.common.Report.Status;
import com.framework.testcase.Task.TaskInfo;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class TestCaseSet {
    private ApkInfo targetApk;
    private ApkInfo updateApk;
    private ApkInfo caseApk;
    private ArrayList<ApkInfo> targetApkPlus;
    private Utils utils = new Utils();
    private com.java.common.Utils javaUtils = new com.java.common.Utils();
    private TaskInfo taskInfo;
    private TestCase testCase;
    private ArrayList<Report> reports = new ArrayList<Report>();
    private ScheduledThreadPoolExecutor[] executors;

    public TestCaseSet(ApkInfo targetApk, ApkInfo updateApk, ApkInfo caseApk,
            ArrayList<ApkInfo> targetApkPlus) {
        this.targetApk = targetApk;
        this.updateApk = updateApk;
        this.caseApk = caseApk;
        this.targetApkPlus = targetApkPlus;
    }

    public ArrayList<Report> execute(String[] adbs, TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
        switch (taskInfo.excuteType) {
        case Res.UPDATE:
            testCase = new UpdateCase(targetApk, updateApk, caseApk);
            break;
        case Res.NORMAL:
            testCase = new NormalCase(targetApk, null, caseApk);
            break;
        case Res.SMOKE:
            testCase = new NormalCase(targetApk, null, caseApk);
            break;
        default:
            return reports;
        }
        int deviceNum = adbs.length;
        executors = new ScheduledThreadPoolExecutor[deviceNum];
        for (int i = 0; i < deviceNum; i++) {
            utils.installApk(adbs[i], caseApk, true);
            if (testCase instanceof NormalCase) {
                utils.installApk(adbs[i], targetApk, true);
            }
            executors[i] = new ScheduledThreadPoolExecutor(1);
            executors[i].scheduleWithFixedDelay(new MyRunnable(adbs[i], i), 0, 1, TimeUnit.SECONDS);
        }
        while (true) {
            boolean flag = true;
            for (int i = 0; i < deviceNum; i++) {
                if (!executors[i].isShutdown()) {
                    flag = false;
                }
            }
            if (flag) {
                break;
            }
            javaUtils.sleep(5 * 1000);
        }
        return reports;
    }

    private class MyRunnable implements Runnable {
        private String adb;
        private int index;

        public MyRunnable(String adb, int index) {
            this.adb = adb;
            this.index = index;
        }

        @Override
        public void run() {
            TestClass mClass = getTestClass();
            if (mClass == null) {
                executors[index].shutdown();
                return;
            }
            executeCase(taskInfo, mClass, adb);
        }
    }

    private void executeCase(TaskInfo taskInfo, TestClass testClass, String adb) {
        Report report = new Report();
        report.taskId = taskInfo.taskId;
        report.taskName = taskInfo.taskName;
        report.className = testClass.classSimpleName;
        long time = System.currentTimeMillis();
        if (testClass.className == null) {
            report.status = Status.NOT_FOUND_CLASS;
        } else {
            report.caseId = testClass.annotationNumber;
            report.testContent = testClass.annotationClass;
            utils.ApksBatch(adb, targetApkPlus, true);
            // 清除截屏图片
            javaUtils.runtimeExec(null, utils.cmdDelete(adb, Res.DIR_PHONE_SCREENSHOT), 20);
            testCase.execute(report, adb, testClass);
            // pull截屏图片
            utils.PullScreenShot(adb, targetApk.versionCode, report.className);
            utils.ApksBatch(adb, targetApkPlus, false);
        }
        time = System.currentTimeMillis() - time;
        report.time = (time / 1000) + "." + ((time - time / 1000 * 1000) / 100)
                + ((time - time / 100 * 100) / 10) + (time - time / 10 * 10);
        reports.add(report);
    }

    private synchronized TestClass getTestClass() {
        if (caseApk.classes.size() == 0) {
            return null;
        }
        TestClass testClass = caseApk.classes.get(0);
        caseApk.classes.remove(0);
        return testClass;
    }
}