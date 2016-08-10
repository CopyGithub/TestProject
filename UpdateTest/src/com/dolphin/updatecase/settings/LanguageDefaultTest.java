package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_021")
@TestClass("设置（默认）_Language")
public class LanguageDefaultTest extends BaseTest {
    public void testLanguageDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        // 验证：Language保持（Auto）
        uiUtil.assertSearchText("pref_language_string", "pref_language_choices", 0, false, false,
                0, null);
    }
}
