package com.dolphin.updatecase.speeddial;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_006")
@TestClass("SpeedDial_添加操作")
public class AddOneSpeedDialTest extends BaseTest {

    public void testAddOneSpeedDial() {
        uiUtil.skipWelcome();
        assertTrue("没有正确找到SpeedDial'百度一下'", uiUtil.getSpeedDialByName("百度一下") != null);
    }
}
