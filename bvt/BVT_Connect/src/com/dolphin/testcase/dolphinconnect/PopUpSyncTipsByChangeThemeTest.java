package com.dolphin.testcase.dolphinconnect;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.adolphin.common.XmlUtil;

/**
 * 脚本编号:DOLINT-1201
 * <p>
 * 脚本描述:验证进入登录界面的入口
 * <p>
 * 更换主题
 * 
 * @author jwliu
 * 
 */

// @Reinstall
// @TestNumber(value = "DOLINT-1201")
// @TestClass("验证进入登录界面的入口_更换主题")
public class PopUpSyncTipsByChangeThemeTest extends BaseTest {

    public void testPopUpSyncTipsByChangeTheme() {
        uiUtil.skipWelcome();

        // 更换主题
        changeTheme();
        // 验证正确弹出了Sync的tips
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有正确弹出同步主题的Tips",
                caseUtil.searchText("sync_promotion_theme", 0, true, true, true));
        // 验证:是否正常登出海豚账号显示正常登录界面
        assertTrue("Failed to show login activity.", uiUtil.isDolphinConnectActivity());
        // 登陆海豚账号
        assertTrue("Failed to login Dolphin", signIn());
        // 验证:是否正常登陆海豚账号显示正常登录界面
        assertTrue("没有正确登录海豚账号", new XmlUtil(solo).getConnectUserName() != "");
        // 从设置登出账号
        logoutDCfromSettings();
        // 验证:是否正常登出海豚账号显示正常登录界面
        assertTrue("Failed to show login activity.",
                caseUtil.searchText("dolphinconnect_signin", 0, true, true, true));
        solo.sleep(4 * 1000);
    }

    private void changeTheme() {
        solo.sleep(Res.integer.time_wait);
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_change_activity);
        if (uiUtil.isINTPackage()) {
            TextView textView = (TextView) solo.getView("skin_title", 4);
            String skinTitle = textView.getText().toString();
            solo.clickOnView(caseUtil.getViewByIndex("theme_grid", 4));
            assertTrue("Maybe network is bad",
                    caseUtil.waitForText(skinTitle, -1, true, true, 45000, false));
            // TODO
            assertTrue("成功修改壁纸", uiUtil.isAppliedTheme(skinTitle));
        } else {
            caseUtil.clickOnText("theme_default_wallpaper", 0, true);
            solo.sleep(Res.integer.time_wait);
            assertTrue("成功修改壁纸",
                    uiUtil.isAppliedTheme(caseUtil.getTextByRId("theme_default_wallpaper")));
        }
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
    }

    private void logoutDCfromSettings() {
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_change_activity);
        caseUtil.clickOnView("account_name");
        solo.sleep(Res.integer.time_change_activity);
        caseUtil.slideDireciton(null, false, 2 / 3f, 1f);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("logout", 0, true);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("button2");
        solo.sleep(Res.integer.time_change_activity);
    }

    private boolean signIn() {
        uiUtil.enterDolphinAccountLoginInterface();
        uiUtil.signInDolphinAccount(Res.string.dolphin_user_name, Res.string.dolphin_user_password);
        uiUtil.waitForSyncCompleteDialog();
        return true;
    }
}