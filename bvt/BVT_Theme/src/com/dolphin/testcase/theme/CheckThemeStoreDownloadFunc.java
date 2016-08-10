package com.dolphin.testcase.theme;

import java.util.ArrayList;

import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.By;

/**
 * 脚本编号：DOLINT-279
 * <p>
 * 脚本描述：验证通过主题商店下载主题应用功能正常
 * 
 * @author xweng
 * 
 */
// @Reinstall
// @TestNumber(value = "DOLINT-279")
// @TestClass("验证通过主题商店下载主题应用功能正常")
public class CheckThemeStoreDownloadFunc extends BaseTest {
    private String name = new String();

    // 暂未考虑小屏机
    public void testCheckThemeStoreDownloadFunc() {
        uiUtil.skipWelcome();

        // 验证：点击Theme Store图标新建Tab，进入Theme Store,打开http://theme.dolphin.com页面
        assertTrue("网页没有加载成功或进入Theme store", checkEnterThemeStore());
        // TODO 通知栏检测无法实现：选择一张壁纸进行下载，通知栏上显示下载任务/下载完成图标
        // 验证：等待弹出"Apply"对话框："The wallpaper has been installed.Apply？"
        assertTrue("没有弹出The wallpaper has been installed.Apply？ ", checkPopUpDialog());
        // 验证：选择"OK"进入Themes主界面，多出刚下载的壁纸A且主题切换成壁纸A
        assertTrue("界面里未出现刚下载的壁纸且主题切换", checkDownloadTheme());
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        // 验证：再次点击Theme Store图标
        assertTrue("网页没有加载成功或进入Theme store", checkEnterThemeStore());
    }

    private boolean checkPopUpDialog() {
        // 选择第三张壁纸下载并获取第三张壁纸的名字
        solo.clickOnWebElement(solo.getWebElement(By.className("icon-download"), 2));
        name = solo.getWebElement(By.className("name"), 2).getText();
        solo.sleep(Res.integer.time_wait);
        TextView message = (TextView) solo.getView("message");
        return message.getText().equals(caseUtil.getTextByRId("wallpaper_installed_dlg_msg"));
    }

    private boolean checkEnterThemeStore() {
        // 进入主题商城
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("theme_store"));
        uiUtil.waitForWebPageFinished();
        return uiUtil.checkURL("http://theme.dolphin.com/");
    }

    private boolean checkDownloadTheme() {
        caseUtil.clickOnText("yes", 1, true);
        solo.sleep(Res.integer.time_wait);
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        ViewGroup themeGridView = (ViewGroup) caseUtil
                .getViewByClassName("ThemeGridView", 0, false);
        ArrayList<String> skinTitles = new ArrayList<String>();
        int count = 0;
        do {
            for (int i = 0; i < themeGridView.getChildCount(); i++) {
                TextView skinTitle = (TextView) solo.getView("skin_title", i);
                if (!skinTitles.contains((String) skinTitle.getText())) {
                    skinTitles.add(count, (String) skinTitle.getText());
                    ++count;
                }
            }
        } while (solo.scrollDown());
        return !skinTitles.contains(name) || solo.getView("skin_selection") != null
                || !uiUtil.isAppliedTheme(name);
    }
}
