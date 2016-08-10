package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_062")
@TestClass("设置（改变默认）_About Dolphin（App info）")
public class AboutDolphinChangeTest extends BaseTest {

    public void testAboutDolphinChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证：About Dolphin
        solo.clickOnText(caseUtil.getTextByRId("pref_category_help"), 0, true);
        solo.sleep(Res.integer.time_wait);
        // UX improvement program 打开
        solo.clickOnText(caseUtil.getTextByRId("pref_ux_improvement"), 0, true);
        assertTrue("UX improvement program 未打开",
                uiUtil.isCheckBoxChecked("normal_data_track_title", 1, new int[] { 1 }));
    }
}
