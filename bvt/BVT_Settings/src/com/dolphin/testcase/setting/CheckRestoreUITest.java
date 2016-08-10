package com.dolphin.testcase.setting;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-993
 * <p>
 * 脚本描述: 验证上一次备份文件路径有效时，"Restore"设置界面UI显示正常
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-993")
@TestClass("验证上一次备份文件路径有效时，\"Restore\"设置界面UI显示正常")
public class CheckRestoreUITest extends BaseTest {
    private static final int BACKUP = 0;
    private static final int RESTORE = 1;
    private String backup;

    public void testCheckRestoreUITest() {
        uiUtil.skipWelcome();
        init();
        // 验证：进入BACKUP AND RESTORE界面，默认显示Backup tab界面
        assertTrue("Current tab is not in backup tab.", isCurrentInterface(BACKUP));
        // 验证：界面滑动到restore tab界面
        assertTrue("Current tab is not in restore tab.", isCurrentInterface(RESTORE));
        /*
         * 提示语：“Current data will be overwritten, including settings, bookmarks
         * and speed dials will be added. And Dolphin will restart afterwards.”
         * •From文本框：默认为最后一次备份文件的绝对路径 •提示语：“Are you sure to continue?”
         */

        uiUtil.assertSearchText("restore_settings_summary", null, 1, false, false, 0, null);
        uiUtil.assertSearchText("location_label_from", null, 1, false, false, 0, null);
        assertTrue("the From label  is not right .", isRightFilePaht());
        uiUtil.assertSearchText("restore_confirm", null, 1, false, false, 0, null);
        assertTrue("底部为\"Restore\"验证失败", isButton());
        // TODO 底部为“Restore”按钮：背景为主题色。
        // 验证： 点击手机返回键，返回上一级Settings
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private void init() {
        uiUtil.enterSetting(false);
        enterBackup();
        TextView fileLocation = (TextView) solo.getView("file_location");
        if (caseUtil.getTextByRId("location_path_empty_remind").equals(fileLocation.getText())) {
            caseUtil.clickOnView("file_location");
            solo.sleep(4 * 1000);
            solo.clickOnView(caseUtil.getViewByIndex("list", 0));
            solo.sleep(Res.integer.time_wait);
            caseUtil.clickOnView("btn_confirm");
            solo.sleep(Res.integer.time_wait);
        }
        String backupPath = fileLocation.getText().toString();
        backup = backupPath + "/" + ((TextView) solo.getView("file_name")).getText().toString()
                + ".dbk";
        caseUtil.clickOnView("setup_button");
        boolean flag = caseUtil.waitForText("backup_successfully", 0, true, true, 20 * 1000, false);
        assertTrue("failed to backup", flag);
        enterBackup();
    }

    private void enterBackup() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("backup_restore_settings", 0, true);
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isButton() {
        TextView buttom = (TextView) solo.getView("setup_button", 1);
        return caseUtil.getTextByRId("restore_progress_dialog_title").equalsIgnoreCase(
                buttom.getText().toString());
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

    private boolean isRightFilePaht() {
        String string = ((TextView) solo.getView("file_location", 1)).getText().toString();
        return backup.equals(string);
    }

    private boolean isCurrentInterface(int num) {
        if (num == RESTORE) {
            caseUtil.slideDireciton(null, true, 1f, 1f);
            solo.sleep(Res.integer.time_wait);
        }
        View backup_view = caseUtil.getViewByIndex("pager", num);
        return caseUtil.isInsideDisplay(backup_view, false);
    }
}