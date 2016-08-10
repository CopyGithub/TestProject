package com.dante;

import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class ModifyDate0103 extends UiAutomatorTestCase {
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
        //设置时间
        UiObject autoDateCheckbox = new UiObject(new UiSelector().resourceId("android:id/list").childSelector(new UiSelector().resourceId("android:id/checkbox").index(0)));
        Log.i("hxiong", "checked is " + String.valueOf(autoDateCheckbox.isChecked()));
        if(autoDateCheckbox.isChecked()) {
            autoDateCheckbox.click();
            sleep(1000);
        }
        UiObject setDate = new UiObject(new UiSelector().text("Set date"));
        setDate.click();
        sleep(1000);
//        UiSelector monthSelector = new UiSelector().resourceId("android:id/pickers").childSelector(new UiSelector().className(android.widget.NumberPicker.class).index(0).childSelector(new UiSelector().resourceId("android:id/numberpicker_input")));
        UiSelector monthSelector = new UiSelector().resourceId("android:id/pickers").childSelector(new UiSelector().resourceId("android:id/numberpicker_input").instance(0));
        UiObject month = new UiObject(monthSelector);
        month.click();
        sleep(500);
        month.setText("Jan");
        sleep(500);

//        UiSelector dateSelector = new UiSelector().resourceId("android:id/pickers").childSelector(new UiSelector().className(android.widget.NumberPicker.class).index(1).childSelector(new UiSelector().resourceId("android:id/numberpicker_input")));
        UiSelector dateSelector = new UiSelector().resourceId("android:id/pickers").childSelector(new UiSelector().resourceId("android:id/numberpicker_input").instance(1));
        UiObject date = new UiObject(dateSelector);
        date.click();
        sleep(500);
        date.setText("03");
        sleep(500);

//        UiSelector yearSelector = new UiSelector().resourceId("android:id/pickers").childSelector(new UiSelector().className(android.widget.NumberPicker.class).index(2).childSelector(new UiSelector().resourceId("android:id/numberpicker_input")));
        UiSelector yearSelector = new UiSelector().resourceId("android:id/pickers").childSelector(new UiSelector().resourceId("android:id/numberpicker_input").instance(2));
        UiObject year = new UiObject(yearSelector);
        year.click();
        sleep(500);
        year.setText("2015");
        sleep(500);
        nexus.pressBack();
        sleep(500);
        try {
            new UiObject(new UiSelector().text("Done")).click();
        } catch (UiObjectNotFoundException e) {
            Log.i("Fail", "Failed to click 'Done'! " + e.getMessage());
        }
        nexus.pressHome();
        sleep(1500);
    }
}
