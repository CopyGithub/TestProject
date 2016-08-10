package com.dolphin.testcase.gesture;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-595
 * <p>
 * 脚本描述: 画正确的手势
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-595")
@TestClass("验证手势正常识别")
public class CheckRightGestureTest extends BaseTest {
    private String urlName = "facebook.com";

    public void testCheckRightGesture() {
        uiUtil.skipWelcome();
        // 预置条件：当前为DRAW A GESTURE界面
        uiUtil.enterGestureAndSonar(false);
        // 画正确的手势（如正确的预置facebook手势:f）
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "F");

        solo.sleep(Res.integer.time_wait);
        // 验证："DRAW A GESTURE"界面关闭,打开网页:facebook.com
        assertTrue("DRAW A GESTURE界面未关闭",
                !caseUtil.searchText("gesture_pad_tips", 0, true, true, false));
        solo.sleep(4 * 1000);
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        assertTrue("未打开相应网页", uiUtil.checkURL(urlName));
    }
}