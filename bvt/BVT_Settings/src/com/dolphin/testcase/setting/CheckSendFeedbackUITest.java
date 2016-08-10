package com.dolphin.testcase.setting;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1012
 * <p>
 * 脚本描述: 验证Send feedback界面设置项显示正确
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-1012")
@TestClass("验证Send feedback界面设置项显示正确")
public class CheckSendFeedbackUITest extends BaseTest {

    public void testCheckSendFeedbackUITest() {
        uiUtil.skipWelcome();
        // 验证：<在标题栏左边
        assertTrue("the back ic '<' should be on the left of title. ", isBackIconOnTheLeft());
        // 验证：New feature request >
        uiUtil.assertSearchText("send_feedback_some_else_report_title", null, -1, false, false, 0,
                null);
        // 验证： Stopped unexpectedly >
        uiUtil.assertSearchText("send_feedback_crash_report_title", null, -1, false, false, 0, null);
        // 验证：Not responding >
        uiUtil.assertSearchText("send_feedback_anr_report_title", null, -1, false, false, 0, null);
        // 验证：Other critical issues >
        uiUtil.assertSearchText("send_feedback_other_bug_report_title", null, -1, false, false, 0,
                null);
        // 验证：点击手机返回键，返回上一级Settings
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private boolean isSettingsInterface() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        ArrayList<View> views = caseUtil.getViews(null, "action_bar_title_container");
        if (views.size() == 1) {
            String title = ((TextView) ((ViewGroup) views.get(0)).getChildAt(1)).getText()
                    .toString();
            if (title.equalsIgnoreCase(caseUtil.getTextByRId("menu_preferences"))) {
                return true;
            }
        }
        return false;
    }

    private boolean isBackIconOnTheLeft() {
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_send_feedback"));
        solo.sleep(Res.integer.time_wait);
        ImageView back_icon = (ImageView) solo.getView("id/btn_done");
        TextView title = (TextView) solo.getView("id/title");
        return utils.ubietyOfView(back_icon, title, true, false, false) != -1;
    }
}