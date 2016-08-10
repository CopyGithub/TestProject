package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_025")
@TestClass("设置（默认）_删除一个手势")
public class DeleteGestureTest extends BaseTest {
    public void testDeleteGesture() {
        uiUtil.skipWelcome();

        // 验证：手势列表里没有Exit browser这个手势
        uiUtil.enterGestureAndSonarSettings(false);
        solo.clickOnText(caseUtil.getTextByRId("gesture_more_action"), 0, true);
        assertTrue("手势列表里出现Exit browser这个手势", !solo.searchText("Exitbrowser"));
    }
}
