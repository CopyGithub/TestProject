
package com.dolphin.testcase.history;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1449
 * <p>
 * 脚本描述：验证Clear Most visited功能正常
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1449")
@TestClass("验证Edit Most visited界面删除所有数据的操作")
public class ClearMostVisitedTest extends BaseTest {
    // ulrs和name是相互对应的
    private String[] urls = { Res.string.url_two, "www.amazon.com", "webapps.dolphin.com/int/", };
    private String[] name = { "Web Store", "Amazon.com", Res.string.testurl_name_2 };
    private boolean flag;

    public void test1Prepare() {
        uiUtil.skipWelcome();
        // 前置条件：Most visited中有数据A和B和C
        uiUtil.visitURL(urls);
    }

    public void testClearMostVisitedTest() {
        uiUtil.skipWelcome();
        // 验证： 点击Most visited,进入Most visited界面
        assertTrue("Failed to enter Most Visited activity .", isEnterMostVisitedLeftSideBar());
        /**
         * 验证： 点击界面下方的Clear most visited data按钮，弹出Clear Most Visited弹框
         */
        clickOnClearBtn(0);
        /**
         * 验证：验证弹框
         * <p>
         * 弹窗标题：“Clear Most Visited data”
         * <p>
         * 内部文案："Are you sure you want to clear all most visited data?"
         * <p>
         * 选项：“Cancel”、“Clear”
         */
        assertTrue("Failed to pop 'Clear most visited data' dialog .", isAlertDialog(true));
        assertTrue(
                "Failed to show the prompt 'Are you sure you want to clear all most visited data?' ",
                solo.searchText(caseUtil.getTextByRId("clear_all_most_visit")));
        assertTrue("Failed to show the 'Cancel' button .",
                solo.searchText(caseUtil.getTextByRId("cancel")));
        assertTrue("Failed to show the 'Clear' button .",
                solo.searchText(caseUtil.getTextByRId("clear")));
        /**
         * 验证： 点击cancel, 弹框消失
         */
        assertFalse("Failed to pop 'Clear most visited data' dialog .", isAlertDialog(false));
        // 再次点击界面下方的Clear Most visited按钮→点击Clear
        clickOnClearBtn(2);
        /**
         * 验证： toast提醒：“Cleared".对应的Most visited全部被清除
         */
        assertTrue("Failed to show the toast 'Cleared' .", flag);
        /**
         * 验证：对应的Most visited全部被清除
         */
        assertFalse("Failed to delete the most visited item " + name[0], solo.searchText(name[0]));
        assertFalse("Failed to delete the most visited item " + name[1], solo.searchText(name[1]));
        assertFalse("Failed to delete the most visited item " + name[2], solo.searchText(name[2]));
        /**
         * 验证：Clear Most Visited按钮消失
         */
        assertFalse("The button 'Clear most visited data' does not disappear. '",
                solo.searchText(caseUtil.getTextByRId("search_bottom_clear_mostvisited"), true));
        /**
         * 验证： 界面中间出现灰色小字“Most visited list is empty"(TODO 界面中间出现灰色小字)
         */
        assertTrue("Failed to show tip 'Most visited list is empty' ",
                solo.searchText(caseUtil.getTextByRId("empty_most_visited_list"), true));
    }

    /**
     * 
     * @param choice
     *            true代表不按cancel，false代表按cancel弹框消失
     * @return
     */
    private boolean isAlertDialog(boolean choice) {
        if (choice) {
            solo.sleep(Res.integer.time_wait);
            View view = solo.getView("id/alertTitle");
            return ((TextView) view).getText().toString()
                    .equals((caseUtil.getTextByRId("search_bottom_clear_mostvisited")));
        } else {
            solo.clickOnText(caseUtil.getTextByRId("cancel"));
            solo.sleep(Res.integer.time_wait);
            return solo.searchText(caseUtil.getTextByRId("clear_all_most_visit"));
        }
    }

    /**
     * 
     * @param choice
     *            0代表不点击，1代表Cancel，2代表Clear
     * @return
     */
    private void clickOnClearBtn(int choice) {
        solo.clickOnText(caseUtil.getTextByRId("search_bottom_clear_mostvisited"));
        solo.sleep(Res.integer.time_wait);
        if (choice == 1) {
            solo.clickOnText(caseUtil.getTextByRId("cancel"));
            solo.sleep(Res.integer.time_wait);
        }
        if (choice == 2) {
            solo.clickOnView(solo.getView("id/button2"));
            flag = solo.searchText(caseUtil.getTextByRId("clear_data_toast"));
        }
    }

    private boolean isEnterMostVisitedLeftSideBar() {
        solo.sleep(10 * 1000);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("most_visit_folder_name"));
        solo.sleep(Res.integer.time_wait);
        String title = ((TextView) caseUtil.getViewByIndex(solo.getView("id/path_bar"), new int[] { 0,
                0, 1 })).getText().toString();
        return title.equals(caseUtil.getTextByRId("most_visit_folder_name"));
    }
}