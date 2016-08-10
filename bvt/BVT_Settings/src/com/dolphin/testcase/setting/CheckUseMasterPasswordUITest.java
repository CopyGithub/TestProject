package com.dolphin.testcase.setting;

import android.widget.EditText;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1043
 * <p>
 * 脚本描述: 验证"Use master password"设置项UI显示正常
 * <p>
 * 由于有修改设置项的功能, 添加Reinstall保证重复跑出现问题时也能正确执行
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1043")
@TestClass("验证\"Use master password\"设置项UI显示正常")
public class CheckUseMasterPasswordUITest extends BaseTest {
    private String masterPassword = "123456";
    int[] path = new int[] { 1, 0 };

    public void testCheckUseMasterPasswordUITest() {
        uiUtil.skipWelcome();
        enterPrivacy();
        // 验证：设置项名称为"Use master password",右侧显示开关
        uiUtil.assertSearchText("pref_security_use_master_key", null, -1, true, false, 2, path);
        openCheckBox();
        // 验证use master password开关开启了
        assertTrue("Failed to open 'Create master password' option.", isChecked());
        closeCheckBox();
        // 验证use master password开关关闭了
        assertFalse("Failed to close 'Create master password' option.", isChecked());
    }

    private void enterPrivacy() {
        uiUtil.enterSetting(true);
        // 点击Privacy
        caseUtil.clickOnText("pref_privacy_settings", 0, true);
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isChecked() {
        return uiUtil.isCheckBoxChecked("pref_security_use_master_key", 2, path);
    }

    private void closeCheckBox() {
        // 点击关闭之，验证弹出“REMOVE MASTER PASSWORD”对话框
        uiUtil.setCheckBoxByTitle("pref_security_use_master_key", 2, path, false);
        assertTrue("Failed to popup 'Create master password' dialog.",
                solo.searchText(caseUtil.getTextByRId("master_password_remove")));
        // 输入password后验证use master password开关将关闭
        solo.enterText((EditText) solo.getView("id/input_key"), masterPassword);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnButton(caseUtil.getTextByRId("ok"));
        solo.sleep(Res.integer.time_wait);
    }

    private void openCheckBox() {
        uiUtil.setCheckBoxByTitle("pref_security_use_master_key", 2, path, true);
        solo.sleep(Res.integer.time_wait);
        // 验证弹出Create master password对话框
        assertTrue("Failed to popup 'Create master password' dialog.",
                solo.searchText(caseUtil.getTextByRId("master_password_create")));
        EditText input = (EditText) solo.getView("id/input_key");
        EditText inputConfirm = (EditText) solo.getView("id/input_confirm_key");
        solo.enterText(input, masterPassword);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(inputConfirm, masterPassword);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnButton(caseUtil.getTextByRId("ok"));
        solo.sleep(Res.integer.time_wait);
    }
}