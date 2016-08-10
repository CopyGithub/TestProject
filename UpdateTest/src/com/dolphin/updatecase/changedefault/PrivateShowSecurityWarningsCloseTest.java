package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_059")
@TestClass("设置（改变默认）_隐私")
public class PrivateShowSecurityWarningsCloseTest extends BaseTest {

    public void testPrivateShowSecurityWarningsClose() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);

        // 验证：Show security warnings处于关闭
        uiUtil.assertSearchText("pref_security_show_security_warning",
                caseUtil.getTextByRId("pref_security_show_security_warning_summary"), -1, true,
                false, 2, new int[] { 1, 0 });
    }
}
