package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_060")
@TestClass("设置（改变默认）_个人数据")
public class PersonalDataChangeTest extends BaseTest {

    public void testPersonalDataChange() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        solo.clickOnText(caseUtil.getTextByRId("download_setting"), 0, true);
        // 验证：Always ask before downloading files关闭
        uiUtil.assertSearchText("download_save_path_prompt", null, -1, true, false, 2, new int[] {
                1, 0 });
    }
}
