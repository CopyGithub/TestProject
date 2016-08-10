package com.dolphin.updatecase.updatefunction;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_040")
@TestClass("升级后功能检查_搜索引擎")
public class SearchEngineTest extends BaseTest {
    public void testSearchEngine() {
        uiUtil.skipWelcome();
        // 验证：bing搜索，进入关键字key搜索结果页
        uiUtil.visitUrl("key");
        assertTrue("Failed to search by bing.", uiUtil.searchByEngineCorrectly("bing"));
        // 判断是否搜索的关键字key
        assertTrue("Failed to search keyword 'key'", uiUtil.searchByEngineCorrectly("key"));

        // 切换搜索引擎为google
        solo.goBack();
        checkChangeEngine();
        // 验证:进入关键字test搜索结果页
        uiUtil.visitUrl("test");
        assertTrue("Failed to search keyword 'test'", uiUtil.searchByEngineCorrectly("test"));
    }

    private void checkChangeEngine() {
        // 点击地址栏 -> 点击搜索引擎图标,点击其它搜索引擎图标(如:google!)
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/search_engine"));
        solo.sleep(Res.integer.time_wait);
        int searchSize = solo.getViews().size();
        solo.clickOnText("Google");
        solo.sleep(Res.integer.time_wait);
        // 验证：搜索引擎列表关闭,当前搜索引擎图标显示为google！图标(TODO)
        assertTrue("搜索引擎列表没有关闭", solo.getViews().size() != searchSize);
    }
}
