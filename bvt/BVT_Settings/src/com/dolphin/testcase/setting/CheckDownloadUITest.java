package com.dolphin.testcase.setting;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-976
 * <p>
 * 脚本描述: 验证Downlaods界面设置项显示正确
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-976")
@TestClass("验证Downlaods界面设置项显示正确")
public class CheckDownloadUITest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testCheckDownloadUITest() {
        final String defaultDownloadPath = utils.externalStorageDirectory + "/download";

        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证：<在标题栏左边
        uiUtil.isBackIconOnTheLeft();

        solo.clickOnText(caseUtil.getTextByRId("download_setting", -1));
        solo.sleep(Res.integer.time_wait);

        // 验证: Default location (默认值: /storage/emulated /0/Download )
        uiUtil.assertSearchText("download_default_path", defaultDownloadPath, -1, false, false, 0,
                null);
        // TODO 路径过长时，中间部分省略号显示
        // 验证: Always ask before downloading files （开关默认开启）
        uiUtil.assertSearchText("download_save_path_prompt", null, -1, true, true, 2, path);
        // 验证: Download files on wifi only （开关默认关闭）
        uiUtil.assertSearchText("download_wifi_only", null, -1, true, false, 2, path);
        // 验证: Multi-threads download limit (默认值: "3")
        uiUtil.assertSearchText("download_threads_limit", "download_threads_limit_values", 2,
                false, false, 0, null);
        // 验证: Simulaneous downloads limit (默认值: "4")
        uiUtil.assertSearchText("download_tasks_limit", "download_tasks_limit_values", 3, false,
                false, 0, null);
        // 验证: Default action for media files (默认值: "Always ask")
        uiUtil.assertSearchText("download_media_prompt", "download_media_action", 0, false, false,
                0, null);

        // 点击手机返回键，返回上一级Settings
        assertTrue("Failed back to settings acitvity", isSettingsInterface());
    }

    private boolean isSettingsInterface() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return uiUtil.waitForInterfaceByTitle("menu_preferences", 4 * 1000);
    }
}