package com.dolphin.updatecase.updatefunction;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_044")
@TestClass("升级后功能检查-添加的手势可以使用")
public class AddGestureOperateTest extends BaseTest {
    public void testAddGestureOperate() {
        uiUtil.skipWelcome();

        // 验证：添加手势可用，L手势，访问百度网站
        uiUtil.enterGestureAndSonar(false);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "L");
        solo.sleep(4 * 1000);
        assertTrue("未打开相应网页", uiUtil.checkURL("www.baidu.com"));
    }
}
