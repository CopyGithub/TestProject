package com.dolphin.testcase.bookmark;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1501
 * <p>
 * 脚本描述：验证Sort项UI
 * 
 * @author sylu
 * 
 */
@TestNumber(value = "DOLINT-1501")
@TestClass("验证Sort项UI")
public class CheckSortUITest extends BaseTest {
    public void testCheckSortUITest() {
        uiUtil.skipWelcome();

        // 验证滑出左侧边栏，点击edit按钮
        checkBookmarkUI();
        // 点击三点菜单,Sort选项显示Import/Export上面的第一项
        checkSortUI();
        // 点击Sort选项 ,弹出Sort弹框，显示为：By frequency By date create By name,依次显示
        checkClickSortUI();

    }

    private void checkClickSortUI() {
        // 点击sort操作
        View bodyView = solo.getView("body");
        TextView sortView = (TextView) caseUtil.getViewByIndex(bodyView, new int[] { 4, 0, 0, 0 });
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(sortView);
        solo.sleep(4 * 1000);
        // 验证Sort弹框，依次显示为：By frequency By date create By name
        boolean flag1 = solo.searchText(caseUtil.getTextByRId("bm_sort_by_visit_count"));
        boolean flag2 = solo.searchText(caseUtil.getTextByRId("bm_sort_by_date"));
        boolean flag3 = solo.searchText(caseUtil.getTextByRId("bm_sort_by_title"));
        boolean flag = false;
        // 弹出Sort弹框，显示为By frequencyBy date create By name
        if (flag1 && flag2 && flag3) {
            flag = true;
        }
        assertTrue("The location of sortlist is not correct", flag);
        // 位置关系：依次显示
        ArrayList<View> views = new ArrayList<View>();
        View by1 = caseUtil.getViewByText("bm_sort_by_visit_count", 0, true, true, true);
        View by2 = caseUtil.getViewByText("bm_sort_by_date", 0, true, true, true);
        View by3 = caseUtil.getViewByText("bm_sort_by_title", 0, true, true, true);
        views.add(by1);
        views.add(by2);
        views.add(by3);
        utils.ubietyOfViews(views, 1, false, false, false);
    }

    private void checkSortUI() {
        // 点击三点按钮
        View btnmoreView = solo.getView("btn_more");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(btnmoreView);
        solo.sleep(Res.integer.time_wait);
        // 验证sort，import/export的存在，并且证明他们的位置关系
        View bodyView = solo.getView("body");
        TextView sortView = (TextView) caseUtil.getViewByIndex(bodyView, new int[] { 4, 0, 0, 0 });
        TextView importView = (TextView) caseUtil
                .getViewByIndex(bodyView, new int[] { 4, 0, 1, 0 });
        boolean flag = false;
        boolean flag1 = sortView.getText().toString().equals(caseUtil.getTextByRId("bm_sort"));
        boolean flag2 = importView.getText().toString().equals(caseUtil.getTextByRId("bm_import"));
        boolean flag3 = utils.ubietyOfView(sortView, importView, false, false, false) != -1;
        if (flag1 && flag2 && flag3) {
            flag = true;
        }
        assertTrue("The location of sort and import is not right", flag);

    }

    private void checkBookmarkUI() {
        // 滑动左侧边栏
        uiUtil.enterBookmark();
        // 点击edit按钮
        View editView = solo.getView("more");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(editView);
        solo.sleep(Res.integer.time_wait);
        TextView bookmarkView = (TextView) solo.getView("bookmark_path");
        boolean flag = false;
        if (bookmarkView.getText().toString().equals(caseUtil.getTextByRId("tab_bookmarks"))) {
            flag = true;
        }
        assertTrue("Can not enter into bookmark page", flag);
    }
}
