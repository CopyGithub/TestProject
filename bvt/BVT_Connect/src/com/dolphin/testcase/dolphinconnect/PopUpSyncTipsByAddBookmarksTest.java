package com.dolphin.testcase.dolphinconnect;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

/**
 * 脚本编号:DOLINT-1201
 * <p>
 * 脚本描述:验证进入登录界面的入口
 * <p>
 * 添加3个书签
 * 
 * @author chchen
 * 
 */

// @Reinstall
// @TestNumber(value = "DOLINT-1201")
// @TestClass("验证进入登录界面的入口_添加3个书签")
public class PopUpSyncTipsByAddBookmarksTest extends BaseTest {

    public void testPopUpSyncTipsByAddBookmarks() {
        uiUtil.skipWelcome();
        // 加入3个书签
        addBookmarks();
        // 验证正确弹出了Sync的tips
        // 小屏机不执行添加新的tab
        if (caseUtil.getDisplayRange() == 0) {
            caseUtil.clickOnView("address_home_button");
        } else {
            uiUtil.addNewTab(1);
        }
        solo.sleep(Res.integer.time_wait);
        assertTrue("没有正确弹出同步书签的Tips",
                caseUtil.searchText("sync_promotion_bookmark", 0, true, true, true));
        // 验证:正确进入Dolphin Connect界面
        assertTrue("点击Sync后没有正确进入Dolphin Connect登录界面", uiUtil.isDolphinConnectActivity());
        solo.sleep(4 * 1000);
    }

    private void addBookmarks() {
        solo.sleep(Res.integer.time_wait);
        uiUtil.addBookmark(Res.string.url_aaa, "aaa");
        uiUtil.addBookmark(Res.string.url_downloadtest, "bbb");
        uiUtil.addBookmark(Res.string.url_test, "ccc");
        solo.sleep(Res.integer.time_wait);
    }
}