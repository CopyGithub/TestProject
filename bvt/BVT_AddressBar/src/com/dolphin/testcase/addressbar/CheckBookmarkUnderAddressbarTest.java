package com.dolphin.testcase.addressbar;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;
import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1604
 * <p>
 * 脚本描述：地址栏下BOOKMARKS列表样式正常
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1604")
@TestClass("地址栏下BOOKMARKS列表样式正常")
public class CheckBookmarkUnderAddressbarTest extends BaseTest {
    String[] urlStrings = new String[] { Res.string.url_one, Res.string.url_two,
            Res.string.url_three };

    public void testCheckBookmarkUnderAddressbar() {
        uiUtil.skipWelcome();
        init();
        /*
         * 验证:列表显示正常: ·格式: 网页/文件夹图标 书签/文件夹名 ·按添加后先顺序从上往下排列 ·界面底部显示"Manage"区域(居中)
         * （样式和数据与左侧边栏Bookmarks列表一致）
         */
        checkBookmarkList();
        // 验证:正常显示所有数据
    }

    private void checkBookmarkList() {
        uiUtil.switchToTargetTabByClicking(2);
        solo.sleep(Res.integer.time_wait);

        // ·按添加后先顺序从上往下排列+
        View view2 = caseUtil.getViewByText("name2", -1, true, true, true);
        View view1 = caseUtil.getViewByText("name1", -1, true, true, true);
        View view0 = caseUtil.getViewByText("name0", -1, true, true, true);
        ArrayList<View> views = new ArrayList<View>();
        views.add(view2);
        views.add(view1);
        views.add(view0);
        utils.ubietyOfViews(views, 1, false, false, false);

        // ·格式: 网页/文件夹图标 书签/文件夹名
        // 验证：上下滑动列表,正常显示所有数据
        View bookmark = caseUtil.getViewByClassName("SearchTabContainerBookmark", 0, false);
        ArrayList<String> addressBookmarks = getAddressBarBookmark(bookmark);

        // ·界面底部显示"Manage"区域(居中)
        checkManageUI(bookmark);

        // 验证：样式(未验证)和数据与左侧边栏Bookmarks列表一致
        ArrayList<String> leftBarBookmarks = getLeftBarBookmark();
        assertTrue("数据与左侧边栏Bookmarks列表不一致", addressBookmarks.equals(leftBarBookmarks));
    }

    private ArrayList<String> getAddressBarBookmark(View bookmark) {
        ArrayList<String> bookmarks = new ArrayList<String>();
        // 小屏机需要再做修改由于其机型自身计算拖动值时有误
        caseUtil.slideDireciton(bookmark, false, 1 / 3f, 1f);
        do {
            View list = caseUtil.getViewByIndex(bookmark, new int[] { 0, 1 });
            ArrayList<View> views = utils.getChildren(list, false);
            for (int i = 0; i < views.size(); i++) {
                View icon = caseUtil.getViewByIndex(views.get(i), new int[] { 0, 1 });
                View name = caseUtil.getViewByIndex(views.get(i), new int[] { 0, 2, 0 });
                assertTrue("列表第" + (i + 1) + "个显示错误。未满足格式：网页/文件夹图标没有在书签/文件夹名左边",
                        utils.ubietyOfView(icon, name, true, false, false) != -1);
                String text = ((TextView) name).getText().toString();
                if (!bookmarks.contains(text)) {
                    bookmarks.add(text);
                }
            }
        } while (caseUtil.isScroll(bookmark, 1));
        return bookmarks;
    }

    // 左边bookmark列表
    private ArrayList<String> getLeftBarBookmark() {
        enterLeftBookmark();
        ArrayList<String> bookmarks = new ArrayList<String>();
        View list = solo.getView("list");
        // 小屏机需要再做修改由于其机型自身计算拖动值时有误
        do {
            ArrayList<View> views = utils.getChildren(list, false);
            for (int i = 0; i < views.size(); i++) {
                View textView = caseUtil.getViewByIndex(views.get(i), new int[] { 2, 0 });
                String text = ((TextView) textView).getText().toString();
                if (text.equals(caseUtil.getTextByRId("most_visit_folder_name", -1))) {
                    continue;
                }
                if (text.equals(caseUtil.getTextByRId("history", -1))) {
                    continue;
                }
                if (!bookmarks.contains(text)) {
                    bookmarks.add(text);
                }
            }
            list = solo.getView("list");
        } while (caseUtil.isScroll(list, 1));
        return bookmarks;
    }

    private void enterLeftBookmark() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterBookmark();
    }

    private void checkManageUI(View parent) {
        TextView manage = (TextView) caseUtil.getViewByIndex(parent, new int[] { 0, 3, 0, 1 });
        int[] manageLoc = new int[2];
        int[] size = new int[2];
        manage.getLocationOnScreen(manageLoc);
        size = caseUtil.getDisplaySize(false);
        String showText = manage.getText().toString();
        String originText = caseUtil.getTextByRId("search_bottom_edit_manager", -1);
        assertTrue("界面底部未显示Manage", manage.isShown() && showText.equals(originText));
        assertTrue("Manage区域不居中", size[0] - manage.getWidth() == manageLoc[0] * 2);
    }

    private void init() {
        // 预置条件
        // 1.依次添加以下网页为书签:三个网页
        addBookmarks(urlStrings);
        // 2.当前为搜索编辑界面(主页点击地址栏)
        // 小屏机执行到uiUtils.class58行出错，找不到id为bottom_container的view
        solo.sleep(Res.integer.time_change_activity);
        if (caseUtil.getDisplayRange() == 0) {
            solo.clickOnView(caseUtil.getViewByIndex("address_home_button", 0));
        } else {
            solo.clickOnView(uiUtil.getMenubarItem(4, 0));
        }
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("title_bg");
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }

    private void addBookmarks(String[] urls) {
        for (int i = 0; i < urls.length; i++) {
            uiUtil.addBookmark(urls[i], "name" + i);
            solo.sleep(Res.integer.time_wait);
        }
    }
}
