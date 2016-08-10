package com.framework.performance;

import java.util.Random;

class TaskInfo {
    protected int taskId = new Random().nextInt();// 随机生成,用户唯一标识任务
    protected String[] deviceInfo;
    protected String[] performanceType;
    protected String testCaseName;
    protected int num;
    // APK相关信息
    protected String fileName = "";
    protected String extraInfo = "";
}
