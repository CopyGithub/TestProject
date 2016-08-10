package com.dolphin.testcase.setting;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1079
 * <p>
 * 脚本描述: 验证"Smart cache"设置项UI显示正常
 * 
 * @author chchen
 * 
 */
@TestNumber(value = "DOLINT-1079")
@TestClass("验证\"Smart cache\"设置项UI显示正常")
public class CheckSmartCacheUITest extends BaseTest {

    public void testCheckSmartCacheUI() {
        uiUtil.skipWelcome();
        uiUtil.enterSetting(true);
        caseUtil.clickOnText("pref_smart_cache", 0, false);
        solo.sleep(Res.integer.time_change_activity);

        // 验证:Smart cache开关：默认开启
        // (Smart cache与标题SMART CACHE重复，无法使用uiUtil.assertSearchText方法判断)
        assertSmartCache();
        // 验证:Default location：默认路径为/storage/sdcard0/TunnyBrowser/cache
        uiUtil.assertSearchText("download_default_path", utils.externalStorageDirectory
                + "/TunnyBrowser/cache", -1, false, false, 0, null);
    }

    private void assertSmartCache() {
        View list = solo.getView("list");
        String hostText = caseUtil.getTextByRId("pref_smart_cache", 0);
        View hostView = caseUtil.getViewByIndex(list, new int[] { 0, 0, 0, 0 });
        boolean flag = ((TextView) hostView).getText().toString().equals(hostText);
        assertTrue("没有找到指定的文本:" + hostText, flag);
        CheckBox checkBox = (CheckBox) caseUtil.getViewByIndex(list, new int[] { 0, 0, 1, 0 });
        assertTrue(hostText + "的默认值不是true", checkBox.isChecked());
    }
}