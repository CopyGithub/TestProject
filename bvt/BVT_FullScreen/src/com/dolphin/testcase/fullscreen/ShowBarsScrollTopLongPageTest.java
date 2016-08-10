package com.dolphin.testcase.fullscreen;

import android.view.View;
import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1382
 * <p>
 * 脚本编号：DOLINT-1383
 * <p>
 * 脚本描述：在长网页滑至页面顶部，浏览器bars呼出
 * <p>
 * 脚本描述：验证在4.4以下的机器上,在长网页滑至页面顶部，浏览器bars呼出
 * <p>
 * 脚本描述：验证在4.4及以上带虚拟键的机器上,在长网页滑至页面顶部，浏览器bars呼出
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1382,DOLINT-1383")
@TestClass("在长网页滑至页面顶部，浏览器bars呼出")
public class ShowBarsScrollTopLongPageTest extends BaseTest {
    private String longUrl = "amazon.cn";
    private View backToTopView;

    public void testShowBarsScrollTopLongPageTest() {
        uiUtil.skipWelcome();
        // 1.测试机为4.4及以上带有虚拟机的系统
        openFullScreen();

        // 点击地址栏， 输入关键字进行搜索，查看页面加载后bars的显示
        clickAddressbarAndVisiteUrl();
        // 验证： 页面正常打开，所有bars保持隐藏（内核开时，Title bar显隐与网页本身有关,可能显示也可能不显示）
        // TODO　这是一个已知的bug还没有解决,导致以下第二行处case出错
        assertTrue("Network is bad. ", uiUtil.waitForWebPageFinished());
        uiUtil.isBarShow(false, false);
        // 在页面中部上下滑动
        dragMiddle(true);
        dragMiddle(false);
        // 验证： 所有bars均保持隐藏；（TODO )页面随着滑动方向，正确移动
        uiUtil.isBarShow(false, false);
        // 从屏幕上边缘往内滑动，查看此时bars的显隐情况
        caseUtil.slideDireciton(null, false, 0f, 1f);
        // 验证：所有bars均显示出来，(TODO 网页随着滑动， 正确下移)
        uiUtil.isBarShow(true, true);
        // 将页面滑至底部，再轻轻将手指向下滑页面
        scrollBottomAndDragUp();
        // 验证： back to top按钮呼出
        assertTrue("Failed to show back to top", isBackToTopShow());
        // 点击back to top按钮
        clickOnBackToTop();
        // 验证：页面回到顶部，浏览器bars正常呼出，手机bars隐藏 (TODO 页面回到顶部)
        uiUtil.isBarShow(true, false);

        // 从屏幕上边缘往内滑动，查看此时bars的显隐情况
        caseUtil.slideDireciton(null, false, 0f, 1f);
        // 验证：所有bars均显示出来，(TODO 网页随着滑动， 正确下移)
        uiUtil.isBarShow(true, true);
        // 手指向上滑动至网页底部
        scrollTo(true);
        // 验证： 所有bars隐藏
        uiUtil.isBarShow(false, false);
        // 手指向下滑至页面顶部
        scrollTo(false);
        // 验证： 网页回到顶部后，浏览器bars正常呼出
        uiUtil.isBarShow(true, false);
        // 进行横竖屏切换
        setLANDSCAPE();
        // 验证：网页显示正常，手机bars保持隐藏（TODO 网页显示正常）
        assertTrue("Failed to hide phone bars. ", uiUtil.isPhoneBarHide());
    }

    private void setLANDSCAPE() {
        solo.sleep(Res.integer.time_wait);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void clickOnBackToTop() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(backToTopView);
        solo.sleep(Res.integer.time_wait * 5);
    }

    private boolean isBackToTopShow() {
        solo.sleep(Res.integer.time_wait * 4);
        ViewGroup view = (ViewGroup) solo.getView("id/content_view");
        for (int i = 0; i < view.getChildCount(); ++i) {
            String name = view.getChildAt(i).getClass().getSimpleName();
            if (!name.equals("FrameLayout") && !name.equals("TinyTitleBar")) {
                backToTopView = view.getChildAt(i);
            }
        }
        return backToTopView != null;
    }

    private void scrollTo(boolean isTop) {
        float per = isTop ? 3 / 5f : 2 / 5f;
        for (int i = 0; i < 8; ++i) {
            caseUtil.slideDireciton(null, false, per, 6f);
        }
        solo.sleep(Res.integer.time_wait);
    }

    private void scrollBottomAndDragUp() {
        scrollTo(true);
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, false, 1 / 3f, 6f);
        solo.sleep(Res.integer.time_wait);
    }

    private void dragMiddle(boolean isUp) {
        float per = isUp ? 2 / 3f : 1 / 3f;
        caseUtil.slideDireciton(null, false, per, 1f);
        solo.sleep(Res.integer.time_wait);
    }

    private void openFullScreen() {
        uiUtil.enterSideBar(false);
        solo.sleep(Res.integer.time_wait);
        View fullScreen = caseUtil.getViewByIndex(solo.getView("id/list_installed_plugin"),
                new int[] { 0, 0, 0 });
        solo.clickOnView(fullScreen);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to hide phone bars", uiUtil.isPhoneBarHide());
        if (caseUtil.getDisplayRange() == 0) {
            solo.clickOnText("Got it");
            solo.sleep(Res.integer.time_wait);
        }
    }

    private void clickAddressbarAndVisiteUrl() {
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
        solo.clearEditText(0);
        solo.enterText(0, longUrl);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/go"));
        solo.sleep(Res.integer.time_wait);
    }
}