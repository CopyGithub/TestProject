package com.dolphin.testcase.bookmark;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1491
 * <p>
 * 脚本描述：验证书签管理界面添加文件夹的操作
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-1491")
@TestClass("验证书签管理界面添加文件夹的操作")
public class CreateFolderInManageTest extends BaseTest {
    private String[] folders = { "A", "B" };
    private String bookmarkActivity = "BookmarkEntranceActivity";

    public void testCreateFolderInManageTest() {
        uiUtil.skipWelcome();
        // 验证： 进入根目录书签管理界面
        assertTrue("Failed to enter manage activity .", isMangeActivity());
        clickOnCreateFolder();
        // 验证：弹出New Folder弹框
        assertTrue("Failed to pop 'New folder' dialog .",
                solo.searchText(caseUtil.getTextByRId("new_folder_title")));
        // 验证：弹出软键盘
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to show softkeyboard.",
                caseUtil.hideSoftKeyboard((TextView) solo.getView("input")));
        // 验证：输入框中显示为空
        assertTrue("The edittext is not empty.", isEmpty("input"));
        // TODO 光标闪烁
        // 底部: Cancel 按钮，OK按钮（灰显不可点击）
        // TODO OK按钮灰显
        assertTrue("Failed to show 'Cancel' button.",
                solo.searchText(caseUtil.getTextByRId("cancel")));
        assertTrue("Failed to show 'OK' button.", solo.searchText(caseUtil.getTextByRId("btn_ok")));
        // OK不可点击
        assertFalse("Failed to show 'OK' button.",
                ((Button) solo.getView("id/button2")).isEnabled());
    }

    private boolean isEmpty(String editviewId) {
        EditText editText = (EditText) solo.getView(editviewId);
        return editText.getText().toString().equals("");
    }

    private void clickOnCreateFolder() {
        solo.clickOnView(solo.getView("id/btn_create_folder"));
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isMangeActivity() {
        // 书签列表中有文件夹A和B
        uiUtil.addFoldersFromRoot(folders, false);
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterBookmark();
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("more");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("footer_container");
        solo.sleep(Res.integer.time_wait);
        return solo.getCurrentActivity().toString().contains(bookmarkActivity);
    }
}