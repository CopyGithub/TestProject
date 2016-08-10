package com.dolphin.testcase.addressbar;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1549
 * <p>
 * 脚本描述：中屏机横竖屏下,地址栏位置和样式正常
 * 
 * @author sjguo
 * 
 */
@TestNumber("DOLINT-1549")
@TestClass("中屏机横竖屏下,地址栏位置和样式正常")
public class CheckAddressBarTest extends BaseTest {
    private String grayColor = "ff999999";
    private String green = "ff999999";

    public void testCheckAddressBar() {
        // 非中屏机直接返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        uiUtil.skipWelcome();
        init();

        // 观察地址栏,横/竖屏:地址栏显示在tab bar下方,内含灰字:Search or enter address(左对齐)
        // 切换横竖屏,地址栏仍然显示正常
        checkAddressBarUI(grayColor);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
        checkAddressBarUI(green);

        // 上下滑动浏览器顶部/中部/底部,地址栏固定不动
        // checkAddressBarFixed();
    }

    private void checkAddressBarFixed() {
        solo.setActivityOrientation(Solo.PORTRAIT);

        int[] locBeforeScroll = new int[2];
        int[] locAfterScroll = new int[2];
        for (int i = 0; i < 3; i++) {
            View addressbar = solo.getView("id/address_bar");
            addressbar.getLocationOnScreen(locBeforeScroll);
            scrollBrowser(i);
            addressbar = solo.getView("id/address_bar");
            addressbar.getLocationOnScreen(locAfterScroll);
            assertTrue("第" + i + "次滑动后地址栏发生变化", locBeforeScroll[0] == locAfterScroll[0]
                    && locBeforeScroll[1] == locAfterScroll[1]);
            solo.sleep(Res.integer.time_change_activity);
        }
    }

    /**
     * 
     * @param i
     *            0:上下滑动浏览器顶部,1:上下滑动浏览器中部,2:上下滑动浏览器底部
     */
    private void scrollBrowser(int i) {
        View pagedView = solo.getView("id/paged_view");
        switch (i) {
        case 0:
            caseUtil.slide(pagedView, 1 / 2f, 1 / 10f, 1 / 2f, 1 / 2f, 1f);
            solo.sleep(Res.integer.time_change_activity);
            break;
        case 1:
            caseUtil.slide(pagedView, 1 / 2f, 1 / 2f, 1 / 2f, 4 / 5f, 1f);
            solo.sleep(Res.integer.time_change_activity);
            break;
        case 2:
            caseUtil.slide(pagedView, 1 / 2f, 9 / 10f, 1 / 2f, 1 / 2f, 1f);
            solo.sleep(Res.integer.time_change_activity);
            break;
        }
    }

    private void checkAddressBarUI(String color) {
        View tabbar = solo.getView("id/top_container");
        View addressbar = solo.getView("id/address_bar");
        assertTrue("地址栏没有显示在tab bar下方",
                utils.ubietyOfView(tabbar, addressbar, false, false, false) != -1);
        // 地址栏内含灰字:Search or enter address(左对齐)
        checkAddressBarInputBox(color);
    }

    private void checkAddressBarInputBox(String color) {
        TextView hint = (TextView) solo.getView("id/title_design");
        assertTrue(
                "地址栏内不含灰字：Search or enter address",
                hint.getHint().toString()
                        .equals(caseUtil.getTextByRId("search_box_hint_addressbar", -1)));
        assertTrue("地址栏内的：Search or enter address不是左对齐", hint.getTextAlignment() == 1);
        // 判断字体灰色
        String screen = Integer.toHexString(hint.getTextColors().getDefaultColor());
        assertTrue("界面上颜色是" + screen + ",匹配是" + color, screen.equals(color));
    }

    private void init() {
        if (!uiUtil.isINTPackage()) {
            grayColor = "80ffffff";
        }
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_change_activity);
    }
}