package com.dolphin.testcase.dolphinnow;

import android.view.View;
import android.view.ViewGroup;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-863
 * <p>
 * 脚本描述: 验证video卡片的开启和关闭功能
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-863")
@TestClass("验证video卡片的开启和关闭功能")
public class CheckVideoOpenAndCloseTest extends BaseTest {

    public void testCheckVideoOpenAndClose() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        uiUtil.closeWhistleWhenOpen();
        solo.scrollToBottom();
        solo.sleep(Res.integer.time_wait);
        // 验证：video后面的开关为关闭
        checkOnOff(false);

        // 验证：video模块不显示
        checkVideoShow(false);

        // 验证：video后面的开关为开启
        checkOnOff(true);

        // 验证：video模块显示
        checkVideoShow(true);

    }

    private void checkVideoShow(boolean isShow) {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        ViewGroup parent = (ViewGroup) solo.getView("card_page_root");
        if (isShow) {
            assertTrue("video模块未显示", parent.getChildAt(3).getVisibility() == View.VISIBLE);
        } else {
            assertTrue("video模块不应该显示", parent.getChildAt(3).getVisibility() == View.GONE);
        }

    }

    private void checkOnOff(boolean b) {
        ViewGroup parent = (ViewGroup) solo.getView("card_page_root");
        solo.clickOnView(parent.getChildAt(8));
        solo.sleep(Res.integer.time_wait);
        View list = solo.getView("card_management_list");
        View checkBox = caseUtil.getViewByIndex(list, new int[] { 2, 3 });
        solo.clickOnView(checkBox);
        solo.sleep(Res.integer.time_wait);

        if (b) {
            assertTrue("开关没有开启", checkBox.isSelected());
        } else {
            assertTrue("开关没有关闭", !checkBox.isSelected());
        }

    }
}