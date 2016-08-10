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
 * 脚本编号: DOLINT-1028
 * <p>
 * 脚本描述: 验证Privacy界面设置项显示正确
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-1028")
@TestClass("验证Privacy界面设置项显示正确")
public class CheckPrivacyUITest extends BaseTest {
    private String textColor;
    private static final String GREY = "8a000000";
    private int[] path = { 1, 0 };

    public void testCheckPrivacyUITest() {
        uiUtil.skipWelcome();
        // 验证：<在标题栏左边
        assertTrue("the back ic '<' should be on the left of title. ", isBackIconOnTheLeft());
        // 验证：Remember passwords (开关: 默认开启)
        uiUtil.assertSearchText("pref_security_remember_passwords", null, -1, true, true, 2, path);
        // 验证：Remember form data (开关: 默认开启)
        uiUtil.assertSearchText("pref_security_save_form_data", null, -1, true, true, 2, path);
        // 验证：Enable location (开关: 默认开启)
        uiUtil.assertSearchText("pref_privacy_enable_geolocation", null, -1, true, true, 2, path);
        // 验证：Use master password (开关: 默认关闭)
        uiUtil.assertSearchText("pref_security_use_master_key",
                caseUtil.getTextByRId("pref_security_use_master_key_summary"), -1, true, false, 2,
                path);
        // TODO 验证：Protect saved logins and passwords 灰色 (小字,未实现)
        isGreyColorText();

        // 验证：点击手机返回键，返回上一级Settings
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private void isGreyColorText() {
        View childView = ((ViewGroup) solo.getView("list")).getChildAt(3);
        TextView title = (TextView) caseUtil.getViewByIndex(childView, new int[] { 0, 0, 1 });
        textColor = Integer.toHexString(title.getCurrentTextColor());
        assertTrue("'Protect saved logins and passwords'颜色值应该是" + GREY + ";界面上是" + textColor,
                textColor.equals(GREY));
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
        uiUtil.enterSetting(true);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("pref_privacy_settings", 0, true);
        solo.sleep(Res.integer.time_wait);
        ImageView back_icon = (ImageView) solo.getView("btn_done");
        TextView title = (TextView) solo.getView("title");
        return utils.ubietyOfView(back_icon, title, true, false, false) != -1;

    }
}