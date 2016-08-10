
package com.dolphin.testcase.history;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1411
 * <p>
 * 脚本描述：验证访问今天的历史记录
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1411")
@TestClass("验证访问今天的历史记录")
public class CheckTodayHistoryTest extends BaseTest {
    // testulr 和testurlName是网页地址和网站名字一次对应
    private String testurl[] = { Res.string.url_one, Res.string.url_two, Res.string.url_three,
            Res.string.url_four, Res.string.url_five, Res.string.url_sixth, Res.string.url_seven,
            Res.string.url_eight };
    private String testurlName[] = { Res.string.testurl_name_1, Res.string.testurl_name_2,
            Res.string.testurl_name_3, Res.string.testurl_name_4, Res.string.testurl_name_5,
            Res.string.testurl_name_6, Res.string.testurl_name_7, Res.string.testurl_name_8 };
    // 访问了其他网页之后需要验证的正确顺序列表
    private String testurlName1[] = { Res.string.testurl_name_4, Res.string.testurl_name_8,
            Res.string.testurl_name_7, Res.string.testurl_name_6, Res.string.testurl_name_5 };
    private String testurlName2[] = { Res.string.testurl_name_2, Res.string.testurl_name_4,
            Res.string.testurl_name_8, Res.string.testurl_name_7, Res.string.testurl_name_6 };
    private String testurlName3[] = { Res.string.testurl_name_5, Res.string.testurl_name_3,
            Res.string.testurl_name_1 };

    public void testCheckTodayHistory() {
        uiUtil.skipWelcome();
        // 前置条件：历史记录列表中已经存在Today文件夹,文件夹中有历史记录，从上往下的顺序为3,2,1,文件夹外面有历史，顺序为8,7,6,5,4
        uiUtil.prepareTodayFolder(testurl);
        // 进入History列表中→点击历史4
        clickInHistoryItem(testurlName[3]);
        /**
         * 验证：打开历史4对应的网页
         */
        assertTrue("Failed to open website " + testurlName[3], uiUtil.checkURL(testurl[3]));
        // 进入History列表中观察Today文件夹以外的记录
        /**
         * 验证：列表中的历史记录排序为4,8,7,6,5
         */
        assertTrue("The history order is wrong.", isOrderHistory(testurlName1, false));
        // 点击Today文件夹→点击历史2
        clickInTodayItem(testurlName[1]);
        /**
         * 验证： 打开历史2对应的网页
         */
        assertTrue("Failed to open website " + testurlName[1], uiUtil.checkURL(testurl[1]));
        /**
         * 验证：进入History列表中，列表中的历史记录排序为2,4,8,7,6
         */
        assertTrue("The history order is wrong.", isOrderHistory(testurlName2, false));
        /**
         * 验证： 点击Today文件夹 列表中的历史记录排序为5,3,1
         */
        assertTrue("The today history order is wrong.", isOrderHistory(testurlName3, true));
    }

    /**
     * 点击today文件夹中的记录
     * 
     * @param name
     */
    private void clickInTodayItem(String name) {
        solo.clickOnText(caseUtil.getTextByRId("today"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(name);
        assertTrue("Network is bad .", uiUtil.waitForWebPageFinished());
    }

    /**
     * 点击history中的记录
     * 
     * @param name
     */
    private void clickInHistoryItem(String name) {
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("history"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(name);
        assertTrue("Network is bad. ", uiUtil.waitForWebPageFinished());
    }

    /**
     * 验证记录的排序
     * 
     * @param names
     *            用于对比的正确的记录列表
     * @param isToday
     *            是否进入Today文件夹
     * @return
     */
    private boolean isOrderHistory(String names[], boolean isToday) {
        if (isToday) {
            solo.clickOnText(caseUtil.getTextByRId("today"));
            solo.sleep(Res.integer.time_wait);
        } else {
            uiUtil.enterSideBar(true);
            solo.sleep(Res.integer.time_wait);
            String title = ((TextView) caseUtil.getViewByIndex(solo.getView("id/path_bar"), new int[] {
                    0, 0, 1 })).getText().toString();
            while (!title.equals(caseUtil.getTextByRId("tab_history_sub_title"))) {
                solo.clickOnView(solo.getView("id/back_icon"));
                solo.sleep(Res.integer.time_wait);
                title = ((TextView) caseUtil.getViewByIndex(solo.getView("id/path_bar"), new int[] {
                        0, 0, 1 })).getText().toString();
            }
        }
        ViewGroup viewGroup = (ViewGroup) solo.getView("id/list");
        // Today 检查全部的list,history检查除了Today的list
        int length = isToday ? viewGroup.getChildCount() : viewGroup.getChildCount() - 1;
        for (int i = 0; i < length; ++i) {
            View parent = viewGroup.getChildAt(i);
            TextView textview = (TextView) caseUtil.getViewByIndex(parent, new int[] { 2, 0 });
            if (!textview.getText().toString().equals(names[i])) {
                return false;
            }
        }
        return true;
    }
}