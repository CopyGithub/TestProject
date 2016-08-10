package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_028")
@TestClass("设置（默认）_隐私")
public class PrivateWhenExitDefaultTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testPrivateWhenExitDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证 退出时清除数据
        caseUtil.clickOnText("pref_category_exit", 0, true);
        // 验证：退出时清除Cache选项处于关闭状态
        uiUtil.assertSearchText("pref_clear_cache_checked", null, -1, true, false, 2, path);
        // 验证：退出时清除History选项处于关闭状态
        uiUtil.assertSearchText("pref_clear_history_checked", null, -1, true, false, 2, path);
        // 验证：退出时清除Cookies选项处于关闭状态
        uiUtil.assertSearchText("pref_clear_cookie_when_exit", null, -1, true, false, 2, path);
    }
}
