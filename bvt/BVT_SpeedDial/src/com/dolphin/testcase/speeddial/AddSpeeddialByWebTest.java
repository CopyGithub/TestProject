package com.dolphin.testcase.speeddial;

import android.view.View;
import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;
import com.robotium.solo.WebElement;

/**
 * 脚本编号：DOLINT-5
 * <p>
 * 脚本描述：验证点击Web Store中网站"+"图标正常添加speed dial
 * 
 * @author jwliu
 * 
 */

// @Reinstall
// @TestNumber(value = "DOLINT-5")
// @TestClass("验证点击Web Store中网站'+'图标正常添加speed dial")
public class AddSpeeddialByWebTest extends BaseTest {
    final private String webappurl = "webapps.dolphin.com/int/";
    // name和code是对应的关系,如果修改了一个, 那么就需要修改另一个
    final private String[] name = { "YouTube", "Tripadvisor", "TouchPal -Emoji Keyboard&Theme",
            "Google" };
    final private String[] code = { "169", "1004", "974", "320" };

    public void testAddSpeeddialByWebTest() {
        uiUtil.skipWelcome();
        // 当前为浏览器主页,预置的speed dial中有Google(www.google.com)
        init();

        /**
         * 打开Web
         * Store网页:http://webapps.dolphin.com/int/?l=zh_CN(不同locale下"l"后显示的值不同):
         * <p>
         * ·已预置的speed dial网站(如:Google)后显示"√"图标(TODO)
         * <p>
         * ·未预置的speed dial网站后显示"+"图标(TODO)
         */
        clickAddWebIcon();
        assertTrue("the web app url is not right. ", uiUtil.checkURL(webappurl));
        /**
         * 验证:"+"图标变为"√"图标(TODO "+"图标变为"√"图标),同时弹出toast
         * "Successfully add to speed dial"
         */
        addSpeedialToCheckWebIcon(code[0], name[0], false, true);
        /**
         * 验证：在Web Store中添加的网站显示在speeddial列表中:
         * <p>
         * ·按添加先后顺序追加显示在预置speeddial后,如:Ebay、Youtube、Google Play
         * <p>
         * ·图标(TODO 图标)和名称显示正确,所有新speeddial图标(TODO 图标)风格显示一致(与Web
         * Store中显示的网站名和图标(TODO 图标)一致)
         */
        addSpeedialToCheckWebIcon(code[1], name[1], true, false);
        addSpeedialToCheckWebIcon(code[2], name[2], false, true);
        assertTrue("the order speeddials is not right. ", checkOrderOfItem());
        /**
         * 验证:正确打开相应网页:m.ebay.com(与Web Store中点击Ebay网站后的url一致)
         */
        openSpeedDial(name[0]);
    }

    private void init() {
        solo.sleep(Res.integer.time_wait);
        if (solo.searchText(name[3], 0, true)) {
            return;
        } else {
            addSpeedialToCheckWebIcon(code[3], name[3], true, true);
        }
    }

    private void openSpeedDial(String title) {
        solo.clickOnText(title);
        solo.sleep(Res.integer.time_wait);
        solo.sleep(4 * 1000);// 这里应该不需要等待页面打开,只需验证url
        assertTrue(title.toLowerCase() + " url address is not right. ",
                uiUtil.checkURL(title.toLowerCase()));
    }

    private boolean checkOrderOfItem() {
        caseUtil.slideDireciton(null, false, 3 / 4f, 1f);
        ViewGroup parent = (ViewGroup) caseUtil.getViewByClassName("CellLayout", 0, false);
        int num = name.length - 1;
        for (int i = 1; i <= num; ++i) {
            String speedDialName = uiUtil.getSpeedDialName(parent.getChildAt(num - i + 1));
            if (!name[i - 1].equals(speedDialName)) {
                return false;
            }
        }
        return true;
    }

    private void clickAddWebIcon() {
        solo.sleep(Res.integer.time_wait);
        // 点击homepage的加号,划到底部
        View hideView = solo.getView("id/hide_promotion");
        if (hideView != null)
            caseUtil.clickOnView("hbtn_cancel");
        caseUtil.slideDireciton(null, false, 3 / 4f, 1f);
        solo.clickOnView(caseUtil.getViewByIndex(solo.getView("workspace"), new int[] { 0, 0 }));
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available.", uiUtil.waitForWebPageFinished());
    }

    /**
     * 
     * @param eid
     *            网站上对应+号的id
     * @param title
     *            网站对应的名字
     * @return
     */
    private void addSpeedialToCheckWebIcon(String eid, String title, boolean isClick,
            boolean isGoback) {
        // 若页面没加载好，则get不到webelement，方法getWebElement()直接报错
        if (isClick) {
            clickAddWebIcon();
            if (solo.searchText("Load more")) {
                solo.goBack();
                solo.sleep(Res.integer.time_wait);
                clickAddWebIcon();
            }
        }
        int retry = 5;
        boolean isToastShow = false;
        while (!isToastShow && retry > 0 && !solo.scrollDown()) {
            // 点击网页上后面的+
            while (!solo.searchText(title, 0, true) && !solo.scrollDown()) {
                caseUtil.slideDireciton(null, false, 3 / 4f, 1f);
            }
            WebElement webElement = solo.getWebElement(By.className("action app" + eid), 0);
            if (webElement.getLocationY() > caseUtil.getDisplaySize(false)[1] * 4 / 5) {
                caseUtil.slideDireciton(null, false, 3 / 4f, 1f); // 避免点击的+图标被menubar给挡住,从而点击了menubar上的多窗口图标
                webElement = solo.getWebElement(By.className("action app" + eid), 0);
            }
            solo.clickOnWebElement(webElement);
            isToastShow = solo.searchText("Successfully added to speed dial");
            retry--;
        }
        // 判断是否弹出toast
        assertTrue("Toast didn't appear or failed to click.", isToastShow);
        if (isGoback) {
            // 回到home页去查看，是否添加speed dial成功
            solo.goBack();
            assertTrue(
                    "Failed to add 'speed dial' to homescreen by clicking '+' on Web Store page.",
                    solo.searchText(title, 0, true));
        }
    }
}