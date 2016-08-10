package com.dolphin.testcase.download;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.android.common.SqliteUtil;

/**
 * 脚本编号：DOLINT-134
 * <p>
 * 脚本描述：下载等待_已知文件大小
 * 
 * @author jwliu
 * 
 */
// @TestClass("下载等待_已知文件大小")
// TODO 这条用例有问题，暂时不写
public class DownloadWaitKonwnsizeFileTest extends BaseTest {

    public void testDownloadWaitKonwnsizeFileTest() {
        uiUtil.skipWelcome();
        // 同时下载四个准备好的资源，观察下载管理器
        downloadAndEnterDownloadScreen();
    }

    private void downloadAndEnterDownloadScreen() {
        // 删除所有下载数
        new SqliteUtil(instrumentation.getTargetContext())
                .deleteAllRow("downloads.db", "downloads");
        solo.sleep(Res.integer.time_wait);
        javaUtils.deleteFileOrDir(utils.externalStorageDirectory + "/download");// 清除sdcard下的内容
        solo.sleep(Res.integer.time_wait);
        // 准备四个下载资源
        uiUtil.createOneDownloadTask(Res.string.down_apk1);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.createOneDownloadTask(Res.string.down_apk2);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.createOneDownloadTask(Res.string.down_apk3);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.createOneDownloadTask(Res.string.down_apk4);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.clickOnMenuItem("action_menu_item_download");
        uiUtil.waitForInterfaceByTitle("download_tab_title", 10 * 1000);
        solo.sleep(Res.integer.time_wait);
    }
}
