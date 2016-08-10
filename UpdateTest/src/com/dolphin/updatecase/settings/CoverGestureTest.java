package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_024")
@TestClass("设置（默认）_覆盖一个手势")
public class CoverGestureTest extends BaseTest {
    public void testCoverGesture() {
        uiUtil.skipWelcome();

        // 验证：预置手势（New Tab，由N成star）
        uiUtil.enterGestureAndSonar(false);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.sleep(Res.integer.time_wait);
        assertTrue("New tab未增加,当前tab数目是" + uiUtil.getTabNumber(), uiUtil.getTabNumber() == 2);
    }
}
