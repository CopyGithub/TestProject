package com.dolphin.testcase.addressbar;

import android.view.View;
import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1590
 * <p>
 * 脚本描述： 搜索引擎图标显示正常
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1590")
@TestClass("搜索引擎图标显示正常")
public class CheckEngineIconTest extends BaseTest {
    public void testCheckEngineIcon() {
        uiUtil.skipWelcome();
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_change_activity);

        // 点击地址栏,地址栏左侧显示圆形当前搜索引擎图标,图标右下侧显示小三角
        checkIcons();

        // 点击搜索引擎图标,展开搜索引擎列表:
        // ·列表中显示已配置的圆形搜索引擎图标
        // ·图标下方显示搜索引擎的名称
        // ·各搜索引擎图标间距适中
        checkIconList(false);

        // 横竖屏切换,搜索引擎列表仍然展开,显示正常
        checkIconList(true);

        // 点击手机back键/列表之外,搜索引擎列表关闭
        checkIconListClose();
    }

    private void checkIconListClose() {
        int size = solo.getViews().size();
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        assertTrue("搜索引擎列表未关闭", solo.getViews().size() != size);
    }

    private void checkIconList(boolean isChanged) {
        int size = solo.getViews().size();
        if (!isChanged) {
            solo.clickOnView(solo.getView("id/search_engine"));
            solo.sleep(Res.integer.time_wait);
            assertTrue("搜索引擎列表未展开", solo.getViews().size() != size);
        } else {
            solo.setActivityOrientation(Solo.LANDSCAPE);
            solo.sleep(Res.integer.time_change_activity);
            assertTrue("搜索引擎列表未展开", solo.getViews().size() == size);
        }
    }

    private void checkIcons() {
        clickAddressBar();
        ViewGroup searchBox = (ViewGroup) solo.getView("id/search_engine");
        View engineIcon = searchBox.getChildAt(0);
        View arrowIcon = searchBox.getChildAt(1);
        assertTrue("搜索引擎图标未出现", engineIcon.getVisibility() == View.VISIBLE);
        assertTrue("小三角图标未出现", arrowIcon.getVisibility() == View.VISIBLE);
        boolean flag1 = utils.ubietyOfView(engineIcon, arrowIcon, false, false, false) != -1;
        boolean flag2 = utils.ubietyOfView(engineIcon, arrowIcon, true, false, false) != -1;
        assertTrue("小三角不是在搜索引擎图标右下侧", flag1 && flag2);
    }

    // 点击地址栏
    private void clickAddressBar() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
    }
}