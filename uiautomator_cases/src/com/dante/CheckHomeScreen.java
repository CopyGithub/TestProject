package com.dante;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class CheckHomeScreen extends UiAutomatorTestCase {
    public void testCheckHomeScreen() {
        UiDevice nexus = getUiDevice();
        nexus.pressHome();
        sleep(3000);
        UiObject target = new UiObject(new UiSelector().text("ToHome_" + getCurrentTime()));
        boolean b = target.exists();
        assertTrue("Failed to add speed dial to homescreen by shortcut.", b);
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMddhh");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }
}
