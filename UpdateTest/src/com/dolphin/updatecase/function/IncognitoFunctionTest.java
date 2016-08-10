package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_067")
@TestClass("升级后功能检查_隐私模式")
public class IncognitoFunctionTest extends BaseTest {
    public void testIncognitoFunction() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);

        // 验证：右侧边栏隐私模式处于开启状态
        assertTrue("隐私模式未保持打开", uiUtil.getControlPanelView(4).isSelected());
        // TODO 地址栏有眼睛图标？？
        // 验证：访问'百度一下'，不存在'百度一下'的浏览历史
        uiUtil.visitUrl("www.baidu.com");
        solo.goBack();
        solo.goBack();
        uiUtil.enterBookmark();
        caseUtil.clickOnText("History", -1, true);
        assertTrue("存在'百度一下'的浏览历史", !solo.searchText("百度"));
        solo.goBack();

        // 关闭隐私模式
        uiUtil.enterSideBar(false);
        solo.clickOnView(uiUtil.getControlPanelView(4));
        // 验证：弹出toast提示:Incognito browsing is turned off
        assertTrue("Fail to show . Incognito browsing is turned off",
                solo.searchText("Incognito browsing is turned off"));

        // 验证：访问hao123，存在这条历史记录
        uiUtil.visitUrl("www.hao123.com");
        solo.goBack();
        solo.goBack();
        uiUtil.enterSideBar(true);
        assertTrue("不存在'hao123'的浏览历史", solo.searchText("hao123")||solo.searchText("Webpage not available"));
    }
}
