package com.dolphin.testcase.speeddial;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-28
 * <p>
 * 脚本描述：验证在中屏机上,speed dial在主页上的排列显示正常
 * 
 * @author jwliu
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-28")
@TestClass("验证在中屏机上,speed dial在主页上的排列显示正常")
public class CheckSpeedDialHomeUITest extends BaseTest {
    String[] str = new String[] { Res.string.url_one, Res.string.url_two, Res.string.url_three,
            Res.string.url_four, Res.string.url_five, Res.string.url_sixth,
            Res.string.url_layouttest, Res.string.url_player, Res.string.url_jetpacktest,
            Res.string.url_downloadtest };

    public void testCheckSpeedDialHomeUITest() {
        uiUtil.skipWelcome();
        if (caseUtil.getDisplayRange() != 1) {
            // 非中屏机直接成功
            return;
        }
        init();

        /**
         * 竖屏:
         * <p>
         * 一屏显示9个speed dial,排列方式为:3*3
         */
        checkDisplay(4);
        // TODO 可查看所有speed dial 未实现：左右滑动列表
        TextToCheck();
        // 可查看所有speed dial
        // 横屏:
        changToLANDSCAPE();
        // 一屏显示10个speed dial,排列方式为:5*2
        checkDisplay(6);
        // TODO 可查看所有speed dial 未实现：左右滑动列表
        TextToCheck();
    }

    private void init() {
        // 当前为浏览器主页,且已添加speed dial至总数>9(如19个)
        for (int i = 0; i < 10; ++i) {
            uiUtil.addSpeedDial(str[i], "name" + i);
            solo.sleep(Res.integer.time_wait);
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
    }

    private void changToLANDSCAPE() {
        solo.sleep(Res.integer.time_wait);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void TextToCheck() {
        int count = 0;
        for (int i = 0; i < 10; ++i) {
            if (solo.searchText("name" + i)) {
                ++count;
                continue;
            }
        }
        if (count != 10) {
            assertTrue("some name speeddial didn't show", false);
        }
    }

    private void checkDisplay(int column) {
        View parent = caseUtil.getViewByClassName("CellLayout", 0, true);
        ArrayList<View> views = utils.getChildren(parent, true);
        utils.ubietyOfViews(views, column, true, true, true);
    }
}