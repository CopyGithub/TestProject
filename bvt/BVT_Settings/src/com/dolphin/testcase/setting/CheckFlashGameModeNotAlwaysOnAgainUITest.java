package com.dolphin.testcase.setting;

import android.view.View;
import android.widget.TextView;
import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-2145
 * <p>
 * 脚本描述：验证"Flash game mode"设置项UI显示正常（Flash player状态非Always on）
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-2145")
@TestClass("验证\"Flash game mode\"设置项UI显示正常（Flash player状态非Always on）")
public class CheckFlashGameModeNotAlwaysOnAgainUITest extends BaseTest {
    private static String flash_player;
    int[] path = new int[] { 1, 0 };

    public void testcheckFlashGameModeNotAlwaysOnUITest() {
        uiUtil.skipWelcome();
        if (!uiUtil.isWebkit()) {
            return;
        }
        init();
        // 验证：设置项名称为"Flash game mode",右侧显示开关
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, false, 2, path);
        solo.clickOnText(caseUtil.getTextByRId("pref_content_flash_game_mode"));
        solo.sleep(Res.integer.time_wait);
        // 验证：标题：Flash game mode
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, false, false, 0, null);
        // 验证：正文：Turning on flash player will provide you better game experience
        uiUtil.assertSearchText("flash_game_mode_tip", null, 0, false, false, 0, null);
        // 验证：按钮：Cancel ，Turn on
        assertTrue("Button Cancel is not found", solo.searchButton(caseUtil.getTextByRId("cancel")));
        assertTrue("Button 'Turn on' is not found",
                solo.searchButton(caseUtil.getTextByRId("turn_on")));
        // 点击Turn on
        solo.clickOnButton(caseUtil.getTextByRId("turn_on"));
        solo.sleep(Res.integer.time_wait);
        // 验证：Flash game mode开关开启
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, true, 2, path);
        // 验证：Flash player状态变为Always On
        assertTrue(
                "Flash player mode should change to 'Always on'",
                getFlashPlayerStateAndBackToAdvanced().equals(
                        caseUtil.getTextByRId("pref_plugin_state_choices", 0)));
        solo.clickOnText(caseUtil.getTextByRId("settings_advanced_title"));
        solo.sleep(Res.integer.time_wait);

        // 再次点击该设置项
        solo.clickOnText(caseUtil.getTextByRId("pref_content_flash_game_mode"));
        solo.sleep(Res.integer.time_wait);
        // 验证：Flash game mode开关关闭
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, false, 2, path);

        // 切换至General Tab
        solo.clickOnText(caseUtil.getTextByRId("settings_general_title"));
        solo.sleep(Res.integer.time_wait);
        // 将Flash player状态设置为非Always On状态
        solo.clickOnText(caseUtil.getTextByRId("pref_content_plugins"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_plugin_state_choices", 1));
        solo.sleep(Res.integer.time_wait);
        String str = getFlashPlayerStateAndBackToAdvanced();
        solo.clickOnText(caseUtil.getTextByRId("settings_advanced_title"));
        solo.sleep(Res.integer.time_wait);
        // 再次点击Flash game mode开关
        solo.clickOnText(caseUtil.getTextByRId("pref_content_flash_game_mode"));
        solo.sleep(Res.integer.time_wait);
        // 验证：Flash game mode开关开启
        uiUtil.assertSearchText("pref_content_flash_game_mode", null, 0, true, true, 2, path);
        // 验证Flash player状态保持不变
        assertTrue("Flash Player should stay unchanged", getFlashPlayerStateAndBackToAdvanced()
                .equals(str));
    }

    private void init() {
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        flash_player = getFlashPlayerStateAndBackToAdvanced();
        if (flash_player.equals(caseUtil.getTextByRId("always_on"))) {
            solo.clickOnText(caseUtil.getTextByRId("pref_content_plugins"));
            solo.sleep(Res.integer.time_wait);
            solo.clickOnText(caseUtil.getTextByRId("pref_plugin_state_choices", 1));
            solo.sleep(Res.integer.time_wait);
        }
        solo.clickOnText(caseUtil.getTextByRId("settings_advanced_title"));
        solo.sleep(Res.integer.time_wait);
    }

    private String getFlashPlayerStateAndBackToAdvanced() {
        solo.clickOnText(caseUtil.getTextByRId("settings_general_title"));
        solo.sleep(Res.integer.time_wait);
        View parent = (View) caseUtil.getViewByText("pref_content_plugins", 0, true, true, true)
                .getParent();
        TextView child = (TextView) caseUtil.getViewByIndex(parent, new int[] { 1 });
        return child.getText().toString();
    }
}
