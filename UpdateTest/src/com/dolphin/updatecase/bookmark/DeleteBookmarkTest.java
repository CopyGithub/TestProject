package com.dolphin.updatecase.bookmark;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_003")
@TestClass("Bookmark>删除操作")
public class DeleteBookmarkTest extends BaseTest {

    public void testDeleteBookmark() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(true);
        //进入bookmark
        solo.clickOnView(solo.getView("id/more"));
        assertTrue("'Amazon'存在于书签列表中，未被成功删除", !caseUtil.searchText("Amazon", -1, true, true, true));
    }
}
