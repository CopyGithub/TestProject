package com.dolphin.testcase.sidebar;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1320
 * <p>
 * 脚本描述：Sidebar开关关闭时,中屏机上Sidebar操作正常
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1320")
@TestClass("Sidebar开关关闭时,中屏机上Sidebar操作正常")
public class CheckSidebarCloseTest extends BaseTest {
    public void testCheckSidebarClose() {
        uiUtil.skipWelcome();
        // 非中屏机直接返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        // 预置条件：开关状态为关
        uiUtil.enableSwipeSideBar(false);
        // 验证：横/竖屏从屏幕左边缘向右滑(或从屏幕右边缘向左滑)，均无反应
        // 验证：竖屏:书签和Control Panel图标分别位于地址栏左、右两侧
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_wait);
        uiUtil.isSidebarIconShow(true, false);
        isChanged();
        // 验证：横屏:书签图标位于"←"图标左侧,Control Panel图标位于Gesture图标右侧
        // solo.setActivityOrientation(Solo.LANDSCAPE);
        // solo.sleep(Res.integer.time_wait);
        // uiUtil.isSidebarIconShow(true, true);
        // isChanged();
        //
        // solo.setActivityOrientation(Solo.PORTRAIT);
        // solo.sleep(Res.integer.time_wait);
        // 验证：左侧边栏展开
        checkLeftSide();
        // 验证：左侧边栏关闭
        checkClose(true);

        // 验证：右侧边栏展开
        checkRightSide();
        // 验证：右侧边栏关闭
        checkClose(false);

    }

    private void checkClose(boolean isLeft) {
        /**
         * ①侧边栏中手指从右向左滑 <br>
         * ②再次点击书签(Control Panel)图标 <br>
         * ③点击主屏区域 <br>
         * ④点击手机back键 <br>
         * TODO
         */
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        if (isLeft) {
            View leftView = caseUtil.getViewByIndex(solo.getView("content"), new int[] { 0, 1 });
            assertTrue("Still in Left side...", leftView.getVisibility() == View.INVISIBLE);
        } else {
            View rightView = caseUtil.getViewByIndex(solo.getView("content"), new int[] { 0, 2 });
            assertTrue("Still in Right side...", rightView.getVisibility() == View.INVISIBLE);
        }

    }

    private void checkRightSide() {
        /**
         * 从上至下依次为: <br>
         * ·标题:CONTROL PANEl <br>
         * ·Modes区域 <br>
         * ·标题:ADD-ONS <br>
         * ·插件列表 <br>
         */
        View rightIcon = solo.getView("id/right_sidebar");
        solo.clickOnView(rightIcon);
        solo.sleep(Res.integer.time_wait);
        View rightView = caseUtil.getViewByIndex(solo.getView("content"), new int[] { 0, 2 });
        assertTrue("Not into Right side...", rightView.getVisibility() == View.VISIBLE);

        ArrayList<View> rightViews = new ArrayList<View>();
        View title = caseUtil.getViewByIndex(solo.getView("content"), new int[] { 0, 2, 0, 0 });
        View modes = caseUtil.getViewByIndex("list_installed_plugin", 0);
        View addOnTitle = caseUtil.getViewByIndex("list_installed_plugin", 1);
        View addOnList = caseUtil.getViewByIndex("list_installed_plugin", 2);
        assertTrue("Title 'CONTROL PANEl' is not right...", ((TextView) title).getText().toString()
                .equals(caseUtil.getTextByRId("ctrl_pl_header_title")));
        rightViews.add(title);
        rightViews.add(modes);
        rightViews.add(addOnTitle);
        rightViews.add(addOnList);
        utils.ubietyOfViews(rightViews, 1, false, false, false);
    }

    private void checkLeftSide() {
        /**
         * 从上至下依次显示: <br>
         * ·Dolphin Connect登录入口 <br>
         * ·BOOKMARKS列表 <br>
         * ·底部为"Add bookmark"按钮 <br>
         */
        View leftIcon = solo.getView("id/left_sidebar");
        solo.clickOnView(leftIcon);
        solo.sleep(Res.integer.time_wait);
        View leftView = caseUtil.getViewByIndex(solo.getView("content"), new int[] { 0, 1 });
        assertTrue("Not into Left side...", leftView.getVisibility() == View.VISIBLE);

        ArrayList<View> leftViews = new ArrayList<View>();
        View connect = solo.getView("bookmark_header");
        View bookmark = solo.getView("list");
        View addBtn = solo.getView("add_bookmark_view");
        leftViews.add(connect);
        leftViews.add(bookmark);
        leftViews.add(addBtn);
        utils.ubietyOfViews(leftViews, 1, false, false, false);
    }

    private void isChanged() {
        int size = solo.getViews().size();
        caseUtil.slideDireciton(null, true, 0.01f, 1f);
        solo.sleep(Res.integer.time_wait);
        assertTrue("There shouldn't be any change...", solo.getViews().size() == size);
    }
}