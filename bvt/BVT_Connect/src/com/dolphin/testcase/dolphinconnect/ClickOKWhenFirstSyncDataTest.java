package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1172
 * <p>
 * 脚本描述: 验证弹出的确认框中OK按钮功能
 * <p>
 * 账号默认主题为-2(默认壁纸2),且同步时不能有需要重启的设置
 * 
 * @author chchen
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-1172")
@TestClass("验证弹出的确认框中OK按钮功能")
public class ClickOKWhenFirstSyncDataTest extends BaseTest {

    public void testClickOKWhenFirstSyncData() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 登录dolphin账号
        signIn();
        // 验证:是否弹出数据同步对话框
        boolean popupDialog = caseUtil.waitForText("synced_tips_dialog_title", 0, true, true,
                60 * 1000, true);
        assertTrue("账号数据同步失败,没有正确弹出同步完成的对话框", popupDialog);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("btn_ok", 0, true);
        // 验证:点击OK是否取消主题同步
        // assertTrue("点击OK并没有同步主题", isAppliedTheme());
    }

    private boolean isAppliedTheme() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("btn_ok", 0, true);
        solo.sleep(20 * 1000);
        // 强制等待20s,以便壁纸(名字city)下载成功 TODO 如果壁纸更改或没有,将失败
        return uiUtil.isAppliedTheme(11000998);
    }

    private void signIn() {
        uiUtil.enterDCLoginInterface();
        uiUtil.enterDolphinAccountLoginInterface();
        uiUtil.signInDolphinAccount("autotest1@gmail.com", "123456");
    }
}