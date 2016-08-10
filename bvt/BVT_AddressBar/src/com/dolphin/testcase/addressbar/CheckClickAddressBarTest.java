package com.dolphin.testcase.addressbar;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1552
 * <p>
 * 脚本描述：主页中点击地址栏后,界面显示正常
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1552")
@TestClass("主页中点击地址栏后,界面显示正常")
@Reinstall
public class CheckClickAddressBarTest extends BaseTest {

    public void testCheckClickAddressBar() {
        uiUtil.skipWelcome();

        // 验证：搜索编辑界面是否正常
        checkSearchUI();

        // 点击"×",返回浏览器主页
        checkIsBackToMain(0);

        // 再次点击地址栏 -> 点击手机back键,软键盘关闭
        checkKeyboardClosed();

        // 再次点击手机back键 返回浏览器主页
        checkIsBackToMain(1);

    }

    private void checkKeyboardClosed() {
        clickAddressBar();
        solo.goBack();
        // 验证：软键盘关闭
        assertTrue("软键盘没有关闭", !caseUtil.hideSoftKeyboard((TextView) solo.getView("search_input")));
    }

    private void checkIsBackToMain(int flag) {
        int searchSize = solo.getViews().size();
        if (flag == 0) {
            solo.clickOnView(solo.getView("id/cancel"));
        } else if (flag == 1) {
            solo.goBack();
        }
        // 验证：返回浏览器主页
        assertTrue("没有返回到浏览器主页", solo.getViews().size() != searchSize);
    }

    private void checkSearchUI() {
        clickAddressBar();

        // 验证：从左到右为搜索引擎图标、url输入框(全选显示当前网址和"×"图标)、"→"图标
        ArrayList<View> views = new ArrayList<View>();
        ViewGroup searchBar = (ViewGroup) solo.getView("id/search_bar");
        View engineView = searchBar.getChildAt(0);
        View inputView = searchBar.getChildAt(1);
        View buttonView = searchBar.getChildAt(2);
        views.add(engineView);
        views.add(inputView);
        views.add(buttonView);
        utils.ubietyOfViews(views, 0, false, false, false);

        // 验证：url输入框(内含灰字:Search or enter address(左对齐))
        checkAddressBarInputBox();

        // 验证：从上到下为地址栏、标签页、软键盘
        View indicator = solo.getView("id/indicator");
        assertTrue("搜索页面的内容从上到下位置不正确",
                utils.ubietyOfView(searchBar, indicator, false, false, false) != -1);

        // 验证：标签页为HISTORY / BOOKMARKS(默认选中) / MOST VISITED
        assertTrue("BOOKMARKS不是默认选中", uiUtil.getCurrentTabName() == 2);

        // 验证：软键盘自动弹出
        assertTrue("软键盘没有自动弹出", caseUtil.hideSoftKeyboard((TextView) solo.getView("search_input")));

    }

    private void checkAddressBarInputBox() {
        TextView hint = (TextView) solo.getView("id/title_design");
        assertTrue(
                "地址栏内不含灰字：Search or enter address",
                hint.getHint().toString()
                        .equals(caseUtil.getTextByRId("search_box_hint_addressbar", -1)));
        assertTrue("地址栏内的：Search or enter address不是左对齐", hint.getTextAlignment() == 1);
    }

    // 点击地址栏
    private void clickAddressBar() {
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
    }
}