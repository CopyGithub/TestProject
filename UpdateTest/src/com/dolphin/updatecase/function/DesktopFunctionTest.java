package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_066")
@TestClass("升级后功能检查_桌面模式")
public class DesktopFunctionTest extends BaseTest {
    public void testDesktopFunction() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);

        // 验证：右侧边栏桌面模式处于开启状态
        assertTrue("桌面模式未保持打开", uiUtil.getControlPanelView(3).isSelected());
        // 验证：打开百度一下，页面为桌面模式，不能上下移动

        uiUtil.visitUrl("www.baidu.com");
        assertTrue("页面不是桌面模式", !caseUtil.isScroll(null, 1));

        // 关闭桌面模式
        uiUtil.enterSideBar(false);
        solo.clickOnView(uiUtil.getControlPanelView(3));
        // 验证：弹出toast提示:Switch to mobile mode
        assertTrue("Fail to show . Switch to mobile mode", solo.searchText("Switch to mobile mode"));
        // 验证：百度一下页面切换为移动模式
        assertTrue("页面未切换为移动模式", caseUtil.isScroll(null, 1));
    }
}
