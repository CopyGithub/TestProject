package com.dante;

import java.io.IOException;

import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class CleanCacheAutoTest extends UiAutomatorTestCase {
    UiDevice nexus;

    public void testA() throws UiObjectNotFoundException {
        nexus = UiDevice.getInstance();
        // 1.进入File Manager查看cache,成功生成cache（TestCase中已验证）

        // 2.将手机系统时间调整为7天后，访问其它网页: 如url_four,url_five
        modifyDate();
        visitUrl();

        // 3.重启Dolphin,再次查看cache,webview内的cache仅保留最近3天内的，其他cache被删除
        checkWebviewCache();

    }

    public void testToDefult() throws UiObjectNotFoundException {
        // 内存环境准备：删除newFiles文件夹
        deleteNewFiles();

        // 将时间改回来
        modifyDateAutomatic();
    }

    private void modifyDateAutomatic() throws UiObjectNotFoundException {
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
        // 设置时间为自动
        UiObject autoDateCheckbox = new UiObject(new UiSelector().resourceId("android:id/list")
                .childSelector(new UiSelector().resourceId("android:id/checkbox").index(0)));
        if (!autoDateCheckbox.isChecked()) {
            autoDateCheckbox.click();
            sleep(1000);
        }
        nexus.pressHome();
        sleep(1500);

    }

    private void checkWebviewCache() throws UiObjectNotFoundException {
        UiObject dolphin = new UiObject(new UiSelector().text("Dolphin"));
        dolphin.click();
        sleep(1000);

        UiObject menu = new UiObject(new UiSelector()
                .resourceId("mobi.mgeek.TunnyBrowser:id/bottom_container")
                .childSelector(new UiSelector().index(0)).childSelector(new UiSelector().index(2)));
        menu.click();
        sleep(1000);

        // TODO 点击downloads(出错)
        UiObject downloads = new UiObject(new UiSelector().text("Downloads"));
        downloads.click();
        sleep(1000);
        UiObject fileManager = new UiObject(new UiSelector().text("File Manager"));
        fileManager.click();
        sleep(1000);

        UiObject webviewCache = new UiObject(new UiSelector().text("webviewCache"));
        if (!webviewCache.exists()) {
            UiObject folder1 = new UiObject(new UiSelector().text("0"));
            folder1.click();
            sleep(1000);
            UiScrollable scroll = new UiScrollable(new UiSelector().scrollable(true));
            scroll.setAsVerticalList();
            scroll.scrollTextIntoView("TunnyBrowser");
            UiObject tunnyBrowser = new UiObject(new UiSelector().text("TunnyBrowser"));
            tunnyBrowser.click();
            sleep(1000);
            UiObject cache = new UiObject(new UiSelector().text("cache"));
            cache.click();
            sleep(1000);

            webviewCache.click();
            sleep(1000);
        }

        UiObject list = new UiObject(new UiSelector().resourceId("mobi.mgeek.TunnyBrowser:id/list"));
        Log.i("sjguo", list.getChildCount() + "");
        assertTrue("The cache is not the latest 3 days", list.getChildCount() < 8);

        nexus.pressHome();
        sleep(1500);
    }

    private void visitUrl() throws UiObjectNotFoundException {
        UiObject dolphin = new UiObject(new UiSelector().text("Dolphin"));
        dolphin.click();
        sleep(1000);

        UiObject url4 = new UiObject(new UiSelector().resourceId(
                "mobi.mgeek.TunnyBrowser:id/workspace").childSelector(
                new UiSelector().index(0).childSelector(new UiSelector().index(0))));
        url4.click();
        sleep(1000);
        nexus.pressBack();
        sleep(1000);

        UiObject url5 = new UiObject(new UiSelector().resourceId(
                "mobi.mgeek.TunnyBrowser:id/workspace").childSelector(
                new UiSelector().index(0).childSelector(new UiSelector().index(1))));
        url5.click();
        sleep(1000);
        nexus.pressBack();
        sleep(1000);

        nexus.pressBack();
        nexus.pressBack();
        sleep(1000);
    }

    private void modifyDate() throws UiObjectNotFoundException {
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
        // 设置时间
        UiObject autoDateCheckbox = new UiObject(new UiSelector().resourceId("android:id/list")
                .childSelector(new UiSelector().resourceId("android:id/checkbox").index(0)));
        if (autoDateCheckbox.isChecked()) {
            autoDateCheckbox.click();
            sleep(1000);
        }
        UiObject setDate = new UiObject(new UiSelector().text("Set date"));
        setDate.click();
        sleep(1000);

        UiSelector monthSelector = new UiSelector().resourceId("android:id/pickers").childSelector(
                new UiSelector().resourceId("android:id/numberpicker_input").instance(0));
        UiObject month = new UiObject(monthSelector);
        month.click();
        sleep(500);
        month.setText("May");
        sleep(500);

        UiSelector dateSelector = new UiSelector().resourceId("android:id/pickers").childSelector(
                new UiSelector().resourceId("android:id/numberpicker_input").instance(1));
        UiObject date = new UiObject(dateSelector);
        date.click();
        sleep(500);
        date.setText("20");
        sleep(500);

        UiSelector yearSelector = new UiSelector().resourceId("android:id/pickers").childSelector(
                new UiSelector().resourceId("android:id/numberpicker_input").instance(2));
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

    private void deleteNewFiles() {
        // 内存环境：删除newFiles文件夹
        String filePath = "mnt/sdcard/newFiles";
        try {
            Process process = Runtime.getRuntime().exec("rm -rf " + filePath);
            process.waitFor();
        } catch (IOException e) {
            assertTrue("Failed to delete newFiles.", false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
