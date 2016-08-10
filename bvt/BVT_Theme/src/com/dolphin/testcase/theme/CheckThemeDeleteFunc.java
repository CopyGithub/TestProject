package com.dolphin.testcase.theme;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-304
 * <p>
 * 脚本描述：验证在主题主界面可以成功删除主题
 * <p>
 * 需要点击Theme Store, 目前必定失败
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-304")
@TestClass("验证在主题主界面可以成功删除主题")
public class CheckThemeDeleteFunc extends BaseTest {
    private String themeNameA = new String();
//    private String themeNameB = new String();

    public void testCheckThemeDeleteFunc() {
        uiUtil.skipWelcome();

        init();

        // TODO 长按默认主题没有删除项，无法实现
        // TODO 长按自定义主题C后相关功能无法实现
        // 长按预置主题A 选择"Delete"后
        caseUtil.longClickAndClickPopIndex(solo.getView("skin_icon_container", 5), 1);
        solo.sleep(Res.integer.time_wait);
        TextView message = (TextView) solo.getView("message");
        CharSequence mes = message.getText();
        CharSequence mesHope = caseUtil.getTextByRId("theme_activity_uninstall_msg");
        // 验证：弹出"Are you sure want to delete "预置主题A名称"?"对话框
        assertTrue("没有弹出Are you sure want to delete 预置主题A名称?对话框",
                mes.subSequence(0, 32).equals(mesHope.subSequence(0, 32)));
        // 验证：选择"Cancel", 弹出框消失，主题A不会被删除
        solo.clickOnText(caseUtil.getTextByRId("theme_installed_dlg_cancel"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("主题A被错误删除",
                ((TextView) solo.getView("skin_title", 5)).getText().equals(themeNameA));
        // 验证：选择"OK"，弹出框消失，主题A被删除
        caseUtil.longClickAndClickPopIndex(solo.getView("skin_icon_container", 5), 1);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("theme_installed_dlg_confirm"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("主题A未被删除",
                !((TextView) solo.getView("skin_title", 5)).getText().equals(themeNameA));
        // 验证：长按通过Theme Store下载的主题B后删除的功能
//        caseUtil.longClickAndClickPopIndex(solo.getView("skin_icon_container", 4), 1);
//        solo.sleep(Res.integer.time_wait);
//        solo.clickOnText(caseUtil.getTextByRId("theme_installed_dlg_confirm"));
//        solo.sleep(Res.integer.time_wait);
//        assertTrue("主题B未被删除",
//                !((TextView) solo.getView("skin_title", 4)).getText().equals(themeNameB));
    }

    private void init() {
        // 1.已下载预置主题A、通过Themes Store下载主题B、(自定义添加主题C无法实现)
        // 2.当前是Themes主界面
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        // 下载预置主题A
        themeNameA = (String) ((TextView) solo.getView("skin_title", 5)).getText();
        solo.clickOnView(solo.getView("skin_icon_container", 5));
        solo.sleep(Res.integer.time_open_url);
        /*
        View themeStore = utils.getParent(
                caseUtil.getViewByText("theme_store", 0, true, true, false), 1);
        solo.clickOnView(themeStore);
        solo.sleep(Res.integer.time_launch);
        // 通过Themes Store下载主题B
        solo.clickOnWebElement(solo.getWebElement(By.className("icon-download"), 2));
        themeNameB = solo.getWebElement(By.className("name"), 2).getText();
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("theme_installed_dlg_confirm"));
        solo.sleep(Res.integer.time_wait);
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        */
    }
}
