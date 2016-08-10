package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_030")
@TestClass("设置（默认）_隐私")
public class PrivateTest extends BaseTest {
    public void testPrivate() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);

        // 验证：Show security warnings处于开启状态
        uiUtil.assertSearchText("pref_security_show_security_warning",
                caseUtil.getTextByRId("pref_security_show_security_warning_summary"), -1, true,
                true, 2, new int[] { 1, 0 });
    }
}
