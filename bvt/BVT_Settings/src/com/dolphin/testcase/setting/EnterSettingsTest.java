package com.dolphin.testcase.setting;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-922
 * <p>
 * 脚本描述: 验证中屏机正常进入Settings界面(横竖屏)
 * 
 * @author chchen
 * 
 */
@TestNumber(value = "DOLINT-922")
@TestClass("验证中屏机正常进入Settings界面(横竖屏)")
public class EnterSettingsTest extends BaseTest {

    public void testEnterSettings() {
        uiUtil.skipWelcome();
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        assertTrue("竖屏下没有正确进入Settings界面", isEnterSettingsInterface());
        goBackAndLANDSCAPE();
        assertTrue("横屏下没有正确进入Settings界面", isEnterSettingsInterface());
    }

    private void clickMenuSettings() {
        clickDolphinPic();
        caseUtil.clickOnText("action_menu_item_settings", 0, true);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void clickDolphinPic() {
        View view = uiUtil.getMenubarItem(3, 0);
        solo.clickOnView(view);
        solo.sleep(Res.integer.time_change_activity);
    }

    private boolean isEnterSettingsInterface() {
        clickMenuSettings();
        return uiUtil.waitForInterfaceByTitle("menu_preferences", 0);
    }

    private void goBackAndLANDSCAPE() {
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        if (solo.searchText(caseUtil.getTextByRId("got_it", -1))) {
            solo.clickOnText(caseUtil.getTextByRId("got_it", -1));
            solo.sleep(Res.integer.time_wait);
        }
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
    }
}