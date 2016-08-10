package com.dolphin.testcase.speeddial;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-17
 * <p>
 * 脚本描述：验证横竖屏下,长按speed dial界面显示正常
 * 
 * @author jwliu
 * 
 */

@TestNumber(value = "DOLINT-17")
@TestClass("验证横竖屏下,长按speed dial界面显示正常")
@Reinstall
public class CheckLongClickSpeedDialUITest extends BaseTest {

    public void testCheckLongClickSpeedDialUITest() {
        uiUtil.skipWelcome();

        /**
         * 验证:竖屏下，界面显示正常:
         * <p>
         * ·所选speed dial图标变大(TODO)
         * <p>
         * ·Address bar、tab bar(和Menu bar)消失
         * <p>
         * ·界面顶部显示背景色"Add to home screen"图标和选项,底部显示"Remove"图标和选项
         */
        assertTwoCheckButton("drop_target_bar", 1);
        // assertTrue("the Address bar is not disppeared.",
        // isDispearBar("id/top_container", true, new int[] { 0, 0 }));
        // assertTrue("the tab bar is not disppeared.",
        // isDispearBar("id/top_container", true, new int[] { 0, 1 }));
        // assertTrue("the Menu bar is not disppeared.",
        // isDispearBar("id/bottom_container", false, null));
        changToLANDSCAPE();
        /**
         * 横屏下，界面显示正常:
         * <p>
         * ·所选speed dial图标变大(TODO)
         * <p>
         * ·Address bar和tab bar消失
         * <p>
         * ·界面顶部左右两遍分别显示"Add to home screen"和"Remove"图标和选项
         */
        assertTwoCheckButton("drop_target_bar", 0);
        // assertTrue("the Address bar is not disppeared.",
        // isDispearBar("id/top_container", true, new int[] { 0, 0 }));
        // assertTrue("the tab bar is not disppeared.",
        // isDispearBar("id/top_container", true, new int[] { 0, 1 }));
    }

    private boolean isDispearBar(String rId, boolean isTop, int[] path) {
        solo.sleep(Res.integer.time_wait);
        View view = solo.getView(rId);
        View childView;
        if (isTop) {
            childView = caseUtil.getViewByIndex(view, path);
        } else {
            childView = caseUtil.getViewByIndex(view, new int[] { 0 });
        }
        return !childView.isShown();
    }

    private void changToLANDSCAPE() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("action_mode_done"));
        solo.sleep(Res.integer.time_wait);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void assertTwoCheckButton(String rId, int index) {
        // 选择第一个非find apps 和 文件夹的图标
        // Wallpaper为int和express都有的
        View view = uiUtil.getSpeedDialByName("Wallpaper");
        caseUtil.motion(view, 0);
        solo.sleep(Res.integer.time_wait);
        String addToHomeString = caseUtil.getTextByRId("option_pin_to_desktop");
        String DoneString = caseUtil.getTextByRId("action_mode_done");
        View addToHomeView = solo.getView(rId, 0);
        View removeView;
        if (index == 0) {
            removeView = solo.getView(rId, 1);
        } else {
            removeView = solo.getView(rId, 1);
        }
        assertTrue(addToHomeString + " button is not found.", solo.searchText(addToHomeString)
                && addToHomeView.isShown());
        assertTrue(DoneString + " button is not found.",
                solo.searchText(DoneString) && removeView.isShown());
        int[] location1 = new int[2];
        addToHomeView.getLocationOnScreen(location1);
        int[] location2 = new int[2];
        removeView.getLocationOnScreen(location2);
        boolean flag = true;
        if (index == 1) {
            // 竖屏时候，在顶部和底部
            if (location1[0] != 0 || location2[0] != 0) {
                flag = false;
            }
            assertTrue(
                    "the 'add to home' button is not on the top ,or the 'remove' button is not on the buttom.",
                    flag);
        } else {
            View parentsBar = solo.getView("id/drop_target_bar");
            int[] location3 = new int[2];
            parentsBar.getLocationOnScreen(location3);
            // 竖屏时候在左边或者右边
            if (location3[0] != 0 || location1[0] > location2[0]) {
                flag = false;
            }
            assertTrue(
                    "the 'add to home' and 'remove' button is not on the left or right ,or the 'add to home' button is not on the left of 'remove' button .",
                    flag);
        }
    }
}