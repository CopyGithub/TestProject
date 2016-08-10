package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_013")
@TestClass("主题_切换壁纸")
public class ChangeThemeTest extends BaseTest {

    public void testChangeTheme() {
        uiUtil.skipWelcome();
        uiUtil.clickOnMenuItem("themes");
        assertTrue("壁纸未变回为默认壁纸", uiUtil.isAppliedTheme("Default"));
    }
}
