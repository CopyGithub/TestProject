
package com.dolphin.testcase.dolphinconnect;

import android.annotation.SuppressLint;
import android.graphics.Point;
import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号:DOLINT-1170
 * <p>
 * 脚本描述:验证初次同步时出现确认弹框
 * 
 * @author chchen
 * 
 */

@SuppressLint("NewApi")
@Reinstall
@TestNumber(value = "DOLINT-1170")
@TestClass("验证初次同步时出现确认弹框")
public class FirstSyncDataPopupDialogTest extends BaseTest {

    public void testFirstSyncDataPopupDialogTest() {
        uiUtil.skipWelcome();
        modifyTheme();// 如果是express版本,需要修改壁纸到default, 然后同步时才会弹出Theme的同步
        uiUtil.enterSetting(false);

        // 登录dolphin账号
        signIn();
        // TODO 这里如果第一次登录后同步失败,也会导致弹窗不出现
        // 验证:是否弹出数据同步对话框,同时验证了标题
        boolean popupDialog = caseUtil.waitForText("synced_tips_dialog_title", 0, true, true,
                30 * 1000, false);
        assertTrue("账号数据同步失败,没有正确弹出同步完成的对话框", popupDialog);
        // 验证:弹出对话框中的内容
        assertTrue("同步项中上部文案没有显示", caseUtil.searchText("sync_apply_message", 0, true, true, true));
        assertTrue("同步项中下部文案没有显示", caseUtil.searchText("sync_apply_notice", 0, false, true, true));
        // 验证:Themes
        //assertTrue("同步项中没有Themes", caseUtil.searchText("synced_theme", 0, true, true, true));
        // 验证:Settings
        assertTrue("同步项中没有Settings", caseUtil.searchText("synced_settings", 0, true, true, true));
        // 验证:按钮
        assertTrue("同步项中没有Settings", caseUtil.searchText("close_sync", 0, true, true, true));
        assertTrue("同步项中没有Settings", caseUtil.searchText("btn_ok", 0, true, true, true));
        // 验证:点击弹框以外的位置,弹框不消失
        assertTrue("点击弹窗意外的位置,同步对话框错误的消失了", isDismissClickBottomRightCorner());
        // 验证:点击手机Back弹框不消失
        assertTrue("点击Back键,同步对话框错误的消失了", isDismissClickBack());
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

    private boolean isDismissClickBottomRightCorner() {
        solo.sleep(Res.integer.time_wait);
        Point point = new Point();
        solo.getCurrentActivity().getWindowManager().getDefaultDisplay().getSize(point);
        solo.clickOnScreen(point.x - 1, point.y - 1);
        solo.sleep(Res.integer.time_wait);
        return caseUtil.searchText("synced_tips_dialog_title", 0, true, true, true);
    }

    private boolean isDismissClickBack() {
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return caseUtil.searchText("synced_tips_dialog_title", 0, true, true, true);
    }

    private void signIn() {
        uiUtil.enterDCLoginInterface();
        uiUtil.enterDolphinAccountLoginInterface();
        uiUtil.signInDolphinAccount("autotest1@gmail.com", "123456");
    }
}