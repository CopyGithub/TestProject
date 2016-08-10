package com.dolphin.testcase.addressbar;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1560
 * <p>
 * 脚本描述：在网页中点击地址栏后,界面显示正常
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1560")
@TestClass("在网页中点击地址栏后,界面显示正常")
public class CheckClickAddressBarHasWebTest extends BaseTest {
    private String url = "http://sina.cn/";
    private String newUrl = Res.string.url_test;
    private EditText editText;
    private String grayColor = "8affffff";

    public void testCheckClickAddressBarHasWeb() {
        uiUtil.skipWelcome();
        init();
        // 验证：搜索编辑界面是否正常
        checkSearchUI();

        // 点击url任意位置,点击处显示光标
        // 点击软键盘中删除键,光标前单个字符被删除
        checkCursor();

        // 验证：点击url框中的"×" 浏览器响应正常
        checkSearchUIAfterClose();

        // 输入新网址 -> 点击"→",返回浏览器主页,开始加载网址,地址栏中显示为网站logo和网址
        checkNewGoSearch();
    }

    private void checkNewGoSearch() {
        int searchSize = solo.getViews().size();
        solo.enterText(0, newUrl);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/go"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有返回到浏览器主页", solo.getViews().size() != searchSize);
        assertTrue("地址栏中网址错误", uiUtil.checkURL(newUrl));
    }

    private void checkSearchUIAfterClose() {
        // 点击url框中的"×"(TODO)

        // 清空url框，代替：点击url框中的"×"
        solo.clearEditText(editText);
        solo.sleep(Res.integer.time_wait);
        checkAddressBarInputBox();
        solo.sleep(Res.integer.time_wait);
        assertTrue("Go图标未消失", solo.getView("id/go_group").getVisibility() == View.GONE);
        assertTrue("x图标未出现", solo.getView("id/cancel_group").getVisibility() == View.VISIBLE);
    }

    private void checkAddressBarInputBox() {
        TextView hint = (TextView) solo.getView("id/title_design");
        assertTrue(
                "地址栏内不含灰字：Search or enter address",
                hint.getHint().toString()
                        .equals(caseUtil.getTextByRId("search_box_hint_addressbar", -1)));
        assertTrue("地址栏内的：Search or enter address不是左对齐", hint.getTextAlignment() == 1);
        String color = Integer.toHexString(hint.getTextColors().getDefaultColor());
        assertTrue("不是灰字", color.equals(grayColor));
    }

    private void checkCursor() {
        // 验证光标
        editText = (EditText) solo.getView("search_input");
        int[] inputLoc = new int[2];
        editText.getLocationOnScreen(inputLoc);
        // 点击输入框的后面部分（3/4处）
        solo.clickOnScreen(inputLoc[0] + 3 * editText.getWidth() / 4,
                inputLoc[1] + editText.getHeight() / 2);
        solo.sleep(Res.integer.time_wait);
        assertTrue("点击url最后位置,未在点击处显示光标", url.charAt(editText.getSelectionStart() - 1) == '/');
        solo.sendKey(Solo.DELETE);
        solo.sleep(Res.integer.time_wait);
        String string = ((TextView) solo.getView("search_input")).getText().toString();
        String compareUrl = "http://sina.cn";
        assertTrue("光标前单个字符未被成功删除", compareUrl.equals(string));
    }

    private void checkSearchUI() {
        clickAddressBar();
        // 验证：从左到右为搜索引擎图标、url输入框(全选显示当前网址和"×"图标)、"→"图标
        ArrayList<View> views = new ArrayList<View>();
        ViewGroup searchBar = (ViewGroup) solo.getView("search_bar");
        View engineView = searchBar.getChildAt(0);
        View inputView = searchBar.getChildAt(1);
        View buttonView = searchBar.getChildAt(2);
        views.add(engineView);
        views.add(inputView);
        views.add(buttonView);
        utils.ubietyOfViews(views, 0, false, false, false);
        assertTrue("GO图标未显示",
                ((ViewGroup) buttonView).getChildAt(0).getVisibility() == View.VISIBLE);
        // 验证：软键盘自动弹出
        assertTrue("软键盘没有自动弹出", caseUtil.hideSoftKeyboard((TextView) solo.getView("search_input")));
    }

    // 点击地址栏
    private void clickAddressBar() {
        caseUtil.slideDireciton(null, false, 1 / 3f, 1f);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("title_bg"));
        solo.sleep(3 * 1000);
    }

    private void init() {
        // 预置条件：已打开任意网页
        uiUtil.visitUrl(url);
        solo.sleep(Res.integer.time_change_activity);
    }
}