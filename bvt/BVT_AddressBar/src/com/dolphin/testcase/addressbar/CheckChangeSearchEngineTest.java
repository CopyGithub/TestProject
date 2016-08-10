
package com.dolphin.testcase.addressbar;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1592
 * <p>
 * 脚本描述：当前搜索引擎切换成功
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1592")
@TestClass("当前搜索引擎切换成功")
public class CheckChangeSearchEngineTest extends BaseTest {

    public void testCheckChangeSearchEngine() {
        uiUtil.skipWelcome();
        // 预置条件：当前为浏览器主页，当前搜索引擎为Google
        uiUtil.setDefaultEngine("Google");

        // 验证：搜索引擎列表关闭,当前搜索引擎图标显示为Yahoo！图标
        checkChangeEngine();

        // 验证：显示Yahoo搜索dog结果界面
        checkSearchResult();
    }

    private void checkSearchResult() {
        // 地址栏输入关键字(如:dog),点击"→"
        solo.enterText(0, "dog");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/go"));
        solo.sleep(Res.integer.time_wait);
        // 验证：显示Yahoo搜索dog结果界面
        // 判断是否成功选择Yahoo进行搜索
        assertTrue("Failed to search by yahoo.", uiUtil.searchByEngineCorrectly("yahoo"));
        // 判断是否搜索的关键字dog
        assertTrue("Failed to search keyword.", uiUtil.searchByEngineCorrectly("dog"));
    }

    private void checkChangeEngine() {
        // 点击地址栏 -> 点击搜索引擎图标,点击其它搜索引擎图标(如:Yahoo!)
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/search_engine"));
        solo.sleep(Res.integer.time_wait);
        int searchSize = solo.getViews().size();
        solo.clickOnText("Yahoo");
        solo.sleep(Res.integer.time_wait);
        // 验证：搜索引擎列表关闭,当前搜索引擎图标显示为Yahoo！图标(TODO)
        assertTrue("搜索引擎列表没有关闭", solo.getViews().size() != searchSize);
    }

}