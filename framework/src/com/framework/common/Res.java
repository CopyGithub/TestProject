package com.framework.common;

import java.io.File;

public class Res {
    // 环境变量-PC
    public static final String DIR_ROOT = System.getProperty("user.dir") + File.separator;
    public static final String DIR_EXTRA = DIR_ROOT + "extra" + File.separator;
    private static final String DIR_APK = DIR_EXTRA + "apk" + File.separator;
    public static final String DIR_TARGET_APK = DIR_APK + "target" + File.separator;
    public static final String DIR_CASE_APK = DIR_APK + "case" + File.separator;
    public static final String DIR_OTHER_APK = DIR_APK + "other" + File.separator;
    public static final String DIR_RESULT = DIR_EXTRA + "result" + File.separator;
    public static final String DIR_PC_BACKUP = DIR_EXTRA + "backup" + File.separator;
    public static final String DIR_PC_SCREENSHOT = DIR_EXTRA + "screenshot" + File.separator;
    public static final String DIR_PC_ASSETS = DIR_EXTRA + "assets" + File.separator;
    public static final String DIR_PC_JSON = DIR_EXTRA + "JSON" + File.separator;

    public static final String NAME_APPDATA = "appData";
    public static final String NAME_SDCARDDATA = "sdcardData";
    public static final String NAME_DDATA = "downloadData";
    public static final String NAME_APPDATA_TAR_GZ = NAME_APPDATA + ".tar.gz";
    public static final String NAME_SDCARDDATA_TAR_GZ = NAME_SDCARDDATA + ".tar.gz";
    public static final String NAME_DDATA_TAR_GZ = NAME_DDATA + ".tar.gz";
    public static final String NAME_TOTAL = "Total.xls";
    public static final String NAME_BUSYBOX = "busybox";
    public static final String NAME_SCREENSHOT = "testCaseFailed.jpg";
    // 环境变量-PHONE
    public static final String DIR_PHONE_SDCARD = "/mnt/sdcard/";
    public static final String DIR_PHONE_APPDATA = "/data/data/";
    public static final String DIR_PHONE_TMP = "/data/local/tmp/";
    public static final String DIR_PHONE_SCREENSHOT = "/sdcard/Robotium-Screenshots/";
    public static final String DIR_PHONE_BACKUP = DIR_PHONE_SDCARD + "backup/";
    public static final String DIR_PHONE_TAR = DIR_PHONE_BACKUP + "tar/";
    public static final String DIR_PHONE_DOLPHIN = Res.DIR_PHONE_SDCARD + "TunnyBrowser/";
    public static final String DIR_PHONE_DOWNLOAD = DIR_PHONE_SDCARD + "download/";
    public static final String DIR_PHONE_JSON = DIR_PHONE_BACKUP + "JSON/";
    // 手机相对性能
    public static final String[] HIGH = new String[] { "high device", "0745a20800d43a36", "H" };
    public static final String[] MEDIUM = new String[] { "medium device", "0149BD310A00500E", "M" };
    public static final String[] LOW = new String[] { "low device", "3230F8E5CC8500EC", "L" };
    // 手机系统文件
    public static final String FILE_PHONE_UPTIME = "/proc/uptime";
    // Android命令参数
    public static final String ACT_MAIN = "android.intent.action.MAIN";
    public static final String ACT_VIEW = "android.intent.action.VIEW";
    public static final String CAT_LAUNCHER = "android.intent.category.LAUNCHER";
    // 黑盒测试类型
    public static final String NORMAL = "normal";
    public static final String UPDATE = "update";
    public static final String SMOKE = "smoke";
    // 性能测试点类型
    public static final String[] PERFORMANCE_TIME = new String[] { "time", "1001", };
    public static final String[] PERFORMANCE_CPU = new String[] { "cpu", "1002", };
    public static final String[] PERFORMANCE_MEMORY = new String[] { "memory", "1003", };
    public static final String[] PERFORMANCE_TRAFFIC = new String[] { "traffic", "1004", };
    public static final String[] PERFORMANCE_FPS = new String[] { "fps", "1005", };
    public static final String[] PERFORMANCE_BATTERY = new String[] { "battery", "1006", };
    // 性能测试执行类型
    public static final String[] LAUNCHER_TYPE = new String[] { "first_launcher", "common_launcher",
            "external_link_first_launcher", "external_link_common_launcher", "web_load_test" };
    // CPU测试执行类型
    public static final String[] CPU_TYPE = new String[] { "addressBar" };
    // 性能测试APK说明文档
    public static final String APK_INFO = DIR_TARGET_APK + "apkInfo.xls";
    // 外部链接启动测试url
    public static final String EXTRA_URL = "m.baidu.com";
    // 测试用例变量
    public static final String TEST_ANNOTATION = "Lcom/test/annotation";
    public static final String CASE_REPLACE_DATA = "com.dolphin.updatecase.common.ReplaceDataTest";
    // 数据上传服务器地址
    public static final String PERFORMANCE_UPLOAD_URL = "http://autotest.baina.com:8888/performance/api/uploadData.jspx";
}
