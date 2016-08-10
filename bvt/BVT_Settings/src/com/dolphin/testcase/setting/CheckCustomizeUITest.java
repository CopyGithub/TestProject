package com.dolphin.testcase.setting;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1084
 * <p>
 * 脚本描述: 验证"Customize"界面设置项UI显示正常
 * 
 * @author chchen
 * 
 */
@TestNumber(value = "DOLINT-1084")
@TestClass("验证\"Customize\"界面设置项UI显示正常")
public class CheckCustomizeUITest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testCheckCustomizeUI() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);
        solo.clickOnText(caseUtil.getTextByRId("settings_more_title"), 0, true);
        solo.sleep(Res.integer.time_wait);

        // 验证:Open links in (默认：By default)
        uiUtil.assertSearchText("pref_open_links", "pref_open_links_choices", 0, false, false, 0,
                null);
        // 验证:Keep screen on (默认：开关关闭)
        uiUtil.assertSearchText("pref_keep_screen_on_title", null, 0, true, false, 2, path);
        // 验证:Page preloading (默认：Only on Wi-Fi)
        uiUtil.assertSearchText("pref_preload_strategy", "pref_preload_strategy_choices", 1, false,
                false, 0, null);
        // 验证:Orientation (默认：Auto)
        uiUtil.assertSearchText("pref_orientation_title", "pref_orientation_choices", 0, false,
                false, 0, null);
        // 验证:Volume button action (默认：Default Android action)
        uiUtil.assertSearchText("pref_volume_button_action_title",
                "pref_volume_button_action_choices", 0, false, false, 0, null);
        // 验证:Open in app >
        uiUtil.assertSearchText("open_in_app", null, 0, false, false, 0, null);

        // 验证:点击返回后能够返回上级Settings界面
        assertTrue("没有正确返回到Settings界面", isBackToSettings());
    }

    private boolean isBackToSettings() {
        solo.goBack();
        return uiUtil.waitForInterfaceByTitle("panel_menu_item_title_settings", 10 * 1000);
    }
}