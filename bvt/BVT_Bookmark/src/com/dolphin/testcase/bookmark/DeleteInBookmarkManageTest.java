package com.dolphin.testcase.bookmark;

import android.view.View;
import android.widget.CheckedTextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.adolphin.common.SqliteUtil;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1490
 * <p>
 * 脚本描述：验证书签管理界面删除勾选的书签/文件夹的操作
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1490")
@TestClass("验证书签管理界面删除勾选的书签/文件夹的操作")
public class DeleteInBookmarkManageTest extends BaseTest {
    private String[] folders = { "B", "A" };
    private String bookmarkNameString = "Google";
    private String url = "google.com";
    private String bookmarkActivity = "BookmarkEntranceActivity";
    private View folderCheckView;
    private View bookmarkCheckView;

    public void testDeleteInBookmarkManageTest() {
        uiUtil.skipWelcome();
        SqliteUtil sqliteUtil = new SqliteUtil(solo);
        // 进入根目录书签管理界面
        enterBookmarkEdit();
        // 勾选任意的书签和文件夹A
        checkFolderAndBookmark();
        // 验证：删除按钮和发送到其到他文件夹按钮可点击
        // TODO 按钮变为深色
        assertTrue("Delete icon should change to abled mode.", solo.getView("btn_remove_selected")
                .isEnabled());
        assertTrue("Move To Other Folders icon should change to abled mode.",
                solo.getView("btn_move_selected").isEnabled());
        // 点击删除按钮
        clickOnDeleteBtn(false, null);
        // 验证：弹出Delete提示框
        assertTrue("Failed to pop 'Delete' dialog.",
                solo.searchText(caseUtil.getTextByRId("bookmark_delete_confirm_title")));
        // 验证：提示语：Delete the XXX selected bookmark(s)?
        assertTrue("Failed to show prompt 'Delete the 2 selected bookmarks' .",
                solo.searchText("Delete the 2 selected bookmarks"));// 没有找到对应的R文件
        // 验证：按钮：Cancel,Confirm
        assertTrue("Failed to show 'Cancel' button' ",
                solo.searchText(caseUtil.getTextByRId("cancel")));
        assertTrue("Failed to show 'Confirm' button' ",
                solo.searchText(caseUtil.getTextByRId("confirm_button")));
        // 点击弹框以外的区域/Cancel按钮
        clickOnCancel();
        // 验证：弹框消失，勾选的书签/文件夹保持显示，勾选状态保持
        assertFalse("Failed to disppear 'Delete' dialog.",
                solo.searchText(caseUtil.getTextByRId("bookmark_delete_confirm_title")));
        assertTrue("Failed to keep checked status of folder. ", isChecked(folderCheckView));
        assertTrue("Failed to keep checked status of bookmark. ", isChecked(bookmarkCheckView));
        // 再次点击删除按钮→点击Confirm按钮
        clickOnDeleteBtn(true, "button2");
        // 验证：勾选的书签/文件夹全部从书签列表中消失
        // 文件夹A中的选中的书签也一起消失
        assertTrue("Failed to deleted selected folder named 'A' .", sqliteUtil.isFolderDeleted("A"));
        assertTrue("Failed to deleted selected folder named 'A' .",
                sqliteUtil.isBookmarkDeleted(bookmarkNameString, "A"));
    }

    private boolean isChecked(View view) {
        solo.scrollToTop();
        solo.sleep(Res.integer.time_wait);
        return ((CheckedTextView) view).isChecked();
    }

    private void clickOnCancel() {
        caseUtil.clickOnView("button1");
        solo.sleep(Res.integer.time_wait);
    }

    private void clickOnDeleteBtn(boolean isClick, String id) {
        solo.clickOnView(solo.getView("id/btn_remove_selected"));
        solo.sleep(Res.integer.time_wait);
        if (isClick) {
            caseUtil.clickOnView(id);
            solo.sleep(Res.integer.time_wait);
        }
    }

    private void checkFolderAndBookmark() {
        folderCheckView = caseUtil.getViewByIndex(solo.getView("list"), new int[] { 0, 0, 0 });
        // 第一次安装海豚，除了文件夹A,B和Dolphin下面都是书签，选择第二个书签
        bookmarkCheckView = solo.getView("id/checkbox", 4);
        solo.clickOnView(folderCheckView);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(bookmarkCheckView);
        solo.sleep(Res.integer.time_wait);
    }

    private boolean enterBookmarkEdit() {
        // 预置条件：1.书签列表中有书签，文件夹A（A中有书签Google）和文件夹B
        uiUtil.addFoldersFromRoot(folders, false);
        uiUtil.addBookmark(url, bookmarkNameString, folders[0]);
        // 已经进入左侧边栏且当前在Bookmarks根目录
        uiUtil.enterBookmark();
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("more");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("footer_container");
        solo.sleep(Res.integer.time_wait);
        return solo.getCurrentActivity().toString().contains(bookmarkActivity);
    }

}