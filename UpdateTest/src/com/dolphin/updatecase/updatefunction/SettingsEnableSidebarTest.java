package com.dolphin.updatecase.updatefunction;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_042")
@TestClass("升级后功能检查_Settings-enable sidebar")
public class SettingsEnableSidebarTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testSettingsEnableSidebar() {
        uiUtil.skipWelcome();

        // Enable swipe for sidebars升级后默认打开，下一行代码出错
        // 验证：地址栏两侧有sidebar按钮：左侧bookmark按钮，右侧control panel按钮
        View leftBar = caseUtil.getViews(null, "left_sidebar").get(0);
        View rightBar = caseUtil.getViews(null, "right_sidebar").get(0);
        assertTrue("左侧bookmark按钮未显示", leftBar.isShown());
        assertTrue("右侧bookmark按钮未显示", rightBar.isShown());

        // 验证：成功呼出左、右侧边栏 by 按钮
        solo.clickOnView(leftBar);
        solo.goBack();
        solo.clickOnView(rightBar);

        // 验证：Enable swipe for sidebars开关 关闭
        uiUtil.enterSetting(true);
        uiUtil.assertSearchText("pref_enable_swipe_action", null, -1, true, false, 1, path);

        // Enable swipe for sidebars开关打开并验证
        solo.goBack();
        uiUtil.enableSwipeSideBar(true);
        solo.sleep(Res.integer.time_wait);

        // 验证：成功呼出左、右侧边栏 by swipe
        caseUtil.slide(null, 1 / 50f, 1 / 6f, 49 / 50f, 1 / 6f, 1f);
        solo.goBack();
        caseUtil.slide(null, 49 / 50f, 1 / 6f, 1 / 50f, 1 / 6f, 1f);
    }
}
