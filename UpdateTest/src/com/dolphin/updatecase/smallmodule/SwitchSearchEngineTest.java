package com.dolphin.updatecase.smallmodule;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_011")
@TestClass("搜索引擎_更改操作")
public class SwitchSearchEngineTest extends BaseTest {

    public void testSwitchSearchEngine() {
        uiUtil.skipWelcome();
        uiUtil.visitUrl("go");
        assertTrue("地址栏左侧搜索引擎不是Bing", uiUtil.checkURL("bing"));
    }
}
