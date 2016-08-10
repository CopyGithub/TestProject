package com.dolphin.testcase.addto;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1937
 * <p>
 * 脚本描述：验证Add to弹框切换操作
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1937")
@TestClass("验证Add to弹框切换操作")
public class CheckAddToSwitchTest extends BaseTest {
    private String baiduURL = Res.string.url_one;

    public void testCheckAddToSwitch() {
        uiUtil.skipWelcome();
        // 打开add to
        openAddToDialog();

        // 验证：点击Speed Dial图标，切换到Add Speeddial样式
        checkStyle(1, false);

        // 验证：点击Bookmarks图标，切换到Add Bookmarks样式，More保持收缩
        checkStyle(0, true);
        // 验证：点击More图标，More图标消失，展开Folder和Assign gesture选项
        checkSelectionShow();

        // 验证：点击Home Screen图标，切换到Add Home Screen样式
        checkStyle(2, false);

        // 验证：点击Bookmarks图标，切换到Add bookmarks样式，Folder和Assign gesture选项保持显示
        checkStyle(0, false);
    }

    private void checkStyle(int i, boolean showMore) {
        View parent = solo.getView("scrollview_content");
        solo.clickOnView(caseUtil.getViewByIndex(parent, new int[] { 1, i }));
        switch (i) {
        case 0:
            Boolean b;
            if (showMore) {
                b = (caseUtil.getViewByIndex(parent, new int[] { 2 }).getVisibility() == View.VISIBLE);
            } else {
                b = solo.searchText(caseUtil.getTextByRId("default_folder_name"))
                        && solo.searchText(caseUtil.getTextByRId("assign_a_gesture"));
            }
            assertTrue("Not Add Bookmarks style...", b);
            break;
        case 1:
            assertTrue("Not Add Speeddial style...",
                    solo.searchText(caseUtil.getTextByRId("speeddial_description")));
            break;
        case 2:
            assertTrue("Not Home Screen style...",
                    solo.searchText(caseUtil.getTextByRId("homescreen_description")));
            break;
        }

    }

    private void checkSelectionShow() {
        caseUtil.clickOnView("id/more");
        solo.sleep(Res.integer.time_wait);
        assertTrue(
                "Folder and Assign gesture are not show..",
                solo.searchText(caseUtil.getTextByRId("default_folder_name"))
                        && solo.searchText(caseUtil.getTextByRId("assign_a_gesture")));

    }

    private void openAddToDialog() {
        // 打开网页
        uiUtil.visitUrl(baiduURL);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);

        // 点击海豚图标→点击Add to→点击Add
        uiUtil.clickOnMenuItem("add_to");
        solo.sleep(Res.integer.time_wait);

    }
}