package com.dolphin.testcase.gesture;

import android.view.View;

import com.adolphin.common.BaseTest;

import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-583
 * <p>
 * 脚本描述：中屏机:竖屏:快速滑动
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-583")
@TestClass("验证中屏机竖屏下,快速滑动海豚键进入Gesture界面")
public class CheckSlidingFastTest extends BaseTest {
    public void testCheckSlidingFast() {
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        uiUtil.skipWelcome();
        init();
        View dolphinIcon = caseUtil.getViewByIndex(solo.getView("bottom_container"), new int[] { 0, 3,
                0 });
        int[] dolphinLoc = new int[2];
        dolphinIcon.getLocationOnScreen(dolphinLoc);
        solo.drag(dolphinLoc[0], dolphinLoc[0] / 2, dolphinLoc[1], dolphinLoc[1] * 3 / 4, 5);
        // 验证：滑动过程中闪现Gesture图标(TODO),之后进入"DRAW A GESTURE"界面
        assertTrue("滑动过程中未闪现Gesture图标", solo.waitForDialogToOpen());
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入DRAW A GESTURE界面",
                caseUtil.searchText("gesture_pad_tips", 0, true, true, false));
    }

    private void init() {
        // 若存在Enable Swipe For Sidebars,点击Cancel
        uiUtil.closeSidebarTips();
    }
}