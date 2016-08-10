package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-885
 * <p>
 * 脚本描述: 验证Game卡片的显示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-885")
@TestClass("验证Game卡片的显示")
public class CheckGameTest extends BaseTest {
    private ArrayList<View> views;

    public void testCheckGameTest() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        // 验证：Game卡片显示正常
        checkGameUI();
    }

    private void checkGameUI() {
        // 从上至下依次为:Game和6个game图标(3*2),图标下显示game名称
        uiUtil.assertCardText("game", 4, false);
        // 验证game图标和名称
        checkGridUI();
    }

    private void checkGridUI() {
        View parent = solo.getView("card_page_root");
        View gridView = caseUtil.getViewByIndex(parent, new int[] { 4, 1, 0 });
        views = utils.getChildren(gridView, false);
        utils.ubietyOfViews(views, 4, true, true, true);
        // game图标下显示名称
        for (View view : views) {
            View icon = caseUtil.getViewByIndex(view, new int[] { 0, 0 });
            View name = caseUtil.getViewByIndex(view, new int[] { 0, 1 });
            assertTrue("game图标没有显示在名称上面", utils.ubietyOfView(icon, name, false, false, false) != -1);
        }
    }
}