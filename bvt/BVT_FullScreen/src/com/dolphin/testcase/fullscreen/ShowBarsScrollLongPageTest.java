package com.dolphin.testcase.fullscreen;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1385
 * <p>
 * 脚本编号：DOLINT-1386
 * <p>
 * 脚本描述：滑动长网页,bars显隐正常
 * <p>
 * 脚本描述：验证4.4以下的机器上,滑动长网页,bars显隐正常
 * <p>
 * 脚本描述：验证4.4及以上带虚拟键的机器上,滑动长网页,bars显隐正常
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1385,DOLINT-1386")
@TestClass("滑动长网页,bars显隐正常")
public class ShowBarsScrollLongPageTest extends BaseTest {
    private String bookmark = "Wikipedia";

    public void testShowBarsScrollLongPageTest() {
        uiUtil.skipWelcome();
        // TODO 该case需要拖动操作，方法不支持。尚未解决
        // 1 滑出左侧边栏->任意点击一个bookmark，如Youtube（此处想验证页面加载过程中开启全屏的情况）
        clickOnBookmark(bookmark);
        // 验证：页面正常打开,待网页未加载完成时，滑出右侧边栏->开启全屏(TODO 无法判断网页加载完成)
        openFullScreen();
        // 验证：全屏正常打开，进度条正常显示。所有bars隐藏（内核开时，Title bar显隐与网页本身有关,可能显示也可能不显示）
        uiUtil.isBarShow(false, false);
        // 在页面中部上下滑动
        dragMiddle(0);
        dragMiddle(1);
        // 验证： 所有bars均保持隐藏；（TODO )页面随着滑动方向，正确移动
        uiUtil.isBarShow(false, false);
        // 4 从屏幕上边缘往内滑动，查看此时bars的显隐情况
        // caseUtil.slideDireciton(null, false, 0f, 1f);
        dragMiddle(2);
        // 验证：所有bars均正常呼出
        uiUtil.isBarShow(true, true);
        // 手指向上滑动页面
        dragMiddle(0);
        // 验证： 所有bars正常隐藏
        uiUtil.isBarShow(false, false);
        // TODO 从屏幕下边缘往内滑动，查看此时bars的显隐情况
        // 验证:所有bars正常隐藏
    }

    /**
     * 
     * @param way
     *            0: 屏幕中央向上滑动<br>
     *            1:屏幕中央向下滑动<br>
     *            2:屏幕上边缘往内滑动
     */
    private void dragMiddle(int way) {
        if (way == 0) {
            caseUtil.slide(null, 1 / 2f, 3 / 4f, 1 / 2f, 2 / 5f, 1f);
        } else if (way == 1) {
            caseUtil.slide(null, 1 / 2f, 1 / 2f, 1 / 2f, 2 / 3f, 1f);
        } else {
            caseUtil.slide(null, 1 / 2f, 0f, 1 / 2f, 1 / 2f, 1f);
        }
        solo.sleep(Res.integer.time_wait);
    }

    private void openFullScreen() {
        solo.sleep(3 * 1000);
        uiUtil.enterSideBar(false);
        solo.sleep(Res.integer.time_wait);
        View fullScreen = caseUtil.getViewByIndex(solo.getView("id/list_installed_plugin"), new int[] {
                0, 0, 0 });
        solo.clickOnView(fullScreen);
        solo.sleep(Res.integer.time_wait);
        if (caseUtil.getDisplayRange() == 0) {
            solo.clickOnText("Got it");
            solo.sleep(Res.integer.time_wait);
        }
    }

    private void clickOnBookmark(String name) {
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText(name, -1, true);
        assertTrue("加载网页失败,一直处于加载状态", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);
    }
}