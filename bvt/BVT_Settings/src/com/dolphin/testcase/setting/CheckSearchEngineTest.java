package com.dolphin.testcase.setting;

import java.util.ArrayList;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.adolphin.common.BaseTest;

import com.adolphin.common.Res;

import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-945
 * <p>
 * 脚本描述： 验证Search engine界面设置项显示整齐
 * <p>
 * 脚本修改了数据, 重跑需要恢复
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-945")
@TestClass("验证Search engine界面设置项显示整齐")
public class CheckSearchEngineTest extends BaseTest {
    private static String defaultSearEngineName;
    private final String bing = "Bing";

    public void testCheckSearchEngineTest() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);
        /*
         * 验证:进入"Search engine"界面,依次显示: •单选列表： "Google", "Bing","Yahoo!",
         * "DuckDuckGo" •Show Search suggestion开关（开关：默认开启）
         */
        checkSearchEngine();
        // 验证：返回上级SETTINGS界面,"Search engine"属性值未更改
        checkGoback();
        // TODO 验证：Bing被选中，后面的单选框显示为主题色
        isCheckBing();
        // 验证：返回Settings，"Search engine"属性值显示为"Bing"
        assertTrue("Failed to change search engine", isChangeSearchEngine());
        // 验证：Show Search suggestion开关关闭
        assertFalse("Failed to close 'show search suggestion'", isCloseSearchSuggestion(true));
        // 验证：Show Search suggestion开关开启
        assertTrue("Failed to open 'show search suggestion'", isCloseSearchSuggestion(false));
    }

    private boolean isCloseSearchSuggestion(boolean isEnabled) {
        if (isEnabled) {
            caseUtil.clickOnText("pref_content_search_engine", 0, true);
            solo.sleep(Res.integer.time_wait);
        }
        caseUtil.clickOnView("checkbox");
        solo.sleep(Res.integer.time_wait);
        return isShowSearchSuggestion();
    }

    private boolean isChangeSearchEngine() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return bing.equals(getUsingSearchEngine());
    }

    private void isCheckBing() {
        caseUtil.clickOnText("pref_content_search_engine", 0, true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(bing);
        solo.sleep(Res.integer.time_wait);
    }

    private void checkSearchEngine() {
        defaultSearEngineName = getUsingSearchEngine();
        caseUtil.clickOnText("pref_content_search_engine", 0, true);
        solo.sleep(Res.integer.time_wait);
        ArrayList<View> views = utils.getChildren(solo.getView("vertical_engine_list"), false);
        uiUtil.compareSearchEngineListWithOrigin(views, false);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Show search suggestion默认不是开启的", isShowSearchSuggestion());
    }

    private void checkGoback() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        String usingSearchEngine = getUsingSearchEngine();
        assertTrue("Change attribution of Search engine , the name of search engine should be "
                + defaultSearEngineName + ",but it is " + usingSearchEngine,
                usingSearchEngine.equals(defaultSearEngineName));
    }

    private String getUsingSearchEngine() {
        View searchEngine = caseUtil.getViewByText("pref_content_search_engine", 0, true, true, true);
        View useEngine = caseUtil.getViewByIndex(utils.getParent(searchEngine, 2), new int[] { 1 });
        return ((TextView) useEngine).getText().toString();
    }

    private boolean isShowSearchSuggestion() {
        return ((CheckBox) solo.getView("checkbox")).isChecked();
    }
}