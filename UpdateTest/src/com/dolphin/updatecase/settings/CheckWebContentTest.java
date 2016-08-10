package com.dolphin.updatecase.settings;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号:
 * <p>
 * 脚本描述:设置（默认）_网页内容
 * 
 * @author yytang
 *
 */
@TestNumber("test_027")
@TestClass("设置（默认）_网页内容")
public class CheckWebContentTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testCheckPageContent() {
        uiUtil.skipWelcome();
        // 进入General界面
        uiUtil.enterSetting(false);
        // 验证：Text Size设置为默认100%
        uiUtil.assertSearchText("pref_text_size", "100%", -1, false, false, 0, null);
        // 验证： Always show address and menu bar 为默认关
        uiUtil.assertSearchText("pref_keep_bars_title", null, -1, true, false, 2, path);
        // 验证： Flah player为默认On demand
        uiUtil.assertSearchText("pref_content_plugins", "pref_plugin_state_choices", 1, false,
                false, 0, null);

        // 滑动到ADVANCED界面
        scrollToAndvanced();
        // 验证：Page scrolling speed为默认100%
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("sliding_speed_on_pages", "100%", -1, false, false, 0, null);
        }
        // 验证：load images为默认Always on
        uiUtil.assertSearchText("pref_content_load_images", "pref_load_images_choices", 0, false,
                false, 0, null);
        // 验证：Enable javascript选项为默认开
        uiUtil.assertSearchText("pref_content_javascript", null, -1, true, true, 2, path);
        // 验证：阻止弹出式窗口选项为默认开
        uiUtil.assertSearchText("pref_content_block_popups", null, -1, true, true, 2, path);
        // 验证：接收Cookies开关为默认开
        uiUtil.assertSearchText("pref_security_accept_cookies", null, -1, true, true, 2, path);
        // 验证：设置text encoding为默认（Latin-1 ISO-8859-1）
        uiUtil.assertSearchText("pref_default_text_encoding", "Latin-1 (ISO-8859-1)", -1, false,
                false, 0, null);
        // 验证：Auto-fit pages为默认开
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("pref_content_autofit", null, -1, true, true, 2, path);
        }
        // 验证：Flash game mode为默认关
        // uiUtil.assertSearchText("pref_content_flash_game_mode", null, -1,
        // true, false, 2, path);
    }

    private void scrollToAndvanced() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, true, 1f, 1f);
        solo.sleep(Res.integer.time_wait);
    }

}
