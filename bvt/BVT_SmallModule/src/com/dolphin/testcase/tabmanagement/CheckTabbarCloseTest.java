package com.dolphin.testcase.tabmanagement;

import android.view.View;
import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-184
 * <p>
 * 脚本描述：验证tab正常关闭
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-184")
@TestClass("验证中屏机竖屏上,点击海豚键正常展开Menu面板")
public class CheckTabbarCloseTest extends BaseTest {
    final private String aUrl = Res.string.url_test;
    final private String bUrl = Res.string.url_aaa;
    final private String cUrl = Res.string.url_downloadtest;

    public void testCheckTabbarClose() {
        uiUtil.skipWelcome();
        // 小屏机不具备此功能，无需验证
        if (caseUtil.getDisplayRange() == 0)
            return;
        init();
        // 动作：点击tab A的"X"图标
        // 验证：Tab A关闭,tab B为选中状态,当前显示为tab B网页内容
        checkCloseTabA();

        // 动作：单击tab B
        // 验证：Tab B未关闭
        checkClickTabB();

        // 动作： 双击tab c
        // 验证：Tab C关闭
        checkDoubleClickTabC();

        // 动作：单击主页tab/双击主页tab
        // 验证：点击过程中tab文案和"X"图标闪烁,主页tab未关闭;双击则主页Tab关闭
        checkClickMainTab();
    }

    private void checkClickMainTab() {
        // TODO
        /*
         * 验证：单击主页tab/双击主页tab，点击过程中tab文案和"X"图标闪烁,主页tab未关闭 双击则主页关闭
         */
    }

    private void checkDoubleClickTabC() {
        View tabbarView = caseUtil.getViewByClassName("AnimHighLightLinearLayout", 0, false);
        // 跳到tabC
        solo.clickOnView(((ViewGroup) tabbarView).getChildAt(1));
        solo.sleep(Res.integer.time_wait);
        // TODO 双击tabC
        solo.clickOnView(((ViewGroup) tabbarView).getChildAt(1));
        solo.clickOnView(((ViewGroup) tabbarView).getChildAt(1));
        solo.sleep(Res.integer.time_wait);
        // 验证tabC关闭
        // assertTrue("tab C未关闭", tabbar.tabNumber(1));
    }

    private void checkClickTabB() {
        View tabbarView = caseUtil.getViewByClassName("AnimHighLightLinearLayout", 0, false);
        solo.clickOnView(((ViewGroup) tabbarView).getChildAt(0));
        solo.sleep(Res.integer.time_wait);
        assertTrue("tab B关闭了", uiUtil.getTabNumber() == 2);
    }

    private void checkCloseTabA() {
        solo.sleep(Res.integer.time_wait);
        View tabbarView = caseUtil.getViewByIndex(uiUtil.getTabBar(), new int[] { 0, 2 });
        solo.clickOnView(tabbarView);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Tab A未关闭", uiUtil.getTabNumber() == 2);
        assertTrue("当前显示并不是tab B网页内容", uiUtil.checkURL(bUrl));
    }

    private void init() {
        // 打开Tabs
        uiUtil.clickOnControlPanel(true, 1);
        // tabA
        visitURL(aUrl);
        // tabB 新建1个tab
        uiUtil.addNewTab(1);
        visitURL(bUrl);
        // tabC 新建1个tab
        uiUtil.addNewTab(1);
        visitURL(cUrl);

        // 滑动到最左边，点击tabA
        // 获取标签栏
        View tabbarView = utils.getParent(uiUtil.getTabBar(), 1);
        solo.sleep(Res.integer.time_wait);
        // 滑到最左端
        caseUtil.slideDireciton(tabbarView, true, 1 / 4f, 1f);
        solo.sleep(Res.integer.time_wait);
        View tabAView = caseUtil.getViewByIndex(tabbarView, new int[] { 0, 0 });
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(tabAView);
        /*
         * 预置条件 当前为浏览器主页,已打开三个网页tab,选中tab A
         */
        assertTrue("不满足预置条件：三个tab", uiUtil.getTabNumber() == 3);
        assertTrue("不满足预置条件：选中tabA", uiUtil.checkURL(aUrl));
    }

    // 访问一个网页
    private void visitURL(String myUrl) {
        solo.sleep(Res.integer.time_wait);
        uiUtil.visitUrl(myUrl);
        solo.sleep(Res.integer.time_wait);
        uiUtil.waitForWebPageFinished();
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
    }
}