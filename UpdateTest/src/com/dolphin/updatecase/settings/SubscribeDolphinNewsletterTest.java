package com.dolphin.updatecase.settings;

import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_035")
@TestClass("设置（默认）_About Dolphin（App info，Subscribe Dolphin newsletter）")
public class SubscribeDolphinNewsletterTest extends BaseTest {

    public void testSubscribeDolphinNewsletter() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(false);

        // 验证：About Dolphin
        solo.clickOnText(caseUtil.getTextByRId("pref_category_help"), 0, true);

        // 验证：快速订阅中显示升级前输入的邮箱账号 > autotest@gmail.com
        solo.clickOnText(caseUtil.getTextByRId("pref_category_update"), 0, true);
        solo.sleep(Res.integer.time_wait);
        String Email = ((TextView) solo.getView("email")).getText().toString();
        assertTrue("快速订阅中 邮箱账号未显示", Email.equals("autotest@gmail.com"));
    }
}
