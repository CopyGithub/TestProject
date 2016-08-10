package com.dante;

import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class DateAutomatic extends UiAutomatorTestCase {
    public void testModifyDate() throws UiObjectNotFoundException {
        UiDevice nexus = UiDevice.getInstance();
        nexus.pressHome();
        sleep(1500);
        nexus.pressMenu();
        sleep(1000);
        UiObject settings = new UiObject(new UiSelector().text("System settings"));
        settings.click();
        UiScrollable scroll = new UiScrollable(new UiSelector().scrollable(true));
        scroll.setAsVerticalList();
        scroll.scrollTextIntoView("Date & time");
        UiObject dateTime = new UiObject(new UiSelector().text("Date & time"));
        dateTime.click();
        sleep(1000);
        //���ر����Զ�����ʱ�䣬����
        UiObject autoDateCheckbox = new UiObject(new UiSelector().resourceId("android:id/list").childSelector(new UiSelector().resourceId("android:id/checkbox").index(0)));
        Log.i("hxiong", "checked is " + String.valueOf(autoDateCheckbox.isChecked()));
        if(!autoDateCheckbox.isChecked()) {
            autoDateCheckbox.click();
            sleep(1000);
        }
        nexus.pressHome();
        sleep(1500);
    }
}
