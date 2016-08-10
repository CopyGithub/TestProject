package com.dolphin.testcase.addto;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 
 * 脚本编号：DOLINT-1940
 * <p>
 * 脚本描述：验证添加Bookmark弹框修改内容后Add
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-1940")
@TestClass("验证添加Bookmark弹框修改内容后Add")
public class AddToBookmarkAndEditTest extends BaseTest {
    private String baiduURL = Res.string.url_two;
    private String editName = "Test";
    private String folderName = "A";

    public void testAddToBookmarkAndEdit() {
        uiUtil.skipWelcome();
        init();

        // 添加到Bookmark,点击More
        addToBookmarkMore();

        // 验证：弹框中新增Folder和Assign gesture选项
        assertTrue(
                "Folder and Assign gesture are not show..",
                solo.searchText(caseUtil.getTextByRId("default_folder_name"))
                        && solo.searchText(caseUtil.getTextByRId("assign_a_gesture")));

        editBookmark();

        // 验证：Name输入框中显示Test，Folder下拉框中显示A，Assign gesture处显示手势图案α
        assertTrue("The edited info is not right", isShowRight());

        // 点击Add
        caseUtil.clickOnView("id/add");

        // 验证：Add to弹框消失，Toast提示Saved to bookmarks.
        assertTrue("Toast 'Saved to bookmarks' didn't appear.",
                solo.waitForText(caseUtil.getTextByRId("bookmark_saved")));

        // 验证：书签列表的文件夹A中，列表中是否显示书签Test
        assertTrue("Failed to find a shortcut to the home screen",
                isInBookmark(editName, folderName));

        // 验证：点击书签Test，打开百度主页
        assertTrue("Failed to visit the new bookmark",
                uiUtil.isIntoNewBookmark(editName, baiduURL, false));

    }

    private void init() {
        // 预置条件：书签列表中有文件夹A
        String[] folders = { folderName };
        uiUtil.addFoldersFromRoot(folders, false);
    }

    private void addToBookmarkMore() {
        // 打开网页
        uiUtil.visitUrl(baiduURL);
        solo.sleep(Res.integer.time_wait);
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        solo.sleep(Res.integer.time_wait);

        // 点击海豚图标→点击Add to→点击More
        uiUtil.clickOnMenuItem("add_to");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/more");
        solo.sleep(Res.integer.time_wait);

    }

    private void editBookmark() {
        // 修改Name为Test→修改书签目录为文件夹A→添加手势图案为α
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, editName);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/folder");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(folderName);
        solo.sleep(Res.integer.time_wait);

    }

    private boolean isShowRight() {
        View nameView = solo.getView("id/title");
        View folderView = solo.getView("id/folder");
        return ((TextView) nameView).getText().toString().equals(editName)
                && ((Button) folderView).getText().toString().equals(folderName);
        // TODO 手势图案，TODO
        // View gestureView = solo.getView("id/gesture_image");
    }

    private boolean isInBookmark(String webName, String folderName) {
        // 进入书签栏
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        if (solo.getView("id/back") != null) {
            solo.clickOnView(solo.getView("id/back"));
        }
        solo.sleep(Res.integer.time_wait);
        // 寻找文件夹A
        View textview = null;
        View vg = solo.getView("id/list");
        for (int i = 0; i < ((ViewGroup) vg).getChildCount(); i++) {
            if (caseUtil.getViewByIndex(vg, new int[] { i, 3 }).getVisibility() != View.GONE
                    && ((TextView) caseUtil.getViewByIndex(vg, new int[] { i, 2, 0 })).getText()
                            .toString().equals(folderName)) {
                textview = caseUtil.getViewByIndex(vg, new int[] { i, 2, 0 });
                break;
            }
        }
        solo.clickOnView(textview);
        solo.sleep(Res.integer.time_wait);
        return solo.searchText(webName);
    }

}