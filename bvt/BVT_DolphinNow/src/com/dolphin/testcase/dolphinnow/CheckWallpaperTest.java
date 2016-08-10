package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * DOLINT-843:验证wallpaper卡片的展示<br>
 * DOLINT-848:验证壁纸的HOT分类展示 <br>
 * DOLINT-849:验证壁纸的Top分类展示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-843,DOLINT-848,DOLINT-849")
@TestClass("验证wallpaper相关内容")
public class CheckWallpaperTest extends BaseTest {
    private ArrayList<View> views;
    private String RED = "ffd01a1a";

    public void testCheckWallpaper() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        uiUtil.closeWhistleWhenOpen();

        // 验证：卡片名称Wallpaper，右边是刷新icon（icon为顺时针方向箭头的圆圈）
        uiUtil.assertCardText("wallpaper", 6, true);

        // 验证：Tab：Hot（默认选中）和Top，下面是对应的Wallpaper图片,以2*2形式排列
        View parent = solo.getView("card_page_root");
        TextView hot = (TextView) caseUtil.getViewByIndex(parent, new int[] { 6, 1, 0, 0, 0 });
        TextView top = (TextView) caseUtil.getViewByIndex(parent, new int[] { 6, 1, 0, 0, 1 });
        assertTrue("Hot未选中", hot.isSelected() && !top.isSelected());
        View gridView = caseUtil.getViewByIndex(parent, new int[] { 6, 1, 1 });
        views = utils.getChildren(gridView, false);
        utils.ubietyOfViews(views, 2, true, true, true);

        // 验证：More （字体颜色和当前主题色一致）
        uiUtil.assertCardMore(6);

        // 验证：Hot/Top字体变红，下面的下划线也变红(TODO)，wallpaper下显示HOT分类的四张图片(TODO)
        checkRedColor(hot, top);
    }

    private void checkRedColor(TextView hot, TextView top) {
        solo.clickOnView(hot);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Hot字体未变红", Integer.toHexString(hot.getCurrentTextColor()).equals(RED));

        solo.clickOnView(top);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Top字体未变红", Integer.toHexString(top.getCurrentTextColor()).equals(RED));
    }
}