package com.dolphin.testcase.bookmark;

import java.util.ArrayList;

import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1500
 * <p>
 * 脚本描述：验证左侧边栏与地址栏Bookmark Tab界面的一致性
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1500")
@TestClass("验证左侧边栏与地址栏Bookmark Tab界面的一致性")
public class LeftAndSearchTabBookmarkConsistencyFuncTest extends BaseTest {
    private String[] folders = { "A" };
    private String folderString = "dolphin_help_folder_name";
    int[] selections = new int[] { 3, 4 };
    private ArrayList<String> titles1;
    private ArrayList<String> titles2;

    private enum Edit {
        SELECT_ALL, REMOVE_SELECTED, CREATE_FOLDER, MOVE_SELECTED
    }

    Edit icon = Edit.CREATE_FOLDER;

    public void testLeftAndSearchTabBookmarkConsistencyFuncTest() {
        uiUtil.skipWelcome();
        // 预置条件：1.书签列表中有文件夹A和其他的书签
        uiUtil.addFoldersFromRoot(folders, false);
        for (int i = 0; i < 4; ++i) {
            checkFunc();
        }
    }

    private void checkFunc() {
        switch (icon) {
        case CREATE_FOLDER:
            icon = Edit.MOVE_SELECTED;
            // 验证：新建文件夹A
            assertTrue("Failed to create new folder A", addFolder(new String[] { "B" }, false));
            // 验证：新建文件夹A后，左侧边Bookmark栏进行对应的改变
            assertTrue("Bookmarks are not consistent after adding folder A",
                    checkConsistentAfterOperations());
            break;
        case MOVE_SELECTED:
            icon = Edit.REMOVE_SELECTED;
            // 验证：发送到文件夹的功能
            assertTrue("Failed to move selected items after selecting 'OK'",
                    moveSelected(selections, true));
            // 验证：取消发送到文件夹的功能
            assertTrue("Failed to move selected items after selecting 'Cancel'",
                    moveSelected(selections, false));
            // 验证：发送到文件夹后左侧边Bookmark栏进行对应的改变
            assertTrue("Bookmarks are not consistent after moving selected items",
                    checkConsistentAfterOperations());
            break;
        case REMOVE_SELECTED:
            icon = Edit.SELECT_ALL;
            // 验证：删除功能
            assertTrue("Wrongly deleted the selections after 'OK'",
                    deleteSelected(selections, true));
            // 验证：取消删除功能
            assertTrue("Wrongly deleted the selections after 'Cancel'",
                    deleteSelected(selections, false));
            // 验证：删除后左侧边Bookmark栏进行对应的改变
            assertTrue("Bookmarks are not consistent after removing selected items",
                    checkConsistentAfterOperations());
            break;
        case SELECT_ALL:
            icon = Edit.CREATE_FOLDER;
            // 验证：全选功能
            isAllSelected();
            break;
        }
    }

    private void isAllSelected() {
        enterBookmarkEditFromAddressBar();
        solo.clickOnView(solo.getView("btn_select_or_deselect_all"));
        solo.sleep(Res.integer.time_wait);
        // 验证每个bookmark及文件夹前的check box被check
        caseUtil.slideDireciton(solo.getView("list"), false, 1 / 3f, 1f);
        do {
            ArrayList<View> views = utils.getChildren(solo.getView("list"), false);
            for (int i = 0; i < views.size(); i++) {
                CheckedTextView checkbox = (CheckedTextView) solo.getView("checkbox", i);
                boolean flag = checkbox.isChecked();
                assertTrue("The " + i + " checkbox should be checked.", flag);
            }
        } while (caseUtil.isScroll(solo.getView("list"), 1));
    }

    private boolean deleteSelected(int[] selections, boolean yes) {
        boolean flag = false;
        if (yes) {
            enterBookmarkEditFromAddressBar();
            String[] name = new String[selections.length];
            // start to select
            for (int i = 0; i < selections.length; ++i) {
                solo.clickOnView(solo.getView("checkbox", selections[i]));
                name[i] = ((TextView) solo.getView("title", selections[i])).getText().toString();
            }
            solo.sleep(Res.integer.time_wait);
            solo.clickOnView(solo.getView("btn_remove_container"));
            solo.sleep(Res.integer.time_wait);
            flag = checkDeleteSelectedWithConfirm(name);
        } else {
            String[] name = new String[selections.length];
            // start to select
            for (int i = 0; i < selections.length; ++i) {
                solo.clickOnView(solo.getView("checkbox", selections[i]));
                name[i] = ((TextView) solo.getView("title", selections[i])).getText().toString();
            }
            solo.sleep(Res.integer.time_wait);
            solo.clickOnView(solo.getView("btn_remove_container"));
            solo.sleep(Res.integer.time_wait);
            flag = checkDeleteSelectedWithCancel(name);
        }
        return flag;
    }

    private boolean checkDeleteSelectedWithCancel(String[] name) {
        solo.clickOnView(solo.getView("button1"));
        solo.sleep(Res.integer.time_wait);
        return solo.searchText(name[0]) && solo.searchText(name[1]);
    }

    private boolean checkDeleteSelectedWithConfirm(String[] name) {
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_wait);
        return !solo.searchText(name[0]) && !solo.searchText(name[1]);
    }

    private boolean moveSelected(int[] selections, boolean yes) {
        boolean flag = false;
        if (yes) {
            enterBookmarkEditFromAddressBar();
            String[] name = new String[selections.length];
            // 发送到文件夹
            for (int i = 0; i < selections.length; ++i) {
                solo.clickOnView(solo.getView("checkbox", selections[i]));
                name[i] = ((TextView) solo.getView("title", selections[i])).getText().toString();
            }
            solo.sleep(Res.integer.time_wait);
            solo.clickOnView(solo.getView("btn_move_selected"));
            solo.sleep(Res.integer.time_wait);
            flag = checkMoveSelectedWithOK(name);
        } else {
            String[] name = new String[selections.length];
            // 发送到文件夹
            for (int i = 0; i < selections.length; ++i) {
                solo.clickOnView(solo.getView("checkbox", selections[i]));
                name[i] = ((TextView) solo.getView("title", selections[i])).getText().toString();
            }
            solo.sleep(Res.integer.time_wait);
            solo.clickOnView(solo.getView("btn_move_selected"));
            solo.sleep(Res.integer.time_wait);
            flag = checkMoveSelectedWithCancel(name);
        }
        return flag;
    }

    private boolean checkMoveSelectedWithCancel(String[] name) {
        solo.clickOnView(solo.getView("button1"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId(folderString));
        solo.sleep(Res.integer.time_wait);
        // 验证取消发送到文件夹
        boolean flag1 = !solo.searchText(name[0]) && !solo.searchText(name[1]);
        solo.clickOnText(caseUtil.getTextByRId("bookmark_tab_name"));
        solo.sleep(Res.integer.time_wait);
        boolean flag2 = solo.searchText(name[0]) && solo.searchText(name[1]);

        return flag1 && flag2;
    }

    private boolean checkMoveSelectedWithOK(String[] name) {
        solo.clickOnText(caseUtil.getTextByRId(folderString));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId(folderString));
        solo.sleep(Res.integer.time_wait);

        // 验证成功发送到文件夹
        boolean flag1 = solo.searchText(name[0]) && solo.searchText(name[1]);
        solo.clickOnText(caseUtil.getTextByRId("bookmark_tab_name"));
        solo.sleep(Res.integer.time_wait);
        boolean flag2 = !solo.searchText(name[0]) && !solo.searchText(name[1]);

        return flag1 && flag2;
    }

    /**
     * 
     * @param names
     *            子目录文件夹名字 是不是有层级关系的文件夹
     */
    private boolean addFolder(String[] names, boolean isConcluded) {
        enterBookmarkEditFromAddressBar();
        // 新建文件夹
        for (int i = 0; i < names.length; ++i) {
            solo.clickOnView(solo.getView("id/btn_create_folder"));
            solo.sleep(Res.integer.time_wait);
            solo.hideSoftKeyboard();
            solo.sleep(Res.integer.time_wait);
            solo.enterText(0, names[i]);
            solo.clickOnView(solo.getView("id/button2"));
            solo.sleep(Res.integer.time_wait);
            if (isConcluded) {
                solo.clickOnView(caseUtil.getViewByIndex(solo.getView("list"),
                        new int[] { 0, 2, 0 }));
                solo.sleep(Res.integer.time_wait);
            }
        }
        return solo.searchText(folders[0]);
    }

    private boolean checkConsistentAfterOperations() {
        titles2 = getViewContent(solo.getView("list"), new int[] { 2, 0 }, true, 3);
        // 从左侧边栏进入Bookmark编辑界面
        enterBookmarkEditFromLeft();
        titles1 = getViewContent(solo.getView("list"), new int[] { 2, 0 }, true, 3);
        boolean flag = titles1.equals(titles2);

        return flag;
    }

    private boolean enterBookmarkEditFromAddressBar() {
        // 点击地址栏→切换到Bookmarks Tab下→点击Manage
        solo.clickOnView(solo.getView("title_container"));
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("searchtab_bottom_continer"));
        solo.sleep(Res.integer.time_wait);
        return true;
    }

    private void enterBookmarkEditFromLeft() {
        uiUtil.enterBookmark();
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("more");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("footer_container");
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 
     * @param parent
     * @param index
     * @param isBackToHome
     * @param num
     * @return titles
     */
    // TODO 没有记录parent中folder下的目录
    private ArrayList<String> getViewContent(View parent, int[] index, boolean isBackToHome, int num) {
        ArrayList<String> titles = new ArrayList<String>();
        // 小屏机需要再做修改由于其机型自身计算拖动值时有误
        caseUtil.slideDireciton(parent, false, 1 / 3f, 1f);
        do {
            ArrayList<View> views = utils.getChildren(parent, false);
            for (int i = 0; i < views.size(); i++) {
                View titleView = caseUtil.getViewByIndex(views.get(i), index);
                String text = ((TextView) titleView).getText().toString();
                if (!titles.contains(text)) {
                    titles.add(text);
                }
            }
        } while (caseUtil.isScroll(parent, 1));
        if (isBackToHome) {
            for (int i = 0; i < num; ++i) {
                solo.goBack();
                solo.sleep(Res.integer.time_wait);
            }
        }
        return titles;
    }
}
