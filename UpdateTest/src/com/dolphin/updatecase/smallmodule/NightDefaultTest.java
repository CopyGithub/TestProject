package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_020")
@TestClass("右侧边栏(默认)_夜间模式")
public class NightDefaultTest extends BaseTest {
    public void testNightDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);
        assertTrue("夜间模式未保持关闭", !uiUtil.getControlPanelView(2).isSelected());
    }
}
