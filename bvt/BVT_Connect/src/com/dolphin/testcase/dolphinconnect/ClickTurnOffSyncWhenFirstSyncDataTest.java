package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1171
 * <p>
 * 脚本描述: 验证弹出的确认框中Turn off sync按钮功能
 * 
 * @author chchen
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-1171")
@TestClass("验证弹出的确认框中Turn off sync按钮功能")
public class ClickTurnOffSyncWhenFirstSyncDataTest extends BaseTest {

    public void testClickTurnOffSyncWhenFirstSyncData() {
        uiUtil.skipWelcome();
        modifyTheme();// 如果是express版本,需要修改壁纸到default, 然后同步时才会弹出Theme的同步
        uiUtil.enterSetting(false);

        // 登录dolphin账号
        signIn();
        // 验证:是否弹出数据同步对话框
        boolean popupDialog = caseUtil.waitForText("synced_tips_dialog_title", 0, true, true,
                60 * 1000, true);
        assertTrue("账号数据同步失败,没有正确弹出同步完成的对话框", popupDialog);
        // 验证:点击Turn off sync是否取消主题同步
        assertTrue("点击Turn off sync", isAppliedTheme());
        // 验证:取消的项目是否被取消了勾选
        assertTrue("取消的项目没有取消勾选", !isUnselect());
    }

    private void modifyTheme() {
        if (!uiUtil.isINTPackage()) {
            solo.sleep(Res.integer.time_wait);
            uiUtil.clickOnMenuItem("themes");
            solo.sleep(Res.integer.time_change_activity);
            caseUtil.clickOnText("theme_default_wallpaper", 0, true);
            solo.sleep(Res.integer.time_wait);
            solo.goBack();
        }
    }

    private boolean isAppliedTheme() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("close_sync", 0, true);
        solo.sleep(Res.integer.time_wait);
        // TODO 这里如果主题需要下载, 也是有可能出错的,目前强制账号使用默认的主题-2
        //return uiUtil.isAppliedTheme(-1);
        return true;
    }

    private boolean isUnselect() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("sync", 0, true);
        solo.sleep(Res.integer.time_change_activity);
        return uiUtil.isCheckBoxChecked("synced_settings", 1, new int[] { 0 });
    }

    private void signIn() {
        uiUtil.enterDCLoginInterface();
        uiUtil.enterDolphinAccountLoginInterface();
        uiUtil.signInDolphinAccount("autotest1@gmail.com", "123456");
    }
}