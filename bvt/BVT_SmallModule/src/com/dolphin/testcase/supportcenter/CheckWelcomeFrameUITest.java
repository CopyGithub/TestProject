package com.dolphin.testcase.supportcenter;

import java.util.ArrayList;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.WebElement;

/**
 * 脚本编号：DOLINT-2185
 * <p>
 * 脚本描述：点击Welcome框中任意问题,响应正常
 * 
 * @author xweng
 * 
 */
// @TestNumber(value = "DOLINT-2185")
// @TestClass("点击Welcome框中任意问题,响应正常")
public class CheckWelcomeFrameUITest extends BaseTest {
    private static final String QUESTION_TITLE_LINK1 = "How do I enable Flash Player?";
    private static final String QUESTION_TITLE_LINK2 = "How do I always get the latest Dolphin?";
    private static final String QUESTION_TITLE_LINK3 = "How do I disable swipe action (ability to swipe out side bars)?";
    private static final String QUESTION_TITLE_LINK4 = "How do I find out what version of Dolphin I am using?";
    private static final String YES_NO_TITLE = "Was this answer helpful?";
    private static final String[] QUESLINK1 = { QUESTION_TITLE_LINK2, QUESTION_TITLE_LINK3 };
    private static final String[] QUESLINK2 = { QUESTION_TITLE_LINK4, QUESTION_TITLE_LINK3 };

    public void testCheckWelcomeFrameUITest() {
        uiUtil.skipWelcome();
        enterSupportCenter();

        // 验证：用户对话显示所点问题:How do I enable Flash Player?
        assertTrue("Failed to show \"How do I enable Flash Player?\"",
                checkUsrDialog(QUESTION_TITLE_LINK1));
        // TODO 默认用户头像无法验证
        // 验证：用户对话下自动显示Dolphin回话
        ArrayList<WebElement> itemsArrayList1 = checkDolphinDialogContent(QUESLINK1);
        // 验证：Dolphin回话内容顺序从上到下排列
        checkItemOrder(itemsArrayList1);
        // 点击任意相关问题,如: How do I always get the latest Dolphin
        // 验证：用户对话显示所点问题:How do I always get the latest Dolphin?
        assertTrue("Failed to show \"How do I always get the latest Dolphin?\"",
                checkUsrDialog(QUESTION_TITLE_LINK2));
        // 验证：用户对话下自动显示Dolphin回话
        ArrayList<WebElement> itemsArrayList2 = checkDolphinDialogContent(QUESLINK2);
        // 验证：Dolphin回话内容顺序从上到下排列
        checkItemOrder(itemsArrayList2);
    }

    private ArrayList<WebElement> checkDolphinDialogContent(String[] quesLink) {
        // TODO 对话框的位置无法验证
        solo.sleep(Res.integer.time_wait);
        ArrayList<WebElement> items = new ArrayList<WebElement>();
        boolean flag = false;
        do {
            WebElement ques1 = solo.getWebElement(By.textContent(quesLink[0]), 0);
            WebElement ques2 = solo.getWebElement(By.textContent(quesLink[1]), 0);
            WebElement yesNoTitle = solo.getWebElement(By.textContent(YES_NO_TITLE), 0);
            WebElement yes = solo.getWebElement(By.textContent("Yes"), 0);
            WebElement no = solo.getWebElement(By.textContent("No"), 0);
            flag = (yesNoTitle != null) && (ques1 != null) && (ques2 != null) && (yes != null)
                    && (no != null);
            if (!caseUtil.isScroll(solo.getView("center_screen"), 1) && !flag) {
                // 验证：Dolphin回话的内容显示正确
                assertTrue("Failed to show" + quesLink[0], ques1 != null);
                assertTrue("Failed to show" + quesLink[1], ques2 != null);
                assertTrue("Failed to show\"Was this answer helpful?\"", yesNoTitle != null);
                assertTrue("Failed to show\"Yes\" or \"No\"", yes != null && no != null);
            } else if (flag) {
                items.add(0, ques1);
                items.add(1, ques2);
                items.add(2, yesNoTitle);
                items.add(3, yes);
                assertTrue(
                        "'Yes' and 'No' got the wrong place",
                        yes.getLocationX() < no.getLocationX()
                                && yes.getLocationY() == no.getLocationY());
            }
        } while (!flag);
        // TODO 对应问题的回答无法验证
        // TODO Yes|No 颜色无法验证
        return items;
    }

    private void checkItemOrder(ArrayList<WebElement> items) {
        solo.sleep(Res.integer.time_change_activity);
        int[] locationY = new int[4];
        for (int i = 0; i < 4; ++i) {
            int location = items.get(i).getLocationY();
            locationY[i] = location;
            boolean flag = false;
            if (i > 0) {
                flag = locationY[i] > locationY[i - 1];
                assertTrue("The " + (i + 1) + " widget should below the " + i + " one", flag);
            }
        }
    }

    private boolean checkUsrDialog(String queString) {
        solo.clickOnWebElement(By.textContent(queString));
        solo.sleep(Res.integer.time_wait);
        boolean flag = false;
        WebElement ques;
        do {
            ques = solo.getWebElement(By.textContent(queString), 0);
            flag = (ques != null);
            caseUtil.slideDireciton(solo.getView("content_view"), false, 0.3f, 0.5f);
        } while (!flag);
        return ques != null;
    }

    /**
     * 前提：
     * <p>
     * 1. 手机语言地区为:美国(en_US)
     * <p>
     * 2.点击Support speeddial进入Support Center
     */
    private void enterSupportCenter() {
        solo.setActivityOrientation(Solo.PORTRAIT);
        uiUtil.setSmartLocale("en_US");
        solo.clickOnView(caseUtil.getViewByClassName("HomeFolderIcon", 0, false));
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(caseUtil.getViewByText("Support", -1, false, false, false));
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is bad .", uiUtil.waitForWebPageFinished());
        caseUtil.slideDireciton(solo.getView("content_view"), false, 2 / 3f, 2f);
        solo.sleep(Res.integer.time_wait);
    }
}
