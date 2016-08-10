package com.dolphin.testcase.barshow;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1691
 * <p>
 * 脚本描述：Always show address（and menu）bar开关状态-关-jetpack关
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1691")
@TestClass("Always show address（and menu）bar开关状态-关-jetpack关")
public class CheckBarSwitchCloseTest extends BaseTest {
    private String url = Res.string.server_domain;

    public void testCheckBarSwitchClose() {
        uiUtil.skipWelcome();
        // 预置条件：开关状态为关
        uiUtil.enableBarShow(false);
        // 验证：在Dolphin主屏，手指向上滑动，Bars不会被滚走，页面正常显示
        // caseUtil.slideDireciton(null, false, 4 / 5f, 1f);
        drag(1 / 2f, 1 / 2f, 4 / 5f, 1 / 5f, 20);
        solo.sleep(Res.integer.time_wait);
        uiUtil.isBarShow(true, true);

        // 验证：手指快速向下滑动，Bars不会被滚走，页面正常显示
        // caseUtil.slideDireciton(null, false, 1 / 5f, 1f);
        drag(1 / 2f, 1 / 2f, 1 / 5f, 4 / 5f, 20);
        solo.sleep(Res.integer.time_wait);
        uiUtil.isBarShow(true, true);

        // 验证：打开网页，手指向上滑动，Bars以滑动形式同时滚出屏幕
        uiUtil.visitUrl(url);
        solo.sleep(Res.integer.time_wait);
        // caseUtil.slideDireciton(null, false, 4 / 5f, 1f);
        drag(1 / 2f, 1 / 2f, 4 / 5f, 1 / 5f, 20);
        solo.sleep(Res.integer.time_wait);
        uiUtil.isBarShow(false, true);

        // 验证：手指快速向下滑动 Bars被呼出
        // caseUtil.slideDireciton(null, false, 1 / 5f, 1f);
        drag(1 / 2f, 1 / 2f, 1 / 5f, 4 / 5f, 20);
        solo.sleep(Res.integer.time_wait);
        uiUtil.isBarShow(true, true);

    }

    private void drag(float fromX, float toX, float fromY, float toY, int stepCount) {
        int[] display = caseUtil.getDisplaySize(false);
        solo.drag(fromX * display[0], toX * display[0], fromY * display[1], toY * display[1],
                stepCount);
    }

}