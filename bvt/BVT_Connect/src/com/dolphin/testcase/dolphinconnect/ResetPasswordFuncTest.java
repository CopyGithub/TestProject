package com.dolphin.testcase.dolphinconnect;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 版本编号：DOLINT-1929
 * <p>
 * 版本描述：修改已保存密码的账号的密码
 * 
 * @author xweng
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-1929")
@TestClass("修改已保存密码的账号的密码")
public class ResetPasswordFuncTest extends BaseTest {

    public void testResetPasswordFuncTest() {
        uiUtil.skipWelcome();

        // 登录dolphin账号
        signIn();
        // 验证：进入进入PASSWORD界面
        assertTrue("Failed to enter PASSWORD interface.", isEnterPassword());
        // 验证：修改密码
        assertTrue("Failed to change the password",
                resetPassword(Res.string.dolphin_user_password, Res.string.dolphin_user_password));
        // TODO 显示Submiting的动画,无法验证
        // 验证：自动返回Account info界面
        assertTrue("Failed to go back to ACCOUNT interface", isReurnToAccountInfo());
        // 验证：进入Dolphin账号登录界面,自动填充账号A的邮箱和密码
        assertTrue("Failed to fill in the psw and usrname automatically", isAutoFilling());
        // 验证：账号A直接登录成功
        assertTrue("Dolphin account failed to login", isSignInSuccess());
    }

    private Boolean isReurnToAccountInfo() {
        solo.sleep(Res.integer.time_wait);
        String titleString = ((TextView) solo.getView("title")).getText().toString();
        return titleString.equals(caseUtil.getTextByRId("account"));
    }

    private boolean isSignInSuccess() {
        // TODO 这里考虑了尝试三次login的情况, 失败需要等超时, 后续可以优化这里
        for (int i = 0; i < 3; i++) {
            solo.clickOnView(caseUtil.getView(null, "ds_dolphin_login"));
            solo.sleep(3 * 1000);
            // 通过搜索登录完成后界面上是否有Sign in字符来判断是否登录成功
            boolean loginFlag = caseUtil.waitForTextDismiss("sign_in", 0, true, true, 25 * 1000);
            if (loginFlag) {
                return true;
            }
        }
        return false;
    }

    private boolean isAutoFilling() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        // Log out 账户A
        solo.clickOnView(solo.getView("btn_confirm"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("ds_logout_confirm"));
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterDolphinAccountLoginInterface();
        String email = ((TextView) solo.getView("ds_email")).getText().toString();
        String password = ((TextView) solo.getView("ds_password")).getText().toString();
        solo.sleep(Res.integer.time_wait);
        boolean flag = ((email.equals(Res.string.dolphin_user_name)) && (password != null));
        return flag;
    }

    /**
     * 从PASSWORD界面重置Dolphin账号密码
     * 
     * @param userName
     * @param password
     * @return
     */
    private boolean resetPassword(String currentPassword, String newPassword) {
        // 输入用户名和密码
        caseUtil.setText((TextView) solo.getView("ds_password"), currentPassword);
        caseUtil.setText((TextView) solo.getView("ds_new_password"), newPassword);
        caseUtil.setText((TextView) solo.getView("ds_new_password2"), newPassword);
        solo.sleep(Res.integer.time_wait);
        // 点击Submit按钮
        // TODO 这里考虑了尝试三次submit的情况, 失败需要等超时, 后续可以优化这里
        for (int i = 0; i < 3; i++) {
            solo.clickOnView(caseUtil.getView(null, "btn_submit"));
            solo.sleep(3 * 1000);
            // 通过搜索登录完成后界面上是否有Sumit字符来判断是否登录成功
            boolean submitFlag = caseUtil.waitForText("account", 0, true, true, 25 * 1000, true);
            if (submitFlag) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnterPassword() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("account_info"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("dolphin_password"));
        solo.sleep(Res.integer.time_wait);
        String currentActivity = solo.getCurrentActivity().getClass().getSimpleName();
        boolean flag = currentActivity.contains("Password");
        return flag;
    }

    private void signIn() {
        uiUtil.enterSetting(false);
        uiUtil.enterDCLoginInterface();
        uiUtil.enterDolphinAccountLoginInterface();
        uiUtil.signInDolphinAccount(Res.string.dolphin_user_name, Res.string.dolphin_user_password);
        uiUtil.waitForSyncCompleteDialog();
    }
}
