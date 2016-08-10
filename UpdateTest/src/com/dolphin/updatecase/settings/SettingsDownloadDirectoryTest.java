package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_031")
@TestClass("设置（默认）_下载")
public class SettingsDownloadDirectoryTest extends BaseTest {

    public void testSettingsDownloadDirectory() {
        // 下载路径
        final String defaultDownloadPath = utils.externalStorageDirectory
                + "/download/DownloadTest";
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        solo.clickOnText(caseUtil.getTextByRId("download_setting"), 0, true);
        solo.sleep(Res.integer.time_wait);

        // 验证：Download
        // directory路径跟升级前保持一致:路径为/storage/emulated/0/download/DownloadTest
        uiUtil.assertSearchText("download_default_path", defaultDownloadPath, -1, false, false, 0,
                null);
    }
}
