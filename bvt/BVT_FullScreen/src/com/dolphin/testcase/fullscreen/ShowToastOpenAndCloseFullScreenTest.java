package com.dolphin.testcase.fullscreen;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1373
 * <p>
 * 脚本描述：验证开关全屏toast提示正确
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1373")
@TestClass("验证开关全屏toast提示正确")
public class ShowToastOpenAndCloseFullScreenTest extends BaseTest {
    // 主题颜色是默认的时候，选中的颜色
    private String checkedDefaultColor = "ff389d00";
    // 主题颜色是默认的时候，未选中的颜色,11.5.0改成黑色
    private String uncheckedDefaultColor = "de000000";
    private static boolean isCheckedColor;
    private static boolean isToastOn = false;
    private static boolean isToastOff = false;

    public void testShowToastOpenAndCloseFullScreenTest() {
        uiUtil.skipWelcome();
        // express版本的主题色和未选中的颜色与int不一致
        if (!uiUtil.isINTPackage()) {
            checkedDefaultColor = "ff5c7db4";
            uncheckedDefaultColor = "ff6d6d6d";
        }
        // 呼出右侧边栏->开启全屏模式
        enterControlPanel();
        /**
         * 验证：开启时，全屏模式图标点击效果为主题色
         */
        clickOnFullScreen(true);

        assertTrue("Failed to show the theme color when open the FullScreen .", isCheckedColor);
        /**
         * 验证：开启后,右侧边栏收回,手机bars隐藏，出现toast提示：Fullscreen is turned on
         */
        assertTrue("Failed to hide phone bars .", uiUtil.isPhoneBarHide());
        assertTrue("Failed to show toast 'Fullscreen is turned on' .", isToastOn);
        // 呼出右侧边栏->查看全屏图标
        enterControlPanel();
        /**
         * 验证： 全屏模式图标显示主题色
         */
        assertTrue("Failed to show the theme color when open the FullScreen .", isCheckedColor);
        // 关闭全屏模式
        clickOnFullScreen(false);
        /**
         * 验证： 右侧边栏收回,手机bars显示，出现Toast提示：Fullscreen is turned off
         */
        assertTrue("Failed to show phone bars .", !uiUtil.isPhoneBarHide());
        assertTrue("Failed to show toast 'Fullscreen is turned off' .", isToastOff);
        // 呼出右侧边栏->查看全屏图标
        enterControlPanel();
        /**
         * 验证：全屏模式图标显示为灰色
         */
        assertTrue("Failed to show the gray color when close the FullScreen .", isCheckedColor);

    }

    /**
     * 点击FullScreen 图标
     * 
     * @param isOpen
     */
    private void clickOnFullScreen(boolean isOpen) {
        TextView fullScreen = (TextView) caseUtil.getViewByIndex(
                solo.getView("id/list_installed_plugin"), new int[] { 0, 0, 0 });
        solo.clickOnView(fullScreen);
        if (isOpen) {
            isToastOn = solo.searchText(caseUtil.getTextByRId("switch_to_fullscreen_mode"));
            String checkedColor = Integer.toHexString(fullScreen.getCurrentTextColor());
            isCheckedColor = checkedColor.equals(checkedDefaultColor);
        } else {
            isToastOff = solo.searchText(caseUtil.getTextByRId("switch_to_normal_mode"));
            String checkedColor = Integer.toHexString(fullScreen.getCurrentTextColor());
            isCheckedColor = checkedColor.equals(uncheckedDefaultColor);
        }
        if (caseUtil.getDisplayRange() == 0) {
            solo.clickOnText("Got it");
            solo.sleep(Res.integer.time_wait);
        }
    }

    private void enterControlPanel() {
        uiUtil.enterSideBar(false);
        solo.sleep(Res.integer.time_wait);
    }
}