package com.framework.testcase;

import com.framework.common.ApkInfo;
import com.framework.common.Report;
import com.framework.common.Res;
import com.framework.common.ApkInfo.TestClass;
import com.framework.common.Report.Status;
import com.framework.testcase.Communication.Operate;

import java.io.File;
import java.util.ArrayList;

class UpdateCase extends TestCase {
    private String backupData;
    private int retry = 0;

    public UpdateCase(ApkInfo targetApk, ApkInfo updateApk, ApkInfo caseApk) {
        super(targetApk, updateApk, caseApk);
    }

    @Override
    public void execute(Report report, String adb, TestClass testClass) {
        backupData = utils.getBackupDir(String.valueOf(targetApk.versionCode),
                testClass.classSimpleName);
        if (!new File(backupData).exists()) {
            report.status = Status.NOT_DATA;
            report.content = "没有对应版本对应测试类的数据";
            return;
        }
        pushData(adb);
        TestClass replaceData = new TestClass();
        replaceData.className = Res.CASE_REPLACE_DATA;
        communication.deleteJsonFiles(adb);
        do {
            retry++;
            executeCase(report, 0, adb, replaceData);
        } while (report.status != Status.OK && retry < 2);
        if (report.status == Status.OK) {
            report.restoreDefault();
            executeCase(report, 1, adb, testClass);
            while (report.status == Status.CRASH && Operate.RESTART.equals(communication.operate)) {
                communication.status = com.framework.testcase.Communication.Status.FINISHED;
                communication.setJsonValues(adb);
                communication.pushJsonToPhone(adb);
                System.out.println("正常crash,retry this case.");
                executeCase(report, adb, testClass);
            }
            while (report.status == Status.CRASH && retry < 2) {
                execute(report, adb, testClass);
                retry++;
            }
        }
    }

    private void pushData(String adb) {
        com.java.common.Utils javaUtils = new com.java.common.Utils();
        ArrayList<String> cmds = new ArrayList<String>();
        // 清理遗留的备份目录并创建相关目录
        cmds.add(utils.cmdDelete(adb, Res.DIR_PHONE_BACKUP));
        cmds.add(utils.cmdMkdir(adb, Res.DIR_PHONE_TAR));
        // 推送数据到手机
        cmds.add(utils.cmdPush(adb, backupData, Res.DIR_PHONE_TAR));
        // 执行并打印结果
        ArrayList<String> contents = new ArrayList<String>();
        for (String cmd : cmds) {
            javaUtils.runtimeExec(contents, cmd, 30);
            javaUtils.printArrayString(contents);
        }
    }
}
