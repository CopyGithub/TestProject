package com.dolphin.updatecase.function;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_068")
@TestClass("升级后功能检查_无图模式")
public class NoImageFunctionTest extends BaseTest {

    public void testNoImageFunction() {
        uiUtil.skipWelcome();

        // 验证：右侧边栏无图模式处于开启状态
        uiUtil.enterSideBar(false);
        assertTrue("无图模式未保持打开", uiUtil.getControlPanelView(5).isSelected());
        // 验证：加载网页，图片未显示 TODO
        // 验证：.弹出toast提示：Load image
        solo.clickOnView(uiUtil.getControlPanelView(5));
        assertTrue("Fail to show . Load image", solo.searchText("Load image"));
        // 验证：加载网页，图片显示正常
    }
}
