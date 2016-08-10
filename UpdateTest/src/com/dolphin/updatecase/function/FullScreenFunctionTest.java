package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_063")
@TestClass("升级后功能检查_全屏模式")
public class FullScreenFunctionTest extends BaseTest {

    public void testFullScreenFunction() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);

        // 验证：右侧边栏全屏模式处于开启状态
        assertTrue("全屏模式未保持打开", uiUtil.getControlPanelView(0).isSelected());

        // 打开百度网页
        uiUtil.visitUrl("www.baidu.com");

        solo.sleep(Res.integer.time_open_url);
        solo.clickOnButton(1);
        // 验证：status bar,title bar和menu bar均隐藏
        caseUtil.slideDireciton(null, false, 0.6f, 1f);
        uiUtil.isBarShow(false, false);
        solo.goBack();

        // 关闭全屏模式
        uiUtil.enterSideBar(false);
        solo.clickOnView(uiUtil.getControlPanelView(0));
        // 验证：弹出toast提示:Fullscreen is turned off
        assertTrue("Fail to show . Fullscreen is turned off",
                solo.searchText(caseUtil.getTextByRId("switch_to_normal_mode")));
        // 验证：status bar显示
        uiUtil.isBarShow(true, true);
    }
}
