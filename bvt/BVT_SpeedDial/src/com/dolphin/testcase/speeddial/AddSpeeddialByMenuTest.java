package com.dolphin.testcase.speeddial;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-11
 * <p>
 * 脚本描述：验证通过Menu中Add speed dial选项正常添加speed dial
 * <p>
 * 脚本修改了海豚的数据, 目前以重新安装来解决
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-11")
@TestClass("验证通过Menu中Add speed dial选项正常添加speed dial")
public class AddSpeeddialByMenuTest extends BaseTest {
    final private String shortUrl = Res.string.url_test;
    final private String longURL = Res.string.url_downloadtest;
    final private String shortName = "test";
    final private String longName = "downloadtest";

    public void testAddSpeeddialByMenuTest() {
        uiUtil.skipWelcome();
        // 验证："Add speed dial"编辑框关闭,并弹出toast "Successfully add to speed dial"
        uiUtil.addSpeedDial(shortUrl, shortName);
        /**
         * 验证:Speed dial列表中追加显示搜狐网页,图标(TODO
         * 图标)、名称和点击后的url正确(搜狐图标、"搜狐网"、"www.soho.com")
         */
        assertTrue("Failed to show add speeddial .", gobackToCheckAddName(shortName));
        assertTrue("the sohu url address is not right.",
                uiUtil.isIntoNewBookmark(shortName, shortUrl, true));
        // 验证：新增speed dial名为:"Foreign Poli..."
        addNewLongSpeeddial();
        // 验证:新tab中speed dial区域包含新增speed dial,图标(TODO 图标)、位置及点击后的url正确
        // 返回，创建新tab
        uiUtil.addNewTab(1);
        assertTrue("Failed to show add speeddial .", solo.searchText(longName));
        assertTrue("the long  url address is not right.",
                uiUtil.isIntoNewBookmark(longName, longURL, true));
    }

    private void addNewLongSpeeddial() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.addSpeedDial(longURL, longName);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
    }

    private boolean gobackToCheckAddName(String name) {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        return solo.searchText(name);
    }

}