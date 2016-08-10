package com.dante;

import android.os.SystemClock;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class Uninstall extends UiAutomatorTestCase {
    public void testUninstall() throws UiObjectNotFoundException {
        UiDevice nexus = getUiDevice();
        nexus.pressHome();
        sleep(1000);
        String shortcutName;
        if (Constants.PACKAGE_NAME.equals("mobi.mgeek.TunnyBrowser")) {
            shortcutName = "INT";
        } else {
            shortcutName = "EXPRESS";
        }
        UiObject target = new UiObject(new UiSelector().text(shortcutName));
        target.click();
        sleep(10000);
        nexus.drag(nexus.getDisplayWidth() - 1, nexus.getDisplayHeight() / 2, 1,
                nexus.getDisplayHeight() / 2, 40);
        sleep(1000);
        new UiObject(new UiSelector().resourceId(Constants.PACKAGE_NAME + ":id/btn_addon_manage"))
                .click();
        sleep(1000);
        new UiObject(new UiSelector().text("Browse Faster")).click();
        sleep(1000);
        new UiObject(new UiSelector().text("Uninstall")).click();
        sleep(1000);
        new UiObject(new UiSelector().resourceId("com.android.packageinstaller:id/ok_button"))
                .click();
        UiObject uninstall_text = new UiObject(new UiSelector().textStartsWith("Uninstalling"));
        boolean b = uninstall_text.exists();
        long startTime = SystemClock.uptimeMillis();
        while (!b && SystemClock.uptimeMillis() - startTime < 3000) {
            sleep(100);
            b = uninstall_text.exists();
        }
        assertTrue("Failed to enter Uninstalling screen.", b);
    }
}
