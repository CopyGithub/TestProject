package com.dolphin.testcase.barshow;

import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1689
 * <p>
 * 脚本描述：Always show address（ and menu） bar开关_UI
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1689")
@TestClass("Always show address（ and menu） bar开关_UI")
@Reinstall
public class CheckBarSwitchUITest extends BaseTest {
    public void testCheckBarSwitchUI() {
        uiUtil.skipWelcome();

        uiUtil.enterSetting(false);

        // 滑动web content到上面，使得该区域可见
        caseUtil.slideDireciton(null, false, 3 / 4f, 1f);
        solo.sleep(Res.integer.time_wait);

        // 验证：开关文案
        // 中屏机：Always show address and menu bar
        // 大屏机和小平机：Always show address bar
        View barSwitch = null;
        if (caseUtil.getDisplayRange() == 1) {
            barSwitch = caseUtil.getViewByText("pref_keep_bars_title", 0, true, false, true);
        } else {
            barSwitch = caseUtil.getViewByText("pref_keep_titlebar_title", 0, true, false, true);
        }
        assertTrue("The text for switch is not right...", barSwitch != null);

        // 验证：开关位置在Text size和Flash player之间
        View textSize = caseUtil.getViewByText("pref_text_size", 0, true, false, true);

        View flashPlayer = caseUtil.getViewByText("pref_content_plugins", 0, true, false, true);

        ArrayList<View> views = new ArrayList<View>();
        views.add(textSize);
        views.add(barSwitch);
        views.add(flashPlayer);
        utils.ubietyOfViews(views, 1, false, false, false);

        // 验证：开关默认状态为：关闭（JP版本开关默认状态：打开）
        View parent = utils.getParent(barSwitch, 2);
        CheckBox checkBox = (CheckBox) caseUtil.getViewByIndex(parent, new int[] { 1, 0 });
        assertTrue("The default state for switch should be not checked...", !checkBox.isChecked());
    }
}