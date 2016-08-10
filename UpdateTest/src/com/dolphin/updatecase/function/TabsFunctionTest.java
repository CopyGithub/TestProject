package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_064")
@TestClass("升级后功能检查_经典模式")
public class TabsFunctionTest extends BaseTest {
    public void testTabsFunction() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);

        // 验证：tab bar被隐藏
        assertTrue("New tab未被隐藏", !solo.searchText("New tab"));

        // 验证：右侧边栏经典模式处于关闭状态
        assertTrue("经典模式未保持关闭", !uiUtil.getControlPanelView(1).isSelected());

        // 打开经典模式
        solo.clickOnView(uiUtil.getControlPanelView(1));

        // 验证：弹出toast提示:Tabs Mode is turned on
        assertTrue("Fail to show . Tabs Mode is turned on",
                solo.searchText("Tabs Mode is turned on"));

        // 验证：tab bar显示出来"
        assertTrue("New tab未显示", solo.searchText("New tab"));
    }
}
