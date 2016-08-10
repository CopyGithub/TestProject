package com.dolphin.testcase.gesture;

import android.view.View;

import java.util.ArrayList;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-690
 * <p>
 * 脚本描述: Sonar命令识别初始界面:UI
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-690")
@TestClass("验证Sonar命令识别初始界面UI显示正常")
public class CheckSonarInitUITest extends BaseTest {
    public void testCheckSonarInitUI() {
        uiUtil.skipWelcome();
        // 验证：Sonar命令识别界面UI
        checkSonarUI();
        // 验证：进入"GESTURE & SONAR"界面中的SONAR tab
        checkEnterSonarSettings();
        // 验证：返回上级Sonar命令识别界面,Sonar页面显示有效并且可以识别语音
        checkBackToSonar();
        // 验证：切换到DRAW A GESTURE界面
        checkSwitch(true);
        // 验证：切换回Sonar命令识别界面
        checkSwitch(false);
        // 验证：Sonar命令识别界面关闭
        checkSonarClosed();
    }

    private void checkSonarClosed() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        assertTrue("Sonar命令识别界面未关闭",
                !solo.getCurrentActivity().getClass().getSimpleName()
                        .equals("VoiceGestureActivity"));
    }

    private void checkSwitch(boolean isToGesture) {
        solo.clickOnView(solo.getView("vg_switcher"));
        solo.sleep(Res.integer.time_wait);
        if (isToGesture) {
            assertTrue("未切换到DRAW A GESTURE界面", !solo.getView("sonar_logo").isSelected());
        } else {
            assertTrue("未切换回Sonar命令识别界面", solo.getView("sonar_logo").isSelected());
        }
    }

    private void checkBackToSonar() {
        solo.clickOnView(solo.getView("btn_done"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入Sonar命令识别界面",
                solo.waitForActivity("VoiceGestureActivity")
                        && solo.getView("sonar_logo").isSelected());
    }

    private void checkEnterSonarSettings() {
        // 点击设置图标
        solo.clickOnView(solo.getView("vg_btn_settings"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入GESTURE & SONAR界面",
                solo.searchText(caseUtil.getTextByRId("gesture_sonar_settings_title")));
        View sonarTab = caseUtil.getViewByIndex(solo.getView("sub_title_view"),
                new int[] { 1, 0, 0 });
        assertTrue("SONAR未选中，即不是sonar设置界面", sonarTab.isSelected());
    }

    private void checkSonarUI() {
        // 摇一摇手机，无法实现(TODO),进入Sonar
        uiUtil.enterGestureAndSonar(true);
        // 左上角"X"图标,右上角设置图标(TODO)

        // Sonar话筒图标(TODO)

        // 提示语:先显示"Preparing"(TODO),随后显示"HOW CAN I HELP?"
        assertTrue("'HOW CAN I HELP?' is not found.",
                solo.searchText(caseUtil.getTextByRId("voice_title_start_talking")));
        // 提示关键字:"Go to Google","Open Facebook"....(走马灯漂浮显示)(TODO)

        // 从左到右依次为：Gesture图标,手势/声呐切换开关(开关上显示Sonar),Sonar图标(高亮)(TODO)
        ArrayList<View> views = new ArrayList<View>();
        views.add(solo.getView("gesture_logo"));
        views.add(solo.getView("vg_switcher"));
        views.add(solo.getView("sonar_logo"));
        utils.ubietyOfViews(views, 0, false, false, false);
    }
}
