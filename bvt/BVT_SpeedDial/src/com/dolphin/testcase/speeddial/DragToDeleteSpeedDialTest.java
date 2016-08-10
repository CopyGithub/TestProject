package com.dolphin.testcase.speeddial;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-19
 * <p>
 * 脚本描述：验证横竖屏下,speed dial正常删除
 * 
 * @author jwliu
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-19")
@TestClass("验证横竖屏下,speed dial正常删除")
public class DragToDeleteSpeedDialTest extends BaseTest {
    private String deleteName;

    public void testDragToDeleteSpeedDialTest() {
        uiUtil.skipWelcome();
        dragToDelete();
        /**
         * 验证:"Remove"选项背景色变为红色(TODO 背景红色);Wallpaper speed dial从speed
         * dial列表中删除,后面的speed dial依次前挪填补空缺(TODO 依次填补空缺)
         */
        assertTrue("not delete " + deleteName,
                !caseUtil.searchText(deleteName, -1, true, true, true));
        changToLANDSCAPE();
        dragToDelete();
        /**
         * 验证:"Remove"选项背景色变为红色(TODO 背景红色);Wallpaper speed dial从speed
         * dial列表中删除,后面的speed dial依次前挪填补空缺(TODO 依次填补空缺)
         */
        assertTrue("not delete " + deleteName, !solo.searchText(deleteName, true));
    }

    private void changToLANDSCAPE() {
        solo.sleep(Res.integer.time_wait);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void dragToDelete() {
        // 找到非Find Apps的第一个icon
        View view = uiUtil.getEnableRemoveSpeedDial();
        deleteName = uiUtil.getSpeedDialName(view);
        // 长按，判断是否有Done，且有Add to home screen
        assertTrue("'Add to home screen' or 'Done' is not displayed.", uiUtil.clickOnScreen(view,
                2000, caseUtil.getTextByRId("action_mode_done"),
                caseUtil.getTextByRId("send_to_home"), 1));
        // 按X删除
        solo.clickOnView(caseUtil.getViewByIndex(view, new int[] { 0, 1 }));
    }
}