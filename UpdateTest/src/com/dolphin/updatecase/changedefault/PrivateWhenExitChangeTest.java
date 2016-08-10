package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_057")
@TestClass("设置（改变默认）_隐私")
public class PrivateWhenExitChangeTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testPrivateWhenExitChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证 退出时清除数据
        caseUtil.clickOnText("pref_category_exit", 0, true);
        solo.sleep(Res.integer.time_wait);

        // 验证：退出时清除Cache选项处于开启
        uiUtil.assertSearchText("pref_clear_cache_checked", null, -1, true, true, 2, path);
        // 验证：退出时清除History选项处于开启
        uiUtil.assertSearchText("pref_clear_history_checked", null, -1, true, true, 2, path);
        // 验证：退出时清除Cookies选项处于开启
        uiUtil.assertSearchText("pref_clear_cookie_when_exit", null, -1, true, true, 2, path);
    }
}
