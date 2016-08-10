
package com.dolphin.testcase.bookmark;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1467
 * <p>
 * 脚本描述：验证点击Folder/Bookmarks的操作
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1467")
@TestClass("验证点击Folder/Bookmarks的操作")
public class ClickOnFolderAndBookmarksTest extends BaseTest {
    private String[] urls = { Res.string.url_one, Res.string.url_two, Res.string.url_three, };
    private String[] folders = { "A", "B", "C" };
    private String[] urlnames = { "aa", "Test", "dd" };

    public void testCheckAboutDolphinUITest() {
        uiUtil.skipWelcome();
        // 预置条件：1.书签列表中已经存在书签aa，文件夹A(A中有书签Test和文件夹B(B中有文件夹C和书签dd))
        prepareFolderAndBookmarks();
        // 进入Bookmark列表→点击文件夹A
        uiUtil.enterBookmark();
        // 验证：显示A中的内容：文件夹B，书签Test
        assertCheckFolderContent("A", "B", "Test");
        // 验证： 点击书签Test, 进入Test对应的网页
        assertTrue("Failed to open the bookmark Test's website.",
                checkClickOnBookmark("Test", urls[1]));
        // 重复步骤1→点击文件夹B
        uiUtil.enterSideBar(true);
        // 验证：显示B中的内容：文件夹C和书签dd
        assertCheckFolderContent("B", "C", "dd");
        // 验证：点击文件夹C,显示Bookmark list is empty
        assertTrue("Failed to show the prompt of 'Bookmark list is empty' ", checkEmptyFolder("C"));
        // 点击文件夹C左侧的返回图标→点击B左侧的返回图标→点击A左侧的返回图标
        // 验证： 返回至BOOKMARKS列表
        assertTrue("Failed to back to 'BOOKMARKS'", clickOnbackIconToBOOKMARKS());
        // 验证：点击书签aa 进入书签aa对应的网页
        assertTrue("Failed to open the bookmark aa's website.", checkClickOnBookmark("aa", urls[0]));
    }

    private boolean clickOnbackIconToBOOKMARKS() {
        caseUtil.clickOnView("back_icon");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("back_icon");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("back_icon");
        solo.sleep(Res.integer.time_wait);
        return solo.searchText(caseUtil.getTextByRId("tab_bookmarks"));
    }

    private boolean checkEmptyFolder(String folder) {
        solo.clickOnText(folder);
        solo.sleep(Res.integer.time_wait);
        return solo.searchText(caseUtil.getTextByRId("empty_bookmark_list"), true);
    }

    private boolean checkClickOnBookmark(String bookmark, String url) {
        solo.clickOnText(bookmark);
        assertTrue("Network is bad.", uiUtil.waitForWebPageFinished());
        return uiUtil.checkURL(url);
    }

    private void assertCheckFolderContent(String parentolder, String childFolder,
            String childBookmark) {
        solo.clickOnView(uiUtil.getListItemTitle("list", parentolder, new int[] { 2, 0 }));
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to find folder " + childFolder, solo.searchText(childFolder));
        assertTrue("Failed to find folder " + childBookmark, solo.searchText(childBookmark));
    }

    private void prepareFolderAndBookmarks() {
        uiUtil.addFoldersFromRoot(folders, true);
        uiUtil.addBookmark(urls[0], urlnames[0], caseUtil.getTextByRId("bookmarks"));
        for (int i = 1; i < 3; ++i) {
            uiUtil.addBookmark(urls[i], urlnames[i], folders[i - 1]);
        }
    }

}