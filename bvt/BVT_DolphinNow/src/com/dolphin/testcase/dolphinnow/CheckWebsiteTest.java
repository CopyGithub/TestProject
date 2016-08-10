package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-857
 * <p>
 * 脚本描述: 验证website模块的展示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-857")
@TestClass("验证website模块的展示")
public class CheckWebsiteTest extends BaseTest {
    private String[] webStrings = new String[12];
    private ArrayList<View> views;

    public void testCheckWebsite() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.scrollToBottom();
        solo.sleep(Res.integer.time_wait);

        initWebStrings();
        // 验证：website第一行显示标题：Website
        uiUtil.assertCardText("website", 7, false);
        // 验证12个站点的位置:4X3排列
        checkGridUI(false);
        // 验证12个站点的图标和名称
        checkWebName();
        // 验证：第三行显示More
        uiUtil.assertCardMore(7);
        // 验证：12个站点的位置：横屏下4X3显示
        checkGridUI(true);
    }

    private void checkGridUI(boolean isLandscape) {
        if (isLandscape) {
            solo.setActivityOrientation(Solo.LANDSCAPE);
            solo.sleep(Res.integer.time_wait);
        }
        View parent = solo.getView("card_page_root");
        View gridView = caseUtil.getViewByIndex(parent, new int[] { 7, 1, 0 });
        views = utils.getChildren(gridView, false);
        utils.ubietyOfViews(views, 3, true, true, true);
    }

    private void checkWebName() {
        for (int i = 0; i < views.size(); i++) {
            View icon = ((ViewGroup) (views.get(i))).getChildAt(0);
            View name = ((ViewGroup) (views.get(i))).getChildAt(1);
            String nameStr = ((TextView) name).getText().toString();
            assertTrue("不是图标在左，文本在右", utils.ubietyOfView(icon, name, true, false, false) != -1);
            Boolean flag = webStrings[i].equals(nameStr);
            assertTrue("文本不正确", flag);
        }
    }

    private void initWebStrings() {
        webStrings[0] = "Twitter";
        webStrings[1] = "Rutracker";
        webStrings[2] = "РИА новости";
        webStrings[3] = "Рамблер";
        webStrings[4] = "ОЗОН.ру";
        webStrings[5] = "Живой журнал";
        webStrings[6] = "Lenta";
        webStrings[7] = "Kinopoisk";
        webStrings[8] = "Instagram";
        webStrings[9] = "Google";
        webStrings[10] = "Gismeteo";
        webStrings[11] = "Facebook";
    }
}