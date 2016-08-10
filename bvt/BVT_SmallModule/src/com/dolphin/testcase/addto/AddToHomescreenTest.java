package com.dolphin.testcase.addto;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;
import com.test.annotation.UiAutomatorAfter;

/**
 * 
 * 脚本编号： DOLINT-1946
 * <p>
 * 脚本描述：验证添加Homescreen弹框直接Add
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1946")
@TestClass("验证添加Homescreen弹框直接Add")
@UiAutomatorAfter("com.dante.AddToHomescreenTest")
public class AddToHomescreenTest extends BaseTest {
    private String baiduURL = Res.string.url_four;
    private String baiduName = "four";

    public void testAddToHomescreen() {
        uiUtil.skipWelcome();
        // 添加到Home Screen
        addHomeScreen();

        // 验证：是否弹框消失，Toast提示快捷键XX已添加,该提示以及其显示方式由手机系统决定(TODO)
        // assertTrue("Toast 'Saved to home screen' didn't appear.",
        // solo.waitForText(caseUtil.getTextByRId("......")));

        // uiautomator
        // 验证：进入手机的Home Screen,是否新增百度快捷方式
        // 验证：点击快捷方式，是否启动Dolphin并在New Tab中打开XXX网页
    }

    private void addHomeScreen() {
        // 打开网页
        uiUtil.visitUrl(baiduURL);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);

        // 点击海豚图标→点击Add to→点击Home Screen图标→点击Add
        uiUtil.clickOnMenuItem("add_to");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/item_home_screen");
        solo.sleep(Res.integer.time_wait);

        solo.hideSoftKeyboard();
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, baiduName);
        caseUtil.clickOnView("id/add");
        solo.sleep(Res.integer.time_wait);
    }
}