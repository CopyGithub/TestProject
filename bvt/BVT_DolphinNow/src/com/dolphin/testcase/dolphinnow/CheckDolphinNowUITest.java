package com.dolphin.testcase.dolphinnow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: <br>
 * 1.DOLINT-777 <br>
 * 2.DOLINT-778 <br>
 * 3.DOLINT-779 <br>
 * <p>
 * 脚本描述: <br>
 * 1.验证Dolphin now页在大屏机上的显示 <br>
 * 2.验证dolphin now页在中屏机的显示 <br>
 * 3.验证dolphin now页在小屏机(4.0系统以上)的显示 <br>
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-777,DOLINT-778,DOLINT-779")
@TestClass("验证Dolphin now页在大/中/小屏机上的显示")
public class CheckDolphinNowUITest extends BaseTest {

    // 验证：DolphinNow界面
    public void testCheckDolphinNowUI() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        // 验证：search模块格式
        checkSearchModule();

        /**
         * 验证：卡片格式 <br>
         * 1.模块名称（Exchange Rate/Music/Video/Game/Joke/Wallpaper/Website) <br>
         * 2.右边是刷新icon（icon为顺时针方向箭头的圆圈，Website，Game后面无刷新icon） <br>
         * 3.More （字体颜色和当前主题色一致）
         */
        uiUtil.assertCardText("exchange_rate", 1, true);
        uiUtil.assertCardText("music", 2, true);
        uiUtil.assertCardText("video", 3, true);
        uiUtil.assertCardText("game", 4, false);
        uiUtil.assertCardText("joke", 5, true);
        uiUtil.assertCardText("wallpaper", 6, true);
        uiUtil.assertCardText("website", 7, false);

        // 验证：Card Management设置项
        ViewGroup parent = (ViewGroup) solo.getView("card_page_root");
        TextView manage = (TextView) parent.getChildAt(8);
        assertTrue("Card Management未显示",
                caseUtil.getTextByRId("card_management", -1).equals(manage.getText().toString()));

        uiUtil.assertCardMore(1);
        uiUtil.assertCardMore(2);
        uiUtil.assertCardMore(3);
        uiUtil.assertCardMore(6);
        uiUtil.assertCardMore(7);
    }

    private void checkSearchModule() {
        // 验证：居中显示当前搜索引擎的大icon(TODO)，icon右侧是向下箭头。下方是搜索框，搜索框右侧是放大镜icon
        View parent = solo.getView("card_page_root");
        View engine = caseUtil.getViewByIndex(parent, new int[] { 0, 0, 0 });
        ImageView arrow = (ImageView) caseUtil.getViewByIndex(parent, new int[] { 0, 0, 1 });
        assertTrue("向下箭头未在引擎icon右侧", utils.ubietyOfView(engine, arrow, true, false, false) != -1);
    }
}