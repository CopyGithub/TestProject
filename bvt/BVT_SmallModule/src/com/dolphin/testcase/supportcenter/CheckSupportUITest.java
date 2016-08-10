package com.dolphin.testcase.supportcenter;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.WebElement;

/**
 * 脚本编号: DOLINT-2183
 * <p>
 * 脚本描述: Support speeddial出现情况
 * 
 * @author sjguo
 * 
 */
// @TestNumber(value = "DOLINT-2183")
// @TestClass("Support speeddial出现情况")
public class CheckSupportUITest extends BaseTest {
    private String supportURLDisplay = "sp.dolphin.com";
    private static final String SUPPORT_WELCOME_CONTENT1 = "Hi,Welcome to Support Center!";
    private static final String SUPPORT_WELCOME_CONTENT2 = "Are you looking for these questions?";
    private static final String FAQ_BOTTOM_TIPS = "Not helpful? Try searching!";

    public void testCheckSupportUI() {
        uiUtil.skipWelcome();
        solo.setActivityOrientation(Solo.PORTRAIT);
        uiUtil.setSmartLocale("en_US");

        // 验证: Dolphin文件夹展开后，Speedial是否为4列
        boolean flag = checkSpeedialFolderMatrix();
        if (flag) {
            // 验证: 当有Lab speeddial时,Support speeddial显示为第2排第1个
            assertTrue("Support没有显示在第2行第1个", checkSpeedialDolphinFolder(4));
        } else {
            // 验证: 第1排第3个为Support speeddial
            assertTrue("Support没有显示在第1行第3个", checkSpeedialDolphinFolder(2));
        }

        // 验证: 打开网页且地址栏显示url为包含sp.dolphin.com
        assertTrue("The URL is wrong .", checkURL());
        // 验证: 观察页面UI是否正确
        checkSupportUI();
        // 验证: 点击"∨"图标后，列表展开，新增3个热门问题
        assertTrue("Failed to add three more questions", checkMoreQuestions());
        // 验证：对话框底部显示 Not helpful? Try searching!
        assertTrue("Failed to show \"Not helpful? Try searching!\"", checkFAQBottomTips());

        // 切换为横屏
        changeToLandscape();
        // 验证：横屏时观察页面UI是否正确 TODO
        // 验证：点击searching链接后，问题分类栏自动切换为问题搜索栏
        assertTrue("Failed to switch to search-input bar after clicking", checkClickSearching());
    }

    private boolean checkClickSearching() {
        solo.clickOnWebElement(By.className("widget-menu-item-title"));
        solo.sleep(Res.integer.time_wait);
        return solo.getWebElement(By.className("search-input"), 0) != null;
    }

    private void changeToLandscape() {
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_wait);
    }

    private void checkSupportUI() {
        ArrayList<WebElement> items = new ArrayList<WebElement>();
        WebElement title = solo.getWebElement(By.className("head"), 0);
        items.add(0, title);
        // 验证: 标题Support Center
        assertTrue("Title is not 'Support Center'.", title.getText().equals("Support Center"));
        WebElement content1 = solo.getWebElement(By.textContent(SUPPORT_WELCOME_CONTENT1), 0);
        WebElement content2 = solo.getWebElement(By.textContent(SUPPORT_WELCOME_CONTENT2), 0);
        items.add(1, content1);
        items.add(2, content2);
        // 验证: 对话框内容Hi,Welcome to Support Center!
        assertTrue("Welcome content 'Hi,Welcome to Support Center!' does not appear.",
                content1 != null);
        // 验证: 对话框内容Are you looking for these questions?
        assertTrue("Welcome content 'Are you looking for these questions?' does not appear.",
                content2 != null);
        // TODO 海豚图标无法验证
        // 验证：3个热门问题
        // TODO 3个热门问题文本是绿色无法验证
        assertTrue("Failed to show the three default questions",
                solo.getWebElement(By.className("question-title link"), 2) != null);
        // TODO "∨"图标无法验证
        WebElement triangle = solo.getWebElement(By.className("more-triangle"), 0);
        items.add(3, triangle);
        // 验证：页面内容顺序从上到下排列
        checkItemOrder(items);
        // 验证：底部为:问题分类栏,从左至右依次为: 放大镜 | Questions◢ | Features◢ | Feedback◢
        checkWidgitMenuItem();
    }

    private void checkItemOrder(ArrayList<WebElement> items) {
        int[] locationY = new int[4];
        for (int i = 0; i < items.size(); ++i) {
            int location = items.get(i).getLocationY();
            locationY[i] = location;
            boolean flag = false;
            if (i > 0) {
                flag = locationY[i] > locationY[i - 1];
                assertTrue("The " + (i + 1) + " widget should below the " + i + " one", flag);
            }
        }
    }

    private void checkWidgitMenuItem() {
        // TODO 放大镜的图标无法验证
        ArrayList<WebElement> menuItems = new ArrayList<WebElement>();
        menuItems.add(0, solo.getWebElement(By.className("widget-menu-item-title"), 0));
        menuItems.add(1, solo.getWebElement(By.textContent("Questions"), 0));
        menuItems.add(2, solo.getWebElement(By.textContent("Features"), 0));
        menuItems.add(3, solo.getWebElement(By.textContent("Feedback"), 0));
        for (int i = 0; i < 4; ++i) {
            assertTrue("The " + (i + 1) + " widget menu item is missed", menuItems.get(i) != null);
        }
        int[] locationX = new int[4];
        for (int i = 0; i < 4; ++i) {
            int location = menuItems.get(i).getLocationX();
            locationX[i] = location;
            boolean flag = false;
            if (i > 0) {
                flag = locationX[i] > locationX[i - 1];
                assertTrue("The " + (i + 1) + " widget should be on the right side of the " + i
                        + " one", flag);
            }
        }
    }

    private boolean checkFAQBottomTips() {
        WebElement faqBottomTips = solo.getWebElement(By.className("FAQ-tips FAQ-bottom-tips"), 0);
        boolean flag = faqBottomTips.getText().equals(FAQ_BOTTOM_TIPS);
        return flag;
    }

    private boolean checkMoreQuestions() {
        solo.clickOnWebElement(By.className("more-triangle"));
        solo.sleep(Res.integer.time_wait);
        solo.sleep(Res.integer.time_wait);
        return solo.getWebElement(By.className("question-title link"), 5) != null;
    }

    private boolean checkURL() {
        solo.clickOnView(caseUtil.getViewByText("Support", -1, false, false, false));
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is bad .", uiUtil.waitForWebPageFinished());
        caseUtil.slideDireciton(solo.getView("content_view"), false, 2 / 3f, 2f);
        solo.sleep(Res.integer.time_wait);
        return uiUtil.checkURL(supportURLDisplay);
    }

    /**
     * 
     * @return 是否有Lab speedial
     */
    private boolean checkSpeedialFolderMatrix() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(caseUtil.getViewByClassName("HomeFolderIcon", 0, false));
        solo.sleep(Res.integer.time_wait);
        ArrayList<View> views = utils.getChildren(solo.getView("folder_content"), true);
        utils.ubietyOfViews(views, 4, true, true, true);
        if (solo.searchText(caseUtil.getTextByRId("pref_category_lab"))) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkSpeedialDolphinFolder(int flag) {
        ArrayList<View> views = utils.getChildren(solo.getView("folder_content"), true);
        View view = views.get(flag);
        TextView textView = (TextView) caseUtil.getViewByIndex(view, new int[] { 1 });
        return textView.getText().toString().equals("Support");
    }
}
