package com.dolphin.testcase.addressbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;

import com.adolphin.common.Res;

import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1575
 * <p>
 * 脚本描述：关键词搜索推荐来源于搜索引擎的搜索联想
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1575")
@TestClass("关键词搜索推荐来源于搜索引擎的搜索联想")
public class CheckSearchRecommendKeywordTest extends BaseTest {

    public void testCheckSearchRecommendKeyword() {
        uiUtil.skipWelcome();
        // 预置条件：当前为浏览器主页，当前搜索引擎为Google
        uiUtil.setDefaultEngine("Google");

        // 步骤一：点击地址栏,输入关键字"test",等待5s左右
        // 验证：地址栏下方出现Google关键字搜索推荐
        clickAddressBar();
        checkKeywordRecommend();
        // 点击左侧搜索引擎图标,切换为其它搜索引擎(如:Bing) -> 重复步骤1
        // 验证：地址栏下方出现Bing关键字搜索推荐
        changeEngine(2);
        checkKeywordRecommend();
        // 切换为其它搜索引擎,重复步骤1
        // 验证：地址栏下方出现相应搜索引擎的关键字搜索推荐
        changeEngine(3);
        checkKeywordRecommend();
    }

    private void checkKeywordRecommend() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("search_bar"));
        solo.sleep(Res.integer.time_wait);
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        int size = solo.getViews().size();
        solo.enterText(0, "test");
        solo.sleep(5 * 1000);

        // 验证：地址栏下方出现相应搜索引擎的关键词推荐
        assertTrue("没有出现关键词推荐", solo.getViews().size() != size);
        assertTrue("没有出现一个相应的关键词推荐", solo.searchText("test"));
        // 验证：每一个都是相应的关键词推荐
        for (View view : caseUtil.getViewsByClassName("av", false)) {
            assertTrue("出现非相应的关键词推荐", ((TextView) view).getText().toString().contains("test"));
        }
    }

    private void changeEngine(int i) {
        solo.clickOnView(solo.getView("id/search_engine"));
        solo.sleep(Res.integer.time_wait);
        ViewGroup engineGroup = (ViewGroup) solo.getView("id/search_engine_gridview");
        solo.clickOnView(engineGroup.getChildAt(i));
        solo.sleep(Res.integer.time_wait);
    }

    // 点击地址栏
    private void clickAddressBar() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
    }
}