package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_073")
@TestClass("Dolphin connect_改变默认")
public class ConnectChangeTest extends BaseTest {

    public void testConnectChange() {
        uiUtil.skipWelcome();

        // 验证：Dolphin Connect处于登录状态
        uiUtil.enterSetting(false);
        assertTrue("Dolphin Connect未处于登录状态", !solo.searchText("Sign in Dolphin Connect"));

        // 验证：账号的Sync界面里：Sync everythins关闭，tabs、themes、bookmarks不选，其它同步项处于开启状态
        solo.goBack();
        uiUtil.enterSetting(false);
        solo.clickOnView(solo.getView("account_name"));
        caseUtil.clickOnText("sync", 0, true);
        assertTrue(" Sync everythins未关闭",
                !uiUtil.isCheckBoxChecked("sync_everything", 1, new int[] { 0 }));
        String[] string = { "tabs", "bookmarks", "synced_settings", "gesture", "add_on", "history" };
        for (int i = 0; i < 6; i++) {
            if (i < 2) {
                assertTrue(" string[i]选中", !uiUtil.isCheckBoxChecked(string[i], 1, new int[] { 0 }));
            } else {
                assertTrue(" string[i]未选中", uiUtil.isCheckBoxChecked(string[i], 1, new int[] { 0 }));
            }
        }
    }
}
