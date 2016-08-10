package com.dolphin.testcase.fullscreen;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1381
 * <p>
 * 脚本描述：验证开启全屏后,主页与网页交互时的bars显隐正常
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1381")
@TestClass("验证开启全屏后,主页与网页交互时的bars显隐正常")
public class ShowBarHomeInteractWebWithFullScreen extends BaseTest {
    private String wallpaperUrl = "http://m.flikie.com/";

    public void testShowBarHomeInteractWebWithFullScreen() {
        uiUtil.skipWelcome();
        // 在主页滑出右侧边栏 -> 开启全屏，查看此时bars的显隐情况
        swipeFullScreen();
        // 验证：浏览器bars（包括menu bar，address bar，tab bar）均显示，手机bars隐藏
        solo.sleep(Res.integer.time_change_activity);
        uiUtil.isBarShow(true, false);
        // 点击一个speed dial（如：Wallpaper）
        View view = caseUtil.getViewByText("wallpaper", 0, false, false, false);
        solo.clickOnView(utils.getParent(view, 1));
        // 验证： 页面加载过程至加载完毕，所有bars均隐藏,页面顶部进度条显示正常
        // TODO 进度条显示不好验证
        solo.sleep(Res.integer.time_change_activity);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.isBarShow(false, false);
        // 从屏幕上边缘往内滑动，查看此时bars的显隐情况
        caseUtil.slideDireciton(null, false, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        // 验证：所有bars均显示出来，(TODO 网页随着滑动， 正确下移)
        uiUtil.isBarShow(true, true);
        // 重复以上步骤
        // TODO 从屏幕下边缘往内滑动，查看此时bars的显隐情况 （下边缘代码无法实现滑动出菜单栏）
        // 验证：所有bars均显示出来，(TODO 网页随着滑动， 正确上移)
        // 点击menu bar上的后退按钮
        solo.clickOnView(uiUtil.getMenubarItem(0, 0));
        solo.sleep(Res.integer.time_wait);
        // 正常后退到home，所有bars均显示
        uiUtil.isBarShow(true, true);
        // 点击menu bar上的前进按钮
        solo.clickOnView(uiUtil.getMenubarItem(1, 0));
        solo.sleep(Res.integer.time_wait);
        // 验证： 正常前进到刚刚打开的页面，所有bars均显示
        assertTrue("Failed to back to forward page .", uiUtil.checkURL(wallpaperUrl));
        caseUtil.slideDireciton(null, false, 0.3f, 1f);
        uiUtil.isBarShow(true, true);
        // 点击menu bar上的home按钮
        solo.clickOnView(uiUtil.getMenubarItem(4, 0));
        solo.sleep(Res.integer.time_wait);
        // 验证：正常回到home，所有bars均显示
        uiUtil.isBarShow(true, true);
    }

    private void swipeFullScreen() {
        uiUtil.enterSideBar(false);
        View fullScreen = caseUtil.getViewByIndex(solo.getView("list_installed_plugin"), new int[] {
                0, 0, 0 });
        solo.clickOnView(fullScreen);
        solo.sleep(Res.integer.time_wait);
        if (caseUtil.getDisplayRange() == 0) {
            solo.clickOnText("Got it");
            solo.sleep(Res.integer.time_wait);
        }
    }
}