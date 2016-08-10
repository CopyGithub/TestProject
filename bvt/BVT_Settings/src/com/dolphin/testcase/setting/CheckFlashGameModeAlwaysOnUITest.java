package com.dolphin.testcase.setting;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-2144
 * <P>
 * 脚本描述：验证"Flash game mode"设置项UI显示正常（Flash player状态是Always on）
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-2144")
@TestClass("验证\"Flash game mode\"设置项UI显示正常（Flash player状态是Always on）")
public class CheckFlashGameModeAlwaysOnUITest extends BaseTest {
    private static String flash_player;
    int[] path = new int[] { 1, 0 };

    public void testCheckFlashGameModeOnUITest() {
        uiUtil.skipWelcome();
        // shell版本不需检测，无Flash game mode设置项
        if (!uiUtil.isWebkit()) {
            return;
        }
        init();
        // 验证：设置项名称为"Flash game mode",右侧显示开关
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, false, 2, path);
        // 验证：点击该设置项，开关打开
        solo.clickOnText(caseUtil.getTextByRId("pref_content_flash_game_mode"));
        solo.sleep(Res.integer.time_wait);
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, true, 2, path);
        // 验证：再次点击，开关关闭
        solo.clickOnText(caseUtil.getTextByRId("pref_content_flash_game_mode"));
        solo.sleep(Res.integer.time_wait);
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, false, 2, path);
    }

    private void init() {
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_content_plugins"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_plugin_state_choices", 0));
        solo.sleep(Res.integer.time_wait);
        View parent = (View) caseUtil.getViewByText("pref_content_plugins", 0, true, true, true)
                .getParent();
        TextView child = (TextView) caseUtil.getViewByIndex(parent, new int[] { 1 });
        flash_player = child.getText().toString();
        assertTrue("Flash player mode should be 'Always on'",
                flash_player.equals(caseUtil.getTextByRId("pref_plugin_state_choices", 0)));
        solo.clickOnText(caseUtil.getTextByRId("settings_advanced_title"));
        solo.sleep(Res.integer.time_wait);
    }
}
