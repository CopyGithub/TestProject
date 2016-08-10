package com.dolphin.testcase.menu;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-235:
 * <p>
 * 脚本描述：验证中屏机竖屏上,点击海豚键正常展开Menu面板
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-235")
@TestClass("验证中屏机竖屏上,点击海豚键正常展开Menu面板")
public class CheckMenuPanelTest extends BaseTest {
    private String webURL = Res.string.url_test;
    private String[] menuStrings = new String[9];
    private ArrayList<View> views;

    public void testCheckMenuPanel() {
        uiUtil.skipWelcome();
        // 非中屏机直接返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        init();

        // 验证: Menu面板在Menu bar上方展开,且与Menu bar连接在一起
        assertTrue("Menu面板不在Menu bar上方展开,且不与Menu bar连接在一起", checkMenuPanel());

        /*
         * 验证: 其中按3*3方式依次显示选项: ·Add to / Share / Tab push(灰显) ·Find in page(灰显)
         * / Themes / Downloads ·Settings / Clear data / Exit
         * 图标和文案显示正常,文案显示在图标下方
         */
        // TODO 图标和文案显示在图标下方的验证未实现
        checkMenuItemNoWeb();

        // ①点击"Tab push"区域 ②点击"Find in page"区域
        // 判断①②功能无反应，同时面板收回
        checkDisablePanel();

        // 验证: 打开任意网页(如:www.ebay.com) -> 再次展开Menu ,所有功能区域为可用状态
        checkMenuItemHasWeb();

        // 点击海豚键/手机back键/手机Menu键/弹框之外 Menu面板关闭
        checkPanelClosed();
    }

    private void init() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void enterMenuPanel() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(uiUtil.getMenubarItem(3, 0));
        solo.sleep(Res.integer.time_wait);
    }

    private boolean checkMenuPanel() {
        View menuBar = solo.getView("bottom_container");
        int[] menuBarLocation = new int[2];
        menuBar.getLocationOnScreen(menuBarLocation);
        enterMenuPanel();
        View menuPanel = solo.getViews().get(0);
        int[] menuListLocation = new int[2];
        menuPanel.getLocationOnScreen(menuListLocation);
        return menuBarLocation[1] == menuListLocation[1] + menuPanel.getHeight() - 1;
    }

    private void initMenuString() {
        menuStrings[0] = caseUtil.getTextByRId("add_to", -1);// Add to
        menuStrings[1] = caseUtil.getTextByRId("share", -1);// Share
        menuStrings[2] = caseUtil.getTextByRId("panel_menu_item_title_tab_push", -1);// Tabpush
        menuStrings[3] = caseUtil.getTextByRId("find_dot", -1);// Find in page
        menuStrings[4] = caseUtil.getTextByRId("theme", -1);// Themes
        menuStrings[5] = caseUtil.getTextByRId("menu_view_download", -1);// Downloads
        menuStrings[6] = caseUtil.getTextByRId("settings", -1);// Settings
        menuStrings[7] = caseUtil.getTextByRId("clear_data", -1);// Clear data
        menuStrings[8] = caseUtil.getTextByRId("exit", -1);// Exit
    }

    private void checkMenuItemNoWeb() {
        initMenuString();
        View menupanelView = solo.getViews().get(1);
        checkGridUi(menupanelView);
    }

    private void checkGridUi(View parent) {
        views = utils.getChildren(parent, false);
        // 验证View的3*3的排列方式
        utils.ubietyOfViews(views, 3, true, true, true);
        checkMenuPanel(false, false);
    }

    private void checkMenuPanel(boolean isEnable2, boolean isEnable3) {
        boolean flag = false;
        // 验证文本和状态是否正确
        for (int i = 0; i < views.size(); i++) {
            TextView textView = (TextView) views.get(i);
            flag = menuStrings[i].equals(textView.getText());
            assertTrue("菜单中的文本显示不正确, 应该是" + menuStrings[i], flag);
            switch (i) {
            case 2:
                assertTrue(menuStrings[i] + "的状态不正确", views.get(i).isEnabled() == isEnable2);
                break;
            case 3:
                assertTrue(menuStrings[i] + "的状态不正确", views.get(i).isEnabled() == isEnable3);
                break;
            default:
                assertTrue(menuStrings[i] + "的状态不正确", views.get(i).isEnabled());
                break;
            }
        }
    }

    // 验证：①点击"Tab push"区域 ②点击"Find in page"区域
    // ①②功能无反应
    private void checkDisablePanel() {
        int menuPanel = solo.getViews().size();
        // 验证Tab push点击后无反应
        solo.clickOnView(views.get(2));
        solo.sleep(Res.integer.time_wait);
        assertTrue("点击" + menuStrings[2] + "后有新功能", menuPanel == solo.getViews().size());
        // 验证Find in Page点击后无反应
        solo.clickOnView(views.get(3));
        solo.sleep(Res.integer.time_wait);
        assertTrue("点击" + menuStrings[3] + "后有新功能", menuPanel == solo.getViews().size());
    }

    private void checkMenuItemHasWeb() {
        solo.sleep(Res.integer.time_wait);
        uiUtil.visitUrl(webURL);
        enterMenuPanel();
        checkMenuPanel(true, true);
    }

    private void checkPanelClosed() {
        // 验证了其中一个case：手机back键，Menu面板关闭
        int menuPanel = solo.getViews().size();
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        assertTrue("点击menu后menu面板没有收回", menuPanel != solo.getViews().size());
    }
}