package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_048")
@TestClass("右侧边栏（改变默认）_经典模式")
public class RightSideBarTabsCloseTest extends BaseTest {

    public void testRightSideBarTabsClose() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);

        assertTrue("经典模式未保持关闭", !uiUtil.getControlPanelView(1).isSelected());
    }
}
