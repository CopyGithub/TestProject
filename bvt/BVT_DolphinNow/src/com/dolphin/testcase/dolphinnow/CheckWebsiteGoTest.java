package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-858
 * <p>
 * 脚本描述: 验证website里的站点跳转正确
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-858")
@TestClass("验证website里的站点跳转正确")
public class CheckWebsiteGoTest extends BaseTest {
    private String[] urlStrings = new String[12];
    private ArrayList<View> views;

    public void testCheckWebsiteGo() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        uiUtil.closeWhistleWhenOpen();
        solo.scrollToBottom();
        solo.sleep(Res.integer.time_wait);
        initUrlStrings();
        View parent = solo.getView("card_page_root");
        View gridView = caseUtil.getViewByIndex(parent, new int[] { 7, 1, 0 });
        views = utils.getChildren(gridView, false);
        // 出错，每个网站点击两遍，确保每一个都进行访问
        for (int i = 0; i < urlStrings.length; i++) {
            solo.clickOnView(views.get(i));
            solo.sleep(Res.integer.time_wait);
            assertTrue("第" + (i + 1) + "个网址出错了", uiUtil.checkURL(urlStrings[i]));
            solo.goBack();
            solo.sleep(Res.integer.time_wait);

            solo.clickOnView(views.get(i));
            solo.sleep(Res.integer.time_wait);
            assertTrue("第" + (i + 1) + "个网址出错了", uiUtil.checkURL(urlStrings[i]));
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
    }

    private void initUrlStrings() {
        urlStrings[0] = "twitter.com";
        urlStrings[1] = "rutracker.org/forum/index.php";
        urlStrings[2] = "m.ria.ru/";
        urlStrings[3] = "m.rambler.ru";
        urlStrings[4] = "www.ozon.ru/";
        urlStrings[5] = "m.livejournal.com";
        urlStrings[6] = "m.lenta.ru/";
        urlStrings[7] = "www.kinopoisk.ru/";
        urlStrings[8] = "instagram.com";
        urlStrings[9] = "google.com";
        urlStrings[10] = "www.gismeteo.ru/";
        urlStrings[11] = "m.facebook.com";
    }
}