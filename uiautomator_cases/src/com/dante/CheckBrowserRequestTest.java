package com.dante;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class CheckBrowserRequestTest extends UiAutomatorTestCase {
    public void testACheckBrowserRequest() throws UiObjectNotFoundException {
        // 设置locale为巴西,并关闭wifi
        setLanguage(true, "System settings", "Language & input", "English (United States)",
                "Português");

        // 验证：smart locale请求重试机制正常
        checkRequest();

    }

    public void testToDefault() throws UiObjectNotFoundException {
        // 设置locale为默认值,English,并开启wifi
        setLanguage(false, "Configurações do sistema", "Idioma e texto", "Português",
                "English (United States)");
    }

    private void checkRequest() throws UiObjectNotFoundException {
        getUiDevice().pressHome();
        sleep(1500);
        UiObject apps = new UiObject(new UiSelector().resourceId("com.android.launcher:id/layout")
                .childSelector(new UiSelector().index(0)).childSelector(new UiSelector().index(2)));
        // UiObject apps = new UiObject(new UiSelector().description("Apps"));
        apps.click();
        sleep(1000);

        // 验证：第三次进入浏览器后，切换到巴西News模式
        checkOpenDolphin(1);

        sleep(5000);
        checkOpenDolphin(2);

        sleep(5000);
        checkOpenDolphin(3);

    }

    private void checkOpenDolphin(int i) throws UiObjectNotFoundException {
        UiObject dolphin = new UiObject(new UiSelector().text("Dolphin"));
        dolphin.click();
        sleep(1000);

        UiObject start = new UiObject(new UiSelector().text("INÍCIO"));
        boolean a = start.exists();
        if (a) {
            start.click();
        }
        sleep(5000);

        UiObject brasilia = new UiObject(new UiSelector().text("Brasília"));
        boolean b = brasilia.exists();
        if (i == 3) {
            assertTrue("Not changed to Brasil News", b);
        } else {
            assertTrue("Have changed to Brasil News", !b);
        }

        getUiDevice().pressBack();
        getUiDevice().pressBack();
        getUiDevice().pressBack();
        sleep(1000);
    }

    /**
     * @param toClose
     *            是否需要关闭wifi
     * @param systemSettings
     *            系统设置的text
     * @param lanAndInput
     *            语言的text
     * @param lanFrom
     *            当前语言
     * @param lanTo
     *            要设置的语言
     * @throws UiObjectNotFoundException
     */
    private void setLanguage(boolean toClose, String systemSettings, String lanAndInput,
            String lanFrom, String lanTo) throws UiObjectNotFoundException {
        getUiDevice().pressHome();
        sleep(1500);
        getUiDevice().pressMenu();
        sleep(1500);
        UiObject settings = new UiObject(new UiSelector().text(systemSettings));
        settings.click();
        sleep(1000);

        // 开启/关闭wifi
        if (toClose) {
            closeWifi();
        } else {
            openWifi();
        }

        // 设置语言
        UiScrollable settingItems = new UiScrollable(new UiSelector().scrollable(true));
        UiObject languageAndInputItem = settingItems.getChildByText(
                new UiSelector().text(lanAndInput), lanAndInput, true);
        languageAndInputItem.clickAndWaitForNewWindow();

        // 找到“某语言(如，English)”的可点击项（因为当前是该语言环境）
        UiObject setLanItem = new UiObject(new UiSelector().text(lanFrom));
        setLanItem.clickAndWaitForNewWindow();

        UiScrollable scroll = new UiScrollable(new UiSelector().scrollable(true));
        UiObject brasil = scroll.getChildByText(new UiSelector().text(lanTo), lanTo, true);
        brasil.click();
        sleep(1000);

    }

    private void closeWifi() throws UiObjectNotFoundException {
        UiObject wifiOnCheckBox = new UiObject(new UiSelector().className("android.widget.Switch")
                .instance(0));
        if (wifiOnCheckBox.isChecked()) {
            wifiOnCheckBox.click();
            sleep(1000);
        }
    }

    private void openWifi() throws UiObjectNotFoundException {
        UiObject wifiOnCheckBox = new UiObject(new UiSelector().className("android.widget.Switch")
                .instance(0));
        if (!wifiOnCheckBox.isChecked()) {
            wifiOnCheckBox.click();
            sleep(1000);
        }
    }

}
