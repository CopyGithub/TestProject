package com.dolphin.updatecase.settings;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_026")
@TestClass("设置（默认）_声纳摇摇开关")
public class CheckSonarShakeTest extends BaseTest {
    public void testCheckSonarShake() {
        uiUtil.skipWelcome();
        enterSonarSetttings();
        assertTrue("sonar摇一摇开关为关闭",
                !uiUtil.isCheckBoxChecked("shake_to_open_vg", 1, new int[] { 1 }));
    }

    private void enterSonarSetttings() {
        uiUtil.enterSetting(false);
        solo.clickOnText(caseUtil.getTextByRId("pref_category_gesture_voice_settings", -1));
        solo.sleep(Res.integer.time_wait);
        View sonarTab = caseUtil.getViewByIndex(solo.getView("sub_title_view"),
                new int[] { 1, 0, 0 });
        solo.clickOnView(sonarTab);
        solo.sleep(Res.integer.time_wait);
    }
}
