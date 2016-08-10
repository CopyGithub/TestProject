package com.dolphin.updatecase.speeddial;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_008")
@TestClass("SpeedDial_Speed dial移除")
public class MoveOutOfSpeedDialFolderTest extends BaseTest {

    public void testMoveOutOfSpeedDialFolder() {
        uiUtil.skipWelcome();

        View folder = caseUtil.getViewByText("Dolphin", -1, true, true, true);
        assertTrue("没有正确找到文件夹Dolphin", folder != null);
        assertTrue("没有正确找到speedDial'Features'", uiUtil.getSpeedDialByName("Features") != null);

        solo.clickOnView(folder);
        solo.sleep(Res.integer.time_wait);
        assertTrue("speedDial'Features'显示在文件夹中",
                !caseUtil.searchText("Features", -1, true, false, true));
    }
}
