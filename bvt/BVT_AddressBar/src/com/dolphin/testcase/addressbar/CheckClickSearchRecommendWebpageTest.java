package com.dolphin.testcase.addressbar;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1585
 * <p>
 * 脚本描述：网站搜索建议中点击记录或↖响应正常
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1585")
@TestClass("网站搜索建议中点击记录或↖响应正常")
public class CheckClickSearchRecommendWebpageTest extends BaseTest {
    private String url1 = Res.string.url_downloadtest;
    private String url2 = Res.string.url_test;

    public void testCheckClickSearchRecommendWebpage() {
        uiUtil.skipWelcome();
        init();
        // 验证:响应url填充到url框,url未被全选
        checkClickIcon();
        // 验证:打开相应网页
        checkClickList();
    }

    private void checkClickList() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        clickAddressBarAndEnterText(false);
    }

    private void checkClickIcon() {
        clickAddressBarAndEnterText(true);
    }

    private void clickAddressBarAndEnterText(boolean isClickIcon) {
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("title_bg");
        solo.sleep(Res.integer.time_wait);
        TextView searchBar = (TextView) solo.getView("search_input");
        caseUtil.setText(searchBar, "b");
        solo.sleep(5 * 1000);// 网络慢时结果出现比较慢
        View list = caseUtil.getViewByClassName("DropDownListView", 0, true);
        View suggestion = caseUtil.getViewByIndex(list, new int[] { 1, 0 });
        View icon = caseUtil.getViewByIndex(suggestion, new int[] { 0, 0 });
        String url = ((TextView) caseUtil.getViewByIndex(suggestion, new int[] { 2, 1 })).getText()
                .toString();
        if (isClickIcon) {
            solo.clickOnView(icon);
            solo.sleep(Res.integer.time_wait);
            assertTrue("相应网页url出错", url.equals(searchBar.getText().toString()));
        } else {
            solo.clickOnView(suggestion);
            solo.sleep(Res.integer.time_wait);
            assertTrue("未打开相应网页", uiUtil.checkURL(url));
        }
    }

    // 访问两个网页,返回浏览器主页
    private void init() {
        uiUtil.visitUrl(url1);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);
        uiUtil.visitUrl(url2);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);
        if (caseUtil.getDisplayRange() == 0) {
            solo.clickOnView(solo.getView("address_open_menu_more"));
            solo.sleep(Res.integer.time_wait);
            solo.clickOnView(solo.getView("id/0x100"));
            solo.sleep(Res.integer.time_wait);
        } else {
            solo.clickOnView(uiUtil.getMenubarItem(4, 0));
        }
        solo.sleep(Res.integer.time_wait);
    }
}