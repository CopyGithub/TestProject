package com.dolphin.testcase.bookmark;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1461
 * <p>
 * 脚本描述：验证将网页直接添加Bookmark的操作
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1461")
@TestClass("验证将网页直接添加Bookmark的操作")
public class AddBookmarkByMenuTest extends BaseTest {
    private String gooleURL = Res.string.url_one;
    private String gooleName = Res.string.testurl_name_1;

    public void testCheckAboutDolphinUITest() {
        uiUtil.skipWelcome();
        // 预置条件： 已经打开网页，如：google.com;之前没有保存过Google书签
        openAddItem();
        // 验证： 弹出Add to弹框
        assertTrue("Failed to pop 'Add bookmark' dialog.",
                solo.searchText(caseUtil.getTextByRId("add_to")));
        // 验证： 弹框中Bookmark被选中
        assertTrue("The Bookmarks item is not focus.", solo.getView("id/more").isShown());
        // 点击Add
        // 验证： 弹出Toast提示：Saved to bookmarks.
        assertTrue("Failed to find toast 'Saved to bookmarks'.", addBookmark());
        // 验证： 弹框消失
        assertTrue("The dialog is not disappeared.",
                !solo.searchText(caseUtil.getTextByRId("add_to"), true));
        // 进入书签栏中查看
        // 验证：文件夹列表的最下方（书签列表的最上方）显示Google书签;
        // TODO 书签的左侧显示对应的网页Logo
        assertTrue("The first bookmark item is not " + gooleName,
                uiUtil.isFirstNewBookmark(gooleName));
        // 新建tab→进入书签栏中点击Google书签
        addTabAndCilckAddBookmark();
        // 验证：进入Google网页
        assertTrue("Failed to enter " + gooleName + " website.", uiUtil.checkURL(gooleURL));
    }

    private void addTabAndCilckAddBookmark() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.addNewTab(1);
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSideBar(true);
        solo.clickOnText(gooleName);
        assertTrue("Network is not available.", uiUtil.waitForWebPageFinished());
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
    }

    private boolean addBookmark() {
        solo.hideSoftKeyboard();
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, gooleName);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/add"));
        return solo.waitForText(caseUtil.getTextByRId("bookmark_saved"));
    }

    private void openAddItem() {
        uiUtil.visitUrl(gooleURL);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.clickOnMenuItem("add_to");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/item_bookmark");
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }
}