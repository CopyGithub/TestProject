package com.dolphin.testcase.history;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;

/**
 * 脚本编号：DOLINT-1420
 * <p>
 * 脚本描述：验证Edit history界面入口和界面UI
 * 
 * @author jwliu
 * 
 */
@Reinstall
// @TestClass("验证Edit history界面入口和界面UI")
// TODO yesterday文件夹无法不依赖case创建
public class CheckEditHistoryUITest extends BaseTest {
    private String selectCount;
    private String urls[] = { Res.string.url_one, Res.string.url_two, Res.string.url_three,
            Res.string.url_four, Res.string.url_five, Res.string.url_sixth };

    public void testCheckEditHistoryUITest() {
        uiUtil.skipWelcome();
        // 前置条件：History有历史记录A和B，以及时间分组文件夹：Today和Yesterday
        uiUtil.prepareTodayFolder(urls);
        // 进入历史编辑界面
        enterHisitoryEdit();
        // 验证： 界面顶部显示：√0 Selected
        // TODO √ 无法验证
        assertTrue("Count of select should be 0.", selectCount.equals("0"));
        // 验证： 所有的历史记录，每条历史记录前面显示复选框，默认不勾选
        assertHasCheckboxBeforeHistoryAndUncheck();
        // 验证： Today/Yesterday/文件夹前面没有复选框
        // 验证：界面底部: 全选按钮和删除按钮（灰色不可点击）
        assertTrue("'Select All' icon should be enabled.",
                solo.getView("btn_select_or_deselect_all").isEnabled());
        assertFalse("'Select All' icon should be disabled mode.",
                solo.getView("btn_remove_selected").isEnabled());
    }

    private void assertHasCheckboxBeforeHistoryAndUncheck() {
        ViewGroup viewGroup = (ViewGroup) solo.getView("id/list");
        // 历史记录最多五条，因为存在today文件夹，所以只有五条
        for (int i = 0; i < 5; ++i) {
            View check = caseUtil.getViewByIndex(viewGroup.getChildAt(i), new int[] { 0, 0 });
            View title = caseUtil.getViewByIndex(viewGroup.getChildAt(i), new int[] { 2, 0 });
            assertTrue("checkbox dose not before the " + i + " history  .",
                    utils.ubietyOfView(check, title, true, false, false) != -1);
            assertTrue("the " + i + " history does not have checkbox.", check != null);
            assertFalse("the " + i + " history does not have checkbox.",
                    ((CheckedTextView) check).isChecked());
        }
    }

    private void enterHisitoryEdit() {
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("history"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/more_icon"));
        solo.sleep(Res.integer.time_wait);
        selectCount = ((TextView) solo.getView("bookmark_path")).getText().toString()
                .substring(0, 1);
    }
}
