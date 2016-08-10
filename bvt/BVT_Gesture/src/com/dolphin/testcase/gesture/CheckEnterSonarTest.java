package com.dolphin.testcase.gesture;

import android.view.View;

import com.adolphin.common.BaseTest;

import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-681
 * <p>
 * 脚本描述：Sonar入口:Sonar命令识别界面 -> 点击设置图标
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-681")
@TestClass("验证通过声呐命令识别界面正常进入Sonar设置界面")
public class CheckEnterSonarTest extends BaseTest {

    public void testCheckEnterSonar() {
        uiUtil.skipWelcome();
        if (caseUtil.getDisplayRange() == 0) {
            return;
        } else {
            // 验证：进入Sonar命令识别界面
            checkEnterSonar();
        }
        // 验证：进入Sonar设置界面
        checkEnterSonarSettings();
    }

    private void checkEnterSonarSettings() {
        // 点击设置图标
        solo.clickOnView(solo.getView("vg_btn_settings"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入GESTURE & SONAR界面",
                solo.searchText(caseUtil.getTextByRId("gesture_sonar_settings_title", -1)));
        View sonarTab = caseUtil.getViewByIndex(solo.getView("sub_title_view"),
                new int[] { 1, 0, 0 });
        assertTrue("SONAR未选中，即不是sonar设置界面", sonarTab.isSelected());
    }

    private void checkEnterSonar() {
        // 长按海豚键 -> 滑动到Sonar图标后释放
        View dolphinIcon = caseUtil.getViewByIndex(solo.getView("bottom_container"), new int[] { 0,
                3, 0 });
        caseUtil.motion(dolphinIcon, 0);
        solo.sleep(Res.integer.time_wait);
        caseUtil.dragViewToView(dolphinIcon,
                caseUtil.getViewByIndex(solo.getView("arc_menu"), new int[] { 0, 0 }));
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入Sonar命令识别界面",
                solo.waitForActivity("VoiceGestureActivity")
                        && solo.getView("sonar_logo").isSelected());
    }
}