package com.dolphin.testcase.theme;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-288
 * <P>
 * 脚本描述：验证通过点击进行下载预置主题后应用，主题显示正常
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-288")
@TestClass("验证通过点击进行下载预置主题后应用，主题显示正常")
public class CheckThemeActivityDownloadFunc extends BaseTest {
    private String themeName = "";

    public void testCheckThemeActivityDownloadFunc() {
        uiUtil.skipWelcome();

        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);

        // 验证：点击一张预置壁纸：壁纸开始下载，壁纸名称区域显示当前下载进度
        assertTrue("壁纸未开始下载或壁纸名称区域未显示下载进度", checkThemeDownloadFunc());
        // 验证：下载完成后：进度条消失
        assertTrue("下载进度条未消失", checkThemeDownloadFinishFunc());
        // 验证：壁纸右下角显示被选中图标。主题切换成刚刚下载的主题
        assertTrue("壁纸右下角未显示被选中图标或主题未切换",
                solo.getView("skin_selection") != null || uiUtil.isAppliedTheme(themeName));
        // TODO 颜色跟壁纸颜色值一致，无法验证
        // 验证：退出Themes主界面后重新进入：刚下载的预置壁纸显示在默认壁纸后的第一个位置
        assertTrue("刚下载的预置壁纸未显示在默认壁纸后的第一个位置", checkThemeSkinLocation());
    }

    private boolean checkThemeDownloadFunc() {
        themeName = ((TextView) solo.getView("skin_title", 5)).getText().toString();
        solo.clickOnView(solo.getView("skin_icon_container", 5));
        CharSequence rate = ((TextView) solo.getView("skin_title", 5)).getText();
        solo.sleep(Res.integer.time_wait);
        return rate.charAt(1) == '%' || rate.charAt(2) == '%';
    }

    private boolean checkThemeDownloadFinishFunc() {
        solo.sleep(Res.integer.time_wait);
        CharSequence rate = ((TextView) solo.getView("skin_title", 5)).getText();
        return rate.charAt(2) != '%' && rate.charAt(3) != '%';
    }

    private boolean checkThemeSkinLocation() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_change_activity);
        TextView skinTitle = (TextView) solo.getView("skin_title", 3);
        return skinTitle.getText().toString().equals(themeName);
    }
}
