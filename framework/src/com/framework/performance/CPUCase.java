package com.framework.performance;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.framework.common.ApkInfo;
import com.framework.common.Res;

public class CPUCase extends TestCase {
    private boolean flag = false;
    private long caseStartTime = 0; // 记录case开始执行的时间，这个由case传过来，传输数据的方式为写出的文件 20
    private long caseEndTime = 0; // 记录case运行完成的时间

    private ScheduledThreadPoolExecutor executor;
    private ArrayList<int[]> cpus = new ArrayList<int[]>();

    protected CPUCase(TaskInfo taskInfo, ApkInfo caseInfo, ApkInfo targetInfo) {
        super(taskInfo, caseInfo, targetInfo);
    }

    @Override
    protected void execute() {
        // 安装apk
        utils.installApk(adb, targetInfo, true);
        javaUtils.fileCreate(Res.DIR_RESULT, true, false);
        executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleWithFixedDelay(new MyRunnable(), 0, 1000, TimeUnit.MILLISECONDS);
        System.out.println("开始" + System.currentTimeMillis());
        javaUtils.sleep(5 * 1000);
        System.out.println("结束" + System.currentTimeMillis());
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            if (flag) {
                executor.shutdown();
            }
            saveCpuData();
        }
    }

    synchronized private void saveCpuData() {
        long start = System.currentTimeMillis();
        int currentTimeMillis = getPhoneCurrentTimeMillis();
        long end = System.currentTimeMillis();
        System.out.println("获取时间耗时:" + (end - start));
        start = System.currentTimeMillis();
        int cpuFrequency = getCpuFrequency();
        end = System.currentTimeMillis();
        System.out.println("获取CPU耗时:" + (end - start));
        int[] cpu = new int[] { currentTimeMillis, cpuFrequency };
        cpus.add(cpu);
    }

    /**
     * 获取手机启动时间(毫秒)
     * 
     * @return 返回毫秒数
     */
    private int getPhoneCurrentTimeMillis() {
        ArrayList<String> out = new ArrayList<String>();
        long start = System.currentTimeMillis();
        javaUtils.runtimeExec(out, utils.cmdCatFile(adb, Res.FILE_PHONE_UPTIME), 10);
        long end = System.currentTimeMillis();
        System.out.println("获取时间命令耗时:" + (end - start));
        return (int) (Float.valueOf(out.get(0).trim().split(" ")[0]) * 1000);
    }

    /**
     * 获取CPU消耗百分比
     * 
     * @return 返回CPU消耗百分比值
     */
    private int getCpuFrequency() {
        ArrayList<String> out = new ArrayList<String>();
        long start = System.currentTimeMillis();
        javaUtils.runtimeExec(out, utils.cmdTop(adb), 10);
        long end = System.currentTimeMillis();
        System.out.println("获取CPU命令耗时:" + (end - start));
        for (String line : out) {
            if (line.contains(targetInfo.packageName)) {
                Pattern pattern = Pattern.compile("[0-9]+%");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return Integer.valueOf(matcher.group().replace("%", ""));
                }
            }
        }
        return -1;
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
        getValidData(cpus);
        report.peakVal = calculatePeak(cpus);
        System.out.println("平均时间:" + report.averageValue);
        System.out.println("中位值时间:" + report.median);
        JSONObject json = report.convertJSONArray();
        javaUtils.writeText(json.toString(), Res.DIR_RESULT + System.currentTimeMillis() + ".txt",
                false);
    }

    /**
     * 去除无效数据
     * 
     * @param datas
     */
    private void getValidData(ArrayList<int[]> datas) {
        int i = 0;
        while (i < datas.size()) {
            int[] data = datas.get(i);
            if (data[0] > caseEndTime || data[0] < caseStartTime || -1 == data[1]) {
                datas.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
     * 获取峰值
     * 
     * @param datas
     * @return
     */
    private float calculatePeak(ArrayList<int[]> datas) {
        int maxVal = 0;
        for (int[] data : datas) {
            if (data[1] > maxVal) {
                maxVal = data[1];
            }
        }
        return (float) maxVal;
    }

}
