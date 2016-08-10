package com.dolphin.testcase.bookmark;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.ImageUtil;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1500
 * <p>
 * 脚本描述：验证左侧边栏与地址栏Bookmark Tab界面的一致性
 * 
 * @author sylu
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-1500")
@TestClass("验证左侧边栏与地址栏Bookmark Tab界面的一致性")
public class CheckBookmarkSameTest extends BaseTest {
    private String[] folders = { "A" };
    private String[] urls = { "http://autotest.baina.com/test/downloadtest/one.html" };
    private String[] urlnames = { "autotest" };
    private ImageUtil imageUtil;

    public void testCheckBookmarkSameTest() {
        uiUtil.skipWelcome();
        imageUtil = new ImageUtil(solo);
        // 一个预制条件，有文件夹A和书签
        uiUtil.addFoldersFromRoot(folders, true);
        uiUtil.addBookmark(urls[0], urlnames[0], caseUtil.getTextByRId("bookmarks"));
        // 进入的根目录编辑界面样式跟左侧边栏进入根目录管理界面一致
        checkBookmarkIsTheSame();
        // 进行全选，删除，新建文件夹，发送到，移动书签位置等操作都能正常Work
        checkBookmarkWork();
        // 验证左侧栏对应的改变
        checkTheSameChange();
    }

    private void checkTheSameChange() {
        // 验证验证左侧栏对应的改变
        solo.sleep(Res.integer.time_wait);
        // 截图
        solo.takeScreenshot("1");
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        // 滑动左侧边栏
        uiUtil.enterSideBar(true);
        // 点击edit按钮
        View editView = solo.getView("more");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(editView);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("bottom_text"));
        solo.sleep(Res.integer.time_wait);
        solo.takeScreenshot("2");
        solo.sleep(Res.integer.time_wait);
        String filePath1 = utils.externalStorageDirectory + "/Robotium-Screenshots/1.jpg";
        Bitmap bitmap1 = BitmapFactory.decodeFile(filePath1);
        String filePath2 = utils.externalStorageDirectory + "/Robotium-Screenshots/2.jpg";
        Bitmap bitmap2 = BitmapFactory.decodeFile(filePath2);
        assertTrue("cannot change ", imageUtil.compareBitmap(bitmap1, bitmap2, false));
    }

    private void checkBookmarkWork() {
        // 进行全选，删除，新建文件夹，移动书签位置等操作都能正常Work TODO 发送到
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("btn_create_folder"));
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, "B");
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_wait);
        boolean flag1 = solo.searchText("B");
        // 验证新增加的文件夹B
        assertTrue("cannot creat a new folder ", flag1);
        // 验证可以移动书签
        View buttonView1 = caseUtil.getViewByIndex(solo.getView("list"), new int[] { 0, 4, 0 });
        View buttonView2 = caseUtil.getViewByIndex(solo.getView("list"), new int[] { 1, 4, 0 });
        // 移动书签的操作
        solo.sleep(Res.integer.time_wait);
        caseUtil.dragViewToView(buttonView1, buttonView2);
        solo.sleep(Res.integer.time_wait);
        // 验证书签成功移动，如果成功位于第一个的文件夹的名字从B变成A
        TextView textView = (TextView) caseUtil.getViewByIndex(solo.getView("list"), new int[] { 0,
                2, 0 });
        boolean flag2 = false;
        String aString = "A";
        if (textView.getText().toString().equals(aString)) {
            flag2 = true;
        }
        assertTrue("cannot drag a folder ", flag2);
        // 验证可以把所以的书签和文件夹全选中
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("btn_select_or_deselect_all"));
        solo.sleep(Res.integer.time_wait);
        View checkView1 = caseUtil.getViewByIndex(solo.getView("list"), new int[] { 0, 0, 0 });
        View checkView2 = caseUtil.getViewByIndex(solo.getView("list"), new int[] { 1, 0, 0 });
        View checkView3 = caseUtil.getViewByIndex(solo.getView("list"), new int[] { 2, 0, 0 });
        CheckedTextView box1 = (CheckedTextView) checkView1;
        CheckedTextView box2 = (CheckedTextView) checkView2;
        CheckedTextView box3 = (CheckedTextView) checkView3;
        boolean flag3 = false;
        if (box1.isChecked() == true && box2.isChecked() == true && box3.isChecked() == true) {
            flag3 = true;
        }
        assertTrue("cannot check all the box ", flag3);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(checkView3);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(checkView2);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("btn_remove_selected"));
        solo.sleep(Res.integer.time_wait);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("button2"));
        solo.sleep(Res.integer.time_wait);
        boolean flag4 = true;
        String string = "A";
        TextView textView4 = (TextView) caseUtil.getViewByIndex(solo.getView("list"), new int[] {
                0, 2, 0 });
        if (textView4.getText().toString().equals(string)) {
            flag4 = false;
        }
        assertTrue("cannot delete the choosen box ", flag4);
    }

    private void checkBookmarkIsTheSame() {
        // 判断这两种方式得到的界面是相同的
        uiUtil.enterSideBar(true);
        // 点击edit按钮
        View editView = solo.getView("more");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(editView);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText("Manage");
        solo.sleep(Res.integer.time_wait);
        // 截图
        solo.takeScreenshot("sidebar");
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("title_bg");
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.clickOnText("Manage");
        solo.sleep(Res.integer.time_wait);
        // 截图
        solo.takeScreenshot("addbar");
        solo.sleep(Res.integer.time_wait);
        String filePath1 = utils.externalStorageDirectory + "/Robotium-Screenshots/sidebar.jpg";
        Bitmap bitmap1 = BitmapFactory.decodeFile(filePath1);
        String filePath2 = utils.externalStorageDirectory + "/Robotium-Screenshots/addbar.jpg";
        Bitmap bitmap2 = BitmapFactory.decodeFile(filePath2);
        assertTrue("this two image is not the same ",
                imageUtil.compareBitmap(bitmap1, bitmap2, false));
    }

}
