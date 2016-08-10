package com.dolphin.testcase.menu;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: <br>
 * 1.DOLINT-224<br>
 * 2.DOLINT-225<br>
 * 3.DOLINT-226<br>
 * 4.DOLINT-227
 * <p>
 * 脚本描述: <br>
 * 1.验证中屏机竖屏上,Menu bar显示在浏览器底部,且UI正常 <br>
 * 2.验证中屏机横屏上,Menu bar显示在tab bar下方,且UI正常 <br>
 * 3.验证大屏机横竖屏上,Menu bar显示在tab bar下方,且UI正常 <br>
 * 4.验证小屏机横竖屏上,Menu bar显示在浏览器顶部,且UI正常
 * 
 * <p>
 * 4个Case都是验证的同一个内容, 只是运行在不同屏幕上,所以进行了整合,由Case判断现在跑的是什么屏幕
 * 
 * @author chchen
 * 
 */

@TestNumber(value = "DOLINT-224,DOLINT-225,DOLINT-226,DOLINT-227")
@TestClass("验证小/中/大屏机竖屏上及中屏机横屏,Menu bar显示的部分正确,且UI正常")
@Reinstall
public class CheckMenuBarTest extends BaseTest {
    boolean activityLandscape = false;
    ViewGroup addressViewGroup;

    public void testCheckMenuBar() {
        uiUtil.skipWelcome();
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_change_activity);

        // 验证:竖屏下menubar显示的位置是否正确
        assertTrue("竖屏下MenuBar显示的位置不正确", isShowMenuBar());
        // 验证:竖屏下menubar上的图标显示顺序和状态正确
        isShowOrder();

        // 横屏 （TODO）
        // if (caseUtil.getDisplayRange() == 1) {
        // // 验证:横屏下menubar显示的位置是否正确
        // solo.sleep(Res.integer.time_wait);
        // solo.setActivityOrientation(Solo.LANDSCAPE);
        // solo.sleep(Res.integer.time_change_activity);
        // activityLandscape = true;
        // assertTrue("横屏下MenuBar显示的位置不正确", isShowMenuBar());
        // // 验证:横屏下menubar上的图标显示顺序和状态正确
        // isShowOrder();
        // }
    }

    private boolean isShowMenuBar() {
        boolean flag = false;
        switch (caseUtil.getDisplayRange()) {
        case 0:
            isShowMenuBar(false);
            break;
        case 1:
            flag = isShowMenuBar(!activityLandscape);
            break;
        case 2:
            isShowMenuBar(false);
            break;
        }
        return flag;
    }

    private void isShowOrder() {
        switch (caseUtil.getDisplayRange()) {
        case 0:
            // TODO 小屏幕机型
            isShowOrderSmall();
            break;
        case 1:
            isShowOrderMiddle(!activityLandscape);
            break;
        case 2:
            // TODO 大屏幕机型
            isShowOrderBig();
            break;
        }
    }

    private void isShowOrderBig() {
        // TODO Auto-generated method stub

    }

    private void isShowOrderSmall() {
        // TODO Auto-generated method stub

    }

    private boolean isShowMenuBar(boolean isBottom) {
        boolean flag = false;
        View back = uiUtil.getMenubarItem(0, 0);
        View menuBar = utils.getParent(back, 2);
        if (isBottom) {
            // 判断是否在底部的方式是view的y轴和屏幕可视最大y点相等
            int[] xy = new int[2];
            menuBar.getLocationOnScreen(xy);
            flag = menuBar.getHeight() + xy[1] == caseUtil.getDisplaySize(false)[1];
            return flag;
        } else {
            // 判断是否在adressbar上, 是采用是否包含地址栏(id/title_bg)的形式判断
            ViewGroup viewGroup = (ViewGroup) menuBar;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                flag = viewGroup.getChildAt(i).equals(solo.getView("title_bg"));
                if (flag) {
                    return true;
                }
            }
            return false;
        }
    }

    private void isShowOrderMiddle(boolean isBottom) {
        if (isBottom) {
            menuBarStatusAndOrder();
        } else {
            addressBarStatusAndOrderMiddle();
        }
    }

    private void menuBarStatusAndOrder() {
        View back = uiUtil.getMenubarItem(0, 0);
        View forword = uiUtil.getMenubarItem(1, 0);
        View menu = uiUtil.getMenubarItem(3, 0);
        View home = uiUtil.getMenubarItem(4, 0);
        View tablist = uiUtil.getMenubarItem(5, 0);
        assertFalse("返回键不是灰显", back.isEnabled());
        assertFalse("前进键不是灰显", forword.isEnabled());
        assertTrue("海豚键不是灰显", menu.isEnabled());
        assertFalse("主页键不是灰显", home.isEnabled());
        assertTrue("tablist键不是灰显", tablist.isEnabled());
        ArrayList<View> views = new ArrayList<View>();
        views.add(back);
        views.add(forword);
        views.add(menu);
        views.add(home);
        views.add(tablist);
        utils.ubietyOfViews(views, 0, false, false, false);
    }

    private void addressBarStatusAndOrderMiddle() {
        // 后退图标，通过back获取到父类视图
        View back = uiUtil.getMenubarItem(0, 0);
        addressViewGroup = (ViewGroup) utils.getParent(back, 2);
        // 书签图标
        View bookmark = addressViewGroup.getChildAt(0);
        // 前进图标
        View forword = addressViewGroup.getChildAt(2);
        // 地址栏，url输入框
        View addressbar = addressViewGroup.getChildAt(4);
        // 海豚图标
        View menu = addressViewGroup.getChildAt(6);
        // 主页图标
        View home = addressViewGroup.getChildAt(7);
        // 手势图标
        View gesture = addressViewGroup.getChildAt(8);
        // control panel图标
        View controlPanel = addressViewGroup.getChildAt(9);
        // tab list图标
        View tablist = addressViewGroup.getChildAt(10);
        // 验证：各图标是否灰显
        assertTrue("书签键不是灰显", bookmark.isEnabled());
        assertFalse("返回键不是灰显", back.isEnabled());
        assertFalse("前进键不是灰显", forword.isEnabled());
        assertTrue("海豚键不是灰显", menu.isEnabled());
        assertFalse("主页键不是灰显", home.isEnabled());
        assertTrue("手势键不是灰显", gesture.isEnabled());
        assertTrue("controlPanel键不是灰显", controlPanel.isEnabled());
        assertTrue("tablist键不是灰显", tablist.isEnabled());

        ArrayList<View> views = new ArrayList<View>();
        views.add(bookmark);
        views.add(back);
        views.add(forword);
        views.add(addressbar);
        views.add(menu);
        views.add(home);
        views.add(gesture);
        views.add(controlPanel);
        views.add(tablist);
        // 验证：图标的左右顺序是否正确
        utils.ubietyOfViews(views, 0, false, false, false);
    }

}