package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_055")
@TestClass("设置（改变默认）_声纳")
public class SonarChangeTest extends BaseTest {

    public void testSonarChangeTest() {
        uiUtil.skipWelcome();
        uiUtil.enterGestureAndSonarSettings(true);

        // 验证：摇一摇开关被关闭
        assertTrue("sonar摇一摇开关为关闭",
                !uiUtil.isCheckBoxChecked("shake_to_open_vg", 1, new int[] { 1 }));
    }
}
