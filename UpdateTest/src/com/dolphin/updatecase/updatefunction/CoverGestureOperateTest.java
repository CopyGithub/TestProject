package com.dolphin.updatecase.updatefunction;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_045")
@TestClass("升级后功能检查_覆盖的手势可以使用")
public class CoverGestureOperateTest extends BaseTest {
    public void testGestureOperate() {
        uiUtil.skipWelcome();

        // 验证：覆盖手势可用，star手势，添加new tab
        uiUtil.enterGestureAndSonar(false);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.sleep(Res.integer.time_wait);
        assertTrue("New tab未增加,当前tab数目是" + uiUtil.getTabNumber(), uiUtil.getTabNumber() == 2);
    }
}
