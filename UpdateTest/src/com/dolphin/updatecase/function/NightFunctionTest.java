package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_065")
@TestClass("升级后功能检查_夜间模式")
public class NightFunctionTest extends BaseTest {

    public void testNightFunction() {
        uiUtil.skipWelcome();

        // 如果夜间模式未打开，打开夜间模式，考虑到其他版本的升级
        uiUtil.clickOnControlPanel(true, 2);

        // 验证：右侧边栏夜间模式处于开启状态
        uiUtil.enterSideBar(false);
        assertTrue("夜间模式未保持打开", uiUtil.getControlPanelView(2).isSelected());
        // 验证：页面处于夜间模式 TODO

        // 关闭夜间模式
        solo.clickOnView(uiUtil.getControlPanelView(2));
        // 验证：弹出toast提示:Night Mode is turned off
        assertTrue("Fail to show . Night Mode is turned off",
                solo.searchText("Night Mode is turned off"));
        // 验证：页面处于白天模式 TODO
    }
}
