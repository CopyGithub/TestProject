package com.dolphin.updatecase.speeddial;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_009")
@TestClass("SpeedDial_Speed dial删除")
public class DeleteSpeedDialTest extends BaseTest {

    public void testDeleteSpeedDial() {
        uiUtil.skipWelcome();
        assertTrue("'Wallpaper'显示在Speed dial列表里", uiUtil.getSpeedDialByName("Wallpaper") == null);
    }
}
