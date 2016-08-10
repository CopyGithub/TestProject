package com.dolphin.testcase.setting;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-930
 * <p>
 * 脚本描述: 验证Settings-ADVANCED界面区域内容、排序显示正确
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-930")
@TestClass("验证Settings-ADVANCED界面区域内容、排序显示正确")
public class CheckSettingsAndvancedUITest extends BaseTest {
    private static final String BLACK = "8a000000";
    private static final String GREEN = "ff389d00";
    private static final int GENERAL = 0;
    private static final int ADVANCED = 1;
    private int[] path = { 1, 0 };

    public void testCheckSettingsAndvancedUITest() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        // 验证：默认是General 界面
        assertTrue("Current tab is not in general tab.", isCurrentInterface(GENERAL));

        // 验证：滑动到选项卡是ADVANCED界面
        scrollToAndvanced();
        assertTrue("Current tab is not in advanced tab.", isCurrentInterface(ADVANCED));
        // TODO 未实现: 依次显示三个区域,区域之间以留白分隔
        // 验证：Privacy and security(绿色)
        // TODO 加粗未实现
        assertTextAndColorItem("settings_header_privacy_security", GREEN);
        // 验证： Privacy >
        uiUtil.assertSearchText("pref_privacy_settings", null, -1, false, false, 0, null);
        // 验证： Check for server certificate revocation （开关:默认关闭）
        uiUtil.assertSearchText("pref_security_check_ssl_cert_revocation", null, -1, true, false,
                2, path);
        // 验证：Show security warnings （开关:默认开启）
        uiUtil.assertSearchText("pref_security_show_security_warning", null, -1, true, true, 2,
                path);
        // 验证：Show warning when the site has security problem(灰色)
        // TODO 小字未实现
        assertTextAndColorItem("pref_security_show_security_warning_summary", BLACK);
        // 验证： Reset to default
        uiUtil.assertSearchText("pref_extras_reset_default", null, -1, false, false, 0, null);

        // 验证：TODO Customize（绿色)
        assertTextAndColorItem("pref_security_show_security_warning_summary", BLACK);
        // 验证： User agent(默认值: "Android" )
        uiUtil.assertSearchText("feedback_user_agent", "pref_development_ua_choices", 0, false,
                false, 0, null);
        // 验证： Set my homepage（默认值:"Use the new tab page"）
        uiUtil.assertSearchText("pref_content_homepage", "option_speed_dial_homepage_tilte", 0,
                false, false, 0, null);
        // 验证：Enable Swipe for sidebars （开关:默认关闭）
        uiUtil.assertSearchText("pref_enable_swipe_action", null, -1, true, true, 1, path);
        // 验证：Enable Dolphin notification （开关:默认开启）
        uiUtil.assertSearchText("pref_push_notification_enabled", null, -1, true, true, 2, path);
        // 验证： Open previous tab on startup （开关:默认关闭）
        uiUtil.assertSearchText("pref_on_startup", null, -1, true, false, 2, path);
        // 验证： Smart cache >
        uiUtil.assertSearchText("pref_smart_cache", null, -1, false, false, 0, null);
        // 验证：TODO Smartly save cache to speed up page loading(灰色)(小字未实现)
        assertTextAndColorItem("save_cache_to_sdcard_summary", BLACK);
        // 验证：More >
        uiUtil.assertSearchText("more_item_label", null, -1, false, false, 0, null);

        // 验证：TODO Web content（绿色)(加粗,未实现）
        assertTextAndColorItem("pref_content_title", GREEN);
        // 验证：Page Scrolling speed （默认值:"100%"）（内核开启后显示）
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("sliding_speed_on_pages", "100%", -1, false, false, 0, null);
        }
        // 验证：Load images （默认值:"Always on"），
        uiUtil.assertSearchText("pref_content_load_images", "pref_load_images_choices", 0, false,
                false, 0, null);
        // 验证：Enable javaScript （开关:默认开启）
        uiUtil.assertSearchText("pref_content_javascript", null, -1, true, true, 2, path);
        // 验证：Block pop-up windows （开关:默认开启
        uiUtil.assertSearchText("pref_content_block_popups", null, -1, true, true, 2, path);
        // 验证：Accept cookies （开关:默认开启）
        uiUtil.assertSearchText("pref_security_accept_cookies", null, -1, true, true, 2, path);
        // 验证:Text encoding （默认值为与系统语言相关）
        uiUtil.assertSearchText("pref_default_text_encoding", null, -1, false, false, 0, null);
        // 验证：Auto-fit pages （开关:默认开启）（内核开启后显示）
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("pref_content_autofit", null, -1, true, true, 2, path);
        }
        // 验证：Page zoom >
        uiUtil.assertSearchText("settings_page_zoom_title", null, -1, false, false, 0, null);
    }

    private void scrollToAndvanced() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, true, 1f, 1f);
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isCurrentInterface(int num) {
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