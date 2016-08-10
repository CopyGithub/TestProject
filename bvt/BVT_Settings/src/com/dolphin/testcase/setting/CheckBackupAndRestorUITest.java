package com.dolphin.testcase.setting;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-984
 * <p>
 * 脚本描述: 验证默认备份路径有效时，"BACKUP AND RESTORE"设置界面UI显示正常
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-984")
@TestClass("验证默认备份路径有效时，\"BACKUP AND RESTORE\"设置界面UI显示正常")
public class CheckBackupAndRestorUITest extends BaseTest {

    public void testCheckBackupAndRestorUITest() {
        uiUtil.skipWelcome();
        // 预置条件：使用带有内置SD卡的手机，并且/storage/sdcard0/TunnyBrowser/backup路径是存在的
        assertTrue("backup  file is not existed or successfully built", javaUtils.createFileOrDir(
                utils.externalStorageDirectory + "/TunnyBrowser/backup", true, false));
        enterBackAndRestore();
        // 验证：默认显示Backup tab界面
        assertTrue("Current tab is not in backup tab.", isBackupInterface());
        // 验证提示语：“We will compress your browser settings and all your data, and
        // save to selected location.”
        uiUtil.assertSearchText("backup_description", null, -1, false, false, 0, null);
        // assertTrue("prompt informatino  is not true", isRightPrompt());
        // 验证 Name文本框
        uiUtil.assertSearchText("username", null, -1, false, false, 0, null);
        // 验证 文本框内容：默认为“日期时间+Backup”，如201411032159Backup
        assertTrue("file name  is not right format", isRightNamedBackupFile());
        // 验证Location文本框
        uiUtil.assertSearchText("location_label", null, -1, false, false, 0, null);
        // 验证 文本框内容 默认路径为/storage/sdcard0/TunnyBrowser/backup
        assertTrue("file location  is not right format", isRightLocationPath());
        // 验证 Set password before backup，并且默认未勾选
        uiUtil.assertSearchText("set_backup_password", null, -1, true, false, 0, null);
        // 验证 底部为“Backup”按钮。
        assertTrue("button is not background color.", isButton());
        // TODO 验证 底部按钮的背景为主题色。未实现
        assertTrue("button is not background color.", isBgColorButtom());
        // 验证：点击手机返回键，返回上一级Settings
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private boolean isBackupInterface() {
        solo.sleep(Res.integer.time_wait);
        View backup_view = caseUtil.getViewByIndex("id/pager", 0);
        return caseUtil.isInsideDisplay(backup_view, false);
    }

    private boolean isButton() {
        View buttom = solo.getView("id/setup_button");
        return buttom != null;
    }

    // TODO 未实现
    private boolean isBgColorButtom() {
        View buttom = solo.getView("id/setup_button");
        return buttom != null;
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

    private void enterBackAndRestore() {
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("backup_restore_settings"));
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isRightLocationPath() {
        String line = ((TextView) solo.getView("id/file_location")).getText().toString();
        String path = utils.externalStorageDirectory + "/TunnyBrowser/backup";
        return path.equals(line);
    }

    private boolean isRightNamedBackupFile() {
        String line = ((TextView) solo.getView("id/file_name")).getText().toString();
        // TODO 这里是简单判断12位数加上Backup
        String pattern = "^\\d{12}" + "Backup";
        Pattern p = Pattern.compile(pattern);
        Matcher r = p.matcher(line);
        return r.find();
    }
}