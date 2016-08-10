package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_023")
@TestClass("设置（默认）_手势&声纳：添加一个手势")
public class AddGestureTest extends BaseTest {
    public void testAddGesture() {
        uiUtil.skipWelcome();

        // 验证：手势列表显示自定义的手势（L，对应URL是http://www.baidu.com）
        uiUtil.enterGestureAndSonar(false);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "L");
        solo.sleep(4 * 1000);
        assertTrue("未打开相应网页", uiUtil.checkURL("www.baidu.com"));
    }
}
