package com.dolphin.testcase.setting;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.ImageUtil;
import com.adolphin.common.RDolphin;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1000
 * <p>
 * 脚本描述: 验证"ABOUT DOLPHIN"界面UI显示正确
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-1000")
@TestClass("验证\"ABOUT DOLPHIN\"界面UI显示正确")
public class CheckAboutDolphinUITest extends BaseTest {

    public void testCheckAboutDolphinUITest() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        solo.clickOnText(caseUtil.getTextByRId("pref_category_help"));
        solo.sleep(Res.integer.time_wait);

        // 验证：<在标题栏左边
        assertTrue("the back ic '<' should be on the left of title. ", isBackIconOnTheLeft());
        // 验证: 浏览器的名称(加粗，显示主题色),Dolphin的logo
        // TODO 加粗和主题色未验证
        assertTrue("显示的图像不是海豚图像", isDolphinTextAndLogo());
        // 验证: Check updata （下方显示当前版本）
        uiUtil.assertSearchText("pref_category_check_update", "V" + caseUtil.appvn, -1, false,
                false, 0, null);
        // 验证: UX improvement program >
        uiUtil.assertSearchText("pref_ux_improvement", null, -1, false, false, 0, null);
        // 验证: FAQ >
        uiUtil.assertSearchText("pref_get_help", null, -1, false, false, 0, null);
        // 验证: Subscribe Dolphin newsletter >
        uiUtil.assertSearchText("pref_category_update", null, -1, false, false, 0, null);

        // 验证: Contact us
        uiUtil.assertSearchText("pref_footer_contact_us", null, -1, false, false, 0, null);
        // 验证: Blog | Email | Facebook | Twitter（显示主题色）
        // TODO 主题色未验证
        uiUtil.assertSearchText("pref_footer_blog", null, -1, false, false, 0, null);
        uiUtil.assertSearchText("pref_footer_mail", null, -1, false, false, 0, null);
        uiUtil.assertSearchText("pref_footer_facebook", null, -1, false, false, 0, null);
        uiUtil.assertSearchText("pref_footer_twitter", null, -1, false, false, 0, null);
        // 验证: Copyright © 2009-2015 Dolphin Browser.
        uiUtil.assertSearchText("pref_footer_copyright", null, -1, false, false, 0, null);
        // 验证: All rights reserved.Privacy policy（Privacy policy显示主题色）
        // TODO 主题色未验证
        uiUtil.assertSearchText("pref_footer_desc", null, -1, false, false, 0, null);
        uiUtil.assertSearchText("pref_privacy_policy", null, -1, false, false, 0, null);

        // 验证：点击手机返回键，返回上一级Settings
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private boolean isSettingsInterface() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return uiUtil.waitForInterfaceByTitle("menu_preferences", 0);
    }

    private boolean isBackIconOnTheLeft() {
        solo.sleep(Res.integer.time_wait);
        View back_icon = solo.getView("id/btn_done");
        View title = solo.getView("id/title");
        return utils.ubietyOfView(back_icon, title, true, false, false) != -1;
    }

    private boolean isDolphinTextAndLogo() {
        ImageUtil imageUtil = new ImageUtil(solo);
        TextView dolphin = (TextView) solo.getView("application_name");
        boolean flag = dolphin.getText().equals(caseUtil.getTextByRId("application_name", 0));
        assertTrue("程序名不正确,不是Dolphin", flag);
        return imageUtil.compareTextView(dolphin, 2, RDolphin.drawable.about_icon);
    }
}