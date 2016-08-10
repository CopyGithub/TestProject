package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;
import com.robotium.solo.WebElement;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1211
 * <p>
 * 脚本描述：验证使用Facebook网页版登录
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1211")
@TestClass("验证使用Facebook网页版登录")
public class EnterFacebookLoginTest extends BaseTest {
    private WebElement email;
    private WebElement password;
    private WebElement login;

    public void testEnterFacebookLoginTest() {
        uiUtil.skipWelcome();
        enterFacebookWeb();
        // 验证网页是否有账户输入框
        assertTrue("Failed to show email box.", email != null);
        // 验证网页是否密码户输入框
        assertTrue("Failed to show password box.", password != null);
        // 验证网页是否有登录按钮
        assertTrue("Failed to show login button.", login != null);
        // 验证是否返回显示正常登录界面
        assertTrue("Failed to show login activity.", idGobackLogin());
    }

    private void enterFacebookWeb() {
        uiUtil.enterSideBar(true);
        caseUtil.clickOnView(Res.string.sign_in_id);
        solo.sleep(Res.integer.time_change_activity);
        caseUtil.clickOnView(Res.string.login_facebook_id);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to access facebook web ,maybe network is bad",
                solo.waitForWebElement(By.name("email"), 2 * 60 * 1000, true));
        // solo.sleep(Res.integer.network_timeout);
        email = solo.getWebElement(By.name("email"), 0);
        password = solo.getWebElement(By.name("pass"), 0);
        login = solo.getWebElement(By.name("login"), 0);
    }

    private boolean idGobackLogin() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return caseUtil.waitForText(Res.string.dolphinconnect_signin, 0, true, true, 60 * 1000,
                true);
    }
}