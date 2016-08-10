package com.dolphin.testcase.cleanstorage;

import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;
import com.test.annotation.UiAutomatorAfter;

/**
 * 
 * 脚本编号：DOLINT-1703
 * <p>
 * 脚本描述：自动清理逻辑-1M<存储<50M
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1703")
@TestClass("自动清理逻辑-1M<存储<50M")
@UiAutomatorAfter("com.dante.CleanCacheAutoTest")
public class CleanCacheAutoTest extends BaseTest {
    private String[] urls = { Res.string.url_one, Res.string.url_two, Res.string.url_three };

    public void testCleanCacheAuto() {
        uiUtil.skipWelcome();
        // 进行内存环境准备，使1M<存储空间<50M
        utils.cramSD(50, utils.externalStorageDirectory + "/newFiles");

        // 验证：访问多个网页,访问成功
        for (int i = 0; i < urls.length; i++) {
            uiUtil.visitUrl(urls[i]);
            solo.sleep(Res.integer.time_wait);
            assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        }

        /**
         * 1.进入File Manager查看cache,成功生成cache <br>
         * 2.将手机系统时间调整为7天后，访问其它网页: 如url_four,url_five (uiautomator)<br>
         * 3.重启Dolphin,再次查看cache,webview内的cache仅保留最近3天内的，其他cache被删除
         * (uiautomator)<br>
         */

        // 验证：进入File Manager查看cache,成功生成cache
        enterFileManagerCache();
    }

    private void enterFileManagerCache() {
        uiUtil.clickOnMenuItem("action_menu_item_download");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("file_manage_tab_title"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText("0");
        solo.sleep(Res.integer.time_wait);
        if (uiUtil.isINTPackage()) {
            solo.clickOnText("TunnyBrowser");
            solo.sleep(Res.integer.time_wait);
        } else {
            solo.clickOnText("com.dolphin.browser.express.web");
            solo.sleep(Res.integer.time_wait);
        }
        solo.clickOnView(caseUtil.getViewByText("cache", -1, true, true, true));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText("webviewCache");
        solo.sleep(Res.integer.time_wait);
        // 验证：成功生成cache
        ViewGroup list = (ViewGroup) caseUtil.getViewByIndex(solo.getView("pager"), new int[] { 1,
                0, 2 });
        assertTrue("There is no webview cache...", list.getChildCount() > 1);
    }
}