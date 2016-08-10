package com.dolphin.testcase.bookmark;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1489
 * <p>
 * 脚本描述：验证书签管理入口和书签管理界面的UI
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1489")
@TestClass("验证书签管理入口和书签管理界面的UI")
public class CheckBookmarksManageUITest extends BaseTest {
    private String[] folder = { "A" };
    private String[] folders = { "B", "C" };
    private String[] newAddPath = { "BOOKMARKS", "A" };
    private String selectedNum;

    public void testCheckBookmarksManageUITest() {
        uiUtil.skipWelcome();
        enterBookmarkEdit();
        // 验证:Title最左侧显示0 Selected
        // TODO　显示√
        assertTrue("Expected result is that no bookmark is selected.", selectedNum.equals("0"));
        // 验证:所有的书签和文件夹前面有复选框，默认都不勾选
        assertCheckedBookmarksAndFolders();
        // TODO 书签&书签文件夹后面有拖动排序图标（三横）
        // 验证:界面底部为：全选按钮、删除按钮（默认灰色不可点击）、添加文件夹按钮，发送到其他文件夹按钮（默认灰色不可点击）
        assertTrue("Select All  icon should be in enabled mode.",
                solo.getView("btn_select_or_deselect_all").isEnabled());
        assertFalse("Delete icon should be in disabled mode.", solo.getView("btn_remove_selected")
                .isEnabled());
        assertTrue("Create Folder icon should be in enabled mode.",
                solo.getView("btn_create_folder").isEnabled());
        assertFalse("Move To Other Folders icon should be in disabled mode.",
                solo.getView("btn_move_selected").isEnabled());
        // 进入文件夹A的管理界面
        enterFolder("A");
        // 界面布局和根目录管理界面布局一致
        // 验证:Title最左侧显示0 Selected
        // TODO　显示√
        assertTrue("Expected result is that no bookmark is selected.", selectedNum.equals("0"));
        // 验证:所有的书签和文件夹前面有复选框，默认都不勾选
        assertCheckedBookmarksAndFolders();
        // TODO 书签&书签文件夹后面有拖动排序图标（三横）
        // 验证:界面底部为：全选按钮、删除按钮（默认灰色不可点击）、添加文件夹按钮，发送到其他文件夹按钮（默认灰色不可点击）
        assertTrue("Select All  icon should be in enabled mode.",
                solo.getView("btn_select_or_deselect_all").isEnabled());
        assertFalse("Delete icon should be in disabled mode.", solo.getView("btn_remove_selected")
                .isEnabled());
        assertTrue("Create Folder icon should be in enabled mode.",
                solo.getView("btn_create_folder").isEnabled());
        assertFalse("Move To Other Folders icon should be in disabled mode.",
                solo.getView("btn_move_selected").isEnabled());
        // 验证:在Title下方增加了当前的路径：BOOKMARKS>A
        // TODO BOOKMARKS>A 中的图片>无法验证
        isNewAddPathBelowTiltleAndRightShow(newAddPath);
    }

    private boolean isNewAddPathBelowTiltleAndRightShow(String[] path) {
        String name[] = new String[path.length];
        View titlevView = solo.getView("id/title_container");
        View view = solo.getView("id/path_bar_container");
        int[] location1 = new int[2];
        int[] location2 = new int[2];
        titlevView.getLocationOnScreen(location1);
        view.getLocationOnScreen(location2);
        if (location1[1] > location2[1]) {
            assertTrue("The new add path should below Title", false);
        }
        for (int i = 0; i < path.length; ++i) {
            TextView textView = (TextView) caseUtil.getViewByIndex(view,
                    new int[] { 0, 0, 0, 2 * i });
            if (!textView.getText().toString().equals(name[i])) {
                return false;
            }
        }
        return true;
    }

    private void enterFolder(String folder) {
        solo.clickOnText(folder);
        solo.sleep(Res.integer.time_wait);
        selectedNum = ((TextView) solo.getView("id/bookmark_path")).getText().toString()
                .substring(0, 1);
    }

    private void enterBookmarkEdit() {
        // 预置条件：书签列表中有书签和文件夹A（A中有子文件夹B,书签C)
        uiUtil.addFoldersFromRoot(folder, false);
        uiUtil.addFoldersFromFolder(folder, folders, false);
        // 已经进入左侧边栏且当前在Bookmarks根目录
        uiUtil.enterBookmark();
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/more");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/footer_container");
        solo.sleep(Res.integer.time_wait);
        selectedNum = ((TextView) solo.getView("id/bookmark_path")).getText().toString()
                .substring(0, 1);
    }

    private void assertCheckedBookmarksAndFolders() {
        // 验证每个bookmark及文件夹前的check box都没有被check
        ViewGroup list = (ViewGroup) solo.getView("id/list");
        for (int i = 1; i < list.getChildCount(); i++) {
            assertFalse(
                    "The " + i + " item should not be checked.",
                    ((CheckedTextView) caseUtil.getViewByIndex(solo.getView("list"), new int[] { i,
                            0, 0 })).isChecked());
        }
    }
}