package com.dolphin.updatecase.speeddial;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_007")
@TestClass("SpeedDial_Speed dial文件夹")
public class MergeSpeedDialTest extends BaseTest {

    public void testMergeSpeedDial() {
        uiUtil.skipWelcome();
        View folder = caseUtil.getViewByText("Folder", -1, true, true, true);
        assertTrue("没有正确找到文件夹Folder", folder != null);
        solo.clickOnView(folder);
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有正确找到speedDial'Amazon'", caseUtil.searchText("Amazon", -1, true, false, true));
        assertTrue("没有正确找到speedDial'eBay'", caseUtil.searchText("eBay", -1, true, false, true));
    }
}
