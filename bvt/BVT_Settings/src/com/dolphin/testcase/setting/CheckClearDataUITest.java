package com.dolphin.testcase.setting;

import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-960
 * <p>
 * 脚本描述: 验证"Clear Data"设置项UI显示正常
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-960")
@TestClass("验证\"Clear Data\"设置项UI显示正常")
public class CheckClearDataUITest extends BaseTest {
    private Button btnButton;
    private int[] path = { 0 };

    public void testCheckClearDataUITest() {
        uiUtil.skipWelcome();
        enterClearData();
        // 检查clear data 的中复选列表顺序正确
        // 验证： 复选列表:"Browsing history"（默认勾选）
        uiUtil.assertSearchText("pref_privacy_clear_history", null, -1, true, true, 1, path);
        // 验证： "Cache and site data"（默认勾选）
        uiUtil.assertSearchText("pref_privacy_clear_cache", null, -1, true, true, 1, path);
        // 验证："Cookies"（默认勾选）
        uiUtil.assertSearchText("pref_privacy_clear_cookies", null, -1, true, true, 1, path);
        // 验证："Form data"（默认不勾选）
        uiUtil.assertSearchText("pref_privacy_clear_form_data", null, -1, true, false, 1, path);
        // 验证："Passwords"（默认不勾选）
        uiUtil.assertSearchText("pref_privacy_clear_passwords", null, -1, true, false, 1, path);
        // 验证： "Location access"（默认不勾选）
        uiUtil.assertSearchText("pref_privacy_clear_geolocation_access", null, -1, true, false, 1,
                path);
        // 验证：选项框在文字左边
        assertTrue("Checkbox shouble be in left of item.", isPositionRighBetweenCheckBoxAndItem());
        // 验证：按钮 Clear selected data
        assertTrue("Failed to show 'Clear selected data' buttom", isShowBtn());
        // 验证：按钮字白色
        assertTrue("The buttom text should be white color.", isWhiteTxtCheckBtn());
        // TODO 验证：按钮红色，这里还没有验证
        // assertTrue("The buttom should be red color.", isRedCheckBtn());
        // 点击手机返回键，返回上一级Settings
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private boolean isSettingsInterface() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return uiUtil.waitForInterfaceByTitle("menu_preferences", 0);
    }

    private void enterClearData() {
        uiUtil.enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("clear_data"));
        solo.sleep(Res.integer.time_wait);
        btnButton = (Button) solo.getView("id/btn_confirm");
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isPositionRighBetweenCheckBoxAndItem() {
        for (int i = 0; i < 6; ++i) {
            solo.sleep(Res.integer.time_wait);
            TextView dataItemTextView = (TextView) solo.getView("id/title", i + 1);
            CheckBox checkBox = (CheckBox) solo.getView("id/title_check", i);
            // 验证：选项框在文字左边
            if (utils.ubietyOfView(checkBox, dataItemTextView, true, false, false) == -1) {
                return false;
            }
        }
        return true;
    }

    private boolean isShowBtn() {
        solo.sleep(Res.integer.time_wait);
        if (solo.getView("id/btn_confirm") == null) {
            return false;
        }
        return true;
    }

    private boolean isWhiteTxtCheckBtn() {
        btnButton = (Button) solo.getView("id/btn_confirm");
        solo.sleep(Res.integer.time_wait);
        int txtColor = btnButton.getCurrentTextColor();
        if (txtColor != Color.WHITE) {
            return false;
        }
        return true;
    }
}