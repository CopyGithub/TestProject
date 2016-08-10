package com.dolphin.testcase.setting;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-929
 * <p>
 * 脚本描述: 验证Settings-GENERAL界面区域内容、排序显示正确
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-929")
@TestClass("验证Settings-GENERAL界面区域内容、排序显示正确")
public class CheckSettingsGeneralUITest extends BaseTest {
    private static final String GREY = "8a000000";
    private static final String BLACK = "8a000000";
    private static final String GREEN = "ff389d00";
    private static final int GENERAL = 0;
    private int[] path = { 1, 0 };

    public void testCheckSettingsGeneralUITest() {
        uiUtil.skipWelcome();
        // 验证：默认是General 界面
        assertTrue("Current tab is not in general tab.", isCurrentInterface(GENERAL));

        // 验证：TODO Sign in Dolphin Connect(主题色,加粗未实现)
        uiUtil.assertSearchText("no_account_info_title", null, -1, false, false, 0, null);
        // 验证： TODO Sync your bookmarks and more on all your devices(灰色)(小字未实现)
        assertTextAndColorItem("no_account_info_summary", GREY);

        // 验证： TODO Basic（绿色)
        assertTextAndColorItem("settings_header_basic", GREEN);

        if (uiUtil.isWebkit()) {
            // 验证: Dolphin Jetpack (开关: 默认开启)（shell版本无此设置项）
            uiUtil.assertSearchText("use_dolphin_webkit", null, -1, true, true, 2, path);
            // 验证：TODO Dolphin rendering engine speeds up page
            // loading(灰色)(小字未实现)
            assertTextAndColorItem("use_dolphin_webkit_summary", BLACK);
        }

        // 验证： Language > (默认值: "Auto" )
        uiUtil.assertSearchText("pref_language_string", "pref_language_choices", 0, false, false,
                0, null);
        // 验证：Set as default browser (开关: 默认关闭)
        uiUtil.assertSearchText("pref_content_set_as_default_browser", null, -1, true, false, 2,
                path);
        // 验证： Gesture and sonar >
        uiUtil.assertSearchText("pref_category_gesture_voice_settings", null, -1, false, false, 0,
                null);
        // 验证： Search engine > (默认值: 由预置数据决定 )
        uiUtil.assertSearchText("pref_content_search_engine", null, -1, false, false, 0, null);

        // 验证：TODO Web content（绿色)
        assertTextAndColorItem("pref_content_title", GREEN);
        // 验证：Text size (默认值: 100% )
        uiUtil.assertSearchText("pref_text_size", "100%", -1, false, false, 0, null);
        // 验证： Always show address and menu bar （开关: 默认关闭）
        uiUtil.assertSearchText("pref_keep_bars_title", null, -1, true, false, 2, path);
        // 验证： Flah player (默认值: On demand(打包版本) off(Shell版本)) )
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("pref_content_plugins", "pref_plugin_state_choices", 1, false,
                    false, 0, null);
        } else {
            uiUtil.assertSearchText("pref_content_plugins", "pref_plugin_state_choices", 2, false,
                    false, 0, null);
        }

        // 验证：TODO Data（绿色)
        assertTextAndColorItem("settigns_header_data", GREEN);
        // 验证：Clear data >
        uiUtil.assertSearchText("clear_data", null, -1, false, false, 0, null);
        // 验证：Always clear data when exiting >
        uiUtil.assertSearchText("pref_category_exit", null, -1, false, false, 0, null);
        // 验证：Download >
        uiUtil.assertSearchText("download_setting", null, -1, false, false, 0, null);
        // 验证：Backup and restore >
        uiUtil.assertSearchText("backup_restore_settings", null, -1, false, false, 0, null);

        // TODO About（绿色)
        assertTextAndColorItem("settings_header_about", GREEN);
        // 验证：About Dolphin >
        uiUtil.assertSearchText("pref_category_help", null, -1, false, false, 0, null);
        // 验证：Send feedback >
        uiUtil.assertSearchText("pref_send_feedback", null, -1, false, false, 0, null);

    }

    private boolean isCurrentInterface(int num) {
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        View general_view = caseUtil.getViewByIndex("id/pager", num);
        return caseUtil.isInsideDisplay(general_view, false);
    }

    private void assertTextAndColorItem(String cpName, String cmcolor) {
        String compareString = caseUtil.getTextByRId(cpName);
        TextView title = (TextView) caseUtil.getViewByText(cpName, 0, true, true, true);
        assertTrue("没有正确找到" + compareString, solo.searchText(compareString));
        String color = Integer.toHexString(title.getCurrentTextColor());
        assertTrue("颜色值应该是" + cmcolor + ";界面上是" + color, cmcolor.equals(color));
    }
}