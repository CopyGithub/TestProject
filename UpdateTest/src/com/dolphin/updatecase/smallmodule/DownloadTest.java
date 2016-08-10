package com.dolphin.updatecase.smallmodule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_012")
@TestClass("下载管理_下载几个文件")
public class DownloadTest extends BaseTest {
    public void testDownload() {
        // 升级前访问的是Res.string.down_apk,Res.string.down_pdf,对这两项分别进行下载
        uiUtil.skipWelcome();
        uiUtil.clickOnMenuItem("action_menu_item_download");

        ArrayList<String> strings = getDownloadList();
        assertTrue("下载列表中不存在记录", strings != null);

        for (int i = 0; i < strings.size(); i++) {
            Pattern namePattern = Pattern.compile("down.*?\\.[a-z]{3}");
            Matcher matcher = namePattern.matcher(strings.get(i));
            assertTrue("下载列表中不是之前下载的记录", matcher.find());
        }
    }

    private ArrayList<String> getDownloadList() {
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<View> views = caseUtil.getViews(null, "download_title");
        for (View view : views) {
            strings.add(((TextView) view).getText().toString());
        }
        return strings;
    }

}
