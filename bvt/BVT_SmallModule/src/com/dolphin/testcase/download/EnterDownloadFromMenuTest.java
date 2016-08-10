package com.dolphin.testcase.download;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-50
 * <p>
 * 脚本描述：Menu中进入
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-50")
@TestClass("Menu中进入")
public class EnterDownloadFromMenuTest extends BaseTest {

    public void testEnterDownloadFromMenuTest() {
        uiUtil.skipWelcome();
        // 启动Dolphin,在Menu中点击Download
        enterFromMenu();
        /**
         * 验证：打开下载管理器界面
         */
        uiUtil.waitForInterfaceByTitle("download_tab_title", 10 * 1000);
    }

    private void enterFromMenu() {
        uiUtil.clickOnMenuItem("action_menu_item_download");
        solo.sleep(Res.integer.time_change_activity);
    }
}