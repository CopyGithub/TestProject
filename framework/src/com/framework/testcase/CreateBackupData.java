package com.framework.testcase;

import java.io.File;
import java.util.ArrayList;

import com.framework.common.Res;
import com.framework.common.Utils;

/**
 * 使用时打成可执行jar包, 传入3个参数, 第一个是备份数据的apk版本,第二个是Case Name,第三个是设备的序列号,
 * 
 * @author chchen
 *
 */
public class CreateBackupData {
    private static final String PACKAGE = "mobi.mgeek.TunnyBrowser";
    private static final String TEMP = Res.DIR_PHONE_BACKUP + PACKAGE;
    private static Utils utils = new Utils();
    private static com.java.common.Utils javaUtils = new com.java.common.Utils();
    private static String adb;
    private static String classSimpleName;
    private static String versionCode;

    public static void main(String[] args) {
        verifyArgs(args);
        String dirData = utils.getBackupDir(versionCode, classSimpleName);
        ArrayList<String> cmds = new ArrayList<String>();
        // 清理和创建使用的目录
        javaUtils.fileCreate(dirData, true, true);
        cmds.add(utils.cmdDelete(adb, Res.DIR_PHONE_BACKUP));
        cmds.add(utils.cmdMkdir(adb, Res.DIR_PHONE_TAR));
        cmds.add(utils.cmdMkdir(adb, Res.DIR_PHONE_DOLPHIN));
        cmds.add(utils.cmdMkdir(adb, Res.DIR_PHONE_DOWNLOAD));
        // 复制和压缩程序数据
        cmds.add(utils.cmdPush(adb, Res.DIR_ROOT + Res.NAME_BUSYBOX, Res.DIR_PHONE_TMP));
        cmds.add(utils.cmdChmod(adb, "777", Res.DIR_PHONE_TMP + Res.NAME_BUSYBOX));
        cmds.add(utils.cmdCpAppData(adb, Res.DIR_PHONE_APPDATA + PACKAGE, TEMP));
        cmds.add(utils.cmdCompress(adb, Res.DIR_PHONE_TAR + Res.NAME_APPDATA_TAR_GZ, TEMP, "."));
        // 压缩sdcard下TunnyBrowser目录数据
        cmds.add(utils.cmdCompress(adb, Res.DIR_PHONE_TAR + Res.NAME_SDCARDDATA_TAR_GZ,
                Res.DIR_PHONE_DOLPHIN, "."));
        // 压缩sdcard下download目录数据
        cmds.add(utils.cmdCompress(adb, Res.DIR_PHONE_TAR + Res.NAME_DDATA_TAR_GZ,
                Res.DIR_PHONE_DOWNLOAD, "."));
        // 搜集所有压缩数据
        cmds.add(utils.cmdPull(adb, Res.DIR_PHONE_TAR, dirData));
        // 清理海豚的数据
        cmds.add(utils.cmdClearAppData(adb, PACKAGE));
        cmds.add(utils.cmdDelete(adb, Res.DIR_PHONE_DOLPHIN));
        cmds.add(utils.cmdDelete(adb, Res.DIR_PHONE_DOWNLOAD));
        // 执行并打印结果
        for (String cmd : cmds) {
            ArrayList<String> result = new ArrayList<String>();
            javaUtils.runtimeExec(result, cmd, 30);
            javaUtils.printArrayString(result);
        }
    }

    private static void verifyArgs(String[] args) {
        if (args.length == 0) {
            System.out.println("请输入备份APK版本号, 如:503");
            versionCode = javaUtils.getStringOfSystemIn() + File.separator;
            System.out.println("请输入备份的case名, 如:AddBookmarkTest");
            classSimpleName = javaUtils.getStringOfSystemIn();
        } else if (args.length == 1) {
            versionCode = args[0];
            System.out.println("请输入备份的case名, 如:AddBookmarkTest");
            classSimpleName = javaUtils.getStringOfSystemIn();
        } else {
            versionCode = args[0];
            classSimpleName = args[1];
        }
        String device = args.length >= 3 ? args[2] : javaUtils.selectInput(
                javaUtils.getAllDevices(), false);
        adb = utils.getAdbs(device)[0];
    }
}
