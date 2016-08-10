package com.dolphin.testcase.addon;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-2159
 * <p>
 * 脚本描述: 中屏机:入口&管理界面:未安装插件时UI(横竖屏)
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-2159")
@TestClass("中屏机:入口&管理界面:未安装插件时UI(横竖屏)")
public class CheckNoAddonUITest extends BaseTest {
    private String downloadURL = "dolphin-browser.com/android/addons/index.html?l=zh_cn";

    public void testCheckNoAddonUI() {
        // 非中屏机直接返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        uiUtil.skipWelcome();

        // 验证:展开右侧边栏,界面是否正确
        uiUtil.enterSideBar(false);
        checkAddonUI(false);
        // 验证:横竖屏切换,ADD-ONS界面显示正常
        checkAddonUI(true);
        checkAddonUI(false);

        // 验证:打开Dolphin插件下载页面
        checkIntoDownload(1);
        // 验证:打开Dolphin插件下载页面
        checkIntoDownload(2);

        // 验证:进入Add-ONS界面,界面是否正确
        uiUtil.enterSideBar(false);
        solo.clickOnView(solo.getView("btn_addon_manage"));
        solo.sleep(Res.integer.time_change_activity);
        checkAddonListUI(false);
        // 验证:横竖屏切换,界面显示正常
        checkAddonListUI(true);
        checkAddonListUI(false);

        // 验证:打开Dolphin插件下载页面
        checkIntoDownload(3);
    }

    private void checkAddonListUI(boolean isLandscape) {
        /**
         * 界面从上至下依次为: <br>
         * ①< ADD-ONS ﹢ <br>
         * ②插件列表区域: <br>
         * 海豚图标,周围环绕插件图标 <br>
         * Click here to get more add-ons <br>
         */
        if (isLandscape) {
            solo.setActivityOrientation(Solo.LANDSCAPE);
        } else {
            solo.setActivityOrientation(Solo.PORTRAIT);
        }
        solo.sleep(Res.integer.time_change_activity);
        ArrayList<View> views = new ArrayList<View>();
        View back = solo.getView("btn_done");
        View title = solo.getView("title");
        View add = solo.getView("btn_store");
        views.add(back);
        views.add(title);
        views.add(add);
        utils.ubietyOfViews(views, 0, false, false, false);
        View imageView = solo.getView("empty_addon_image");
        View tips = solo.getView("empty_addon_tips");
        assertTrue("The image is not above the tips.",
                utils.ubietyOfView(imageView, tips, false, false, false) != -1);
    }

    private void checkIntoDownload(int tag) {
        switch (tag) {
        case 1:
            solo.clickOnView(caseUtil.getViewByText("empty_addon_note", 0, true, true, true));
            break;
        case 2:
            uiUtil.enterSideBar(false);
            solo.clickOnView(solo.getView("btn_addon_store"));
            break;
        case 3:
            solo.clickOnView(caseUtil.getViewByText("empty_installed_plugin", 0, true, true, true));
            break;
        default:
            assertTrue("tag is wrong.", false);
            break;
        }
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is bad .", uiUtil.waitForWebPageFinished());
        caseUtil.slideDireciton(null, false, 0.2f, 1f);
        solo.sleep(Res.integer.time_wait);
        assertTrue("The URL is wrong .", uiUtil.checkURL(downloadURL));
    }

    private void checkAddonUI(boolean isLandscape) {
        if (isLandscape) {
            solo.setActivityOrientation(Solo.LANDSCAPE);
        } else {
            solo.setActivityOrientation(Solo.PORTRAIT);
        }
        solo.sleep(Res.integer.time_change_activity);
        /**
         * CONTROL PANEL下方显示ADD-ONS区域,从上至下依次为: <br>
         * ①ADD-ONS ﹢ 三点菜单 <br>
         * ② Get more add-ons <br>
         */
        ArrayList<View> views = new ArrayList<View>();
        View addonText = caseUtil.getViewByIndex(solo.getView("list_installed_plugin"), new int[] {
                1, 3 });
        View add = caseUtil.getViewByIndex(solo.getView("list_installed_plugin"),
                new int[] { 1, 2 });
        View manage = caseUtil.getViewByIndex(solo.getView("list_installed_plugin"), new int[] { 1,
                1 });
        views.add(addonText);
        views.add(add);
        views.add(manage);
        utils.ubietyOfViews(views, 0, false, false, false);
        View textHint = caseUtil.getViewByIndex(solo.getView("list_installed_plugin"), new int[] {
                2, 0 });
        assertTrue("'ADD-ONS' is not above the 'Get more add-ons'.",
                utils.ubietyOfView(addonText, textHint, false, false, false) != -1);
    }
}
