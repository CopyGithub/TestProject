package com.dante;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class AddToHomescreenTest extends UiAutomatorTestCase {
    private String url = "autotest.baina.com/test/downloadtest/four.html";
    private String name = "four";

    public void test() throws UiObjectNotFoundException {
        getUiDevice().pressHome();
        sleep(1500);

        // 验证：进入手机的Home Screen,是否新增快捷方式
        UiObject webName = new UiObject(new UiSelector().text(name));
        assertTrue("Failed to add a shortcut to the home screen", webName.exists());
        webName.click();
        sleep(1000);

        // 验证：点击快捷方式，是否启动Dolphin并在New Tab中打开XXX网页
        UiObject webURL = new UiObject(new UiSelector().text(url));
        assertTrue("Failed to open the shortcut in the home scrren", webURL.exists());
    }
}