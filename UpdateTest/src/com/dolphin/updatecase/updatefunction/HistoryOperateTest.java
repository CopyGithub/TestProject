package com.dolphin.updatecase.updatefunction;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_036")
@TestClass("升级后功能检查_历史记录操作")
public class HistoryOperateTest extends BaseTest {
    public void testHistory() {
        uiUtil.skipWelcome();
        uiUtil.enterBookmark();

        caseUtil.clickOnText("History", -1, true);
        // 新的文件夹，按日期
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(caseUtil.getViewByIndex(solo.getView("list"), new int[] { 0 }));
        solo.sleep(Res.integer.time_wait);
        caseUtil.longClickAndClickPopIndex(caseUtil.getViewByText("百度一下", -1, true, true, true), 2);
        assertTrue("'百度一下' 未被成功 删除", !caseUtil.searchText("百度一下", -1, false, true, true));
        solo.sleep(Res.integer.time_wait);

        // 验证：网易正常加载
        solo.clickOnText("手机网易网");
        assertTrue("Failed to enter 网易 website.", uiUtil.checkURL("163.com"));

        // Most Visited部分
        uiUtil.enterBookmark();
        caseUtil.clickOnText("Most Visited", -1, true);
        solo.sleep(Res.integer.time_wait);

        // 验证：hao123被成功删除
        caseUtil.longClickAndClickPopIndex(caseUtil.getViewByText("hao123", -1, false, true, true),
                2);
        assertTrue("hao123 未被成功 删除", !caseUtil.searchText("hao123", -1, false, true, true));
    }
}
