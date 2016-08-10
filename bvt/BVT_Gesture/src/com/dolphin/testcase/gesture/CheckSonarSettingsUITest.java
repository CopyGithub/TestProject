package com.dolphin.testcase.gesture;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-680
 * <p>
 * 脚本描述：Sonar入口:Settings -> Gesture&Sonar -> Sonar
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-680")
@TestClass("验证通过Settings界面正常进入Sonar设置界面")
public class CheckSonarSettingsUITest extends BaseTest {
    private int[] path = { 1 };

    public void testCheckSonarSettingsUI() {
        uiUtil.skipWelcome();

        // 验证：进入Sonar设置界面，界面UI是否正常显示
        checkSonarSettingsUI();
        // 验证：返回上级SETTINGS界面
        checkBackToSettings();
    }

    private void checkBackToSettings() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        assertTrue("未返回上级SETTINGS界面", uiUtil.waitForInterfaceByTitle("menu_preferences", 0));
    }

    private void checkSonarSettingsUI() {
        // 进入Settings -> Gesutre and sonar,点击"SONAR" tab
        enterSonarSetttings();
        // 验证：界面
        // 1."Shake to activate" (开关,默认开启)
        uiUtil.assertSearchText("shake_to_open_vg", null, -1, true, true, 1, path);
        // 2."Language Settings" >
        uiUtil.assertSearchText("voice_language_settings", null, -1, false, false, 0, null);
        // 3."Help:Dolphin Sonar" >
        uiUtil.assertSearchText("voice_help", null, -1, false, false, 0, null);
        // 4."Create shortcut" >
        uiUtil.assertSearchText("voice_shortcut", null, -1, false, false, 0, null);
        // 5.提示语:"Attention Enabling shake to active,Dolphin Sonar may use more power."
        uiUtil.assertSearchText("attention_title", "attention_content", 1, false, false, 0, null);
    }

    private void enterSonarSetttings() {
        uiUtil.enterSetting(false);
        solo.clickOnText(caseUtil.getTextByRId("pref_category_gesture_voice_settings", -1));
        solo.sleep(Res.integer.time_wait);
        View sonarTab = caseUtil.getViewByIndex(solo.getView("sub_title_view"),
                new int[] { 1, 0, 0 });
        solo.clickOnView(sonarTab);
        solo.sleep(Res.integer.time_wait);
    }
}