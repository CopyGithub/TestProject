package com.dolphin.updatecase.settings;

import android.view.View;
import android.widget.CheckBox;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号:
 * <p>
 * 脚本描述:
 * 
 * @author yytang
 *
 */
@TestNumber("test_033")
@TestClass("设置（默认）_自定义")
public class CheckCustomizeTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testCheckCustomize() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);
        // 验证： User agent 选项为Android
        uiUtil.assertSearchText("feedback_user_agent", "pref_development_ua_choices", 0, false,
                false, 0, null);
        // 验证： Set my homepage 为Use the new tab page
        uiUtil.assertSearchText("pref_content_homepage", "option_speed_dial_homepage_tilte", 0,
                false, false, 0, null);
        // 验证：Enable Swipe for sidebars 关闭
        uiUtil.assertSearchText("pref_enable_swipe_action", null, -1, true, false, 1, path);
        // 验证：Enable Dolphin notification 开启
        uiUtil.assertSearchText("pref_push_notification_enabled", null, -1, true, true, 2, path);
        // 验证：Open previous tab on startup 关闭
        uiUtil.assertSearchText("pref_on_startup", null, -1, true, false, 2, path);

        // 验证： Smart cache > 开启
        uiUtil.assertSearchText("pref_smart_cache", null, -1, false, false, 0, null);
        solo.clickOnText(caseUtil.getTextByRId("pref_smart_cache"), 0, true);
        solo.sleep(Res.integer.time_wait);
        View checkbox = solo.getView("checkbox");
        assertTrue("setting中Smart cache 未开启", ((CheckBox) checkbox).isChecked());

        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("settings_more_title"), 0, true);
        solo.sleep(Res.integer.time_wait);

        // 验证：cutomize more部分
        // 验证:Open links in 为 By default
        uiUtil.assertSearchText("pref_open_links", "pref_open_links_choices", 0, false, false, 0,
                null);
        // 验证:Keep screen on 开关关闭
        uiUtil.assertSearchText("pref_keep_screen_on_title", null, 0, true, false, 2, path);
        // 验证:Page preloading 为 Only on Wi-Fi
        uiUtil.assertSearchText("pref_preload_strategy", "pref_preload_strategy_choices", 1, false,
                false, 0, null);
        // 验证:Orientation (默认：Auto)
        uiUtil.assertSearchText("pref_orientation_title", "pref_orientation_choices", 0, false,
                false, 0, null);
        // 验证:Volume button action (默认：Default Android action)
        uiUtil.assertSearchText("pref_volume_button_action_title",
                "pref_volume_button_action_choices", 0, false, false, 0, null);
    }
}
