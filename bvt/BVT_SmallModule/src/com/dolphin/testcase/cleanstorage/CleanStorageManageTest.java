package com.dolphin.testcase.cleanstorage;

import android.widget.Button;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1706
 * <p>
 * 脚本描述：清理工具入口
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1706")
@TestClass("清理工具入口")
public class CleanStorageManageTest extends BaseTest {
    private String url = "dolphin://cleanup";

    public void testA() {
        uiUtil.skipWelcome();
        // 进行内存环境准备,使存储空间<1M
        utils.cramSD(1, utils.externalStorageDirectory + "/newFiles");
        // 验证：进入"STORAGE MANAGEMENT"界面
        checkEnterManage(2);
    }

    public void testCleanStorageManage() {
        // 验证：弹出内存清理提示框
        assertTrue("The dialog does not pop up...", solo.waitForDialogToOpen());
        // 验证：进入"STORAGE MANAGEMENT"界面
        checkEnterManage(1);
        // // 验证：进入"STORAGE MANAGEMENT"界面
        // checkEnterManage(2);

    }

    public void testToDefult() {
        // 内存环境准备：删除文件夹"newFiles"
        javaUtils.deleteFileOrDir(utils.externalStorageDirectory + "/newFiles");
    }

    private void checkEnterManage(int i) {
        switch (i) {
        case 1:
            // 点击ok键
            Button okBtn = (Button) solo.getView("button2");
            solo.clickOnView(okBtn);
            solo.sleep(Res.integer.time_wait);
            break;
        case 2:
            // 返回Dolphin主屏,在地址栏输入：dolphin://cleanup 后,点击go
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
            uiUtil.visitUrl(url);
            solo.sleep(Res.integer.time_wait);
            break;
        }
        assertTrue("Not enter 'STORAGE MANAGEMENT'...",
                solo.searchText(caseUtil.getTextByRId("cleanstore_title")));
    }
}