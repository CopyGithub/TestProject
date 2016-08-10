
package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1214
 * <p>
 * 脚本描述：验证使用Dolphin账号登录，忘记密码以及创建新密码界面
 * 
 * @author jwliu
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-1214")
@TestClass("验证使用Dolphin账号登录，忘记密码以及创建新密码界面")
public class EnterDolphinLoginTest extends BaseTest {
    public void testEnterDolphinLoginTest() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        enterDC();
        enterForgetPass();
        // 验证: 进入忘记密码界面
        assertTrue("Failed to enter reset password activity.", solo.getCurrentActivity().toString()
                .contains(Res.string.forget_password_activityname));
        enterBuildAccount();
        // 验证: 进入创建新账户界面
        assertTrue("Failed to enter create an account activity.", solo.getCurrentActivity()
                .toString().contains("DolphinSignUpActivity"));
    }

    private void enterDC() {
        // 1. 进入Dolphin账号登录界面
        uiUtil.enterDCLoginInterface();
        uiUtil.enterDolphinAccountLoginInterface();
        solo.sleep(Res.integer.time_wait);
    }

    private void enterForgetPass() {
        // 2.进入忘记密码界面
        solo.clickOnText(caseUtil.getTextByRId(Res.string.forget_password));
        solo.sleep(Res.integer.time_change_activity);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }

    private void enterBuildAccount() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        // 3.进入创建新账户界面
        solo.clickOnText(caseUtil.getTextByRId("sign_up_text", -1));
        solo.sleep(Res.integer.time_change_activity);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }
}