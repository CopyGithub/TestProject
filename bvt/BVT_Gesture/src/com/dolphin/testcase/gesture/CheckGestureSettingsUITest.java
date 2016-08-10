package com.dolphin.testcase.gesture;

import android.view.View;
import android.widget.EditText;

import com.adolphin.common.BaseTest;

import com.adolphin.common.Res;

import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-591
 * <p>
 * 脚本描述：画Gesture界面 -> 点击设置图标
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-591")
@TestClass("验证通过DRAW A GESTURE界面正常进入Gesture设置界面")
public class CheckGestureSettingsUITest extends BaseTest {

    public void testCheckGestureSettingsUI() {
        uiUtil.skipWelcome();
        // 验证："GESTURE & SONAR"界面UI是否正常显示
        checkGestureSettingsUI();
        // 验证：返回上级DRAW A GESTURE界面
        checkBackToDraw();

    }

    private void checkBackToDraw() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        assertTrue("未返回上级DRAW A GESTURE界面",
                solo.searchText(caseUtil.getTextByRId("gesture_pad_tips", -1)));

    }

    private void checkGestureSettingsUI() {
        // 验证：进入"GESTURE & SONAR"界面,标题左侧显示返回图标"<"
        uiUtil.enterGestureAndSonarSettings(false);
        assertTrue("返回图标未在标题左侧显示", isBackIconOnTheLeft());
        // 验证："GESTURE"/"SONAR" tab(当前默认为"GESTURE" tab)
        checkCurrentTab();
        // 验证：Gesture list
        assertTrue("Gesture list is not found.",
                solo.searchText(caseUtil.getTextByRId("gesture_list", -1)));
        // 验证：网址输入框(默认值:http://), "Add +"
        checkUrlAndAddButton();
        // 验证：预置手势列表（手势+网页地址/命令）
        checkList();
        // 验证：More actions >
        assertTrue("More actions is not found.",
                solo.searchText(caseUtil.getTextByRId("gesture_more_action", -1)));
    }

    private void checkList() {
        // 验证：手势(TODO)+网页地址/命令
        String[] list = new String[] { "YouTube", "www.facebook.com/", "www.google.com/",
                caseUtil.getTextByRId("gesture_help", -1) };
        for (int i = 0; i < list.length; i++) {
            assertTrue(list[i] + " is not found.", solo.searchText(list[i]));
        }
    }

    private void checkUrlAndAddButton() {
        View input = solo.getView("action_url_input");
        String inputString = ((EditText) input).getText().toString();
        assertTrue("网址输入框的默认值不是http://", inputString.equals(caseUtil.getTextByRId("http", -1)));
        View add = solo.getView("ok");
        assertTrue("add + 未在输入框的右边", utils.ubietyOfView(input, add, true, false, false) != -1);
    }

    private void checkCurrentTab() {
        View view = solo.getView("sub_title_view");
        View gestureTab = caseUtil.getViewByIndex(view, new int[] { 0, 0, 0 });
        View sonarTab = caseUtil.getViewByIndex(view, new int[] { 1, 0, 0 });
        assertTrue("GESTURE tab is not found.",
                solo.searchText(caseUtil.getTextByRId("panel_menu_tab_title_gesture", -1)));
        assertTrue("SONAR tab is not found.",
                solo.searchText(caseUtil.getTextByRId("panel_menu_tab_title_sonar", -1)));
        assertTrue("当前默认不是GESTURE", gestureTab.isSelected() && !sonarTab.isSelected());
    }

    private boolean isBackIconOnTheLeft() {
        solo.sleep(Res.integer.time_wait);
        View back_icon = solo.getView("btn_done");
        View title = caseUtil.getViewByIndex("action_bar_title_container", 1);
        return utils.ubietyOfView(back_icon, title, true, false, false) != -1;
    }
}