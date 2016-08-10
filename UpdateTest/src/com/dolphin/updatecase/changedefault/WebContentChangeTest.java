package com.dolphin.updatecase.changedefault;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_056")
@TestClass("设置（改变默认）_网页内容")
public class WebContentChangeTest extends BaseTest {
    private int[] path = { 1, 0 };

    public void testWebContentChange() {
        uiUtil.skipWelcome();
        // 进入General界面
        uiUtil.enterSetting(false);
        // 验证：Text Size设置为默认150%
        uiUtil.assertSearchText("pref_text_size", "150%", -1, false, false, 0, null);
        // 验证： Always show address and menu bar 为开
        uiUtil.assertSearchText("pref_keep_bars_title", null, -1, true, true, 2, path);
        // 验证： Flash player为off
        uiUtil.assertSearchText("pref_content_plugins", "pref_plugin_state_choices", 2, false,
                false, 0, null);

        // 滑动到ADVANCED界面
        scrollToAndvanced();
        // 验证：Page scrolling speed为80%
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("sliding_speed_on_pages", "80%", -1, false, false, 0, null);
        }
        // 验证：load images为Always off
        uiUtil.assertSearchText("pref_content_load_images", "pref_load_images_choices", 1, false,
                false, 0, null);
        // 验证：Enable javascript选项为关
        uiUtil.assertSearchText("pref_content_javascript", null, -1, true, false, 2, path);
        // 验证：阻止弹出式窗口选项为关
        uiUtil.assertSearchText("pref_content_block_popups", null, -1, true, false, 2, path);
        // 验证：接收Cookies开关为为关
        uiUtil.assertSearchText("pref_security_accept_cookies", null, -1, true, false, 2, path);
        // 验证：设置text encoding为Unicode（UTF-8）
        uiUtil.assertSearchText("pref_default_text_encoding", "pref_default_text_encoding_choices",
                1, false, false, 0, null);
        // 验证：Auto-fit pages为关
        if (uiUtil.isWebkit()) {
            uiUtil.assertSearchText("pref_content_autofit", null, -1, true, false, 2, path);
        }
        // 验证：Flash game mode为开
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, -1, true, true, 2, path);
    }

    private void scrollToAndvanced() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, true, 1f, 1f);
        solo.sleep(Res.integer.time_wait);
    }
}
