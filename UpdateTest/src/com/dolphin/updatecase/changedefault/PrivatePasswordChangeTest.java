package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_058")
@TestClass("设置（改变默认）_隐私")
public class PrivatePasswordChangeTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testPrivatePasswordChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);

        caseUtil.clickOnText("pref_privacy_settings", 0, true);

        // 验证：Remember password开关处于打开状态
        uiUtil.assertSearchText("pref_security_remember_passwords", null, -1, true, true, 2, path);
        // 验证：表单数据开关处于关闭
        uiUtil.assertSearchText("pref_security_save_form_data", null, -1, true, false, 2, path);
        // 验证：位置信息开关处于关闭
        uiUtil.assertSearchText("pref_privacy_enable_geolocation", null, -1, true, false, 2, path);
        // 验证：Use master password处于开启
        uiUtil.assertSearchText("pref_security_use_master_key",
                caseUtil.getTextByRId("pref_security_use_master_key_summary"), -1, true, true, 2,
                path);
    }
}
