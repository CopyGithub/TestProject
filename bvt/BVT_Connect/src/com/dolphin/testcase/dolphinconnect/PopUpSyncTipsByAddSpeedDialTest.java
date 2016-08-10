package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

/**
 * 脚本编号:DOLINT-1201
 * <p>
 * 脚本描述:验证进入登录界面的入口
 * <p>
 * 添加1个SpeedDial
 * 
 * @author jwliu
 * 
 */
// @Reinstall
// @TestNumber(value = "DOLINT-1201")
// @TestClass("验证进入登录界面的入口_添加1个SpeedDial")

public class PopUpSyncTipsByAddSpeedDialTest extends BaseTest {

    public void testPopUpSyncTipsByAddSpeedDial() {
        uiUtil.skipWelcome();

        // 加入1个SpeedDial
        addSpeedDial();
        // 验证正确弹出了Sync的tips
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有正确弹出同步SpeedDail的Tips",
                caseUtil.searchText("sync_promotion_speed_dial", 0, true, true, true));
        // 验证:是否正常登出海豚账号显示正常登录界面
        assertTrue("Failed to show login activity.", uiUtil.isDolphinConnectActivity());
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to login Dolphin", signIn());
        solo.sleep(Res.integer.time_wait);
        // 验证是否正常登出海豚账号显示正常登录界面
        assertTrue("Failed to show browser activity.", solo.getView("id/bottom_container") != null);
        // 滑到侧边栏登出dolphin账号
        logoutDCfromControlPanel();
        // 验证:是否正常登出海豚账号显示正常登录界面
        assertTrue("Failed to show login activity.",
                caseUtil.searchText("dolphinconnect_signin", 0, true, true, true));
        solo.sleep(4 * 1000);
    }

    private void addSpeedDial() {
        solo.sleep(Res.integer.time_wait);
        uiUtil.addSpeedDial(Res.string.url_aaa, "SpeedDial");
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
    }

    private void logoutDCfromControlPanel() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_change_activity);
        caseUtil.clickOnView("name_txt");
        solo.sleep(Res.integer.time_change_activity);
        boolean popDialogFlag = caseUtil.waitForText("synced_tips_dialog_title", 0, true, true,
                60 * 1000, true);
        if (popDialogFlag) {
            // 弹出同步成功的对话框则点击OK按钮
            caseUtil.clickOnText("btn_ok", 0, true);
            solo.sleep(Res.integer.time_wait);
        }
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