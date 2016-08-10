package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_022")
@TestClass("设置（默认）_默认的浏览器")
public class DefaultBrowerTest extends BaseTest {
    public void testDefaultBrower() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        // 验证： 默认浏览器 保持（关闭）
        uiUtil.assertSearchText("pref_content_set_as_default_browser", null, -1, true, false, 2,
                new int[] { 1, 0 });
    }
}
