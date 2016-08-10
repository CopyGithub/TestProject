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
 * 脚本编号：DOLINT-1434
 * <p>
 * 脚本描述：验证Most visited列表是在每次启动10s计算一次
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1434")
@TestClass("验证Most visited列表是在每次启动10s计算一次")
public class CountMostVisited10secWhenFirstStartTest extends BaseTest {
    private String[] urls = { Res.string.url_one, "www.amazon.com", "webapps.dolphin.com/int/" };
    private String[] name = { "Web Store", "Amazon.com", "one" };

    public void test1mostvisitedIsEmpty() {
        uiUtil.skipWelcome();
        // 首次安装Dolphin并启动
        // 访问网页a,b,c
        solo.sleep(10 * 1000);// 避免访问完第一个网页后, 刚好触发10秒的most visited更新
        uiUtil.visitURL(urls);
        // 验证：产生历史记录c,b,a
        assertTrue("The history should be " + name[0] + " " + name[1] + " " + name[2],
                checkHistory());
        // 验证： 进入Most visited列表中，列表中数据为空(这里客户端进行了更改,已经和shell
        // Test沟通,之后会修改Testlink)
        assertTrue("The Most Visited folder is not empty", checkMostVisited(null, false, true));
        exitMostVisit();
    }

    public void test2CountMostVisited10secWhenFirstStartTest() {
        uiUtil.skipWelcome();
        // 验证：启动Dolphin等待10s进入Most visited列表中,列表中显示记录c,b,a
        solo.sleep(1000 * 10);
        assertTrue("The most visited should be " + name[0] + " " + name[1] + " " + name[2],
                checkMostVisited(name, true, false));
        // 将History中的记录c删除→进入Most visited列表中
        deletedHistory(0);
        // 验证： Most visited列表有变化,删除了第一个
        assertTrue("The most visited should be  " + name[1] + " " + name[2],
                checkMostVisited(removeElement(name, 0), false, false));
        // 退出Dolphin
        exitMostVisit();
    }

    public void test3DisappearedMostVisited() {
        uiUtil.skipWelcome();
        // 退出Dolphin→启动Dolphin等待10s进入Most visited列表中
        solo.sleep(10 * 1000);
        /**
         * 验证：Most visited列表中的记录c消失
         */
        assertFalse(name[0] + "does not disappeared. ", solo.searchText(name[0]));
    }

    private String[] removeElement(String[] origin, int index) {
        String[] result = new String[origin.length - 1];
        int flag = 0;
        for (int i = 0; i < result.length; i++) {
            if (i == index) {
                flag = 1;
            }
            result[i] = origin[i + flag];
        }
        return result;
    }

    private void deletedHistory(int index) {
        solo.clickOnView(solo.getView("id/back_icon"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("history"));
        solo.sleep(Res.integer.time_wait);
        caseUtil.longClickAndClickPopIndex(caseUtil.getViewByIndex("id/list", index), 3);
    }

    private void exitMostVisit() {
        solo.clickOnView(solo.getView("id/back_icon"));
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
    }

    private boolean checkMostVisited(String[] checkList, boolean isFirst, boolean isEmpty) {
        solo.sleep(Res.integer.time_wait);
        if (isFirst) {
            uiUtil.enterSideBar(true);
            solo.sleep(Res.integer.time_wait);
        } else {
            caseUtil.clickOnView("back_icon");
            solo.sleep(Res.integer.time_wait);
        }
        caseUtil.clickOnText("most_visit_folder_name", 0, true);
        solo.sleep(Res.integer.time_wait);
        if (isEmpty) {
            return caseUtil.searchText("empty_most_visited_list", 0, true, true, false);
        } else {
            return checkList(checkList);
        }
    }

    private boolean checkHistory() {
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("history"));
        solo.sleep(Res.integer.time_wait);
        return checkList(name);
    }

    private boolean checkList(String[] checkList) {
        ViewGroup viewGroup = (ViewGroup) solo.getView("list");
        if (viewGroup.getChildCount() != checkList.length) {
            return false;
        }
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            View parent = viewGroup.getChildAt(i);
            TextView textview = (TextView) caseUtil.getViewByIndex(parent, new int[] { 2, 0 });
            if (!textview.getText().toString().equals(checkList[i])) {
                return false;
            }
        }
        return true;
    }
}