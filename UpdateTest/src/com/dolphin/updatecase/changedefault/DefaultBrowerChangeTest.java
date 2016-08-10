package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_054")
@TestClass("设置（改变默认）_默认的浏览器")
public class DefaultBrowerChangeTest extends BaseTest {

    public void testDefaultBrowerChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证： 默认浏览器 保持 打开
        uiUtil.assertSearchText("pref_content_set_as_default_browser", null, -1, true, true, 2,
                new int[] { 1, 0 });
    }
}
