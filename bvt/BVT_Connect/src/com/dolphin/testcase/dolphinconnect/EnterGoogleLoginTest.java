package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;
import com.robotium.solo.WebElement;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1210
 * <p>
 * 脚本描述：验证使用Google网页版登录
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1210")
@TestClass("验证使用Google网页版登录")
public class EnterGoogleLoginTest extends BaseTest {
    public static String goole_login_id = "btn_login_google";

    public void testEnterGoogleLoginTest() {
        uiUtil.skipWelcome();
        enterGoogleLoginWeb();
        // 验证登录中途返回，是否显示正常登录界面
        assertTrue("Failed to show login activity.", idGobackLogin());
    }

    private void enterGoogleLoginWeb() {
        uiUtil.enterSideBar(true);
        caseUtil.clickOnView(Res.string.sign_in_id);
        solo.sleep(Res.integer.time_change_activity);
        caseUtil.clickOnView(goole_login_id);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to access google web ,maybe network is bad",
                solo.waitForWebElement(By.name("Email"), 2 * 60 * 1000, true));
        // 验证，邮箱密码和登录的对话框是否正常显示
        WebElement email = solo.getWebElement(By.name("Email"), 0);
        WebElement password = solo.getWebElement(By.name("Passwd"), 0);
        WebElement login = solo.getWebElement(By.name("signIn"), 0);
        // 验证网页是否有账户输入框
        assertTrue("Failed to show email box.", email != null);
        // 验证网页是否密码户输入框
        assertTrue("Failed to show password box.", password != null);
        // 验证网页是否有登录按钮
        assertTrue("Failed to show login button.", login != null);
        email.setTextContent("autotest1@gmail.com");
        password.setTextContent("123456");
        solo.clickOnWebElement(login);
    }

    private boolean idGobackLogin() {
        // 中途中断
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return caseUtil.waitForText(Res.string.dolphinconnect_signin, 0, true, true, 60 * 1000,
                true);
    }
}