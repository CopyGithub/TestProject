package com.dolphin.testcase.addto;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1938
 * <p>
 * 脚本描述：验证添加Bookmark弹框直接Add
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1938")
@TestClass("验证添加Bookmark弹框直接Add")
@Reinstall
public class AddToBookmarkTest extends BaseTest {
    private String baiduURL = Res.string.url_one;
    private String baiduName = "one";

    public void testAddToBookmark() {
        uiUtil.skipWelcome();

        // 验证：添加到Bookmark,是否弹框消失，Toast提示Saved to bookmarks.
        uiUtil.addBookmark(baiduURL, baiduName);

        // 验证：是否书签列表的第一项（文件夹列表的最后一项）为新增书签
        assertTrue("The first item in Addressbar is not the new added one",
                uiUtil.isFirstNewBookmark(baiduName));

        // 验证：点击新增书签是否进入新增书签
        assertTrue("Failed to visit the new bookmark",
                uiUtil.isIntoNewBookmark(baiduName, baiduURL, false));

    }

}