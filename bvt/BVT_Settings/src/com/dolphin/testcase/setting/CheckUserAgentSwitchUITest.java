
package com.dolphin.testcase.setting;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1063
 * <p>
 * 脚本描述: 验证"User agent"设置项UI显示正常
 * <p>
 * 由于有修改设置项的功能, 添加Reinstall保证重复跑出现问题时也能正确执行
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1063")
@TestClass("验证\"User agent\"设置项UI显示正常")
public class CheckUserAgentSwitchUITest extends BaseTest {
    private String defaultUA;

    public void testCheckUserAgentSwitchUITest() {
        uiUtil.skipWelcome();
        enterUserAgent();
        // 验证：单选列表:"Andriod","Desktop","iPhone","iPad", "Custom"
        assertTrue("Failed to search Android item text",
                checkItem("pref_development_ua_choices", 0));
        assertTrue("Failed to search Desktop item text",
                checkItem("pref_development_ua_choices", 1));
        assertTrue("Failed to search iPhone item text", checkItem("pref_development_ua_choices", 2));
        assertTrue("Failed to search iPad item text", checkItem("pref_development_ua_choices", 3));
        assertTrue("Failed to search Custom item text", checkItem("pref_development_ua_choices", 4));
        // 验证： 底部为"OK"按钮
        assertTrue("The buttom button is not right", checkbutton());
        // 验证：点击任意选项,如:"Desktop" -> 点击手机Back键
        // 验证：弹框关闭,"User agent"属性值未更新
        assertTrue("The user agent should not be changed",
                checkUA("pref_development_uastring", defaultUA, true));
        // 再次点击"User agent"设置项,点击其它选项,如:"ipad" ->点击"OK"
        changeUserAgentString("pref_development_uastring", "pref_development_ua_choices", 3);
        // 按钮"User agent"属性值显示为"ipad"
        assertTrue(
                "The user agent should  be changed to iPad",
                checkUA("pref_development_uastring",
                        caseUtil.getTextByRId("pref_development_ua_choices", 3), false));
        assertTrue("click on custom should show edit text", isShowCustomTextEdit());
        assertTrue("click on except custom should not show edit text", isGoneCustomTextEdit());
        // 点击 < 返回
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private boolean isSettingsInterface() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        ArrayList<View> views = caseUtil.getViews(null, "action_bar_title_container");
        if (views.size() == 1) {
            String title = ((TextView) ((ViewGroup) views.get(0)).getChildAt(1)).getText()
                    .toString();
            if (title.equalsIgnoreCase(caseUtil.getTextByRId("pref_root_title"))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkUA(String rId, String choiceUA, boolean isGoback) {
        return choiceUA.equals(getUA(rId, isGoback));
    }

    private String getUA(String rId, boolean isGoback) {
        if (isGoback) {
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
        solo.sleep(Res.integer.time_wait);
        View root = utils.getParent(caseUtil.getViewByText(rId, 0, true, true, true), 1);
        String ua = ((TextView) caseUtil.getViewByIndex(root, new int[] { 1 })).getText().toString();
        return ua;
    }

    private boolean checkbutton() {
        View view = solo.getView("id/button2");
        return ((Button) view).getText().equals(caseUtil.getTextByRId("btn_ok"));
    }

    private boolean checkItem(String rId, int num) {
        String text = caseUtil.getTextByRId(rId, num);
        return solo.searchText(text, true);
    }

    private void enterUserAgent() {
        uiUtil.enterSetting(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_development_uastring"));
        solo.sleep(Res.integer.time_wait);
        defaultUA = getUA("pref_development_uastring", true);
        solo.clickOnText(caseUtil.getTextByRId("pref_development_uastring"));
        solo.sleep(Res.integer.time_wait);
    }

    private boolean isGoneCustomTextEdit() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_development_ua_choices", 1));
        solo.sleep(Res.integer.time_wait);
        View editView = solo.getView("id/agent_custom_text");
        return !editView.isShown();
    }

    private boolean isShowCustomTextEdit() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_development_uastring"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("pref_development_ua_choices", 4));
        solo.sleep(Res.integer.time_wait);
        View editView = solo.getView("id/agent_custom_text");
        return editView.isShown();
    }

    private void changeUserAgentString(String id1, String id2, int num) {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId(id1));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId(id2, num));
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("button2");
        solo.sleep(Res.integer.time_wait);
        defaultUA = getUA(id1, false);
    }
}