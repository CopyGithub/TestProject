package com.dolphin.testcase.setting;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;

/**
 * 该case没有完成启动关闭jetpack，废弃
 * <p>
 * 脚本编号: DOLINT-1030
 * <p>
 * 脚本描述: 验证开启"Remember passwords"设置项后,登录需输入账号网站时,正确记住密码
 * 
 * @author jwliu
 * 
 */
public class CheckRemeberPasswordUITest extends BaseTest {
    private String account = "heroinfairy@126.com";
    private String password = "lsy1314520";
    private String url = "http://enginetest.baina.com:82/login.html";
    private String expectedMsg;
    private String factMsg;
    int[] path = new int[] { 1, 0 };

    public void testCheckRemeberPasswordUITest() {
        uiUtil.skipWelcome();
        try {
            boolean isUsingWebkit = uiUtil.isWebkit();
            if (!isUsingWebkit && android.os.Build.VERSION.SDK_INT > 18) {
                assertTrue("该case需要运行在带jetpack的版本上或者shell版本安装在android 4.4以下的系统，当前手机API Level 为"
                        + utils.SDK, false);
            }
        } catch (Exception e) {
            assertTrue("Failed to reflect to judge whether webkit is using or not.", false);
        }
        visitURL();
        // 验证：提示语:"Do you want Dolphin to remember this password?If yes ,you don't have to type it again the next time you visit."
        // 底部依次显示按钮: "Not Now","Never", "Yes"
        assertTrue("Failed to find save_password_message in dialog ", expectedMsg.equals(factMsg));
        assertTrue("Failed to popup 'Never' text.",
                solo.waitForText(caseUtil.getTextByRId("never")));
        assertTrue("Failed to popup 'Yes' text.",
                solo.waitForText(caseUtil.getTextByRId("save_password_save")));
        assertTrue("Failed to popup 'Not Now' text.",
                solo.waitForText(caseUtil.getTextByRId("save_password_not_now")));
    }

    private void visitURL() {
        // 开启remember password选项
        uiUtil.enterSetting(true);
        caseUtil.clickOnText("pref_privacy_settings", 0, true);
        solo.sleep(Res.integer.time_wait);
        uiUtil.setCheckBoxByTitle("pref_security_remember_passwords", 2, path, true);
        solo.sleep(Res.integer.time_wait);
        uiUtil.visitUrl(url);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is bad.", uiUtil.waitForWebPageFinished());
        // 将网页向下拉动使tabbar显示出来
        solo.sleep(Res.integer.time_wait);
        solo.enterTextInWebElement(By.id("username"), account);
        solo.sleep(Res.integer.time_wait);
        solo.enterTextInWebElement(By.id("password"), password);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnWebElement(By.id("loginBtn"), false);
        expectedMsg = caseUtil.getTextByRId("save_password_message");
        factMsg = ((TextView) solo.getView("id/msg")).getText().toString();
    }
}
