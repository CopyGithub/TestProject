package com.dolphin.updatecase.updatefunction;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_041")
@TestClass("升级后功能检查_下载")
public class DownLoadOperateTest extends BaseTest {
    private ArrayList<View> views;

    public void testDownLoadOperate() {
        // 下载路径
        final String defaultDownloadPath = utils.externalStorageDirectory
                + "/download/DownloadTest";
        uiUtil.skipWelcome();

        // 1.长按升级前下载好的apk(downapk.apk),选择删除
        uiUtil.clickOnMenuItem("action_menu_item_download");
        View downapk = getDownapkView();
        assertTrue("未找到下载过的apk内容", downapk != null);
        if (solo.searchText("File cannot be found")) {
            caseUtil.longClickAndClickPopIndex(downapk, 0);
        } else {
            caseUtil.longClickAndClickPopIndex(downapk, 1);
        }
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_wait);
        // 验证:内容可以正常删除
        assertTrue("内容未正常删除", getDownapkView() == null);

        // 2.再次下载downapk.apk
        uiUtil.visitUrl(Res.string.down_apk);
        solo.sleep(Res.integer.time_wait);
        TextView path = (TextView) solo.getView("tv_save_path");
        assertTrue("下载路径与升级前不一致", path.getText().toString().equals(defaultDownloadPath));
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_change_activity);
        // 验证:内容可以成功下载,下载路径与升级前保持一致
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.clickOnMenuItem("action_menu_item_download");
        assertTrue("apk内容未能成功下载", getDownapkView() != null);
    }

    private View getDownapkView() {
        ArrayList<String> strings = getDownloadList();
        assertTrue("下载列表中不存在记录", strings != null);
        View downapk = null;
        for (int i = 0; i < strings.size(); i++) {
            Pattern namePattern = Pattern.compile("down.*?\\.apk");
            Matcher matcher = namePattern.matcher(strings.get(i));
            if (matcher.find()) {
                downapk = views.get(i);
                break;
            }
        }
        return downapk;
    }

    private ArrayList<String> getDownloadList() {
        ArrayList<String> strings = new ArrayList<String>();
        views = caseUtil.getViews(null, "download_title");
        for (View view : views) {
            strings.add(((TextView) view).getText().toString());
        }
        return strings;
    }
}
