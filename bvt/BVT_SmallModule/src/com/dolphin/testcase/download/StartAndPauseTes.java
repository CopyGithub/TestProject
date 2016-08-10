package com.dolphin.testcase.download;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.android.common.SqliteUtil;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-129
 * <p>
 * 脚本描述：下载与暂停
 * 
 * @author jwliu
 * 
 */
@TestNumber(value = "DOLINT-129")
@TestClass("下载与暂停")
public class StartAndPauseTes extends BaseTest {
    private TextView downloading;
    private String status;
    private boolean flag_pause;
    private boolean flag_start;

    public void testStartAndPauseTest() {
        uiUtil.skipWelcome();
        // 下载资源A,下载完成前查看下载管理器
        downloadAndEnterDownloadScreen();
        /**
         * 验证： Downloading分类中显示正在下载任务项A，由图标+文件名+下载进度条+下载进度+下载速度+按钮组成：
         * <p>
         * ·TODO 文件名：若超长则中间部分用...省略
         * <p>
         * ·TODO 进度条：为确定进度条（进度条是实体的），有色部分为绿色，表示当前下载进度；
         * <p>
         * ·下载进度：显示"已下载大小/总文件大小"，
         * <p>
         * TODO ·按钮：为暂停按钮，用‖的icon示意
         */
        assertTrue("The download process does not matche XXMB/XXMB(download sizes/total sizes",
                isMatchDownloadProcess());
        // 点击暂停按钮
        clickOnIcon(2);
        /**
         * 验证： 点击后下载任务暂停，进度条有色部分变为黄色，下载速度提示变成文字“Paused”，按钮变为继续下载按钮，用↓的icon示意
         */
        assertTrue("The download speed does not change to text 'Paused", flag_pause);
        // 点击下载任务项（非Icon区域）
        clickOnNoneIcon(true);
        /**
         * 验证：任务继续下载
         */
        assertTrue("The download task does not start to download", flag_start);
        // 再次点击下载任务项（非Icon区域）
        clickOnNoneIcon(false);
        /**
         * 验证： 任务暂停
         */
        assertTrue("The download task does not pause", flag_pause);
        // 点击继续下载按钮
        clickOnIcon(1);
        /**
         * 验证：任务继续下载
         */
        assertTrue("The download task does not start to download", flag_start);
    }

    private boolean isMatchDownloadProcess() {
        String processText = ((TextView) solo.getView("id/progress_text")).getText().toString();
        // 简单的正则表达式，匹配 例如32.1MB/50MB这样的格式
        String pattern = "\\d*\\.?\\d*" + "MB/" + "\\d*\\.?\\d*" + "MB";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(processText);
        return matcher.find();
    }

    private void clickOnNoneIcon(boolean isStart) {
        solo.clickOnView(caseUtil.getViewByIndex(solo.getView("id/time_list"),
                new int[] { 0, 1, 2 }));
        solo.sleep(Res.integer.time_wait * 2);
        downloading = (TextView) caseUtil.getViewByIndex(solo.getView("id/time_list"), new int[] {
                0, 1, 2, 2, 1 });
        status = downloading.getText().toString();
        if (isStart) {
            if (status.substring(status.length() - 2, status.length()).equals("/s")) {
                flag_start = true;
            } else {
                flag_start = false;
            }
        } else {
            if (status.equals(caseUtil.getTextByRId("download_paused"))) {
                flag_pause = true;
            } else {
                flag_pause = false;
            }
        }
    }

    /**
     * 点击按钮
     * 
     * @param choice
     *            1代表是开始，2代表是暂停
     */
    private void clickOnIcon(int choice) {
        // 点击Icon按钮
        View view = solo.getView("time_list");
        solo.clickOnView(caseUtil.getViewByIndex(view, new int[] { 0, 1, 1 }));
        solo.sleep(Res.integer.time_wait * 2);
        downloading = (TextView) caseUtil.getViewByIndex(view, new int[] { 0, 1, 2, 2, 1 });
        status = downloading.getText().toString();
        if (choice == 1) {
            if (status.substring(status.length() - 2, status.length()).equals("/s")) {
                flag_start = true;
            } else {
                flag_start = false;
            }
        }
        if (choice == 2) {
            if (status.equals(caseUtil.getTextByRId("download_paused"))) {
                flag_pause = true;
            } else {
                flag_pause = false;
            }
        }
    }

    private void downloadAndEnterDownloadScreen() {
        // 删除所有下载数
        new SqliteUtil(instrumentation.getTargetContext())
                .deleteAllRow("downloads.db", "downloads");
        solo.sleep(Res.integer.time_wait);
        javaUtils.deleteFileOrDir(utils.externalStorageDirectory + "/download");// 清除sdcard下的内容
        solo.sleep(Res.integer.time_wait);
        // 下载
        uiUtil.createOneDownloadTask(Res.string.url_download_long_name);
        solo.sleep(Res.integer.time_open_url);
        uiUtil.clickOnMenuItem("action_menu_item_download");
        uiUtil.waitForInterfaceByTitle("download_tab_title", 10 * 1000);
        solo.sleep(Res.integer.time_wait);
    }
}