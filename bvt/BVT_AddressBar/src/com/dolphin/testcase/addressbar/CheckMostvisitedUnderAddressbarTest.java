package com.dolphin.testcase.addressbar;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-1612
 * <p>
 * 脚本描述：地址栏下MOST VISITED列表样式正常
 * 
 * @author sjguo
 * 
 */

@TestNumber(value = "DOLINT-1612")
@TestClass("地址栏下MOST VISITED列表样式正常")
public class CheckMostvisitedUnderAddressbarTest extends BaseTest {

    public void testCheckMostvisitedUnderAddressbar() {
        uiUtil.skipWelcome();
        init();
        // 点击MOST VISITED tab -> 观察tab下列表,列表显示正常
        if(solo.getView("id/empty_view_test1")==null)
        {
            checkMostvisitedList();
        }else {
            //MOST VISITED为空
            checkEmptyUI();
        }
    }

    private void checkMostvisitedList() {
        uiUtil.switchToTargetTabByClicking(3);
        solo.sleep(Res.integer.time_wait);
        // 验证：列表显示正常
        // 验证格式:网页图标 网页标题 "↖"图标 网页url(标题和url未完整显示部分以...代替(TODO))
        View mostVisited = caseUtil.getViewByClassName("SearchTabContainerMostVisited", 0, false);
        View list = caseUtil.getViewByIndex(mostVisited, new int[] { 0, 1 });
        ArrayList<String> views = new ArrayList<String>();
        for (int i = 0; i < ((ViewGroup) list).getChildCount(); i++) {
            View icon = caseUtil.getViewByIndex(list, new int[] { i, 0, 1 });
            View go = caseUtil.getViewByIndex(list, new int[] { i, 0, 0 });
            View name = caseUtil.getViewByIndex(list, new int[] { i, 0, 2, 0 });
            View url = caseUtil.getViewByIndex(list, new int[] { i, 0, 2, 1 });
            int[] iconLoc = new int[2];
            int[] goLoc = new int[2];
            int[] nameLoc = new int[2];
            int[] urlLoc = new int[2];
            icon.getLocationOnScreen(iconLoc);
            go.getLocationOnScreen(goLoc);
            name.getLocationOnScreen(nameLoc);
            url.getLocationOnScreen(urlLoc);
            assertTrue("列表第" + (i + 1) + "个显示错误。未满足格式：网页图标 网页标题 ↖图标 网页url", iconLoc[0] < nameLoc[0]
                    && nameLoc[0] < goLoc[0] && iconLoc[0] < urlLoc[0] && urlLoc[0] < goLoc[0]
                    && nameLoc[1] < urlLoc[1]);
            views.add(((TextView) name).getText().toString());
        }

        // 界面底部显示"Clear most visited data"区域(居中)
        checkManageUI(mostVisited);

        // 样式(未验证)和数据与左侧边栏Most Visited列表一致
        // 验证：上下滑动列表,正常显示所有数据
        // 滑动：继续添加
        while (solo.scrollDown()) {
            int size = solo.getViews().size();
            caseUtil.slideDireciton(solo.getView("pager"), false, 9 / 10f, 1f);
            solo.sleep(Res.integer.time_wait);
            assertTrue("上下滑动列表,不能正常显示所有数据", solo.getViews().size() == size);

            mostVisited = caseUtil.getViewByClassName("SearchTabContainerMostVisited", 0, false);
            list = caseUtil.getViewByIndex(mostVisited, new int[] { 0, 1 });
            for (int i = 0; i < ((ViewGroup) list).getChildCount(); i++) {
                View name = caseUtil.getViewByIndex(list, new int[] { i, 0, 2, 0 });
                String string = ((TextView) name).getText().toString();
                if (!views.contains(string)) {
                    views.add(string);
                }
            }
        }
        // 进入左边Most Visited
        enterLeftMostVisited();
        View mostvisitedInLeft = solo.getView("id/list");
        ArrayList<String> viewsInLeft = new ArrayList<String>();
        for (int i = 0; i < ((ViewGroup) list).getChildCount(); i++) {
            View nameInLeft = caseUtil.getViewByIndex(mostvisitedInLeft, new int[] { i, 2, 0 });
            viewsInLeft.add(((TextView) nameInLeft).getText().toString());
        }
        // 滑动：继续添加
        while (solo.scrollDown()) {
            caseUtil.slideDireciton(solo.getView("list"), false, 9 / 10f, 1f);
            solo.sleep(Res.integer.time_wait);
            mostvisitedInLeft = solo.getView("id/list");
            for (int i = 0; i < ((ViewGroup) list).getChildCount(); i++) {
                View nameInLeft = caseUtil.getViewByIndex(mostvisitedInLeft, new int[] { i, 2, 0 });
                String stringInLeft = ((TextView) nameInLeft).getText().toString();
                if (!viewsInLeft.contains(stringInLeft)) {
                    viewsInLeft.add(stringInLeft);
                }
            }
        }
        assertTrue("数据与左侧边栏Most Visited列表不一致", views.equals(viewsInLeft));

    }

    private void enterLeftMostVisited() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        // 判断是否是左侧边栏的初始页面，否则回到初始页面
        if (solo.getView("id/back") != null) {
            solo.clickOnView(solo.getView("id/back"));
        }
        solo.sleep(Res.integer.time_wait);
        // 进入Most Visited
        solo.clickOnText(caseUtil.getTextByRId("most_visit_folder_name", -1));
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入Most Visited列表",
                solo.searchText(caseUtil.getTextByRId("search_bottom_clear_mostvisited", -1)));
    }

    private void checkManageUI(View parent) {
        View empty = caseUtil.getViewByIndex(parent, new int[] { 0, 2 });
        if (empty.getVisibility() == View.VISIBLE) {
            assertTrue("Most Visited记录为空", false);
        } else {
            TextView manage = (TextView) caseUtil.getViewByIndex(parent, new int[] { 0, 3, 0, 1 });
            int[] manageLoc = new int[2];
            int[] size = new int[2];
            manage.getLocationOnScreen(manageLoc);
            size = caseUtil.getDisplaySize(false);
            String string = manage.getText().toString();
            assertTrue("界面底部未显示Clear most visited data", manage.getVisibility() == View.VISIBLE
                    && string.equals(caseUtil.getTextByRId("search_bottom_clear_mostvisited", -1)));
            assertTrue("Clear most visited data区域不居中",
                    size[0] - manage.getWidth() == manageLoc[0] * 2);
        }

    }
    private void checkEmptyUI()
    {
        uiUtil.switchToTargetTabByClicking(3);
        solo.sleep(Res.integer.time_wait);
        TextView empty= (TextView)solo.getView("id/empty_view_text1");
        String m=empty.getText().toString();
        assertTrue("Empty未显示"+m,
                empty.getText().toString().equals("Empty"));
        assertTrue("Your most visited sites will be here未显示",
                solo.searchText(caseUtil.getTextByRId("search_empty_mostVisited", -1)));
        //进入左侧边栏
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        if (solo.getView("id/back") != null) {
            solo.clickOnView(solo.getView("id/back"));
        }
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("most_visit_folder_name", -1));
        solo.sleep(Res.integer.time_wait);
        View parent = solo.getView("id/body");
        TextView emptyTips=(TextView) caseUtil.getViewByIndex(parent, new int[] { 3, 0 });
        m= emptyTips.getText().toString();
        assertTrue("Most visited list is empty未显示"+m,
                emptyTips.getText().toString().equals(caseUtil.getTextByRId("empty_most_visited_list", -1)));
    }
    private void init() {
        // 1.已存在MOST VISITED记录(TODO)

        // 2.当前为搜索编辑界面(主页点击地址栏)
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
    }
}