package com.dolphin.updatecase.bookmark;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_004")
@TestClass("Bookmark_History")
public class AddHistoryTest extends BaseTest {

    public void testAddHistory() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(true);
        caseUtil.clickOnText("History", -1, true);
        solo.sleep(Res.integer.time_wait);
        // 若存在 Last Month或Last 7 days等文件夹
        View folderIcon = caseUtil.getView(solo.getView("list"), "indicator");
        if (folderIcon != null) {
            solo.clickOnView(folderIcon);
            solo.sleep(Res.integer.time_wait);
        }
        checkList("History");

    }

    private void checkList(String folder) {
        String[] strings = { "hao123", "网易", "Yahoo", "百度一下", "Google" };
        for (int i = 0; i < strings.length; i++) {
            assertTrue("没有在" + folder + "中正确找到历史记录'" + strings[i] + "'",
                    caseUtil.searchText(strings[i], -1, false, true, true));
        }
    }
}
