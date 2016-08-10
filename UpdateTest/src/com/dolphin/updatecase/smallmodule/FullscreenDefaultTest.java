package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_016")
@TestClass("右侧边栏(默认)_全屏模式")
public class FullscreenDefaultTest extends BaseTest {
    public void testFullscreenDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);
        assertTrue("全屏模式未保持关闭", !uiUtil.getControlPanelView(0).isSelected());
    }
}
