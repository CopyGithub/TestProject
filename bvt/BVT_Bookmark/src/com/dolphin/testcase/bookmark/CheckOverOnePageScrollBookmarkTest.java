
package com.dolphin.testcase.bookmark;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1466
 * <p>
 * 脚本描述：验证添加的Bookmark和Folder超过一屏可以滑动查看
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1466")
@TestClass("验证添加的Bookmark和Folder超过一屏可以滑动查看")
public class CheckOverOnePageScrollBookmarkTest extends BaseTest {
    String[] bookmarks = new String[] { Res.string.url_one, Res.string.url_two,
            Res.string.url_three, Res.string.url_four, Res.string.url_five, Res.string.url_sixth };
    String[] folders = new String[] { "folder0", "folder1", "folder2", "folder3", "folder4",
            "fodler6" };

    public void testCheckOverOnePageScrollBookmarkTest() {
        uiUtil.skipWelcome();
        // 预置条件：添加书签/文件夹列表超过一屏,当前在Bookmark列表
        addBookmarks(bookmarks);
        uiUtil.addFoldersFromRoot(folders, false);
        // 可以滑动查看所有的书签/文件夹
        assertTrue("Failed to scroll down to check all bookmarks and folders.",
                isScrollDown(bookmarks, folders));
    }

    private boolean isScrollDown(String[] urls, String[] names) {
        uiUtil.enterBookmark();
        for (int i = names.length - 1; i >= 0; --i) {
            solo.sleep(Res.integer.time_wait);
            if (!solo.searchText(names[i])) {
                return false;
            }
            solo.sleep(Res.integer.time_wait);
        }
        for (int i = urls.length - 1; i >= 0; --i) {
            solo.sleep(Res.integer.time_wait);
            if (!solo.searchText("name" + i)) {
                return false;
            }
            solo.sleep(Res.integer.time_wait);
        }
        return solo.scrollDown();
    }

    private void addBookmarks(String[] urls) {
        for (int i = 0; i < urls.length; ++i) {
            uiUtil.addBookmark(urls[i], "name" + i);
            solo.sleep(Res.integer.time_wait);
        }
    }
}