package com.dolphin.testcase.history;

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
 * 脚本编号：DOLINT-1443
 * <p>
 * 脚本描述：验证Edit history界面入口和界面UI
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1443")
@TestClass("验证Mostvisited编辑界面入口和UI")
public class CheckEditMostVisitedUITest extends BaseTest {
    private String selectCount;

    public void test1Prepare() {
        uiUtil.skipWelcome();
        // 前置条件：Most visited中有数据A和B和C
        uiUtil.visitURL(new String[] { Res.string.url_one });
    }

    public void testCheckEditMostVisitedUITest() {
        uiUtil.skipWelcome();
        // 进入most visited编辑界面
        enterMostVisitedEdit();
        /**
         * 验证：界面显示
         * <p>
         * 界面顶部显示：√0 Selected(TODO √ )
         * <p>
         * 界面中间显示Most visited数据列表，每条数据的右侧显示复选框，默认为不勾选
         * <p>
         * 界面底部: 全选按钮和删除按钮（灰色不可点击）(TODO 灰色)
         */
        assertTrue("Count of select should be 0.", selectCount.equals("0"));
        assertHasCheckboxBeforeMostVisitedAndUncheck();
        assertTrue("'Select All' icon should be enabled.",
                solo.getView("btn_select_or_deselect_all").isEnabled());
        assertFalse("'Select All' icon should be disabled mode.",
                solo.getView("btn_remove_selected").isEnabled());
    }

    private void assertHasCheckboxBeforeMostVisitedAndUncheck() {
        ViewGroup viewGroup = (ViewGroup) solo.getView("id/list");
        // 在前置条件中只创建了一条most visited，所以只验证一条记录
        View check = caseUtil.getViewByIndex(viewGroup.getChildAt(0), new int[] { 0, 0 });
        View title = caseUtil.getViewByIndex(viewGroup.getChildAt(0), new int[] { 2, 0 });
        assertTrue("checkbox dose not before the most visited item  .",
                utils.ubietyOfView(check, title, true, false, false) != -1);
        assertTrue("the most visited item does not have checkbox.", check != null);
        assertFalse("the most visited itemdoes not have checkbox.",
                ((CheckedTextView) check).isChecked());
    }

    private void enterMostVisitedEdit() {
        solo.sleep(10 * 1000);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("most_visit_folder_name"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/more_icon"));
        solo.sleep(Res.integer.time_wait);
        selectCount = ((TextView) solo.getView("bookmark_path")).getText().toString()
                .substring(0, 1);
    }
}