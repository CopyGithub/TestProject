package com.dante;

import android.util.Log;
import android.widget.RelativeLayout;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class Detail extends UiAutomatorTestCase {
    public void test1() throws UiObjectNotFoundException {
        UiDevice nexus = getUiDevice();
        nexus.pressHome();
        sleep(1000);
//        UiScrollable scroll = new UiScrollable(new UiSelector().scrollable(true));
//        scroll.setAsHorizontalList();
        String shortcutName;
        if(Constants.PACKAGE_NAME.equals("mobi.mgeek.TunnyBrowser")) {
            shortcutName = "INT";
        } else {
            shortcutName = "EXPRESS";
        }
//        boolean isScrolled = scroll.scrollIntoView(new UiSelector().text(shortcutName));
//        Log.i(getClass().getName(), "is scrolled: " + String.valueOf(isScrolled));
        UiObject target = new UiObject(new UiSelector().text(shortcutName));
        target.click();
        sleep(10000);
        nexus.drag(nexus.getDisplayWidth() - 1, nexus.getDisplayHeight()/2, 1, nexus.getDisplayHeight()/2, 40);
        sleep(1000);
        new UiObject(new UiSelector().resourceId(Constants.PACKAGE_NAME + ":id/btn_addon_manage")).click();
        sleep(1000);
        new UiObject(new UiSelector().text("Browse Faster")).click();
        sleep(1000);
        new UiObject(new UiSelector().text("Detail")).click();
        sleep(1000);
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message").packageName("mobi.mgeek.browserfaster"));
        Log.i("hxiong", "exit:" + String .valueOf(message.exists()));
        if(!message.exists()) {
            assertTrue("Failed to enter plugin description screen.", false);
        }
        UiObject homeScreen = new UiObject(new UiSelector().resourceId(Constants.PACKAGE_NAME + ":id/center_screen").className(RelativeLayout.class));
        int count = 0;
        while(!homeScreen.exists()) {
            nexus.pressBack();
            count++;
            sleep(500);
        }
        Log.i("hxiong", "count is " + count);
        nexus.pressHome();
    }
}
