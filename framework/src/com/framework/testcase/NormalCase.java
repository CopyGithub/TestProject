package com.framework.testcase;

import com.framework.common.ApkInfo;
import com.framework.common.Report;
import com.framework.common.ApkInfo.TestClass;
import com.framework.common.Report.Status;
import com.framework.testcase.Communication.Operate;

class NormalCase extends TestCase {
    private int retry = 0;

    NormalCase(ApkInfo targetApk, ApkInfo updateApk, ApkInfo caseApk) {
        super(targetApk, updateApk, caseApk);
    }

    @Override
    void execute(Report report, String adb, TestClass testClass) {
        do {
            communication.deleteJsonFiles(adb);
            if (testClass.annotationReinstall) {
                executeCase(report, 0, adb, testClass);
            } else {
                executeCase(report, -1, adb, testClass);
            }
            while (report.status == Status.CRASH && Operate.RESTART.equals(communication.operate)) {
                communication.status = com.framework.testcase.Communication.Status.FINISHED;
                communication.setJsonValues(adb);
                communication.pushJsonToPhone(adb);
                System.out.println("正常crash,retry this case.");
                executeCase(report, adb, testClass);
            }
            retry++;
        } while (report.status == Status.CRASH && retry < 2);
    }

}
