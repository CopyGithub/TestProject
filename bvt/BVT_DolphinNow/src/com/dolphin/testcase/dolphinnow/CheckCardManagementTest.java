package com.dolphin.testcase.dolphinnow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-861
 * <p>
 * 脚本描述: 验证card management UI显示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-861")
@TestClass("验证card management UI显示")
public class CheckCardManagementTest extends BaseTest {

    public void testCheckCardManagement() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        uiUtil.closeWhistleWhenOpen();
        solo.scrollToBottom();
        solo.sleep(Res.integer.time_wait);
        ViewGroup parent = (ViewGroup) solo.getView("card_page_root");
        solo.clickOnView(parent.getChildAt(8));
        solo.sleep(Res.integer.time_wait);
        /**
         * 验证： card management里从左到右依次显示 <br>
         * ①“三” <br>
         * ②卡片名称（Exchange Rate,Music,Video,Game,Joke,Wallpaper,Website）七个选项 <br>
         * ③右侧是开关，默认开关为开启 <br>
         */
        assertCardManagement("exchange_rate", 0);
        assertCardManagement("music", 1);
        assertCardManagement("video", 2);
        assertCardManagement("game", 3);
        assertCardManagement("joke", 4);
        assertCardManagement("wallpaper", 5);
        assertCardManagement("website", 6);
    }

    /**
     * 验证：CardManagement的icon，卡片名称，右侧开关默认开
     * 
     * @param rid
     *            所验证卡片的文字的Rid
     * @param index
     *            所验证卡片的索引号
     */
    private void assertCardManagement(String rid, int index) {
        View list = solo.getView("card_management_list");

        // ICON TODO
        TextView title = (TextView) caseUtil.getViewByIndex(list, new int[] { index, 2 });
        String string = caseUtil.getTextByRId(rid, -1);
        assertTrue("第" + (index + 1) + "行文字不匹配", string.equals(title.getText().toString()));

        View rightIcon = caseUtil.getViewByIndex(list, new int[] { index, 3 });
        assertTrue("第" + (index + 1) + "行右侧开关未开启", rightIcon.isSelected());
    }
}