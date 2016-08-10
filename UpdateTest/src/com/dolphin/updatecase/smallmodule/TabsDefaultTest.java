package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_017")
@TestClass("右侧边栏(默认)_经典模式")
public class TabsDefaultTest extends BaseTest {
    public void testTabsDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);
        assertTrue("经典模式未保持打开", uiUtil.getControlPanelView(1).isSelected());
    }
}
