package com.dolphin.testcase.theme;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-270
 * <P>
 * 脚本描述：验证中屏机Themes主界面在有网时显示正常
 * 
 * @author xweng
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-270")
@TestClass("验证中屏机Themes主界面在有网时显示正常")
public class CheckThemeActivityUI extends BaseTest {
    ArrayList<String> skinTitles = new ArrayList<String>();

    public void testCheckThemeActivityUI() {
        uiUtil.skipWelcome();

        // 非中屏机返回，进入THEMES界面，获取竖屏下壁纸顺序
        prepareAndEnterThemeActicity();
        // 验证：标题左侧显示返回图标"<"
        assertTrue("左侧未显示返回图标'<'", solo.getView("back_btn") != null);
        /**
         * 验证：竖屏下
         * <p>
         * Theme Store，Customize和默认壁纸（被选中）
         * <p>
         * 预置壁纸（壁纸缩略图），右下角有下载图标
         */
        checkThemeUI();
        // 验证：底部为“Management”按钮
        assertTrue("未显示Management按钮或不在底部", checkManagementUI());
        // 验证：壁纸一行三个排列
        checkDisplay(3);

        // 切换为横屏且回到顶部后
        changToLANDSCAPEAndTop();
        // 验证：壁纸内容按1行5张排列，超过5张排列到下一行
        checkDisplay(5);
        // 验证：壁纸顺序与竖屏时相同
        checkSkinOrderAndThemeUI();
    }

    private void prepareAndEnterThemeActicity() {
        // 非中屏机返回
        if (caseUtil.getDisplayRange() != 1) {
            return;
        }
        // 进入THEMES界面
        uiUtil.clickOnMenuItem("themes");
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有正确进入Theme界面", solo.getCurrentActivity().toString().contains("ThemeActivity"));
        getSkinTitle();
    }

    private void checkThemeUI() {
        // 记录前四张没有下载图标的skin
        int recordInvisibleCheckbox = 0;
        ViewGroup themeGridView = (ViewGroup) caseUtil
                .getViewByClassName("ThemeGridView", 0, false);
        do {
            for (int i = 0; i < themeGridView.getChildCount(); i++) {
                TextView skinTitle = (TextView) solo.getView("skin_title", i);
                Drawable pic = skinTitle.getCompoundDrawables()[2];
                if (recordInvisibleCheckbox < 3) {
                    recordInvisibleCheckbox++;
                    assertTrue("Theme Store，Customize和默认壁纸右下角不该出现图标.", pic == null);
                } else {
                    assertTrue("预置壁纸右下角无下载图标.", pic != null);
                }
            }
        } while (solo.scrollDown());

        // assertTrue("未显示Theme Store",
        // skinTitles.get(0).equals(caseUtil.getTextByRId("theme_store")));
        assertTrue("未显示Customize",
                skinTitles.get(1).equals(caseUtil.getTextByRId("theme_customize")));
        assertTrue("未显示默认壁纸或未被选中", ((ImageView) solo.getView("skin_selection", 2)) != null
                || skinTitles.get(2).equals(caseUtil.getTextByRId("theme_default_wallpaper")));

        // 回到顶部
        while (solo.scrollUp()) {
            caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        }
    }

    private void getSkinTitle() {
        ViewGroup themeGridView = (ViewGroup) caseUtil
                .getViewByClassName("ThemeGridView", 0, false);
        int count = 0;
        // Customize通过在Id为icon_desc里面，需要另外处理
        // theme store的处理也是如此，目前用空处理theme store，见下第二行
        TextView skinTitle1 = (TextView) solo.getView("icon_desc", 1);
        skinTitles.add(count++, "");
        skinTitles.add(count++, (String) skinTitle1.getText());
        do {

            for (int i = 2; i < themeGridView.getChildCount(); i++) {
                TextView skinTitle = (TextView) solo.getView("skin_title", i);
                if (!skinTitles.contains((String) skinTitle.getText())) {
                    skinTitles.add(count, (String) skinTitle.getText());
                    ++count;
                }
            }
        } while (solo.scrollDown());
        // 回到顶部
        while (solo.scrollUp()) {
            caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        }
    }

    private boolean checkManagementUI() {
        TextView management = (TextView) caseUtil.getView(null, "id/theme_manager");
        String showText = management.getText().toString();
        String originText = caseUtil.getTextByRId("theme_manage");
        // 判断是否在底部的方式是view的y轴和屏幕可视最大y点相等
        int[] xy = new int[2];
        View bottomView = caseUtil.getView(null, "bottom_bar");
        bottomView.getLocationOnScreen(xy);
        boolean flag = bottomView.getHeight() + xy[1] == caseUtil.getDisplaySize(false)[1];
        return flag && showText.equals(originText);
    }

    private void changToLANDSCAPEAndTop() {
        solo.sleep(Res.integer.time_wait);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_change_activity);
        while (solo.scrollUp()) {
            caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        }
    }

    private void checkDisplay(int column) {
        View parent = caseUtil.getView(null, "theme_grid");
        ArrayList<View> views = utils.getChildren(parent, false);
        utils.ubietyOfViews(views, column, true, true, true);
    }

    private void checkSkinOrderAndThemeUI() {
        int count = 0;
        ViewGroup themeGridView = (ViewGroup) caseUtil
                .getViewByClassName("ThemeGridView", 0, false);
        ArrayList<String> skinTitlesAfter = new ArrayList<String>();
        // Customize通过在Id为icon_desc里面，需要另外处理
        // theme store的处理也是如此，目前用空处理theme store，见下第二行
        TextView skinTitle1 = (TextView) solo.getView("icon_desc", 1);
        skinTitlesAfter.add(count++, "");
        skinTitlesAfter.add(count++, (String) skinTitle1.getText());
        do {
            for (int i = 2; i < themeGridView.getChildCount(); i++) {
                TextView skinTitle = (TextView) solo.getView("skin_title", i);
                if (!skinTitlesAfter.contains((String) skinTitle.getText())) {
                    skinTitlesAfter.add(count, (String) skinTitle.getText());
                    ++count;
                }
                if (skinTitles.get(count - 1).equals(skinTitlesAfter.get(count - 1))) {
                    continue;
                } else {
                    assertTrue("横屏的skin_icon第" + i + "个与竖屏不同", false);
                }
            }
        } while (solo.scrollDown());

        // assertTrue("未显示Theme Store",
        // skinTitlesAfter.get(0).equals(caseUtil.getTextByRId("theme_store")));
        assertTrue("未显示Customize",
                skinTitlesAfter.get(1).equals(caseUtil.getTextByRId("theme_customize")));
        assertTrue("未显示默认壁纸或未被选中", ((ImageView) solo.getView("skin_selection", 2)) != null
                || skinTitlesAfter.get(2).equals(caseUtil.getTextByRId("theme_default_wallpaper")));
    }
}
