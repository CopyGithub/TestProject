package com.dolphin.updatecase.common;

import java.io.File;

import junit.framework.Assert;

import com.adolphin.common.BaseTest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;

public class ReplaceDataTest extends BaseTest {
    private Context context;
    private String dirBackup;
    private String dirTar;
    private String dirSdcard;
    private String dirApp;
    private String dirDownload;
    private static final String NAME_APPDATA = "appData.tar.gz";
    private static final String NAME_SDCARDDATA = "sdcardData.tar.gz";
    private static final String NAME_DOWNLOADDATA = "downloadData.tar.gz";

    public void testReplaceData() {
        uiUtil.skipWelcome();
        solo.sleep(10 * 1000);// 尽可能保证数据不在有变化
        initValues();
        // 删除应用数据,并应用数据
        assertTrue("数据文件不存在", new File(dirTar + NAME_APPDATA).exists());
        deleteData();
        javaUtils.extractTarGz(dirTar + NAME_APPDATA, dirTar + "appData");
        copyData(dirTar + "appData");
        // 删除并当存在时恢复sdcard数据
        if (new File(dirTar + NAME_SDCARDDATA).exists()) {
            javaUtils.extractTarGz(dirTar + NAME_SDCARDDATA, dirSdcard);
        } else {
            javaUtils.deleteFileOrDir(dirTar + NAME_SDCARDDATA);
        }
        // 删除并当存在时恢复download数据
        if (new File(dirTar + NAME_SDCARDDATA).exists()) {
            javaUtils.extractTarGz(dirTar + NAME_DOWNLOADDATA, dirDownload);
        } else {
            javaUtils.deleteFileOrDir(dirTar + NAME_DOWNLOADDATA);
        }
    }

    private void initValues() {
        dirBackup = utils.externalStorageDirectory + "/backup/";
        dirTar = dirBackup + "tar/";
        context = solo.getInstrumentation().getTargetContext();
        dirApp = context.getFilesDir().getParent() + File.separator;
        dirSdcard = utils.externalStorageDirectory + "/TunnyBrowser";
        dirDownload = utils.externalStorageDirectory + "/download";
    }

    /**
     * 删除应用程序数据
     */
    private void deleteData() {
        Context context = solo.getInstrumentation().getTargetContext();
        String dataPath = context.getFilesDir().getParent();
        File file = new File(dataPath);
        for (String path : file.list()) {
            if (!path.equals("lib")) {
                javaUtils.deleteFileOrDir(dataPath + File.separator + path);
            }
        }
    }

    /**
     * 恢复应用数据
     */
    private void copyData(String fromPath) {
        File fileDir = new File(fromPath);
        for (File file : fileDir.listFiles()) {
            String fileName = file.getName();
            if (fileName.contains("app_")) {
                createDataFile(0, true, fileName.substring(4));
            } else if (fileName.equals("files")) {
                createDataFile(0, false, null);
            } else if (fileName.equals("databases")) {
                createDataFile(1, false, null);
            } else if (fileName.equals("cache")) {
                createDataFile(2, false, null);
            } else if (fileName.equals("shared_prefs")) {
                createDataFile(3, false, null);
            }
            javaUtils.copyFileOrDir(file.getAbsolutePath(), dirApp + fileName, false);
        }
    }

    private void createDataFile(int type, boolean custom, String fileName) {
        String temp = "testUpdate";
        switch (type) {
        case 0:
            if (custom) {
                context.getDir(fileName, Context.MODE_PRIVATE);
            } else {
                context.getFilesDir();
            }
            break;
        case 1:
            context.openOrCreateDatabase(temp, Context.MODE_APPEND, null);
            SQLiteDatabase.deleteDatabase(context.getDatabasePath(temp));
            break;
        case 2:
            context.getCacheDir();
            break;
        case 3:
            SharedPreferences sharedPreferences = context.getSharedPreferences(temp,
                    Context.MODE_APPEND);
            Editor editor = sharedPreferences.edit();
            editor.putBoolean(temp, false);
            editor.commit();
            break;
        default:
            Assert.assertTrue("输入的数值不在范围内", false);
            break;
        }
    }

    @Override
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}