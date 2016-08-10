package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_072")
@TestClass("Dolphin connect_默认")
public class ConnectDefaultTest extends BaseTest {

    public void testConnectDefault() {
        uiUtil.skipWelcome();

        // 验证：Dolphin Connect处于登录状态
        uiUtil.enterSetting(false);
        assertTrue("Dolphin Connect未处于登录状态", !solo.searchText("Sign in Dolphin Connect"));

        // 验证：账号的Sync界面里：Sync everythins开启，其它同步项处于开启状态
        solo.goBack();
        uiUtil.enterSetting(false);
        solo.clickOnView(solo.getView("account_name"));
        caseUtil.clickOnText("sync", 0, true);
        assertTrue(" Sync everythins未打开",
                uiUtil.isCheckBoxChecked("sync_everything", 1, new int[] { 0 }));
        String[] string = { "tabs", "synced_settings", "bookmarks", "gesture", "add_on", "history" };
        for (int i = 0; i < 6; i++) {
            assertTrue(" string[i]未选中", uiUtil.isCheckBoxChecked(string[i], 1, new int[] { 0 }));
        }
    }
}
