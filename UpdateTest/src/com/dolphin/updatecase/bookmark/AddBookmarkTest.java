package com.dolphin.updatecase.bookmark;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

import android.view.View;

/**
 * 脚本编号:
 * <p>
 * 脚本描述:Bookmark>添加操作
 * 
 * @author chchen
 *
 */
@TestNumber("test_001")
@TestClass("Bookmark>添加操作")
public class AddBookmarkTest extends BaseTest {

    public void testAddBookmark() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(true);
        View aView = caseUtil.getViewByText("A", -1, true, true, true);
        assertTrue("没有正确找到文件夹A", aView != null);
        solo.clickOnView(aView);
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有正确找到书签'百度一下'", caseUtil.searchText("百度一下", -1, true, true, true));
    }
}
