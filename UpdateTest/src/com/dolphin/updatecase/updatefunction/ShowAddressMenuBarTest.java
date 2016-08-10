package com.dolphin.updatecase.updatefunction;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_043")
@TestClass("升级后功能检查_Awlays show address and menu bar")
public class ShowAddressMenuBarTest extends BaseTest {
    public void testShowAddressMenuBar() {
        uiUtil.skipWelcome();
        // 打开Always show address and menu bar并验证
        uiUtil.enableBarShow(true);

        // 进入key搜索界面
        uiUtil.visitUrl("key");
        assertTrue("Failed to search keyword 'key'", uiUtil.searchByEngineCorrectly("key"));
        solo.sleep(Res.integer.time_change_activity);

        // 验证：向上滑动浏览器中部，address and menu bar显示
        caseUtil.slideDireciton(null, false, 0.7f, 1f);
        solo.sleep(Res.integer.time_change_activity);
        uiUtil.isBarShow(true, true);
    }
}
