
package com.dolphin.testcase.gesture;

import android.view.View;

import com.adolphin.common.BaseTest;

import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-584
 * <p>
 * 脚本描述：中屏机:竖屏:缓慢滑动
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-584")
@TestClass("验证中屏机竖屏下,缓慢滑动主页海豚键进入Gesture界面")
public class CheckSlidingSlowTest extends BaseTest {
    public void testCheckSlidingSlow() {
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        uiUtil.skipWelcome();
        init();
        View dolphinIcon = caseUtil.getViewByIndex(solo.getView("bottom_container"), new int[] { 0, 3,
                0 });
        int[] dolphinLoc = new int[2];
        dolphinIcon.getLocationOnScreen(dolphinLoc);
        solo.drag(dolphinLoc[0], dolphinLoc[0] / 2, dolphinLoc[1], dolphinLoc[1] * 3 / 4, 50);
        // 验证：滑动过程中闪现Gesture图标(TODO),之后进入"DRAW A GESTURE"界面
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入DRAW A GESTURE界面",
                solo.searchText(caseUtil.getTextByRId("gesture_pad_tips", -1)));
    }

    private void init() {
        // 若存在Enable Swipe For Sidebars,点击Cancel
        uiUtil.closeSidebarTips();
    }
}