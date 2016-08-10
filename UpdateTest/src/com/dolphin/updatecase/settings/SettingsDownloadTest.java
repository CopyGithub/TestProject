package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_032")
@TestClass("设置（默认）_下载")
public class SettingsDownloadTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testSettingsDownload() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        solo.clickOnText(caseUtil.getTextByRId("download_setting"), 0, true);
        solo.sleep(Res.integer.time_wait);

        // 验证：Always ask before downloading files开启
        uiUtil.assertSearchText("download_save_path_prompt", null, -1, true, true, 2, path);
    }
}
