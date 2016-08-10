package com.dolphin.testcase.history;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1407
 * <p>
 * 脚本描述：验证无记录列表显示
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1407")
@TestClass("验证无记录列表显示")
public class CheckNoHistoryUITest extends BaseTest {
    private View listView;

    public void testCheckNoHistoryUI() {
        uiUtil.skipWelcome();
        // 预置条件：初次安装Dolphin并启动
        /**
         * 验证：进入左侧边栏→点击History文件夹，列表为空，界面下方不显示Clear history 按钮
         */
        checkHistoryListAndUI(true);
        /**
         * 验证：回到bookmark列表，点击一个bookmark A，A可以正常访问
         */
        checkNormalVisit();
        /**
         * 验证：呼出左侧边栏→点击history文件夹，产生了A的历史记录，且界面下方显示有Clear history的按钮
         */
        checkHistoryListAndUI(false);
    }

    private void checkNormalVisit() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/back"));
        solo.sleep(Res.integer.time_wait);
        listView = solo.getView("id/list");
        solo.clickOnView(((ViewGroup) listView).getChildAt(4));
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);
    }

    private void checkHistoryListAndUI(boolean isEmpty) {
        enterHistoryFolder();
        listView = solo.getView("id/list");
        TextView button = (TextView) solo.getView("id/add_bookmark");
        int count = ((ViewGroup) listView).getChildCount();
        if (isEmpty) {
            assertTrue("列表不是空的", count == 0);
            assertTrue("界面下方不应该显示Clear history 按钮", !button.isShown());
        } else {
            assertTrue("列表没有产生新的历史记录", count > 0);
            assertTrue("界面下方不显示Clear history 按钮", button.isShown());
            assertTrue("按钮上显示Clear history ",
                    button.getText().equals(caseUtil.getTextByRId("clear_history", -1)));
        }
    }

    private void enterHistoryFolder() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("history"));
        solo.sleep(Res.integer.time_wait);
    }
}