package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_019")
@TestClass("右侧边栏(默认)_无图模式")
public class NoImageDefaultTest extends BaseTest {
    public void testNoImageDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);
        assertTrue("无图模式未保持关闭", !uiUtil.getControlPanelView(5).isSelected());
    }
}
