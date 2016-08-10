package com.dolphin.testcase.news;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-376
 * <p>
 * 脚本描述: 验证News模式主页界面显示正常
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-376")
@TestClass("验证News模式主页界面显示正常")
public class CheckNewsUITest extends BaseTest {
    public void testCheckNewsUI() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("pt_BR");
        /**
         * 主页显示为news模式,从上至下依次显示: <br>
         * ·Tab bar <br>
         * ·地址栏 <br>
         * ·天气 <br>
         * ·Top news <br>
         * ·Speed dial <br>
         */
        View tabbar = caseUtil.getViewByIndex(solo.getView("top_container"), new int[] { 0, 0 });
        View addressbar = solo.getView("address_bar");
        View weather = solo.getView("weather");
        View news = caseUtil.getViewByIndex(solo.getView("middle_screen"), new int[] { 3, 1 });
        View speeddial = caseUtil.getViewByIndex(solo.getView("middle_screen"), new int[] { 2, 0 });
        ArrayList<View> views = new ArrayList<View>();
        views.add(tabbar);
        views.add(addressbar);
        views.add(weather);
        views.add(news);
        views.add(speeddial);
        utils.ubietyOfViews(views, 1, false, false, false);
    }
}