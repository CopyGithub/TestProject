
package com.dolphin.testcase.history;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1446
 * <p>
 * 脚本描述：验证Edit Most visited界面删除所有数据的操作
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1446")
@TestClass("验证Edit Most visited界面删除所有数据的操作")
public class CheckEditMostVisitedDeleteTest extends BaseTest {
    private String[] urls = { Res.string.url_one, "www.amazon.com", "webapps.dolphin.com/int/", };
    private String[] name = { "Web Store", "Amazon.com", Res.string.testurl_name_1 };

    public void test1Prepare() {
        uiUtil.skipWelcome();
        // 前置条件：Most visited中有数据A和B和C
        uiUtil.visitURL(urls);
    }

    public void testCheckEditMostVisitedDeleteTest() {
        uiUtil.skipWelcome();
        // 进入most visited编辑界面
        enterMostVisitedEdit();
        // 勾选1条数据（如：A）→点击删除按钮
        checkItemAndPressDelete(new int[] { 0 });
        /**
         * 验证：弹出删除弹框
         * <p>
         * 弹框标题：Delete
         * <p>
         * 内部文案："Are you sure to delete 1 items in most visited?"
         * <p>
         * 选项:“Cancel”、“confirm”
         */
        assertTrue("Failed to pop 'Delete' dialog .",
                solo.searchText(caseUtil.getTextByRId("bookmark_delete_confirm_title")));
        assertTrue("Failed to show the prompt 'Are you sure to delete 1 items in most visited?' ",
                solo.searchText("Are you sure to delete 1 item in most visited?"));
        assertTrue("Failed to show the 'Cancel' button .",
                solo.searchText(caseUtil.getTextByRId("cancel")));
        assertTrue("Failed to show the 'Confirm' button .",
                solo.searchText(caseUtil.getTextByRId("confirm_button")));
        /**
         * 验证: 点击Confirm按钮,弹框消失，被勾选的数据A被成功删除。
         */
        clickOnConfirm();
        assertFalse("'Delete' dialog does not disappear  .",
                solo.searchText(caseUtil.getTextByRId("bookmark_delete_confirm_title")));
        assertFalse("Failed to delete the most visited item " + name[0], solo.searchText(name[0]));
        // 再次勾选1条以上的数据（如：B和C）→点击删除按钮
        checkItemAndPressDelete(new int[] { 0, 1 });
        /**
         * 验证：弹出删除弹框
         * <p>
         * 弹框标题：Delete
         * <p>
         * 内部文案："Are you sure to delete 1 items in most visited?"
         * <p>
         * 选项:“Cancel”、“confirm”
         */
        assertTrue("Failed to pop 'Delete' dialog .",
                solo.searchText(caseUtil.getTextByRId("bookmark_delete_confirm_title")));
        assertTrue("Failed to show the prompt 'Are you sure to delete 1 items in most visited?' ",
                solo.searchText("Are you sure to delete 2 items in most visited?"));
        assertTrue("Failed to show the 'Cancel' button .",
                solo.searchText(caseUtil.getTextByRId("cancel")));
        assertTrue("Failed to show the 'Confirm' button .",
                solo.searchText(caseUtil.getTextByRId("confirm_button")));
        // 点击Confirm
        clickOnConfirm();
        /**
         * 验证： 按钮被选中的B和C均被成功删除。
         */
        assertFalse("Failed to delete the most visited item " + name[1], solo.searchText(name[1]));
        assertFalse("Failed to delete the most visited item " + name[2], solo.searchText(name[2]));
        /**
         * 验证：列表显示：Most visited list is empty。
         */
        assertTrue("Failed to show tip 'Most visited list is empty' ",
                solo.searchText(caseUtil.getTextByRId("empty_most_visited_list"), true));
    }

    private void clickOnConfirm() {
        solo.clickOnText(caseUtil.getTextByRId("confirm_button"));
        solo.sleep(Res.integer.time_wait);
    }

    private void checkItemAndPressDelete(int[] index) {
        for (int i : index) {
            checkItem(i);
            solo.sleep(Res.integer.time_wait);
        }
        solo.clickOnView(solo.getView("id/btn_remove_selected"));
        solo.sleep(Res.integer.time_wait);
    }

    private void checkItem(int index) {
        ViewGroup viewGroup = (ViewGroup) solo.getView("id/list");
        View check = caseUtil.getViewByIndex(viewGroup.getChildAt(index), new int[] { 0, 0 });
        if (!((CheckedTextView) check).isChecked()) {
            solo.clickOnView(check);
        }
        solo.sleep(Res.integer.time_wait);
    }

    private void enterMostVisitedEdit() {
        // 进入之后要等待10s most visited才会显示
        solo.sleep(10 * 1000);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("most_visit_folder_name"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/more_icon"));
        solo.sleep(Res.integer.time_wait);
    }
}