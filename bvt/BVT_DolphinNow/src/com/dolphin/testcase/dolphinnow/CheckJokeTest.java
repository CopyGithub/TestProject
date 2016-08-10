package com.dolphin.testcase.dolphinnow;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-879
 * <p>
 * 脚本描述: 验证Jokes卡片的展示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-879")
@TestClass("验证Jokes卡片的展示")
public class CheckJokeTest extends BaseTest {

    public void testCheckJoke() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        // 验证：Joke卡片从上至下为:1.Joke 刷新图标 2.纯文字笑话内容
        uiUtil.assertCardText("joke", 5, true);

        View parent = solo.getView("card_page_root");
        View view = caseUtil.getViewByIndex(parent, new int[] { 5, 0 });
        View content = caseUtil.getViewByIndex(parent, new int[] { 5, 1, 0, 0 });
        assertTrue("Joke和刷新图标不在笑话内容上方",
                utils.ubietyOfView(view, content, false, false, false) != -1);
    }
}