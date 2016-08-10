package com.dolphin.testcase.theme;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-269
 * <P>
 * 脚本描述：验证能正常进入Themes主界面(横竖屏)
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-269")
@TestClass("验证能正常进入Themes主界面(横竖屏)")
public class CheckEnterThemeFunc extends BaseTest {
    public void testCheckEnterTheme() {
        uiUtil.skipWelcome();
        // 竖屏
        solo.setActivityOrientation(Solo.PORTRAIT);
        // 进入主题设置
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        // 验证：判断是否进入到THEMES主界面
        assertTrue("没有正确进入THEMES主界面", solo.getCurrentActivity().toString()
                .contains("ThemeActivity"));

        // 回主界面
        solo.goBack();
        solo.sleep(Res.integer.time_wait);

        // 横屏
        solo.setActivityOrientation(Solo.LANDSCAPE);
        // 进入主题设置
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        // 验证：判断是否进入到THEMES主界面
        assertTrue("没有正确进入THEMES主界面", solo.getCurrentActivity().toString()
                .contains("ThemeActivity"));
    }
}
