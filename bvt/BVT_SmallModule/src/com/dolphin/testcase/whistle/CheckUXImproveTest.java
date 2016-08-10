package com.dolphin.testcase.whistle;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

/**
 * 脚本编号: DOLINT-447 <br>
 * 脚本描述: UX改善计划:出现时机、样式 <br>
 * 
 * @author sjguo
 * 
 */
// @TestNumber(value = "DOLINT-447")
// @TestClass("验证UX改善计划消息在首次启动浏览器时出现,且样式正常")
// @Reinstall
public class CheckUXImproveTest extends BaseTest {

    public void testCheckUXImprove() {
        uiUtil.skipWelcome();
        assertTrue("2分钟内都没有正确弹出whistle,case执行失败", uiUtil.waitForPromotionPopUp());

        // 验证：显示消息bar
        // 验证：显示文字:"Help me out.Join the UX improvement program!"(若未能一行显示,则显示为跑马灯(TODO))
        String uxPlan = caseUtil.getTextByRId("join_ux_plan_title_text", -1);
        uiUtil.assertWaitForWhistle(uxPlan);
        // 验证：从左到右依次显示文字、图标:×|√
        ArrayList<View> views = new ArrayList<View>();
        views.add(solo.getView("htext"));
        views.add(solo.getView("hbtn_cancel"));
        views.add(solo.getView("himg_divider"));
        views.add(solo.getView("hbtn_accept"));
        utils.ubietyOfViews(views, 0, false, false, false);
        // 验证：不点击该消息，消息一直显示
        solo.sleep(60 * 1000);
        uiUtil.assertWaitForWhistle(uxPlan);

    }

    public void testRestart() {
        uiUtil.skipWelcome();
        // 验证：重启浏览器,消息仍然显示
        String uxPlan = caseUtil.getTextByRId("join_ux_plan_title_text", -1);
        uiUtil.assertWaitForWhistle(uxPlan);
        // 点击文本区域无反应
        int size = solo.getViews().size();
        solo.clickOnText(uxPlan);
        solo.sleep(Res.integer.time_wait);
        assertTrue("点击文本区域有响应", solo.getViews().size() == size);
    }
}