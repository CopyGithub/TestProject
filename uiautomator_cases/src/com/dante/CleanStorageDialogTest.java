package com.dante;

import java.io.IOException;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class CleanStorageDialogTest extends UiAutomatorTestCase {
    public void test() {
        // 进入File Manager查看cache,webview内的cache全部被清空（case本身错误）

        // 内存环境准备：删除newFiles文件夹
        deleteNewFiles();
    }

    private void deleteNewFiles() {
        // 内存环境：删除newFiles文件夹
        String filePath = "mnt/sdcard/newFiles";
        try {
            Process process = Runtime.getRuntime().exec("rm -rf " + filePath);
            process.waitFor();
        } catch (IOException e) {
            assertTrue("Failed to delete newFiles.", false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
