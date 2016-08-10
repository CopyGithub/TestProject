package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_018")
@TestClass("右侧边栏(默认)_隐私模式")
public class IncognitoDefaultTest extends BaseTest {
    public void testIncognitoDefault() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);
        assertTrue("隐私模式未保持关闭", !uiUtil.getControlPanelView(4).isSelected());
    }
}
