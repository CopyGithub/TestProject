package com.dolphin.testcase.cleanstorage;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1704
 * <p>
 * 脚本描述：提醒方式UI及自动清理逻辑-存储<1M
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1704")
@TestClass("提醒方式UI及自动清理逻辑-存储<1M")
public class CleanStorageDialogTest extends BaseTest {
    public void testACleanStorageDialog() {
        uiUtil.skipWelcome();
        // 进行内存环境准备,使存储空间<1M
        utils.cramSD(1, utils.externalStorageDirectory + "/newFiles");

        // @UiAutomatorAfter("com.dante.CleanStorageDialogTest")删去
        // case本身有问题，UiAutomator暂时无法进行验证
    }

    public void testStorageDialog() {
        // 验证：弹出内存清理提示框
        assertTrue("The dialog does not pop up...", solo.waitForDialogToOpen());
        /**
         * ·标题："Not enough storage" <br>
         * ·提示语：Do you want to clean up your storage now? <br>
         * ·底部为按钮："Skip"," OK" <br>
         */
        ArrayList<View> views = new ArrayList<View>();
        TextView title = (TextView) solo.getView("alertTitle");
        TextView tips = (TextView) solo.getView("msg");
        Button skipBtn = (Button) solo.getView("button1");
        Button okBtn = (Button) solo.getView("button2");
        assertTrue(
                "Title is wrong ...",
                title.getText().toString()
                        .equals(caseUtil.getTextByRId("cleanstore_memory_unavailable_title")));
        assertTrue(
                "Tips is wrong ...",
                tips.getText().toString()
                        .equals(caseUtil.getTextByRId("cleanstore_memory_unavailable_message")));
        assertTrue("Button 'Skip' is wrong ...",
                skipBtn.getText().toString().equals(caseUtil.getTextByRId("skip")));
        assertTrue("Button 'OK' is wrong ...",
                okBtn.getText().toString().equals(caseUtil.getTextByRId("btn_ok")));

        views.add(title);
        views.add(tips);
        views.add(skipBtn);
        utils.ubietyOfViews(views, 1, false, false, false);
        assertTrue("Button左右顺序不正确", utils.ubietyOfView(skipBtn, okBtn, true, false, false) != -1);

        // (TODO)
        // 验证：点击skip,进入File Manager查看cache,webview内的cache全部被清空
        // 该case有问题，无法进入海豚，则查看cache的操作无法进行

    }

    public void testToDefult() {
        // 内存环境准备：删除文件夹"newFiles"
        javaUtils.deleteFileOrDir(utils.externalStorageDirectory + "/newFiles");
    }
}