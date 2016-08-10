package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;
import android.widget.EditText;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-813
 * <p>
 * 脚本描述: 验证music可以下载
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-813")
@TestClass("验证music可以下载")
public class CheckMusicDownloadTest extends BaseTest {

    public void testCheckMusicDownload() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);
        uiUtil.closeWhistleWhenOpen();

        View parent = solo.getView("card_page_root");
        ArrayList<View> musics = utils.getChildren(
                caseUtil.getViewByIndex(parent, new int[] { 2, 1, 0 }), false);
        // 验证：依次下载5首歌曲
        for (int i = 0; i < musics.size(); i++) {
            // 验证：弹出DOWNLOADS下载框
            View download = caseUtil.getViewByIndex(musics.get(i), new int[] { 2 });
            solo.clickOnView(download);
            assertTrue("第" + (i + 1) + "个歌曲未弹出下载框", solo.waitForDialogToOpen());

            // 验证：点击Cancel，弹窗消失，不会下载music
            solo.clickOnView(solo.getView("button1"));
            assertTrue("第" + (i + 1) + "个歌曲弹窗未消失", solo.waitForDialogToClose());

            // 验证：点击Download，开始下载music，直到下载完成
            solo.clickOnView(download);
            solo.sleep(Res.integer.time_wait);
            String fileName = ((EditText) solo.getView("input")).getText().toString();
            solo.clickOnView(solo.getView("button2"));
            solo.sleep(Res.integer.time_wait);
            assertTrue("第" + (i + 1) + "个歌曲下载超时,没有成功下载",
                    uiUtil.waitForDownloadTaskComplete(fileName, 2 * 60 * 1000, 1000));
        }
    }
}