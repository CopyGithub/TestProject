package com.dolphin.testcase.whistle;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;

/**
 * 脚本编号: DOLINT-468 <br>
 * 脚本描述: 消息bar位置:中屏机 <br>
 * 
 * @author sjguo
 * 
 */
// @TestNumber(value = "DOLINT-468")
// @TestClass("验证中屏机上,消息bar显示正常")
// @Reinstall
public class CheckWhistleBarLocationTest extends BaseTest {

    public void testCheckWhistleBarLocation() {
        uiUtil.skipWelcome();
        // 非中屏机直接返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        init();
        assertTrue("2分钟内都没有正确弹出whistle,case执行失败", uiUtil.waitForPromotionPopUp());

        // 验证：竖屏:Menu bar上方显示白色透明消息bar,消息内容显示正常(UX改善计划消息)
        View menuBar = solo.getView("bottom_container");
        int[] menuBarLocation = new int[2];
        menuBar.getLocationOnScreen(menuBarLocation);
        View whistle = solo.getView("promotion_view");
        int[] whistleLocation = new int[2];
        whistle.getLocationOnScreen(whistleLocation);
        assertTrue("消息bar不在Menu bar上方显示", menuBarLocation[1] > whistleLocation[1]);
        String uxPlan = caseUtil.getTextByRId("join_ux_plan_title_text", -1);
        uiUtil.assertWaitForWhistle(uxPlan);

        // 验证：横屏:浏览器底部显示白色透明消息bar,消息内容显示正常(UX改善计划消息)
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
        uiUtil.assertWaitForWhistle(uxPlan);
    }

    private void init() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_change_activity);
    }
}