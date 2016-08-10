package com.dolphin.updatecase.bookmark;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_002")
@TestClass("Bookmark>修改操作")
public class ModifyBookmarkTest extends BaseTest {

    public void testModifyBookmark() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(true);
        assertTrue("没有正确找到文件夹B", caseUtil.searchText("B", -1, true, true, true));
        assertTrue("没有正确找到书签'天气预报'", caseUtil.searchText("天气预报", -1, true, true, true));
    }
}
