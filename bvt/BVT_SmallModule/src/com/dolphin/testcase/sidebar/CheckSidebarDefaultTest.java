package com.dolphin.testcase.sidebar;

import com.adolphin.common.BaseTest;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1319
 * <p>
 * 脚本描述：Sidebar开关默认值正常
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1319")
@TestClass("Sidebar开关默认值正常")
@Reinstall
public class CheckSidebarDefaultTest extends BaseTest {
    public void testCheckSidebarDefault() {
        uiUtil.skipWelcome();

        // 验证：地址栏左右左侧显示书签图标,右侧显示Control Panel图标
        uiUtil.isSidebarIconShow(false, false);

        // 验证： "Enable swipe for sidebars"开关为开
        uiUtil.enterSetting(true);
        assertTrue("The default state for 'Enable swipe for sidebars' should be checked...",
                uiUtil.isCheckBoxChecked("pref_enable_swipe_action", 1, new int[] { 1, 0 }));
    }
}