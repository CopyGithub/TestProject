package com.dolphin.testcase.tabmanagement;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

/**
 * 脚本编号: DOLINT-218:
 * <p>
 * 脚本描述：验证正常添加/切换多个tab
 * 
 * @author sylu
 * 
 */

public class CheckTabbarSwitchTest extends BaseTest {

    public void testCheckTabbarSwitch() {
        uiUtil.skipWelcome();

        // 新建20个tab,验证新建之后tab的数量
        checkNumofTab();
        // 滑动tab,验证tab滑动正常
        checkDragOfTab();
        // 切换不同的tab,验证tab能正常切换
        checkSwitchOfTab();
    }

    private void checkSwitchOfTab() {
        View parentView = solo.getView("top_container");
        int[] a = { 0, 0, 0, 0 };
        View tabView = caseUtil.getViewByIndex(parentView, a);
        int[] c = { 18, 1 };
        int[] e = { 18 };

        View newview = caseUtil.getViewByIndex(tabView, e);
        View newtabView = caseUtil.getViewByIndex(tabView, c);
        TextView newtabTextView = (TextView) newtabView;
        solo.clickOnView(newtabTextView);

        boolean flag = false;
        if (newview.isSelected()) {
            flag = true;
        }
        assertTrue("the tab can not be switched", flag);
    }

    private void checkDragOfTab() {
        // 滑动上方的tab,todo
        View view = caseUtil.getViewByIndex(solo.getView("id/top_container"), new int[] { 0, 0, 0 });
        caseUtil.slideDireciton(view, true, 1 / 10f, 1f);

        // 获得tab高亮的view
        View parentView = solo.getView("top_container");
        int[] a = { 0, 0, 0, 0 };
        View tabView = caseUtil.getViewByIndex(parentView, a);
        int[] b = { 20 };
        View highView = caseUtil.getViewByIndex(tabView, b);
        boolean flag = true;
        if (caseUtil.isInsideDisplay(highView, false)) {
            flag = false;
        }
        assertTrue("the tab can not be draged", flag);
    }

    private void checkNumofTab() {
        int num = 20;
        uiUtil.addNewTab(num);
        solo.sleep(Res.integer.time_wait);
        View parentView = solo.getView("top_container");
        int[] a = { 0, 0, 0, 0 };
        View tabView = caseUtil.getViewByIndex(parentView, a);
        ViewGroup viewGroup = (ViewGroup) tabView;
        int numOftab = viewGroup.getChildCount();
        boolean flag = false;
        if (numOftab == num + 1) {
            flag = true;
        }
        assertTrue("the number of tab is not right", flag);
    }
}
