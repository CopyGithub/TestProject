package com.framework.performance;

import java.util.ArrayList;

import com.framework.common.ApkInfo;
import com.framework.common.Report;
import com.framework.common.Report.Status;
import com.framework.common.Utils;

abstract class TestCase {
    protected Utils utils = new Utils();
    protected com.java.common.Utils javaUtils = new com.java.common.Utils();
    protected String adb;
    protected TaskInfo taskInfo;
    protected ApkInfo targetInfo;
    protected ApkInfo caseInfo;
    protected PerformanceReport report = new PerformanceReport();

    protected TestCase(TaskInfo taskInfo, ApkInfo targetInfo, ApkInfo caseInfo) {
        adb = "adb -s " + taskInfo.deviceInfo[1];
        this.taskInfo = taskInfo;
        this.targetInfo = targetInfo;
        this.caseInfo = caseInfo;
    }

    protected abstract void execute() throws Exception;

    protected boolean executeCase() {
        ArrayList<String> out = new ArrayList<String>();
        javaUtils.runtimeExec(out, utils.cmdInstrument(adb, taskInfo.testCaseName,
                caseInfo.packageName, caseInfo.instrumentName), 20 * 60);
        Report report = new Report();
        report.status = report.analysis(out);
        return report.status == Status.OK;
    }
}
