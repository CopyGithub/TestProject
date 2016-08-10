package com.dolphin.testcase.addressbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1576
 * <p>
 * 脚本描述：点击搜索引擎关键词搜索推荐后响应正常
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1576")
@TestClass("点击搜索引擎关键词搜索推荐后响应正常")
public class CheckClickSearchRecommendKeywordTest extends BaseTest {
    private View topSuggestItemView;

    public void testCheckClickSearchRecommendKeyword() {
        uiUtil.skipWelcome();
        // 预置条件：当前为浏览器主页，当前搜索引擎为Google
        uiUtil.setDefaultEngine("Google");

        // 验证：地址栏下显示关键字搜索推荐
        checkKeywordRecommend();

        // 验证：进入当前搜索引擎搜索"lv"结果页
        checkClickKeyword();
    }

    private void checkClickKeyword() {
        View clickView = caseUtil.getViewByIndex(topSuggestItemView, new int[] { 0, 1 });
        String clickString = ((TextView) clickView).getText().toString();
        solo.clickOnView(clickView);
        solo.sleep(4 * 1000);
        // 判断是否成功选择Google进行搜索
        assertTrue("Failed to search by google.", uiUtil.searchByEngineCorrectly("google"));
        // 判断是否搜索的关键字
        assertTrue("Failed to search keyword.", uiUtil.searchByEngineCorrectly(clickString));
    }

    private void checkKeywordRecommend() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
        EditText address = (EditText) solo.getView("search_input");
        caseUtil.setText(address, "");
        solo.sleep(Res.integer.time_wait);
        caseUtil.setText(address, "l");
        solo.sleep(4 * 1000);
        // 验证：地址栏下显示关键字搜索推荐
        boolean flag = caseUtil.waitForViewByClassName("TopSuggestItemView", false, 20 * 1000);
        topSuggestItemView = caseUtil.getViewByClassName("TopSuggestItemView", 0, false);
        assertTrue("没有出现和搜索引擎相关的关键词推荐,可能是网络不好没有加载成功", flag);
    }
}