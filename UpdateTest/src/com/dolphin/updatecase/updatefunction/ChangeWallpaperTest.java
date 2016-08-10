package com.dolphin.updatecase.updatefunction;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_039")
@TestClass("升级后功能检查_切换壁纸")
public class ChangeWallpaperTest extends BaseTest {

    public void testChangeWallpaper() {
        uiUtil.skipWelcome();
        uiUtil.enterSideBar(false);

        // 打开夜间模式
        solo.clickOnView(uiUtil.getControlPanelView(2));
        uiUtil.enterSideBar(false);
        assertTrue("夜间模式未打开", uiUtil.getControlPanelView(2).isSelected());

        // 进入Theme，选择其他壁纸，如A
        uiUtil.clickOnMenuItem("themes");
        String themeName = ((TextView) solo.getView("skin_title", 5)).getText().toString();
        solo.clickOnView(solo.getView("skin_icon_container", 5));
        solo.clickOnButton(1);
        solo.sleep(10 * 1000);

        // 验证：themeName被设置为当前壁纸，出错原因，网络不好
        assertTrue("主题未切换", uiUtil.isAppliedTheme(themeName));
    }
}
