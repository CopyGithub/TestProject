package com.dolphin.testcase.setting;

import java.util.HashMap;
import java.util.Map;

import android.widget.ListView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-935
 * <p>
 * 脚本描述: 验证"Language"设置项UI显示正常
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-935")
@TestClass("验证\"Language\"设置项UI显示正常")
public class CheckLanguaUITest extends BaseTest {
    private int m_item;

    public void testCheckLanguaUITest() {

        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证: 进入"LANGUAGE"界面,标题左侧显示返回图标"<", 右上角"Save"按钮默认灰显
        assertTrue("没有正确进入\"LANGUAGE\"界面", isEnterLanguage());
        uiUtil.isBackIconOnTheLeft();
        // 验证右上角显示有save且不可点击为灰色
        assertTrue("Text on 'Save' button is incorrect.", isShowcheckSaveBtn());
        assertFalse("'Save' btn should be unclickable.", isUnenbledSaveBtn());
        // 验证语言列表是否正确
        assertTrue("Language " + m_item + " is incorrect.", isRightShowLanguageList());
        // 点击 < 返回
        assertTrue("Faile to enter settings acitvity", isSettingsInterface());
    }

    private boolean isSettingsInterface() {
        caseUtil.clickOnView("btn_done");
        solo.sleep(Res.integer.time_wait);
        return uiUtil.waitForInterfaceByTitle("menu_preferences", 0);
    }

    private boolean isShowcheckSaveBtn() {
        String textOnSaveBtn = ((TextView) solo.getView("btn_save")).getText().toString();
        String expectedText = caseUtil.getTextByRId("pref_save");
        // 验证右上角显示有save且不可点击
        return textOnSaveBtn.equals(expectedText);
    }

    private boolean isUnenbledSaveBtn() {
        return solo.getView("id/btn_save").isEnabled();
    }

    private boolean isEnterLanguage() {
        solo.clickOnText(caseUtil.getTextByRId("pref_language_string"));
        solo.sleep(Res.integer.time_wait);
        return uiUtil.waitForInterfaceByTitle("pref_language_string", 10 * 1000);
    }

    private boolean isRightShowLanguageList() {
        // 将语言列表放入到一个map
        Map<String, String> languageMap = new HashMap<String, String>();
        int mapIndex = 0;
        final ListView listView = (ListView) solo.getView("id/list_view");
        int reachBottomFlag = 0;
        do {
            for (int i = 0; i < listView.getChildCount(); i++) {
                String text = ((TextView) listView.getChildAt(i)).getText().toString();
                if (!languageMap.containsValue(text)) {
                    languageMap.put(String.valueOf(mapIndex), text);
                    mapIndex++;
                }
            }
            caseUtil.slideDireciton(null, false, 3 / 4f, 1f);
            if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
                reachBottomFlag++;
            }
        } while (reachBottomFlag < 2);
        for (int i = 0; i < languageMap.size(); i++) {
            String expectedLan = caseUtil.getTextByRId("pref_language_choices", i);
            String factLan = languageMap.get(String.valueOf(i));
            if (!expectedLan.equals(factLan)) {
                m_item = i;
                return false;
            }
        }
        return true;
    }
}