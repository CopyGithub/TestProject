package com.framework.performance;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

import com.framework.common.ApkInfo;
import com.framework.common.Res;

class TimeCostCase extends TestCase {
    private int startLog = 0;
    private int endLog = 0;

    protected TimeCostCase(TaskInfo taskInfo, ApkInfo targetInfo, ApkInfo caseInfo) {
        super(taskInfo, targetInfo, caseInfo);
    }

    @Override
    protected void execute() throws Exception {
        boolean flag = false;
        utils.installApk(adb, targetInfo, true);
        javaUtils.createFileOrDir(Res.DIR_RESULT, true, false);
        if (Res.LAUNCHER_TYPE[0].equals(taskInfo.testCaseName)) {
            flag = firstLauncher();
        } else if (Res.LAUNCHER_TYPE[1].equals(taskInfo.testCaseName)) {
            commonLauncher();
        } else if (Res.LAUNCHER_TYPE[2].equals(taskInfo.testCaseName)) {
            externalLinkFirstLauncher();
        } else if (Res.LAUNCHER_TYPE[3].equals(taskInfo.testCaseName)) {
            externalLinkCommonLauncher();
        } else if (Res.LAUNCHER_TYPE[4].equals(taskInfo.testCaseName)) {
            webLoadTest();
            return;
        } else {
            throw new Exception("选择的类型[" + taskInfo.testCaseName + "]不支持");
        }
        // 当有数据且只生成一个报告时,直接初始化report
        if (flag) {
            recordReport(report);
        }
    }

    /**
     * 初始化report值
     * 
     * @param report
     */
    private void recordReport(PerformanceReport report) {
        report.productName = targetInfo.appName;
        report.productCode = targetInfo.packageName;
        report.versionCode = targetInfo.versionCode;
        report.versionName = targetInfo.versionName;
        report.extraInfo = taskInfo.extraInfo;
        report.performanceType = taskInfo.performanceType;
        report.caseName = taskInfo.testCaseName;
        report.deviceInfo = taskInfo.deviceInfo;
        report.num = taskInfo.num;
        report.averageValue = calculateAverage(report.datas);
        report.median = calculateMedian(report.datas);
        System.out.println("平均时间:" + report.averageValue);
        System.out.println("中位值时间:" + report.median);
        JSONObject json = report.convertJSONArray();
        javaUtils.writeText(json.toString(), Res.DIR_RESULT + System.currentTimeMillis() + ".txt",
                false);
    }

    /**
     * 首次启动的性能测试
     */
    private boolean firstLauncher() {
        boolean flag = false;
        for (int i = 0; i < taskInfo.num; i++) {
            javaUtils.runtimeExec(null, utils.cmdClearAppData(adb, targetInfo.packageName), 10);
            boolean record = launchAndRecordLog(report, Res.ACT_MAIN,
                    Res.CAT_LAUNCHER, "", i, 6 * 1000, new String[] { "START u0", Res.ACT_MAIN,
                            Res.CAT_LAUNCHER, targetInfo.packageName },
                    new String[] { "App Start takes" });
            if (record) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 非首次的冷启动的性能测试
     */
    private boolean commonLauncher() {
        boolean flag = false;
        javaUtils.runtimeExec(null, utils.cmdStartActivity(adb, targetInfo.packageName,
                targetInfo.launchableActivity, "", "", ""), 20);
        javaUtils.sleep(20 * 1000);
        for (int i = 0; i < taskInfo.num; i++) {
            boolean record = launchAndRecordLog(report, Res.ACT_MAIN,
                    Res.CAT_LAUNCHER, "", i, 6 * 1000, new String[] { "START u0", Res.ACT_MAIN,
                            Res.CAT_LAUNCHER, targetInfo.packageName },
                    new String[] { "App Start takes" });
            if (record) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 外部链接首次启动的性能测试
     */
    private boolean externalLinkFirstLauncher() {
        boolean flag = false;
        for (int i = 0; i < taskInfo.num; i++) {
            javaUtils.runtimeExec(null, utils.cmdClearAppData(adb, targetInfo.packageName), 10);
            boolean record = launchAndRecordLog(report, Res.ACT_VIEW, "", Res.EXTRA_URL, i,
                    6 * 1000, new String[] { "START u0", Res.ACT_VIEW, "", targetInfo.packageName },
                    new String[] { "App Start takes" });
            if (record) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 外部链接非首次冷启动的性能测试
     */
    private boolean externalLinkCommonLauncher() {
        boolean flag = false;
        javaUtils.runtimeExec(null, utils.cmdStartActivity(adb, targetInfo.packageName,
                targetInfo.launchableActivity, "", "", ""), 20);
        javaUtils.sleep(20 * 1000);
        for (int i = 0; i < taskInfo.num; i++) {
            boolean record = launchAndRecordLog(report, Res.ACT_VIEW, "", Res.EXTRA_URL, i,
                    6 * 1000, new String[] { "START u0", Res.ACT_VIEW, "", targetInfo.packageName },
                    new String[] { "App Start takes" });
            if (record) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 网页列表的加载测试
     */
    private void webLoadTest() {
        javaUtils.runtimeExec(null, utils.cmdStartActivity(adb, targetInfo.packageName,
                targetInfo.launchableActivity, "", "", ""), 20);
        javaUtils.sleep(20 * 1000);
        javaUtils.runtimeExec(null, utils.cmdForceStop(adb, targetInfo.packageName), 10);
        // 获取url list
        String content = javaUtils
                .readText(Res.DIR_EXTRA + "resource" + File.separator + "url.txt");
        String[] urls = content.split("\r\n");
        for (String url : urls) {
            PerformanceReport report = new PerformanceReport();
            boolean flag = false;
            for (int i = 0; i < taskInfo.num; i++) {
                boolean record = launchAndRecordLog(report, Res.ACT_VIEW, "", url, i, 2 * 60 * 1000,
                        new String[] { "onPageStarted:" }, new String[] { "onPageFinished url:" });
                if (record) {
                    flag = true;
                } else {
                    System.out.println("第" + i + "轮没有正确获取到值");
                }
            }
            if (flag) {
                report.caseExtraInfo = url;
                recordReport(report);
            }
        }
    }

    /**
     * 计算平均值<br>
     * 计算的是原始数据集整体的平均值
     * 
     * @param datas数据集
     * @return
     */
    private float calculateAverage(ArrayList<Data> datas) {
        float total = 0;
        for (Data data : datas) {
            total += data.originalDatas.get(0)[1];
        }
        return (float) Math.round(total / (datas.size()) * 100) / 100;
    }

    /**
     * 获取中位数
     * 
     * @param datas
     * @return
     */
    private float calculateMedian(ArrayList<Data> datas) {
        ArrayList<Integer> dataArray = new ArrayList<Integer>();
        for (Data data : datas) {
            dataArray.add(data.originalDatas.get(0)[1]);
        }
        Collections.sort(dataArray);
        int lenth = dataArray.size();
        if (lenth == 0) {
            return 0;
        }
        if (lenth % 2 == 0) {
            return (dataArray.get(lenth / 2 - 1) + dataArray.get(lenth / 2)) / 2f;
        } else {
            return dataArray.get(lenth / 2);
        }
    }

    /**
     * 启动目标测试程序并根据启动参数获取相关日志信息<br>
     * 仅适合海豚系列产品
     * 
     * @param act
     *            action参数
     * @param cat
     *            category参数
     * @param dat
     *            扩展数据,如url等
     * @param i
     *            当前循环次数
     * @param timeout
     *            等待开始和结束日志能够获取到需要耗时的时间
     * @param startArray
     *            开始日志的关键字列表
     * @param endArray
     *            结束日志的关键字列表
     */
    private boolean launchAndRecordLog(PerformanceReport report, String act, String cat, String dat,
            int i, long timeout, String[] startArray, String[] endArray) {
        boolean flag = false;
        String launcherActivity = utils.cmdStartActivity(adb, targetInfo.packageName,
                targetInfo.launchableActivity, act, cat, dat);
        javaUtils.runtimeExec(null, utils.cmdForceStop(adb, targetInfo.packageName), 10);
        javaUtils.runtimeExec(null, utils.cmdLogcatClear(adb), 20);
        javaUtils.runtimeExec(null, launcherActivity, 20);
        ArrayList<String> logs = new ArrayList<String>();
        long endTime = System.currentTimeMillis() + timeout;
        while (endTime > System.currentTimeMillis()) {
            javaUtils.sleep(1000);
            javaUtils.runtimeExec(logs, utils.cmdLogcatDump(adb), 20);
            int time = analyzeLog(logs, startArray, endArray);
            if (time != 0) {
                System.out.println("第" + (i + 1) + "轮时间:" + time);
                Data data = new Data();
                data.testNo = i;
                int[] originalData = new int[2];
                originalData[0] = 0;
                originalData[1] = time;
                data.originalDatas.add(originalData);
                report.datas.add(data);
                flag = true;
                break;
            }
        }
        // 重置开始和结束时间
        startLog = 0;
        endLog = 0;
        return flag;
    }

    /**
     * 分析并获取日志对应时间的毫秒数,并进行计算
     * 
     * @param logs
     *            日志列表
     * @param startArray
     *            开始日志的关键字列表
     * @param endArray
     *            结束日志的关键字列表
     * @return
     */
    private int analyzeLog(ArrayList<String> logs, String[] startArray, String[] endArray) {
        for (String log : logs) {
            if (startLog == 0 && isContain(log, startArray)) {
                startLog = getTime(log);
            }
            if (isContain(log, endArray)) {
                endLog = getTime(log);
                System.out.println("开始时间:" + startLog);
                System.out.println("结束时间:" + endLog);
                break;
            }
        }
        if (startLog != 0 && endLog != 0) {
            if (startLog > endLog) {
                endLog += 24 * 3600 * 1000;
            }
            return endLog - startLog;
        } else {
            return 0;
        }
    }

    /**
     * 指定字符串中是否包含数据中的所有内容
     * 
     * @param content
     *            指定的字符串
     * @param keys
     *            关键字组
     * @return {@code true} 表示所有都包含,否则返回{@code false}
     */
    private boolean isContain(String content, String[] keys) {
        for (String key : keys) {
            if (!content.contains(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取单条日志的时间对应的毫秒数
     * 
     * @param log
     * @return
     */
    private int getTime(String log) {
        log = log.substring(6, 18);
        String[] time = log.split(":");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);
        String[] seconds = time[2].split("\\.");
        int second = Integer.valueOf(seconds[0]);
        int millisecond = Integer.valueOf(seconds[1]);
        return hour * 3600 * 1000 + minute * 60 * 1000 + second * 1000 + millisecond;
    }
}
