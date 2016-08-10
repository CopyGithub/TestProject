package com.dolphin.testcase.gesture;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-582
 * <p>
 * 脚本描述：中屏机:竖屏:默认设定&长按海豚键
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-582")
@TestClass("中屏机:竖屏:默认设定&长按海豚键")
@Reinstall
public class CheckClickLongOnDolphinIconTest extends BaseTest {
    private View dolphinIcon;

    public void testCheckClickLongOnDolphinIcon() {
        uiUtil.skipWelcome();
        init();
        // 验证：出现Gesture和Sonar图标
        checkIconsAppear();
        // 验证：进入"DRAW A GESTURE"界面
        checkEnterDraw(false);
        // 验证：全屏后，进入"DRAW A GESTURE"界面
        checkEnterDraw(true);
        // 关闭全屏
        finish();
    }

    private void finish() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        openFullScreen(false);
    }

    private void openFullScreen(boolean toOpen) {
        // 打开全屏
        uiUtil.clickOnControlPanel(true, 0);
        // 若存在Enable Swipe For Sidebars,点击Cancel
        uiUtil.closeSidebarTips();
    }

    private void checkEnterDraw(boolean isFullscreen) {
        if (isFullscreen) {
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
            // 开启全屏模式
            openFullScreen(true);
            solo.sleep(Res.integer.time_wait);
            dolphinIcon = caseUtil.getViewByIndex(solo.getView("bottom_container"), new int[] { 0,
                    3, 0 });
            caseUtil.motion(dolphinIcon, 0);
            solo.sleep(Res.integer.time_wait);
        }
        // 长按海豚键,拖动到Gesture图标中释放
        dolphinIcon = caseUtil.getViewByIndex(solo.getView("bottom_container"),
                new int[] { 0, 3, 0 });
        caseUtil.dragViewToView(dolphinIcon,
                caseUtil.getViewByIndex(caseUtil.getView(null, "arc_menu"), new int[] { 0, 1 }));
        solo.sleep(Res.integer.time_wait);
        // 验证：进入"DRAW A GESTURE"界面
        assertTrue("未进入DRAW A GESTURE界面",
                solo.searchText(caseUtil.getTextByRId("gesture_pad_tips", -1)));
    }

    private void checkIconsAppear() {
        // 长按海豚键
        dolphinIcon = caseUtil.getViewByIndex(solo.getView("bottom_container"),
                new int[] { 0, 3, 0 });
        caseUtil.motion(dolphinIcon, 0);
        assertTrue("未出现Gesture和Sonar图标", solo.waitForView(solo.getView("arc_menu")));
    }

    // 预置条件：中屏机，竖屏，当前为浏览器主页或网页界面
    private void init() {
        // 非中屏机直接返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_change_activity);
    }
}
