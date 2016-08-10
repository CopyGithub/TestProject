package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_053")
@TestClass("设置（改变默认）_Language")
public class SettingsLanguageChangeTest extends BaseTest {

    public void testSettingsLanguageChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证：Language为中文简体
        uiUtil.assertSearchText("pref_language_string", "pref_language_choices", 3, false, false,
                0, null);
    }
}
