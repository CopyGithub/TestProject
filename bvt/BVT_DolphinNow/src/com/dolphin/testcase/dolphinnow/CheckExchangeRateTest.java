package com.dolphin.testcase.dolphinnow;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: <br>
 * 1. DOLINT-868 <br>
 * 2. DOLINT-874 <br>
 * <p>
 * 脚本描述: <br>
 * 1. 检查Exchange Rate卡片UI <br>
 * 2. 验证汇率数据显示 <br>
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-868,DOLINT-874")
@TestClass("验证Exchange Rate卡片UI显示和汇率数据显示")
public class CheckExchangeRateTest extends BaseTest {
    private String GREEN = "ff22ac38";
    private String RED = "fffa383c";
    private String BLACK = "ff666666";

    public void testCheckExchangeRate() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        // 验证：进入左屏Dolphin now
        checkIntoDolphinNow();
        // 验证：卡片显示正常 && 根据开盘价判断上涨下跌或不变。
        checkUI();

    }

    private void checkIntoDolphinNow() {
        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        if (solo.searchText(caseUtil.getTextByRId("card_retry"))) {
            assertTrue("Network is not available.", true);
            return;
        }
        solo.scrollToTop();
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入Dolphin Now界面", solo.searchText(caseUtil.getTextByRId("exchange_rate")));
    }

    private void checkUI() {
        // "Exchange Rate" 刷新图标
        uiUtil.assertCardText("exchange_rate", 1, true);
        // "USD"(默认选中)和"EUR" tab
        View parent = solo.getView("card_page_root");
        View usd = caseUtil.getViewByIndex(parent, new int[] { 1, 1, 0, 0, 0 });
        View eur = caseUtil.getViewByIndex(parent, new int[] { 1, 1, 0, 0, 1 });
        assertTrue("USD未选中", usd.isSelected() && !eur.isSelected());
        /**
         * 相应tab下最新内容:(TODO) <br>
         * 汇率、浮动百分比 美元或欧元/卢布 时间 <br>
         * 昨日收盘价 最高价 <br>
         * 今日开盘价 最低价 <br>
         */

        // 对比今天的开盘价，绿色↑表示上涨，红色↓表示下跌，黑色→表示不变。
        // （浮动百分比的颜色跟箭头颜色保持一致）(TODO)
        TextView changePercent = (TextView) caseUtil.getViewByIndex(parent,
                new int[] { 1, 1, 1, 0, 2 });
        checkDataColor(changePercent);

        // 验证：底部为"More"(主题色)
        uiUtil.assertCardMore(1);
    }

    private void checkDataColor(TextView changePercent) {
        String string = changePercent.getText().toString();
        String first = string.substring(0, 1);
        if (first.equals("-")) {
            assertTrue("", Integer.toHexString(changePercent.getCurrentTextColor()).equals(RED));
        } else if (first.equals("+")) {
            assertTrue("", Integer.toHexString(changePercent.getCurrentTextColor()).equals(GREEN));
        } else {
            assertTrue("", Integer.toHexString(changePercent.getCurrentTextColor()).equals(BLACK));
        }

    }
}