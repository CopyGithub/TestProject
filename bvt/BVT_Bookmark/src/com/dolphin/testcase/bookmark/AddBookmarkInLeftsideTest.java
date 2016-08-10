package com.dolphin.testcase.bookmark;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1460
 * <p>
 * 脚本描述：验证可以通过left bar下方add bookmark按钮添加书签
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1460")
@TestClass("验证可以通过left bar下方add bookmark按钮添加书签")
public class AddBookmarkInLeftsideTest extends BaseTest {
    private String gooleURL = Res.string.url_one;
    private String gooleName = Res.string.testurl_name_1;

    public void testAddBookmarkInLeftsideTest() {
        uiUtil.skipWelcome();
        // 预置条件Enable swipe for sidebars为关闭状态,已经打开网页
        closeEnableSidebarAndOpenURL();
        // 呼出侧边栏→点击下方add bookmark按钮
        openLeftsideAndClickAddBookmark();
        // 验证： 弹出Add Bookmark弹框
        assertTrue("Failed to pop 'Add bookmark' dialog.",
                solo.searchText(caseUtil.getTextByRId("save_to_bookmarks")));
        // 验证：弹框消失，弹出Toast提示：Saved to bookmarks.
        assertTrue("Failed to find toast 'Saved to bookmarks'.", addBookmark());
        assertTrue("The dialog is not disappeared.",
                solo.searchText(caseUtil.getTextByRId("save_to_bookmarks")));
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
        solo.sleep(3 * 1000);
        assertTrue("Network is not available.", uiUtil.waitForWebPageFinished());
    }

    private boolean addBookmark() {
        solo.hideSoftKeyboard();
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, gooleName);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/OK"));
        return solo.waitForText(caseUtil.getTextByRId("bookmark_saved"));
    }

    private void openLeftsideAndClickAddBookmark() {
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("save_to_bookmarks"));
        solo.sleep(Res.integer.time_wait);
    }

    private void closeEnableSidebarAndOpenURL() {
        uiUtil.enableSwipeSideBar(false);
        uiUtil.visitUrl(gooleURL);
        assertTrue("Network is not available.", uiUtil.waitForWebPageFinished());
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
    }
}