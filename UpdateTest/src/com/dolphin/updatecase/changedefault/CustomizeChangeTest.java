package com.dolphin.updatecase.changedefault;

import android.view.View;
import android.widget.CheckBox;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_062")
@TestClass("设置（改变默认）_自定义")
public class CustomizeChangeTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testCustomizeChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);

        // 验证： User agent 选项为Desktop
        uiUtil.assertSearchText("feedback_user_agent", "pref_development_ua_choices", 1, false,
                false, 0, null);
        // 验证： Customize 为Use the new tab page:http://www.dolphin.com/features
        uiUtil.assertSearchText("pref_content_homepage", "http://www.dolphin.com/features", -1,
                false, false, 0, null);
        // 验证：Enable Swipe for sidebars 开启
        uiUtil.assertSearchText("pref_enable_swipe_action", null, -1, true, true, 1, path);
        // 验证：Enable Dolphin notification 关闭
        uiUtil.assertSearchText("pref_push_notification_enabled", null, -1, true, false, 2, path);
        // 验证：Open previous tab on startup 开启
        uiUtil.assertSearchText("pref_on_startup", null, -1, true, true, 2, path);

        // 验证： Smart cache > 关闭
        uiUtil.assertSearchText("pref_smart_cache", null, -1, false, false, 0, null);
        solo.clickOnText(caseUtil.getTextByRId("pref_smart_cache"), 0, true);
        solo.sleep(Res.integer.time_wait);
        View checkbox = solo.getView("checkbox");
        assertTrue("setting中Smart cache 未关闭", !((CheckBox) checkbox).isChecked());

        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("settings_more_title"), 0, true);
        solo.sleep(Res.integer.time_wait);

        // 验证：cutomize more部分
        // 验证:Open links in 为 Open in background tab
        uiUtil.assertSearchText("pref_open_links", "pref_open_links_choices", 1, false, false, 0,
                null);
        // 验证:Keep screen on 开启
        uiUtil.assertSearchText("pref_keep_screen_on_title", null, 0, true, true, 2, path);
        // 验证:Page preloading 为 Disabled
        uiUtil.assertSearchText("pref_preload_strategy", "pref_preload_strategy_choices", 0, false,
                false, 0, null);
        // 验证:Orientation 为 Landscape
        uiUtil.assertSearchText("pref_orientation_title", "pref_orientation_choices", 1, false,
                false, 0, null);
        // 验证:Volume button action 为 Scroll up/down page
        uiUtil.assertSearchText("pref_volume_button_action_title",
                "pref_volume_button_action_choices", 1, false, false, 0, null);
    }
}
