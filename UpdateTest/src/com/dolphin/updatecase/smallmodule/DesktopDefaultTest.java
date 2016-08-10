package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_015")
@TestClass("右侧边栏(默认)_桌面模式")
public class DesktopDefaultTest extends BaseTest {
    public void testDesktopDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);
        assertTrue("桌面模式未保持关闭", !uiUtil.getControlPanelView(3).isSelected());
    }
}
