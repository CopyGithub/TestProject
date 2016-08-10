package com.dolphin.testcase.fullscreen;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1372
 * <p>
 * 脚本描述：验证全屏显示，文案正确
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1372")
@TestClass("验证全屏显示，文案正确")
public class CheckFullScreenShowRightTest extends BaseTest {
    public void testCheckFullScreenShowRightTest() {
        uiUtil.skipWelcome();
        // 安装并启动Dolphin->滑出右侧边栏 -> 观察第一排第一个全屏模式图标
        swipControlPanel();
        /**
         * 验证： 全屏（Fullscreen）图标显示在第一排第一个，默认为未选中状态
         */
        assertTrue("The first item is not Fullscreen .", isFirstFullScreen());
        assertFalse("The default Fullscreen status should be unchecked .",
                solo.getText(caseUtil.getTextByRId("panel_menu_item_title_fullscreen")).isSelected());
    }

    private boolean isFirstFullScreen() {
        return solo.getText(caseUtil.getTextByRId("panel_menu_item_title_fullscreen")).getText()
                .equals(caseUtil.getTextByRId("panel_menu_item_title_fullscreen"));
    }

    private void swipControlPanel() {
        uiUtil.enableSwipeSideBar(true);
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, true, 0.99f, 1f);
    }
}