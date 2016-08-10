package com.dante;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class CheckBookmarkOnHome extends UiAutomatorTestCase {
    private String url = "autotest.baina.com/test/downloadtest/";
    public void testCheckBookmarkOnHome() throws UiObjectNotFoundException {
        UiDevice nexus = getUiDevice();
        nexus.pressHome();
        sleep(1000);
        UiObject target = new UiObject(new UiSelector().text("bmToHome_" + getCurrentTime()));
        boolean b = target.exists();
        Log.i("hxiong", "add : " + String .valueOf(b));
        assertTrue("Failed to add bookmark to homescreen by shortcut.", b);
        target.click();
        sleep(10000);
        long startTime = SystemClock.uptimeMillis();
        UiObject progress = new UiObject(new UiSelector().resourceId(Constants.PACKAGE_NAME + ":id/tiny_title_bar").className(View.class));
        b = progress.exists();
        while(b && SystemClock.uptimeMillis() - startTime < 60000) {
            Log.i("hxiong", "b is " + String .valueOf(b));
            sleep(1000);
            b = progress.exists();
        }
        if(b) {
            assertTrue("Network is bad! The page is still loading.", false);
        }
        String text = new UiObject(new UiSelector().resourceId(Constants.PACKAGE_NAME + ":id/title").className(TextView.class)).getText();
        Log.i("hxiong", "text is " + text);
        assertTrue("Fail to open the url by clicking bookmark shortcut on homescrenn.", text.equals(url));
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("MMddhh");
        Date now = new Date(System.currentTimeMillis());
        return format.format(now);
    }
}
