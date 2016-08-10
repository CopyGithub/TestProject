package com.dolphin.updatecase.bookmark;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_005")
@TestClass("Bookmark_MostVisited")
public class MostVisitedTest extends BaseTest {
    public void testMostVisited() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(true);
        caseUtil.clickOnText("Most Visited", -1, true);
        solo.sleep(Res.integer.time_wait);
        checkList("Most Visited");
    }

    private void checkList(String folder) {
        String[] strings = { "hao123", "网易", "Yahoo", "百度一下", "Google" };
        for (int i = 0; i < strings.length; i++) {
            assertTrue("没有在" + folder + "中正确找到历史记录'" + strings[i] + "'",
                    caseUtil.searchText(strings[i], -1, false, true, true));
        }
    }
}
