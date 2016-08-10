package com.dante;

import java.util.ArrayList;

import android.util.Log;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class SystemShareListTest extends UiAutomatorTestCase {
    UiDevice nexus;

    public void test() throws UiObjectNotFoundException {
        nexus = UiDevice.getInstance();

        // 查看手机上这些安装的服务的排序方式, 记为序列A
        ArrayList<String> listA = getListA();
        // 进入Dolphin的Share界面中,查看界面下半部分系统服务排序, 记为序列B
        ArrayList<String> listB = getListB();

        assertTrue("Lists are not completely the same...", listA.equals(listB));

    }

    private ArrayList<String> getListB() throws UiObjectNotFoundException {
        nexus.pressHome();
        sleep(1500);

        UiObject dolphin = new UiObject(new UiSelector().text("Dolphin"));
        dolphin.click();
        sleep(1000);

        UiObject menu = new UiObject(new UiSelector()
                .resourceId("mobi.mgeek.TunnyBrowser:id/bottom_container")
                .childSelector(new UiSelector().index(0)).childSelector(new UiSelector().index(2)));
        menu.click();
        sleep(1000);

        // 点击Share (TODO)
        UiObject share = new UiObject(new UiSelector().text("Share"));
        share.click();
        sleep(1000);

        UiObject list = new UiObject(
                new UiSelector().resourceId("mobi.mgeek.TunnyBrowser:id/system_share_grid"));
        ArrayList<String> listB = new ArrayList<String>();
        for (int i = 0; i < list.getChildCount(); i++) {
            UiObject title = list.getChild(new UiSelector().index(i))
                    .getChild(new UiSelector().index(0)).getChild(new UiSelector().index(1));
            listB.add(title.getText());
        }
        Log.i("sjguo", listB + "");
        return listB;

    }

    private ArrayList<String> getListA() throws UiObjectNotFoundException {
        // 查看方式: 参考google分享服务列表排序
        // 打开chrome -> 打开任意网页 -> 点击手机左键 -> 菜单中点击"Share"-> 进入Share界面
        nexus.pressHome();
        sleep(1500);

        UiObject chrome = new UiObject(new UiSelector().text("Chrome"));
        chrome.click();
        sleep(1000);

        // 打开一个网页
        UiObject input = new UiObject(new UiSelector().resourceId("com.android.chrome:id/url_bar"));
        input.click();
        sleep(1000);
        input.setText("www.baidu.com");
        sleep(1000);
        nexus.pressEnter();
        sleep(1000);

        UiObject more = new UiObject(
                new UiSelector().resourceId("com.android.chrome:id/menu_button"));
        more.click();
        sleep(1000);

        UiObject share = new UiObject(new UiSelector().text("Share…"));
        share.click();
        sleep(1000);

        UiObject list = new UiObject(new UiSelector().resourceId("android:id/resolver_list"));
        ArrayList<String> listA = new ArrayList<String>();
        for (int i = 0; i < list.getChildCount(); i++) {
            UiObject title = list.getChild(new UiSelector().index(i))
                    .getChild(new UiSelector().index(1)).getChild(new UiSelector().index(0));
            if (!title.getText().equals("Copy to clipboard")) {
                listA.add(title.getText());
            }
        }
        Log.i("sjguo", listA + "");
        return listA;

    }
}