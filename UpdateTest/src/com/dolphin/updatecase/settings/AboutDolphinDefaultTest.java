package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_034")
@TestClass("设置（默认）_About Dolphin（App info，Subscribe Dolphin newsletter）")
public class AboutDolphinDefaultTest extends BaseTest {

    public void testAboutDolphinDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证：About Dolphin
        solo.clickOnText(caseUtil.getTextByRId("pref_category_help"), 0, true);
        solo.sleep(Res.integer.time_wait);
        // 验证：UX improvement program 关闭
        solo.clickOnText(caseUtil.getTextByRId("pref_ux_improvement"), 0, true);
        uiUtil.assertSearchText("normal_data_track_title", null, -1, true, false, 1,
                new int[] { 1 });
    }
}
