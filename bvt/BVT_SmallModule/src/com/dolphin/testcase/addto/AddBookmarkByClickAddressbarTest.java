package com.dolphin.testcase.addto;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1950
 * <p>
 * 脚本描述：验证地址栏处添加书签弹框直接Add
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1950")
@TestClass("验证地址栏处添加书签弹框直接Add")
@Reinstall
public class AddBookmarkByClickAddressbarTest extends BaseTest {
    private String baiduURL = Res.string.url_five;
    private String baiduName = "five";

    public void testAddBookmarkByClickAddressbar() {
        uiUtil.skipWelcome();

        // 添加书签
        addBookmark();

        // 验证：是否弹框消失，Toast提示Saved to bookmarks.
        assertTrue("Toast 'Saved to bookmarks' didn't appear.",
                solo.waitForText(caseUtil.getTextByRId("bookmark_saved")));

        // 验证：是否书签列表的第一项（文件夹列表的最后一项）为新增书签
        assertTrue("The first item in Addressbar is not the new added one",
                uiUtil.isFirstNewBookmark(baiduName));

        // 验证：点击新增书签是否进入新增书签
        assertTrue("Failed to visit the new bookmark",
                uiUtil.isIntoNewBookmark(baiduName, baiduURL, false));
    }

    private void addBookmark() {
        // 打开网页
        uiUtil.visitUrl(baiduURL);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);

        // 长按地址栏→点击Add bookmark→点击Add
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        caseUtil.longClickAndClickPopIndex(solo.getView("id/title_bg"), 0);
        solo.sleep(Res.integer.time_wait);
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, baiduName);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/OK");
    }
}