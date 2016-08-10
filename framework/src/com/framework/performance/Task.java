package com.framework.performance;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.framework.common.ApkInfo;
import com.framework.common.ExcelUtil;
import com.framework.common.Res;
import com.framework.common.UploadData;
import com.java.common.Utils;

public class Task {

    private static final String ADD_TASK = "继续添加任务";
    private static final String EXECUTE = "执行已添加任务";
    private Utils javaUtils = new Utils();
    // 任务信息集合,存放解析的任务信息
    private ArrayList<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
    // 获取已经连接的所有设备
    private ArrayList<String> devices = javaUtils.getAllDevices();
    // 获取所有需要执行的测试实例
    private ArrayList<TestCase> totalTestCases = new ArrayList<TestCase>();
    // 存放每个设备所需要执行的测试实例, 按高,中,低三个档次存放
    private ArrayList<ArrayList<TestCase>> deviceTestCases = new ArrayList<ArrayList<TestCase>>();
    private ScheduledThreadPoolExecutor[] executors;
    // 存放所有使用到的apk信息, 避免重复解析
    private ArrayList<ApkInfo> apkInfos = new ArrayList<ApkInfo>();

    /**
     * 执行性能测试任务
     * 
     * @param arg
     *            参数格式, 以";"分隔任务,以","分隔任务中的要数,如<br>
     *            {@code h,launch,first_launcher,10;m,launch,first_launcher,10} <br>
     *            第一项表示设备, 参数为{@code h,m,l},大小写不限,参见{@link Res}下的
     *            {@code HIGH,MEDIUM,LOW}<br>
     *            第二项表示任务类型,参见{@link Res}下的性能测试点类型 <br>
     *            第三项表示测试类型,参见{@link Res}下的性能测试执行类型
     * @throws Exception
     *             参数错误时直接抛出异常
     */
    protected void execute(String arg) throws Exception {
        analysisArg(arg);
        readApkExtraInfoFromExcel();
        sortAndVerifyTask();
        sortTestCase();
        executors = new ScheduledThreadPoolExecutor[3];
        for (int i = 0; i < executors.length; i++) {
            executors[i] = new ScheduledThreadPoolExecutor(1);
            executors[i].scheduleWithFixedDelay(new MyRunnable(i), 0, 1, TimeUnit.SECONDS);
        }
        while (true) {
            boolean shutdown = true;
            for (ScheduledThreadPoolExecutor executor : executors) {
                if (!executor.isShutdown()) {
                    shutdown = false;
                }
            }
            if (shutdown) {
                break;
            }
        }
        new UploadData().selectUploadData(Res.PERFORMANCE_UPLOAD_URL, "requestMap.json="
                + new PerformanceReport().tidyJson(Res.DIR_RESULT));
    }

    /**
     * 分析参数,并构造任务列表
     * 
     * @param arg
     *            参数格式, 以";"分隔任务,以","分隔任务中的要数,如<br>
     *            {@code h,launch,first_launcher,10;m,launch,first_launcher,10} <br>
     *            第一项表示设备, 参数为{@code h,m,l},大小写不限,参见{@link Res}下的
     *            {@code HIGH,MEDIUM,LOW}<br>
     *            第二项表示任务类型,参见{@link Res}下的性能测试点类型 <br>
     *            第三项表示测试类型,参见{@link Res}下的性能测试执行类型
     * @throws Exception
     */
    private void analysisArg(String arg) throws Exception {
        if (arg == null || arg.equals("")) {
            inputArgs();
        } else {
            String[] taskInfoStrings = arg.split(";");
            for (String taskInfoString : taskInfoStrings) {
                if (taskInfoString.equals("")) {
                    continue;
                }
                TaskInfo taskInfo = new TaskInfo();
                String[] info = taskInfoString.split(",");
                if (info.length != 4) {
                    throw new Exception("参数需要4位一组,当前参数为:[" + taskInfoString + "]");
                }
                taskInfo.deviceInfo = getDeviceType(info[0]);
                taskInfo.performanceType = getPerformanceType(info[1]);
                taskInfo.testCaseName = info[2];
                taskInfo.num = Integer.valueOf(info[3]);
                taskInfos.add(taskInfo);
            }
        }
    }

    private String inputArg(String arg) throws Exception {
        ArrayList<String> selectList = new ArrayList<String>();
        selectList.add(Res.HIGH[2]);
        selectList.add(Res.MEDIUM[2]);
        selectList.add(Res.LOW[2]);
        System.out.println("请选择测试的设备:\r\n");
        String deviceInfo = javaUtils.selectInput(selectList, false);
        selectList.clear();
        selectList.add(Res.PERFORMANCE_TIME[0]);
        selectList.add(Res.PERFORMANCE_CPU[0]);
        selectList.add(Res.PERFORMANCE_MEMORY[0]);
        selectList.add(Res.PERFORMANCE_TRAFFIC[0]);
        selectList.add(Res.PERFORMANCE_FPS[0]);
        selectList.add(Res.PERFORMANCE_BATTERY[0]);
        System.out.println("请选择性能测试点\r\n");
        String performanceType = javaUtils.selectInput(selectList, false);
        selectList.clear();
        if (Res.PERFORMANCE_TIME[0].equals(performanceType)) {
            for (String type : Res.LAUNCHER_TYPE) {
                selectList.add(type);
            }
        } else if (Res.PERFORMANCE_CPU[0].equals(performanceType)) {
            for (String type : Res.CPU_TYPE) {
                selectList.add(type);
            }
        } else if (Res.PERFORMANCE_MEMORY[0].equals(performanceType)) {
            // TODO
        } else if (Res.PERFORMANCE_TRAFFIC[0].equals(performanceType)) {
            // TODO
        } else if (Res.PERFORMANCE_FPS[0].equals(performanceType)) {
            // TODO
        } else if (Res.PERFORMANCE_BATTERY[0].equals(performanceType)) {
            // TODO
        } else {

        }
        System.out.println("请选择性能测试执行类型\r\n");
        String testCaseName = javaUtils.selectInput(selectList, false);
        System.out.println("请输入执行次数\r\n");
        int num = javaUtils.getIntOfSystemIn();
        arg += String.format("%s,%s,%s,%s;", deviceInfo, performanceType, testCaseName,
                String.valueOf(num));
        return arg;
    }

    private void inputArgs() throws Exception {
        String arg = "";
        ArrayList<String> selectList = new ArrayList<String>();
        selectList.add(ADD_TASK);
        selectList.add(EXECUTE);
        while (true) {
            arg = inputArg(arg) + ";";
            if (EXECUTE.equals(javaUtils.selectInput(selectList, false))) {
                break;
            }
        }
        analysisArg(arg);
    }

    /**
     * 获取设备类型信息
     * 
     * @param deviceType
     * @return 返回设备信息
     * @throws Exception
     */
    private String[] getDeviceType(String deviceType) throws Exception {
        String[] device;
        if (Res.HIGH[2].equalsIgnoreCase(deviceType)) {
            device = Res.HIGH;
        } else if (Res.MEDIUM[2].equalsIgnoreCase(deviceType)) {
            device = Res.MEDIUM;
        } else if (Res.LOW[2].equalsIgnoreCase(deviceType)) {
            device = Res.LOW;
        } else {
            throw new Exception("填入的设备类型\"" + deviceType + "\"不在h,m,l");
        }
        if (!devices.contains(device[1])) {
            throw new Exception("特定设备:" + device[1] + ", 序列号为:" + device[1] + "没有正确连接");
        }
        return device;
    }

    /**
     * 获取性能测试点类型
     * 
     * @param id
     *            测试点编号
     * @return
     * @throws Exception
     */
    private String[] getPerformanceType(String id) throws Exception {
        if (Res.PERFORMANCE_TIME[0].equals(id)) {
            return Res.PERFORMANCE_TIME;
        } else if (Res.PERFORMANCE_CPU[0].equals(id)) {
            return Res.PERFORMANCE_CPU;
        } else if (Res.PERFORMANCE_MEMORY[0].equals(id)) {
            return Res.PERFORMANCE_MEMORY;
        } else if (Res.PERFORMANCE_TRAFFIC[0].equals(id)) {
            return Res.PERFORMANCE_TRAFFIC;
        } else if (Res.PERFORMANCE_FPS[0].equals(id)) {
            return Res.PERFORMANCE_FPS;
        } else if (Res.PERFORMANCE_BATTERY[0].equals(id)) {
            return Res.PERFORMANCE_BATTERY;
        } else {
            throw new Exception("指定的类型\"" + id + "\"不是已知的性能测试类型");
        }
    }

    /**
     * 分拣任务类型, 并验证任务的正确性
     * 
     * @param taskInfos
     *            待分拣的任务
     * @throws Exception
     *             如果任务的正确性验证失败, 则抛出异常
     */
    private void sortAndVerifyTask() throws Exception {
        for (TaskInfo taskInfo : taskInfos) {
            ApkInfo targetInfo = getApkInfo(Res.DIR_TARGET_APK + taskInfo.fileName);
            TestCase testCase = null;
            if (Res.PERFORMANCE_TIME[1].equals(taskInfo.performanceType[1])) {
                testCase = new TimeCostCase(taskInfo, targetInfo, null);// 耗时测试
            } else if (Res.PERFORMANCE_CPU[1].equals(taskInfo.performanceType[1])) {
                testCase = new CPUCase(taskInfo, targetInfo, null);// CPU测试
            } else if (Res.PERFORMANCE_MEMORY[1].equals(taskInfo.performanceType[1])) {
                // TODO
            } else if (Res.PERFORMANCE_TRAFFIC[1].equals(taskInfo.performanceType[1])) {
                // TODO
            } else if (Res.PERFORMANCE_FPS[1].equals(taskInfo.performanceType[1])) {
                // TODO
            } else if (Res.PERFORMANCE_BATTERY[1].equals(taskInfo.performanceType[1])) {
                // TODO
            } else {
                throw new Exception("性能测试点没有正确分拣");
            }
            totalTestCases.add(testCase);
        }
    }

    /**
     * 分拣测试实例的执行设备
     */
    private void sortTestCase() {
        ArrayList<TestCase> hTestCases = new ArrayList<TestCase>();
        ArrayList<TestCase> mTestCases = new ArrayList<TestCase>();
        ArrayList<TestCase> lTestCases = new ArrayList<TestCase>();
        for (TestCase testCase : totalTestCases) {
            if (Res.HIGH[2].equals(testCase.taskInfo.deviceInfo[2])) {
                hTestCases.add(testCase);
            } else if (Res.MEDIUM[2].equals(testCase.taskInfo.deviceInfo[2])) {
                mTestCases.add(testCase);
            } else if (Res.LOW[2].equals(testCase.taskInfo.deviceInfo[2])) {
                lTestCases.add(testCase);
            }
        }
        deviceTestCases.add(hTestCases);
        deviceTestCases.add(mTestCases);
        deviceTestCases.add(lTestCases);
    }

    /**
     * 执行Case并适时关闭多线程
     * 
     * @author chchen
     *
     */
    private class MyRunnable implements Runnable {
        private int index;

        public MyRunnable(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            if (deviceTestCases.get(index).size() == 0) {
                executors[index].shutdown();
                return;
            }
            try {
                deviceTestCases.get(index).get(0).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            deviceTestCases.get(index).remove(0);
        }
    }

    /**
     * 从Excel读取被测apk的额外信息(如: 是否有内核), 并添加到任务中
     * 
     * @param excelPath
     */
    private void readApkExtraInfoFromExcel() {
        ExcelUtil excelUtil = new ExcelUtil();
        ArrayList<HSSFRow> rows = excelUtil.readExcelHssfRows(Res.APK_INFO);
        for (int i = 1; i < rows.size(); i++) {
            HSSFRow row = rows.get(i);
            for (TaskInfo taskInfo : taskInfos) {
                taskInfo.fileName = excelUtil.getCellStringValue(row, 0);
                taskInfo.extraInfo = excelUtil.getCellStringValue(row, 1);
            }
        }
    }

    /**
     * 获取apk基本信息, 如果之前已经解析过, 将直接使用
     * 
     * @param apkPath
     *            apk路径
     * @return 返回apk的基础信息
     * @throws Exception
     */
    synchronized private ApkInfo getApkInfo(String apkPath) throws Exception {
        for (ApkInfo apkInfo : apkInfos) {
            if (apkPath.equals(apkInfo.filePath)) {
                return apkInfo;
            }
        }
        ApkInfo apkInfo = new ApkInfo(apkPath);
        apkInfos.add(apkInfo);
        return apkInfo;
    }
}
