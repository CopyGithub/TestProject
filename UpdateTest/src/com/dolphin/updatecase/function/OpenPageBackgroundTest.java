package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_070")
@TestClass("升级后功能检查_后台打开网页")
public class OpenPageBackgroundTest extends BaseTest {

    public void testOpenPageBackground() {
        uiUtil.skipWelcome();

        // 打开Tabs模式
        uiUtil.enterSideBar(false);
        uiUtil.setControlPanelView(true, 1);

        // 访问百度一下：www.baidu.com，长按'新闻'，选择open in background
        // 验证：后台打开百度新闻
        uiUtil.visitUrl("www.baidu.com");
        solo.sleep(Res.integer.time_open_url);
        caseUtil.slideDireciton(null, false, 0.3f, 1f);
        solo.clickLongOnText("新闻");
        caseUtil.clickOnSelections(1);
        solo.sleep(Res.integer.time_open_url);
        // 新增一个百度新闻的 tab
        assertTrue("后台未打开百度新闻", uiUtil.getTabNumber() == 2);
    }
}
