package com.dolphin.testcase.dolphinnow;


import android.view.View;
import android.widget.ImageView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-787
 * <p>
 * 脚本描述: 验证search模块的UI显示正确
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-787")
@TestClass("验证search模块的UI显示正确")
public class CheckSearchUITest extends BaseTest {

    public void testCheckSearchUI() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        // 验证：进入Dolphin Now界面
        checkIntoDolphinNow();

        // 验证：search模块的UI显示
        checkSearchModule();
        // 验证：设置横屏后，观察search模块的UI显示
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_wait);
        checkSearchModule();
    }

    private void checkIntoDolphinNow() {
        caseUtil.slideDireciton(null, true, 0f, 1f);
        if (solo.searchText(caseUtil.getTextByRId("card_retry"))) {
            assertTrue("Network is not available.", true);
            return;
        }
        solo.scrollToTop();
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入Dolphin Now界面", solo.searchText(caseUtil.getTextByRId("exchange_rate")));
    }

    private void checkSearchModule() {
        // 验证：search 模块在所有模块的顶部，正上方是搜索引擎的图片(TODO)，右侧为下拉箭头,图片下方是输入框和放大镜
        solo.sleep(Res.integer.time_wait);
        View parent = solo.getView("card_page_root");
        View engine = caseUtil.getViewByIndex(parent, new int[] { 0, 0, 0 });
        ImageView arrow = (ImageView) caseUtil.getViewByIndex(parent, new int[] { 0, 0, 1 });
        assertTrue("向下箭头未在引擎icon右侧", utils.ubietyOfView(engine, arrow, true, false, false) != -1);
    }
}