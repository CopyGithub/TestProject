package com.dolphin.testcase.addto;

import android.util.Log;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1942
 * <p>
 * 脚本描述：验证添加Speed Dial弹框直接Add
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1942")
@TestClass("验证添加Speed Dial弹框直接Add")
public class AddToSpeeddialTest extends BaseTest {
    private String baiduURL = Res.string.url_three;
    private String baiduName = "three";

    public void testAddToSpeeddial() {
        uiUtil.skipWelcome();

        // 验证：添加到Speed Dial,Add to弹框消失，Toast提示Successfully add to speed dial.
        uiUtil.addSpeedDial(baiduURL, baiduName);

        // 验证：进入Speed dial,Speed dial中新增xxx
        assertTrue("Failed to add a shortcut to the home screen", isInSpeeddial(baiduName));

        // 验证：点击xxx，是否打开该网页
        assertTrue("Failed to open the shortcut in the home scrren",
                uiUtil.isIntoNewBookmark(baiduName, baiduURL, true));
    }

    private boolean isInSpeeddial(String webName) {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        Log.i("sjguo", solo.searchText(webName) + "");
        return solo.searchText(webName);
    }

}