package com.dolphin.testcase.download;

import android.widget.EditText;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-43
 * <p>
 * 脚本描述：点击下载链接
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-43")
@TestClass("点击下载链接")
public class ClickDownlLinkTest extends BaseTest {
    private String fileName;

    public void testClickDownlLinkTest() {
        uiUtil.skipWelcome();
        // 启动Dolphin,寻找一个APK或者ZIP包的下载链接
        openDownloadWebsite();
        // 点击这个链接
        solo.clickOnWebElement(By.textContent("apk"));
        /**
         * 验证:点击这个链接,弹出下载对话框
         */
        assertTrue("下载对话框没有正确弹出", solo.waitForDialogToOpen());
        // 在下载对话框中选择Download
        clickOnDownload();
        /**
         * 验证:资源文件成功被下载
         */
        assertTrue("下载超时,没有成功下载", uiUtil.waitForDownloadTaskComplete(fileName, 60 * 1000, 1000));
    }

    private void clickOnDownload() {
        solo.sleep(Res.integer.time_wait);
        fileName = ((EditText) solo.getView("input")).getText().toString();
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_wait);
    }

    private void openDownloadWebsite() {
        javaUtils.deleteFileOrDir(utils.externalStorageDirectory + "/download");// 清除sdcard下的内容
        javaUtils.deleteFileOrDir(utils.externalStorageDirectory + "/Robotium-Screenshots");// 初次运行,清除截图文件
        uiUtil.visitUrl(Res.string.url_downloadtest);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is bad .", uiUtil.waitForWebPageFinished());
    }
}