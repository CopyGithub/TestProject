package com.dolphin.updatecase.updatefunction;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_037")
@TestClass("升级后功能检查_Bookmark操作")
public class BookmarkOperateTest extends BaseTest {
    private String baiduURL = "www.baidu.com";
    private String baiduName = "百度一下";
    private String haoURL = "hao123.com";
    private String haoName = "天气预报";

    public void testBookmarkOperate() {

        uiUtil.skipWelcome();
        uiUtil.enterBookmark();

        // 验证：打开A > 百度一下:solo.clickOnText(haoName); not worker
        View Folder = caseUtil.getViewByText("A", -1, true, true, true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(Folder);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(baiduName);
        assertTrue("Failed to enter " + baiduName + " website.", uiUtil.checkURL(baiduURL));

        // 验证：打开hao123的天气页面

        solo.goBack();
        uiUtil.enterBookmark();
        solo.clickOnText(haoName);
        TextView textView = (TextView) caseUtil.getView(solo.getView("title_bg"), "title");
        String text = textView.getText().toString().trim();
        solo.sleep(Res.integer.time_wait);
        assertTrue("Failed to enter " + haoName + " website." + "ULR is " + text,
                uiUtil.checkURL(haoURL));
    }

}
