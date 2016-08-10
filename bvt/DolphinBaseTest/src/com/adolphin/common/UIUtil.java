package com.adolphin.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.common.Utils;
import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.WebElement;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UIUtil {
    private Solo solo;
    private Utils utils;
    private CaseUtil caseUtil;
    private XmlUtil xmlUtil;
    private ImageUtil imageUtil;
    private static final String MARQUEE_TEXT_VIEW_ID = "htext";
    private static final String PROMOTION_VIEW_ID = "promotion_view";

    public UIUtil(Solo solo) {
        this.solo = solo;
        utils = new Utils(solo.getInstrumentation().getTargetContext());
        xmlUtil = new XmlUtil(solo);
        imageUtil = new ImageUtil(solo);
        caseUtil = new CaseUtil(solo);
    }

    /**
     * 启动浏览器,跳过开始的welcome界面
     * <p>
     * 支持int和express版本, 自动实现跳过welcome界面, 如果没有welcome,则直接跳过
     */
    public void skipWelcome() {
        solo.sleep(Res.integer.time_change_activity);
        long endTime = SystemClock.uptimeMillis() + 30 * 1000;
        // 小于50则还在welcome界面
        while (endTime > SystemClock.uptimeMillis()) {
            if (solo.getViews().size() > 50) {
                break;
            }
            if (caseUtil.searchViewById("to_start", true)) {
                caseUtil.clickOnView("to_start");
                solo.sleep(Res.integer.time_change_activity);
            }
            solo.sleep(1000);
        }
        // 用来禁用showGuide,避免GetActivity卡死,海豚代码未修改前零时方案
        xmlUtil.setShowGuideCount(2);
        if (endTime <= SystemClock.uptimeMillis()) {
            Assert.assertTrue("skip welcome超时失败", false);
        }
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 访问一个网站
     * 
     * @param url
     *            访问的url
     */
    public void visitUrl(String url) {
        caseUtil.startActivity("mobi.mgeek.TunnyBrowser.BrowserActivity",
                "android.intent.action.VIEW", url);
    }

    /**
     * 获取menu按钮
     * <p>
     * 如果是全屏状态,则返回为{@code null}
     * 
     * @param index
     *            0:返回,1:前进,2:停止,3:海豚按钮,4:主页键,5:当前打开页
     * @param indexSon
     *            默认为0,当index=5时,0:多层窗口图片,1:窗口数文本,2:小红点
     * @return
     */
    public View getMenubarItem(int index, int indexSon) {
        View view = null;
        if (index > 5 || index < 0) {
            Assert.assertTrue("menu下标越界", false);
        } else if (index != 5) {
            indexSon = 0;
        } else if (indexSon < 0 || indexSon > 2) {
            Assert.assertTrue("menu下标越界", false);
        }
        // 竖屏下中屏机获取menubar
        view = getMenuBar();
        if (view != null) {
            return caseUtil.getViewByIndex(view, new int[] { index, indexSon });
        }
        // 其它情况均在addressBar上
        view = caseUtil.getViewByIndex(getAddressBar(), new int[] { 0, 0 });
        switch (index) {
        case 0:
            view = caseUtil.getViewByIndex("address_go_back", indexSon);
            break;
        case 1:
            view = caseUtil.getViewByIndex("address_go_forward", indexSon);
            break;
        case 2:
            view = caseUtil.getViewByIndex("refresh_stop_btn", indexSon);
            break;
        case 3:
            view = caseUtil.getViewByIndex("address_open_menu", indexSon);
            break;
        case 4:
            view = caseUtil.getViewByIndex("address_home_button", indexSon);
            break;
        case 5:
            view = caseUtil.getViewByIndex("address_open_tablist", indexSon);
            break;
        }
        return view;
    }

    /**
     * 返回中屏机竖屏下的menubar
     * 
     * @return
     */
    private View getMenuBar() {
        View menuBar = null;
        // 只有中屏机竖屏下才存在Menubar,其它都是在AddressBar上
        if (caseUtil.getDisplayRange() == 1 && caseUtil.getDisplayRotation() % 2 == 0) {
            menuBar = caseUtil.getViewByIndex("bottom_container", 0);
            if (menuBar == null) {
                ViewGroup centerScreen = (ViewGroup) solo.getView("center_screen");
                for (int i = 0; i < centerScreen.getChildCount(); i++) {
                    View view = centerScreen.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        if (((ViewGroup) view).getChildCount() == 6) {
                            menuBar = view;
                            break;
                        } else {
                            for (int j = 0; j < ((ViewGroup) view).getChildCount(); j++) {
                                View secondChild = ((ViewGroup) view).getChildAt(j);
                                if (secondChild instanceof ViewGroup
                                        && ((ViewGroup) secondChild).getChildCount() == 6) {
                                    menuBar = secondChild;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return menuBar;
    }

    /**
     * 返回AddressBar, 这里返回的是两个中显示在屏幕上的, 或者是更靠近下面的一个
     * <p>
     * 这样会产生几种情况<br>
     * 1.两个都显示,这种一般是一个被推到界面以外了,另一个正常显示,正常显示的一般在下面,此时返回下面的一个 <br>
     * 2.两个都不显示, 可能是全屏状态, 没有验证,此时返回{@code null}<br>
     * 3.一个显示一个不显示,不知道场景,此时返回显示的哪一个
     * 
     * @return
     */
    private View getAddressBar() {
        View addressBar = null;
        ArrayList<View> views = caseUtil.getViews(null, "address_bar");
        if (views.size() == 1) {
            addressBar = views.get(0);
        } else if (views.size() > 1) {
            boolean isShown1 = views.get(0).isShown();
            boolean isShown2 = views.get(1).isShown();
            if (isShown1 && isShown2) {
                int[] location1 = new int[2];
                int[] location2 = new int[2];
                views.get(0).getLocationOnScreen(location1);
                views.get(1).getLocationOnScreen(location2);
                addressBar = location1[1] > location2[1] ? views.get(0) : views.get(1);
            } else if (isShown1) {
                addressBar = views.get(0);
            } else if (isShown2) {
                addressBar = views.get(1);
            }
            return addressBar;
        }
        return addressBar;
    }

    /**
     * 返回Tab栏
     * 
     * @return
     */
    public View getTabBar() {
        return caseUtil.getViewByClassName("AnimHighLightLinearLayout", 0, false);
    }

    /**
     * 通过点击"+"图标新建空白tab
     * 
     * @param num
     *            新建tab数目
     */
    public void addNewTab(int num) {
        if (num < 1) {
            num = 1;
        }
        while (num > 0) {
            if (caseUtil.getDisplayRange() == 0) {
                caseUtil.clickOnView("address_open_tablist");
                solo.sleep(Res.integer.time_wait);
                solo.clickOnText(caseUtil.getTextByRId("tablist_bottombar_create_new_tab"));
                solo.sleep(Res.integer.time_wait);
            } else {
                solo.clickOnView(getMenubarItem(5, 0));
                solo.sleep(Res.integer.time_wait);
                View parent = caseUtil.getViewByClassName("TabListBottomBar", 0, false);
                solo.clickOnView(caseUtil.getViewByIndex(parent, new int[] { 0 }));
                solo.sleep(Res.integer.time_wait);
            }
            num--;
        }
    }

    /**
     * 返回打开的Tab数量
     * 
     * @return
     */
    public int getTabNumber() {
        return ((ViewGroup) getTabBar()).getChildCount();
    }

    /**
     * 通过打开网页,点击menubar中的add to来添加书签
     * <p>
     * 通过toast来判断是否添加成功
     * 
     * @param url
     *            打开的网页
     * @param name
     *            书签名
     */
    public void addBookmark(String url, String name) {
        solo.sleep(500);
        visitUrl(url);
        solo.sleep(Res.integer.time_open_url);
        clickOnMenuItem("add_to");
        solo.sleep(Res.integer.time_wait);
        Assert.assertTrue("Failed to open 'AddToPage' activity.", solo.getCurrentActivity()
                .getClass().getSimpleName().equals("AddToPage"));
        caseUtil.clickOnView("id/item_bookmark");
        solo.sleep(Res.integer.time_wait);
        // 编辑Name
        solo.hideSoftKeyboard();
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, name);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/add");
        // 判断toast内容
        Assert.assertTrue("Toast 'Saved to bookmarks' is not found.",
                caseUtil.waitForText("bookmark_saved", 0, true, true, 10 * 1000, false));
    }

    /**
     * 进入书签栏，并且返回最开始书签栏界面
     */
    public void enterBookmark() {
        enterSideBar(true);
        String title = ((TextView) solo.getView("id/title")).getText().toString();
        Boolean isBookMark = title.equals(caseUtil.getTextByRId("tab_bookmarks"));
        if (isBookMark == false) {
            caseUtil.clickOnView("id/back_icon");
            solo.sleep(Res.integer.time_wait);
            title = ((TextView) solo.getView("id/title")).getText().toString();
            Boolean isBookMarkTwo = title.equals(caseUtil.getTextByRId("tab_bookmarks"));
            if (isBookMarkTwo == false) {
                caseUtil.clickOnView("id/back_icon");
                solo.sleep(Res.integer.time_wait);
            }
        }
    }

    /**
     * 得到listview里面某一项
     * 
     * @param listId
     *            list的id
     * @param itemName
     *            item的名字
     * @return
     */
    public View getListItemTitle(String listId, String itemName, int[] path) {
        ViewGroup viewGroup = (ViewGroup) solo.getView(listId);
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            TextView textView = (TextView) caseUtil.getViewByIndex(viewGroup.getChildAt(i), path);
            if (textView.getText().toString().equals(itemName)) {
                return textView;
            }
        }
        return null;
    }

    /**
     * 通过左侧边栏,点击add to bookmark 来添加书签
     * <p>
     * 通过toast来判断是否添加成功
     * 
     * @param ulr
     *            添加的网页网址
     * @param name
     *            添加的书签名字
     * @param folder
     *            添加到的文件夹名字
     */
    public void addBookmark(String ulr, String name, String folder) {
        enterBookmark();
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("save_to_bookmarks"));
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();
        solo.sleep(Res.integer.time_wait);
        solo.clearEditText(0);
        solo.enterText(0, name);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(1, ulr);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("more");
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("folder");
        solo.sleep(Res.integer.time_wait);
        View view = getListItemTitle("select_dialog_listview", folder, new int[] { 1, 0 });
        if (view == null) {
            Assert.assertTrue("The folder " + folder + " did not found.", false);
        } else {
            solo.clickOnView(view);
        }
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("OK");
        // 判断toast内容
        Assert.assertTrue("Toast 'Saved to bookmarks' is not found.",
                caseUtil.waitForText("bookmark_saved", 0, true, true, 10 * 1000, false));
    }

    /**
     * 添加书签栏的文件夹
     * <p>
     * 新建在同一层的多个文件夹，或者是这几个文件夹是属于父目录子目录关系每一层一个文件夹
     * 
     * @param path
     *            父目录路径 为null的时候表示从根目录开始建立
     * @param names
     *            子目录文件夹名字
     * @param isConcluded
     *            是不是有层级关系的文件夹
     */
    public void createFolder(String[] path, String[] names, boolean isConcluded) {
        enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("more");
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("search_bottom_edit_manager"));
        solo.sleep(Res.integer.time_wait);
        if (path != null) {
            for (String s : path) {
                View view = getListItemTitle("list", s, new int[] { 2, 0 });
                if (view == null) {
                    Assert.assertTrue("The folder " + s + " did not found.", false);
                } else {
                    solo.clickOnView(view);
                }
            }
        }
        for (int i = 0; i < names.length; ++i) {
            caseUtil.clickOnView("btn_create_folder");
            solo.sleep(Res.integer.time_wait);
            solo.hideSoftKeyboard();
            solo.sleep(Res.integer.time_wait);
            solo.enterText(0, names[i]);
            caseUtil.clickOnView("button2");
            solo.sleep(Res.integer.time_wait);
            if (isConcluded) {
                solo.clickOnView(caseUtil.getViewByIndex(solo.getView("list"),
                        new int[] { 0, 2, 0 }));
                solo.sleep(Res.integer.time_wait);
            }
        }
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 添加书签栏的文件夹,从子目录开始
     * <p>
     * 新建在同一层的多个文件夹，或者是这几个文件夹是属于父目录子目录关系每一层一个文件夹
     * 
     * @param path
     *            父目录路径
     * @param names
     *            子目录文件夹名字
     * @param isConcluded
     *            是不是有层级关系的文件夹
     */
    public void addFoldersFromFolder(String[] path, String[] names, boolean isConcluded) {
        createFolder(path, names, isConcluded);
    }

    /**
     * 添加书签栏的文件夹,从根目录开始
     * <p>
     * 新建在同一层的多个文件夹，或者是这几个文件夹是属于父目录子目录关系每一层一个文件夹
     * 
     * @param names
     *            文件夹名字
     * @param isConcluded
     *            是不是有层级关系的文件夹
     */
    public void addFoldersFromRoot(String[] names, boolean isConcluded) {
        createFolder(null, names, isConcluded);
    }

    /**
     * 通过打开网页,点击menubar中的add to来添加书签
     * <p>
     * 通过toast来判断是否添加成功, 这里如果已经存在, 则会报错
     * 
     * @param url
     *            打开的网页
     * @param name
     *            书签名
     */
    public void addSpeedDial(String url, String name) {
        visitUrl(url);
        solo.sleep(Res.integer.time_open_url);
        clickOnMenuItem("add_to");
        solo.sleep(Res.integer.time_wait);
        Assert.assertTrue("Failed to open 'AddToPage' activity.", solo.getCurrentActivity()
                .toString().contains("AddToPage"));
        caseUtil.clickOnView("id/item_speed_dial");
        solo.sleep(Res.integer.time_wait);
        // 编辑Name
        solo.hideSoftKeyboard();
        solo.clearEditText(0);
        solo.sleep(Res.integer.time_wait);
        solo.enterText(0, name);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("id/add");
        // 判断toast内容
        Assert.assertTrue("Toast 'Successfully added to speed dial' is not found.", caseUtil
                .waitForText("toast_add_shortcut_successfully", 0, true, true, 10 * 1000, false));
    }

    /**
     * 核对指定的urlName和地址栏中的内容是否有相互包含的情况
     * 
     * @param urlName
     * @return
     */
    public boolean checkURL(String urlName) {
        solo.sleep(Res.integer.time_wait);
        TextView textView = (TextView) caseUtil.getView(solo.getView("title_bg"), "title");
        String text = textView.getText().toString().trim();
        if (text.equals("") || text == null) {
            return false;
        }
        return urlName.contains(text) || text.contains(urlName);
    }

    /**
     * 根据SpeedDial的名字返回Home界面的SpeedDial
     * 
     * @param iconName
     * @return
     */
    public View getSpeedDialByName(String iconName) {
        ViewGroup viewGroup = (ViewGroup) caseUtil.getViewByClassName("CellLayout", 0, true);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (iconName.equals(getSpeedDialName(view))) {
                return view;
            }
        }
        return null;
    }

    /**
     * 得带能够删除的SpeedDial
     * 
     * @return {@code null} 表示没有找到合适的Speed Dial
     */
    public View getEnableRemoveSpeedDial() {
        ViewGroup viewGroup = (ViewGroup) caseUtil.getViewByClassName("CellLayout", 0, true);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if ((view instanceof TextView)) {
                continue;
            }
            String speedDial = getSpeedDialName(view);
            if (!speedDial.equals("Find Apps") && !speedDial.equals("")
                    && !speedDial.equals("Dolphin")) {
                return view;
            }
        }
        return null;
    }

    /**
     * 获取SpeedDial的名字
     * 
     * @param view
     *            为HomeShortcutIcon或HomeFolderIcon类
     * @return 返回SpeedDial的名字或文件夹的名字
     */
    public String getSpeedDialName(View view) {
        if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        } else {
            return ((TextView) caseUtil.getViewByIndex(view, new int[] { 1 })).getText().toString();
        }
    }

    /**
     * 前置：判断选择的搜索引擎是否正常搜索
     * 
     * @param engine
     *            选择的搜索引擎
     */
    public boolean searchByEngineCorrectly(String engine) {
        return checkURL(engine);
    }

    /**
     * 返回目前所在的tab, 1表示HISTORY, 2表示BOOKMARS, 3表示MOST VISITED
     */
    public int getCurrentTabName() {
        int tabIndex = 0;
        ViewGroup viewGroup = (ViewGroup) solo.getView("id/pager");
        if (viewGroup.getChildCount() == 3) {
            tabIndex = 2;
        } else {
            if (viewGroup.getChildAt(0).toString().contains("SearchTabContainerHistory")
                    || viewGroup.getChildAt(1).toString().contains("SearchTabContainerHistory")) {
                tabIndex = 1;
            }
            if (viewGroup.getChildAt(0).toString().contains("SearchTabContainerMostVisited")
                    || viewGroup.getChildAt(1).toString().contains("SearchTabContainerMostVisited")) {
                tabIndex = 3;
            }
        }
        return tabIndex;
    }

    /**
     * 前置：切换到目标tab
     * 
     * @param targetTab
     *            输入tab的index，HISTORY为1, BOOKMARKS为2, MOST VISITED为3
     */
    public void switchToTargetTabByClicking(int targetTab) {
        int currentTab = getCurrentTabName();
        // 获取tablan的坐标
        View tabs = caseUtil.getViewByClassName("SearchTabIndicator", 0, false);
        int[] xy = new int[2];
        tabs.getLocationOnScreen(xy);
        int tabsHeight = tabs.getHeight();
        int tabsWidth = tabs.getWidth();
        if (currentTab == targetTab) {
            return;
        }
        if (currentTab == 2 && targetTab == 1) {
            solo.clickOnScreen(tabsWidth / 8, xy[1] + tabsHeight / 2);
        }
        if (currentTab == 2 && targetTab == 3) {
            solo.clickOnScreen(tabsWidth * 7 / 8, xy[1] + tabsHeight / 2);
        }
        if (currentTab == 1 && targetTab == 2) {
            solo.clickOnScreen(tabsWidth * 7 / 8, xy[1] + tabsHeight / 2);
        }
        if (currentTab == 1 && targetTab == 3) {
            solo.clickOnScreen(tabsWidth * 7 / 8, xy[1] + tabsHeight / 2);
            solo.sleep(Res.integer.time_wait);
            solo.clickOnScreen(tabsWidth * 7 / 8, xy[1] + tabsHeight / 2);
        }
        if (currentTab == 3 && targetTab == 2) {
            solo.clickOnScreen(tabsWidth / 8, xy[1] + tabsHeight / 2);
        }
        if (currentTab == 3 && targetTab == 1) {
            solo.clickOnScreen(tabsWidth / 8, xy[1] + tabsHeight / 2);
            solo.sleep(Res.integer.time_wait);
            solo.clickOnScreen(tabsWidth / 8, xy[1] + tabsHeight / 2);
        }
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 前置条件要准备Today文件夹的时候
     * 
     * @param urls
     *            url的数组，并且数目要大于5
     */
    public void prepareTodayFolder(String urls[]) {
        for (int i = 0; i < urls.length; ++i) {
            visitUrl(urls[i]);
            Assert.assertTrue("Network is bad.", waitForWebPageFinished());
        }
    }

    public boolean clickOnScreen(View view, int time, String string1, String string2, int flag) {// 1为两个都选，2为前选后不选，3为都不选
        boolean toast = false;
        Instrumentation inst = new Instrumentation();
        if (view == null)
            Assert.assertTrue("View is null and can therefore not be clicked!", false);
        int[] xy = new int[2];
        view.getLocationOnScreen(xy);
        final int viewWidth = view.getWidth();
        final int viewHeight = view.getHeight();
        final float x = xy[0] + (viewWidth / 2.0f);
        float y = xy[1] + (viewHeight / 2.0f);
        boolean successfull = false;
        int retry = 0;
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y,
                0);
        while (!successfull && retry < 10) {
            try {
                inst.sendPointerSync(event);
                successfull = true;
            } catch (SecurityException e) {
                solo.hideSoftKeyboard();
                retry++;
            }
        }
        if (!successfull) {
            Assert.assertTrue("Click can not be completed!", false);
        }

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x + 1.0f,
                y + 1.0f, 0);
        inst.sendPointerSync(event);
        if (time > 0) {
            solo.sleep(time / 2);
            if (!string1.equals("") && string2.equals("")) {
                toast = solo.searchText(string1, true);
            }
            if (!string2.equals("") && string1.equals("")) {
                toast = solo.searchText(string2, true);
            }
            if (!string2.equals("") && !string1.equals("")) {
                if (flag == 1)
                    toast = solo.searchText(string2, true) && solo.searchText(string1, true);
                if (flag == 2)
                    toast = !solo.searchText(string2, true) && solo.searchText(string1, true);
                if (flag == 3)
                    toast = !solo.searchText(string2, true) && !solo.searchText(string1, true);
            }
            solo.sleep(time / 2);

        } else
            solo.sleep((int) (ViewConfiguration.getLongPressTimeout() * 2.5f));
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        solo.sleep(500);
        return toast;
    }

    public void visitURL(String names[]) {
        // 设置标志，表示第一次启动
        for (String url : names) {
            solo.sleep(Res.integer.time_wait);
            visitUrl(url);
            Assert.assertTrue("Network is bad .", waitForWebPageFinished());
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
    }

    /**
     * 判断手机Bar是否隐藏
     * 
     * @return
     */
    public boolean isPhoneBarHide() {
        solo.sleep(Res.integer.time_change_activity);
        int[] realSize = new int[2];
        int[] screenSize = new int[2];
        realSize = caseUtil.getDisplaySize(true);
        View view = solo.getViews().get(0);
        screenSize[0] = view.getWidth();
        screenSize[1] = view.getHeight();
        return realSize[0] == screenSize[0] && realSize[1] == screenSize[1];
    }

    /**
     * 验证AddressBar, MenuBar(中屏机竖屏), 以及虚拟的Status Bar是否显示
     * 
     * @param isDolphinBarShow
     * @param isStatusBarShow
     *            <p>
     *            {@code true} 表示验证是否显示出来了, {@code false} 验证是否隐藏了
     */
    public void isBarShow(boolean isDolphinBarShow, boolean isStatusBarShow) {
        View view = getAddressBar();
        Assert.assertTrue("AddressBar没有显示正确",
                isDolphinBarShow == caseUtil.isInsideDisplay(view, !isStatusBarShow));
        View menubar = getMenuBar();
        if (menubar != null) {
            Assert.assertTrue("MenuBar没有显示正确",
                    isDolphinBarShow == caseUtil.isInsideDisplay(menubar, !isStatusBarShow));
        }
        if (!Arrays.equals(caseUtil.getDisplaySize(true), caseUtil.getDisplaySize(false))) {
            Assert.assertTrue("Failed to hide phone bars. ", isStatusBarShow != isPhoneBarHide());
        }
    }

    /**
     * 验证书签和Control Panel图标是否显示,以及显示位置
     * 
     * @param isShow
     *            是否显示
     * @param isLandscape
     *            是否横屏
     */
    public void isSidebarIconShow(boolean isShow, boolean isLandscape) {
        ArrayList<View> views = new ArrayList<View>();
        View leftIcon = solo.getView("id/left_sidebar");
        views.add(leftIcon);
        Assert.assertTrue("Bookmark icon does not show...",
                isShow == caseUtil.isViewShow(views, false));
        View rightIcon = solo.getView("id/right_sidebar");
        if (rightIcon != null) {
            views.clear();
            views.add(rightIcon);
            Assert.assertTrue("Control Panel icon does not show...",
                    isShow == caseUtil.isViewShow(views, false));
        }
        if (isShow) {
            if (isLandscape) {
                // 横屏:书签图标位于"←"图标左侧,Control Panel图标位于Gesture图标右侧
                View backIcon = solo.getView("id/address_go_back");
                View gestureIcon = solo.getView("id/address_open_vg");
                boolean bookmarkFlag = utils.ubietyOfView(leftIcon, backIcon, true, false, false) != -1;
                boolean controlPanelFlag = utils.ubietyOfView(gestureIcon, rightIcon, true, false,
                        false) != -1;
                Assert.assertTrue("Back and gesture are not next to Bookmark and Control Panel...",
                        bookmarkFlag && controlPanelFlag);
            } else {
                // 竖屏:书签和Control Panel图标分别位于地址栏左、右两侧
                View address = solo.getView("id/title_bg");
                ArrayList<View> viewList = new ArrayList<View>();
                viewList.add(leftIcon);
                viewList.add(address);
                viewList.add(rightIcon);
                utils.ubietyOfViews(viewList, 0, false, false, false);
            }
        }

    }

    /**
     * 设置'Always show address and menu bar'开关状态
     * 
     * @param isEnable
     *            {@code true}表示开; {@code false}表示关
     */
    public void enableBarShow(boolean isEnable) {
        enterSetting(false);
        String rid = "";
        if (caseUtil.getDisplayRange() == 1) {
            rid = "pref_keep_bars_title";
        } else {
            rid = "pref_keep_titlebar_title";
        }
        int[] path = { 1, 0 };
        setCheckBoxByTitle(rid, 2, path, isEnable);
        solo.sleep(Res.integer.time_wait);
        if (isEnable) {
            Assert.assertTrue("Failed to open 'Always show address and menu bar'.",
                    isCheckBoxChecked(rid, 2, path));
        } else {
            Assert.assertTrue("Failed to close 'Always show address and menu bar'.",
                    !isCheckBoxChecked(rid, 2, path));
        }
        solo.goBackToActivity("BrowserActivity");
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 设置'Enable swipe for sidebars'开关状态
     * 
     * @param isEnable
     *            {@code true}表示开; {@code false}表示关
     */
    public void enableSwipeSideBar(boolean isEnable) {
        enterSetting(true);
        String rid = "pref_enable_swipe_action";
        int[] path = { 1, 0 };
        setCheckBoxByTitle(rid, 1, path, isEnable);
        solo.sleep(Res.integer.time_wait);
        if (isEnable) {
            Assert.assertTrue("Failed to enable swipe for sidebars.",
                    isCheckBoxChecked(rid, 1, path));
        } else {
            Assert.assertTrue("Failed to close 'enable swipe for sidebars'.",
                    !isCheckBoxChecked(rid, 1, path));
        }
        solo.goBackToActivity("BrowserActivity");
        solo.sleep(Res.integer.time_wait);
        if (solo.searchText(caseUtil.getTextByRId("got_it", -1))) {
            solo.clickOnText(caseUtil.getTextByRId("got_it", -1));
            solo.sleep(Res.integer.time_wait);
        }
    }

    /**
     * 将文本对应的checkbox设置为开或关
     * 
     * @param rId
     *            开关所对应文本的Resource ID
     * @param num
     *            当前目录寻找父级目录数
     * @param path
     *            当前选择的开关相对父级的路径
     * @param enable
     *            {@code true}表示开 {@code false}表示关
     */
    public void setCheckBoxByTitle(String rId, int num, int[] path, boolean enable) {
        View checkbox = getCheckBoxView(rId, num, path);
        if (checkbox != null) {
            if (checkbox instanceof CheckBox) {
                if (((CheckBox) checkbox).isChecked() != enable)
                    solo.clickOnView(checkbox);
            } else {
                if (checkbox.isSelected() != enable)
                    solo.clickOnView(checkbox);
            }
        }
    }

    /**
     * 返回文本对应的checkbox开关状态
     * 
     * @param rId
     *            开关所对应文本的Resource ID
     * @param num
     *            当前目录寻找父级目录数
     * @param path
     *            当前选择的开关相对父级的路径
     * @return {@code true}表示开 {@code false}表示关
     */
    public boolean isCheckBoxChecked(String rId, int num, int[] path) {
        View checkbox = getCheckBoxView(rId, num, path);
        if (checkbox != null) {
            if (checkbox instanceof CheckBox) {
                return ((CheckBox) checkbox).isChecked();
            } else {
                return checkbox.isSelected();
            }
        }
        return false;
    }

    /**
     * @param rId
     *            开关所对应文本的Resource ID
     * @param num
     *            当前目录寻找父级目录数 小于0且path为null:代表不根据索引查找 0:代表文本和checkbox本身为一体
     *            大于0:代表根据索引查找
     * @param path
     *            当前选择的开关相对父级的路径
     * @return 根据文本查找对应的CheckBox
     */
    private View getCheckBoxView(String rId, int num, int[] path) {
        if (num < 0 && path == null) {
            return getCheckBoxView(rId);
        } else if (num == 0) {
            return caseUtil.getViewByText(rId, 0, true, true, true);
        } else {
            View root = utils.getParent(caseUtil.getViewByText(rId, 0, true, true, true), num);
            View checkBox = caseUtil.getViewByIndex(root, path);
            return checkBox;
        }
    }

    /**
     * @param name
     *            不是rid，此处为文本（升级测试中）
     * @return 无需索引，仅根据文本查找相应的checkBox
     */
    public View getCheckBoxView(String name) {
        View textview = null;
        View checkbox = null;
        solo.searchText(name);
        List<TextView> t = solo.getCurrentViews(TextView.class);
        for (TextView s : t) {
            if (s.getText().toString().contains(name)) {
                textview = s;
                break;
            }
        }
        if (textview != null) {
            View parent = (View) textview.getParent();
            List<View> child = solo.getViews(parent);
            for (View v : child) {
                if (v.toString().contains("CheckBox") || v.toString().contains("ImageView")) {
                    checkbox = v;
                    break;
                }
            }
            if (checkbox == null) {
                parent = (View) textview.getParent().getParent();
                child = solo.getViews(parent);
                for (View v : child) {
                    if (v.toString().contains("CheckBox") || v.toString().contains("ImageView")) {
                        checkbox = v;
                        break;
                    }
                }
            }

        }
        return checkbox;
    }

    /**
     * 验证Settings界面中的文本项内容
     * <p>
     * 包括以下内容:
     * <p>
     * 1.Settings列表中的主要文本
     * <p>
     * 2.Settings列表中的附属小文本,即当前选择的模式
     * <p>
     * 3.Settings列表中的主要文本的开关状态
     * 
     * @param hostText
     *            主要文本的Resource ID
     * @param crossText
     *            附属小文本的Resource ID,如果不验证,则设置为{@code null}
     *            ,如果要验证原始文本,请将index设置为-1
     * @param index
     *            当前选择的模式所对应的文本在R文件中的数组索引
     * @param isButton
     *            是否存在文本的开关
     * @param defaultValue
     *            存在开关时：开关的状态值
     * @param num
     *            存在开关时：当前目录寻找父级目录数
     * @param path
     *            存在开关时：当前选择的开关相对父级的路径
     */
    public void assertSearchText(String hostText, String crossText, int index, boolean isButton,
            boolean defaultValue, int num, int[] path) {
        boolean flag = caseUtil.searchText(hostText, 0, true, true, true);
        Assert.assertTrue(formatString(hostText, 0), flag);
        if (crossText != null) {
            flag = caseUtil.searchText(crossText, index, false, true, true);
            Assert.assertTrue(formatString(crossText, index), flag);
        }
        if (isButton) {
            flag = (defaultValue == isCheckBoxChecked(hostText, num, path));
            Assert.assertTrue(caseUtil.getTextByRId(hostText) + "的默认值不是" + defaultValue, flag);
        }
    }

    private String formatString(String rId, int index) {
        String text = "";
        if (index == -1) {
            text = rId;
        } else {
            text = caseUtil.getTextByRId(rId, index);
        }
        return "没有找到指定的文本:" + text;
    }

    /**
     * 验证：Dolphin Now卡片文字是否匹配、刷新icon
     * 
     * @param rid
     *            所验证卡片的文字的Rid
     * @param index
     *            所验证卡片的索引号
     * @param hasRefresh
     *            所验证卡片是否有刷新按钮
     */
    public void assertCardText(String rid, int index, boolean hasRefresh) {
        View parent = solo.getView("card_page_root");
        TextView view = (TextView) caseUtil.getViewByIndex(parent, new int[] { index, 0, 2 });
        String string = caseUtil.getTextByRId(rid, -1);
        Assert.assertTrue("卡片文字不匹配", string.equals(view.getText().toString()));

        ImageView refresh = (ImageView) caseUtil.getViewByIndex(parent, new int[] { index, 0, 3 });
        if (hasRefresh) {
            Assert.assertTrue("刷新按钮未显示", refresh.getVisibility() == View.VISIBLE);
            Assert.assertTrue("卡片刷新按钮不正确", imageUtil.compareImageView(refresh, false,
                    RDolphin.drawable.card_refresh, null));
        } else {
            Assert.assertTrue("刷新按钮显示了", refresh.getVisibility() == View.GONE);
        }
    }

    /**
     * 验证：Dolphin Now卡片的More是否和主题色一致
     * 
     * @param index
     *            所验证卡片的索引号
     */
    public void assertCardMore(int index) {
        String THEMECOLOR = "ff5c7db4";
        View parent = solo.getView("card_page_root");
        TextView more = (TextView) caseUtil.getViewByIndex(parent, new int[] { index, 3, 1 });
        Assert.assertTrue("底部未显示More",
                caseUtil.getTextByRId("card_more", -1).equals((more.getText().toString())));
        Assert.assertTrue("More字体与当前主题色不一致", Integer.toHexString(more.getCurrentTextColor())
                .equals(THEMECOLOR));
    }

    /**
     * 从设置界面进入海豚服务登录界面
     */
    public void enterDCLoginInterface() {
        solo.sleep(Res.integer.time_wait);
        // 点击未登录状态的登录按钮
        TextView accountName = (TextView) solo.getView("account_name");
        boolean flag = accountName.getText().equals(
                caseUtil.getTextByRId(Res.string.no_account_info_title));
        solo.clickOnView(accountName);
        if (!flag) {
            // 已经登录过账号的设备会先退出登录
            solo.sleep(Res.integer.time_change_activity);
            caseUtil.clickOnText("logout", 0, true);
            solo.sleep(Res.integer.time_wait);
            caseUtil.clickOnText("ds_logout_confirm", 0, true);
        }
        solo.sleep(Res.integer.time_change_activity);
        // TODO 有预置账号时, 会进入预置账号的界面,而非Login界面,这里后续需要处理
    }

    /**
     * 从Login界面进入Dolphin账号登录
     */
    public void enterDolphinAccountLoginInterface() {
        solo.sleep(Res.integer.time_wait);
        solo.scrollDown();// 当前页没有显示dolphin登录button的情况,主要是小屏机
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("btn_login_dolphin");
        solo.sleep(Res.integer.time_change_activity);
    }

    /**
     * 从Dolphin Account界面登录Dolphin账号
     * 
     * @param userName
     * @param password
     * @return
     */
    public void signInDolphinAccount(String userName, String password) {
        // 输入用户名和密码
        caseUtil.setText((TextView) solo.getView(Res.string.ds_email), userName);
        caseUtil.setText((TextView) solo.getView(Res.string.ds_password), password);
        solo.sleep(Res.integer.time_wait);
        // 点击登录开始登录输入的账号密码
        // TODO 这里考虑了尝试三次登录的情况, 失败需要等超时, 后续可以优化这里
        for (int i = 0; i < 3; i++) {
            caseUtil.clickOnView("ds_dolphin_login");
            solo.sleep(3 * 1000);
            // 通过搜索登录完成后界面上是否有Log out字符来判断是否登录成功
            boolean loginFlag = caseUtil.waitForText("logout", 0, true, true, 25 * 1000, false);
            if (loginFlag) {
                return;
            }
            if (caseUtil.searchText("synced_tips_dialog_title", 0, true, true, false)) {
                // 弹出同步成功的对话框则点击OK按钮
                caseUtil.clickOnText("btn_ok", 0, true);
                solo.sleep(Res.integer.time_wait);
                return;
            }
        }
        Assert.assertTrue("登录账号失败", false);
    }

    /**
     * 从设置的General界面登录dolphin账号, 并点掉出现的对话框
     * 
     * @param userName
     * @param password
     * @return
     */
    public boolean signInAndWaitForDialog(String userName, String password) {
        enterDCLoginInterface();
        enterDolphinAccountLoginInterface();
        signInDolphinAccount(userName, password);
        waitForSyncCompleteDialog();
        return true;
    }

    /**
     * 等待登录完成后的同步完成对话框
     */
    public void waitForSyncCompleteDialog() {
        solo.sleep(Res.integer.time_wait);
        long endTime = SystemClock.uptimeMillis() + 30 * 1000;
        while (endTime > SystemClock.uptimeMillis()) {
            // TODO 这里没有对同步成功但不需要弹出同步对话框的情况进行处理
            if (caseUtil.searchText("cloud_data_sync_failed", 0, true, true, false)) {
                // 同步失败,则无处理
                break;
            }
            if (caseUtil.searchText("synced_tips_dialog_title", 0, true, true, false)) {
                // 弹出同步成功的对话框则点击OK按钮
                caseUtil.clickOnText("btn_ok", 0, true);
                solo.sleep(Res.integer.time_wait);
                break;
            }
        }
    }

    /**
     * 从账户服务管理界面进入Sync界面
     */
    public void enterSyncFromAccountServiceManage() {
        final String cloudDataManage = Res.string.cloud_data_manage_activity;
        caseUtil.clickOnText("sync", 0, true);
        boolean flag = solo.waitForActivity(cloudDataManage);
        Assert.assertTrue("进入Sync界面失败", flag);
    }

    /**
     * 尝试成功同步云数据,尝试2分钟
     * 
     * @return {@code true}同步成功{@code false}同步超时
     */
    public boolean syncCloudData() {
        String syncSuccess = caseUtil.getTextByRId("cloud_data_last_sync_time");
        String syncFailed = caseUtil.getTextByRId("cloud_data_sync_failed");
        TextView syncStatus = (TextView) solo.getView("cloud_data_last_sync_time");

        long endTime = SystemClock.uptimeMillis() + 2 * 60 * 1000;
        while (endTime > SystemClock.uptimeMillis()) {
            String syncText = syncStatus.getText().toString();
            boolean flag = syncText.contains(syncSuccess);
            if (flag) {
                return true;
            }
            flag = syncText.contains(syncFailed);
            if (flag) {
                enterSyncFromAccountServiceManage();
                solo.sleep(Res.integer.time_wait);
                caseUtil.clickOnView("sync_selected_item");
                solo.sleep(Res.integer.time_wait);
                solo.goBack();
                solo.sleep(Res.integer.time_change_activity);
            }
            solo.sleep(3 * 1000);
        }
        return false;
    }

    public void completeLoginAndSync(String userName, String userPassword) {
        // 登录账号
        boolean flag = signInAndWaitForDialog(userName, userPassword);
        Assert.assertTrue("登录账号失败", flag);
        // 同步数据
        flag = syncCloudData();
        Assert.assertTrue("同步数据失败", flag);
    }

    /**
     * 通过点击Tips中的Sync,之后判断是否进入了海豚登录界面
     * <p>
     * 海豚的3个入口测试用到
     * 
     * @return
     */
    public boolean isDolphinConnectActivity() {
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("sync", 0, true);
        solo.sleep(Res.integer.time_change_activity);
        // 通过判断界面上的特定文字(Dolphin Connect下的一段长文字)来判断是否在登录界面
        return caseUtil.searchText("dolphinconnect_signin", 0, true, true, true);
    }

    /**
     * 等待网页加载完成
     * 
     * @return {@code true} 表示加载完成,也可能是无法打开网页 {@code false} 表示加载超时
     */
    public boolean waitForWebPageFinished() {
        solo.sleep(3 * 1000);// 等待页面开始加载
        long endTime = SystemClock.uptimeMillis() + 2 * 60 * 1000;
        while (endTime > SystemClock.uptimeMillis()) {
            ArrayList<View> views = caseUtil.getViews(null, "tiny_title_bar");
            boolean flag = true;
            for (View view : views) {
                if (view.getVisibility() == View.VISIBLE && caseUtil.isInsideDisplay(view, false)) {
                    flag = false;
                }
            }
            if (flag) {
                return true;
            }
            solo.sleep(1000);
        }
        return false;
    }

    /**
     * 点击menu菜单中的特定文本
     * 
     * @param rId
     *            资源文本的ID
     */
    public void clickOnMenuItem(String rId) {
        solo.sleep(Res.integer.time_wait);
        solo.sendKey(KeyEvent.KEYCODE_MENU);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText(rId, 0, true);
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 选择进入设置的General选项卡或Advanced选项卡
     * 
     * @param isAdvanced
     *            true为进入Advanced选项卡, false为进入General选项卡
     */
    public void enterSetting(boolean isAdvanced) {
        // 进入默认的设置界面
        clickOnMenuItem("panel_menu_item_title_settings");
        solo.sleep(Res.integer.time_wait);
        Assert.assertTrue(
                String.format(Res.string.not_enter_activity,
                        caseUtil.getTextByRId("pref_root_title")),
                waitForInterfaceByTitle("pref_root_title", 10 * 1000));
        if (isAdvanced) {
            solo.sleep(Res.integer.time_wait);
            caseUtil.clickOnView(Res.string.second_tab_title);
        }
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 进入"GESTURE & SONAR"的设置界面
     * 
     * @param isSonar
     *            {@code true} 表示进入Sonar的设置界面,{@code false} 表示进入Gesture的设置界面
     */
    public void enterGestureAndSonarSettings(boolean isSonar) {
        // 方法一：(长按海豚键进入GESTURE界面->点击设置图标) 方法二：直接进入GESTURE界面->点击设置图标(使用该方法)
        enterGestureAndSonar(isSonar);
        caseUtil.clickOnView("vg_btn_settings");
        solo.sleep(Res.integer.time_wait);
        solo.hideSoftKeyboard();// 在Gesture界面需要关闭软件盘
        solo.sleep(Res.integer.time_wait);
        Assert.assertTrue("未进入GESTURE & SONAR界面",
                caseUtil.searchText("gesture_sonar_settings_title", 0, true, true, false));
    }

    /**
     * 进入Gesture&Sonar界面
     * 
     * @param isSonar
     *            {@code true} 表示进入Sonar界面, {@code false} 表示进入Gesture界面
     */
    public void enterGestureAndSonar(boolean isSonar) {
        caseUtil.startActivity("com.dolphin.browser.input.VoiceGestureActivity", null, null);
        boolean flag = solo.getView("sonar_logo").isSelected();
        if (flag != isSonar) {
            caseUtil.clickOnView("vg_switcher");
            solo.sleep(Res.integer.time_wait);
        }
    }

    /**
     * 如果Whistle出现了，关闭它
     */
    public void closeWhistleWhenOpen() {
        if (solo.getView("promotion_view").getVisibility() == View.VISIBLE) {
            caseUtil.clickOnView("hbtn_cancel");
            solo.sleep(Res.integer.time_wait);
            Assert.assertTrue("Whistle点击后并没有消失",
                    solo.getView("promotion_view").getVisibility() != View.VISIBLE);
        }
    }

    /**
     * 如果存在Enable Swipe For Sidebars的提醒框， 关闭它
     */
    public void closeSidebarTips() {
        if (caseUtil.searchText("sidebar_tips_view_title", 0, true, true, false)) {
            caseUtil.clickOnText("cancel", 0, true);
            solo.sleep(Res.integer.time_wait);
            Assert.assertTrue("没有关闭Sidebar Tips",
                    !caseUtil.searchText("sidebar_tips_view_title", 0, true, true, false));
        }
    }

    /**
     * 进入Share界面
     * 
     * @param tag
     *            1:点击menu的Share; 2:点击网页中超链接的Share; 3:点击网页图片的Share
     */
    public void enterShare(int tag) {
        switch (tag) {
        case 1:
            // Menu-> 点击"Share"按钮
            clickOnMenuItem("share");
            break;
        case 2:
            // 打开任意网页, 长按网页中的超链接 -> 点击"Share link"选项
            visitUrl(Res.string.url_downloadtest);
            solo.sleep(Res.integer.time_wait);
            Assert.assertTrue("Network is bad .", waitForWebPageFinished());
            WebElement link = solo.getWebElement(By.textContent("test"), 0);
            int[] linkLoc = new int[2];
            link.getLocationOnScreen(linkLoc);
            solo.clickLongOnScreen(linkLoc[0], linkLoc[1]);
            solo.sleep(Res.integer.time_wait);
            caseUtil.clickOnSelections(3);
            break;
        case 3:
            // 打开有图片的网页, 长按所选图片 -> 点击"Share image"选项
            visitUrl(Res.string.url_img);
            solo.sleep(Res.integer.time_wait);
            Assert.assertTrue("Network is bad .", waitForWebPageFinished());
            WebElement image = solo.getWebElement(By.id("kimi"), 0);
            int[] imageLoc = new int[2];
            image.getLocationOnScreen(imageLoc);
            solo.clickLongOnScreen(imageLoc[0], imageLoc[1]);
            solo.sleep(Res.integer.time_wait);
            caseUtil.clickOnSelections(0);
            break;
        }
        Assert.assertTrue("'Share' interface is not open...", solo.waitForDialogToOpen());
    }

    /**
     * 验证是否进入新增书签/新增SpeedDial
     * 
     * @param webName
     *            书签名/SpeedDial名
     * @param webURL
     *            书签(SpeedDial)的正确网址
     * @return
     */
    public boolean isIntoNewBookmark(String webName, String webURL, boolean isSpeedDial) {
        solo.sleep(Res.integer.time_wait);
        if (isSpeedDial) {
            View title = caseUtil.getViewByText(webName, -1, true, false, true);
            solo.clickOnView(utils.getParent(title, 1));
        } else {
            solo.clickOnText(webName);
        }
        solo.sleep(Res.integer.time_wait);
        Assert.assertTrue("Network is not available.", waitForWebPageFinished());
        return checkURL(webURL);
    }

    /**
     * 是否书签列表的第一项（文件夹列表的最后一项）为新增书签
     * 
     * @param webName
     *            新添加的书签名
     * @return
     */
    public boolean isFirstNewBookmark(String webName) {
        // 进入书签栏
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        enterBookmark();
        // 判断书签列表第一项
        String item1 = new String();
        View vg = solo.getView("id/list");
        for (int i = 0; i < ((ViewGroup) vg).getChildCount(); i++) {
            if (caseUtil.getViewByIndex(vg, new int[] { i, 3 }).getVisibility() == View.GONE) {
                item1 = ((TextView) caseUtil.getViewByIndex(vg, new int[] { i, 2, 0 })).getText()
                        .toString();
                break;
            }
        }
        return item1.equals(webName);
    }

    /**
     * 在Setting中设置默认搜索引擎
     * 
     * @param engineName
     *            要设置的搜索引擎
     */
    public void setDefaultEngine(String engineName) {
        enterSetting(false);
        solo.sleep(Res.integer.time_wait);
        String defaultSearchEngineName = getSearchEngineName();
        if (!defaultSearchEngineName.equals(engineName)) {
            solo.clickOnText(caseUtil.getTextByRId("pref_content_search_engine", -1));
            solo.sleep(Res.integer.time_wait);
            solo.clickOnText(engineName);
            solo.sleep(Res.integer.time_wait);
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
    }

    /**
     * 从Settings的SearchEngine下方的小字体获取当前搜索引擎
     * 
     * @return 返回当前搜索引擎
     */
    private String getSearchEngineName() {
        String name = "";
        View searchEngineTitle = caseUtil.getViewByText("pref_content_search_engine", 0, true,
                true, true);
        View parent = (View) searchEngineTitle.getParent();
        if (parent != null) {
            parent = (View) parent.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                name = ((TextView) ((ViewGroup) parent).getChildAt(1)).getText().toString();
                return name;
            }
        }
        return name;
    }

    /**
     * 通过XML获取搜索列表
     * 
     * @return
     */
    private ArrayList<String> getSearchEngineList() {
        String origin = xmlUtil.getSearchCategories();
        ArrayList<String> list = new ArrayList<String>();
        try {
            JSONArray ja = new JSONArray(origin);
            Assert.assertFalse("没有正确获取到预置数据,可能数据结构有变动", ja.length() == 0);
            JSONArray searchEngineList = ja.getJSONObject(0).getJSONArray("searches");
            for (int i = 0; i < searchEngineList.length(); i++) {
                JSONObject searchEngine = searchEngineList.getJSONObject(i);
                String nameString = searchEngine.getString("title");
                list.add(nameString);
            }
        } catch (JSONException e) {
            // Nothings
        }
        return list;
    }

    /**
     * 比较搜索引擎列表是否与预置数据一致
     * 
     * @param views
     *            界面上显示的搜索引擎列表
     * @param hasChild
     *            views中的元素是否包含搜索引擎图标：{@code true}包含名称和图标、{@code false}仅包含名称
     */
    public void compareSearchEngineListWithOrigin(ArrayList<View> views, boolean hasChild) {
        ArrayList<String> originSearchEngine = getSearchEngineList();
        for (int i = 0; i < views.size(); i++) {
            String searchEngineName = "";
            if (hasChild) {
                searchEngineName = ((TextView) ((ViewGroup) views.get(i)).getChildAt(1)).getText()
                        .toString();
            } else {
                searchEngineName = ((TextView) views.get(i)).getText().toString();
            }

            // 当界面显示的内容比xml中的多时, 报错
            if (i + 1 > originSearchEngine.size()) {
                Assert.assertTrue("界面上显示的内容多于配置的内容, 多的为:" + i + "多的第一个内容为:" + searchEngineName,
                        false);
            }
            // 当界面显示的内容比xml中的少时, 报错
            if (i + 1 == views.size()) {
                if (originSearchEngine.size() > i + 1) {
                    Assert.assertTrue("界面上存在没有显示的预置数据,没有显示的第一个为:" + originSearchEngine.get(i + 1),
                            false);
                }
            }
            // 判断每一项是否显示正确的搜索引擎
            if (!searchEngineName.equals(originSearchEngine.get(i))) {
                Assert.assertTrue("第" + (i + 1) + "个需要显示的" + originSearchEngine.get(i)
                        + "没有找到,显示的是" + searchEngineName, false);
            }
        }
    }

    /**
     * 创建一个下载任务
     * 
     * @param url
     */
    public void createOneDownloadTask(String url) {
        visitUrl(url);
        boolean flag = caseUtil.waitForText("download_tab_title", 0, true, true, 20 * 1000, false);
        solo.sleep(Res.integer.time_wait);
        Assert.assertTrue("Failed to popup 'Download' dialog, maybe network is not available.",
                flag);
        caseUtil.clickOnView("button2");
        solo.waitForDialogToClose();
    }

    /**
     * 等待是否进入指定标题的界面,该方法只适用于含有ActionBar的界面
     * 
     * @param titleName
     *            Title的名字的rId
     * @param timeout
     *            超时时间
     * @return
     */
    public boolean waitForInterfaceByTitle(String titleName, long timeout) {
        if (titleName == null || titleName == "") {
            Assert.assertTrue("界面Title不能为空", false);
        }
        timeout = timeout < 1000 ? 1000 : timeout;
        long endTime = SystemClock.uptimeMillis() + timeout;
        String titleText = caseUtil.getTextByRId(titleName);
        do {
            solo.sleep(1000);
            View titleContainer = caseUtil.getView(null, "header");
            if (titleContainer == null) {
                titleContainer = caseUtil.getView(null, "action_bar_title_container");
            }
            if (titleContainer == null) {
                continue;
            }
            TextView title = (TextView) caseUtil.getViewByIndex(titleContainer, new int[] { 1 });
            if (titleText.equals(title.getText().toString())) {
                return true;
            }
        } while (endTime > SystemClock.uptimeMillis());
        return false;
    }

    /**
     * 等待指定文件名的下载任务完成
     * 
     * @param fileName
     * @param timeout
     * @return
     */
    public boolean waitForDownloadTaskComplete(String fileName, long timeout, int cyclesTime) {
        SqliteUtil sqliteUtil = new SqliteUtil(solo);
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (endTime > SystemClock.uptimeMillis()) {
            if (sqliteUtil.isDownloadComplete(fileName)) {
                return true;
            }
            solo.sleep(cyclesTime);
        }
        return false;
    }

    /**
     * 等待promotion弹出,当中如果出现dragup,则将其移除
     */
    public boolean waitForPromotionPopUp() {
        // 中屏机当弹出drag up时进行取消操作，小屏机出现whistle后直接进行下一步
        String dragup = caseUtil.getTextByRId("dolphin_key_guide_tips");
        long endTime = SystemClock.uptimeMillis() + 2 * 60 * 1000;
        while (SystemClock.uptimeMillis() < endTime) {
            if (solo.searchText(dragup, true)) {
                solo.goBack();
            }
            solo.sleep(Res.integer.time_wait);

            if (solo.getView(PROMOTION_VIEW_ID).getVisibility() == View.VISIBLE) {
                solo.sleep(Res.integer.time_wait);
                // 有时whistle和dragup同时出现，需要移除dragup
                if (solo.searchText(dragup, true)) {
                    solo.goBack();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 等待需要的whistle出现
     * 
     * @param whistleText
     * @return
     */
    private int waitForWhistle(String whistleText) {
        TextView marquee = (TextView) solo.getView(MARQUEE_TEXT_VIEW_ID);
        long endTime = SystemClock.uptimeMillis() + 2 * 60 * 1000 + 1000;
        String promotionTextOld = marquee.getText().toString();
        String promotionTextNew = "";
        while (SystemClock.uptimeMillis() < endTime) {
            promotionTextNew = marquee.getText().toString();
            if (promotionTextNew.equals(whistleText)) {
                return 0;
            }
            solo.sleep(Res.integer.time_wait);
        }
        if (promotionTextNew.equals(promotionTextOld)) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 断言是否找到了需要的whistle
     * 
     * @param whistleText
     */
    public void assertWaitForWhistle(String whistleText) {
        int result = -1;// 判断是否找到了whistle
        for (int i = 0; i < 4; i++) {// 目前最多配置了4条
            result = waitForWhistle(whistleText);
            if (result == 1) {
                Assert.assertTrue("2分钟没有变换过whistle,确认无法找到配置的whistle", false);
            } else if (result == 0) {
                break;// 已经成功找到,case结束
            }
            i++;
        }
        if (result == -1) {
            Assert.assertTrue("循环了所有4个whistle没有正确找到配置的whistle", false);
        }
    }

    /**
     * 验证Back图标在标题左侧
     * 
     * @return
     */
    public void isBackIconOnTheLeft() {
        solo.sleep(Res.integer.time_wait);
        View back_icon = solo.getView("id/btn_done");
        View title = solo.getView("id/title");
        Assert.assertTrue("Back图标在标题栏左侧",
                utils.ubietyOfView(back_icon, title, true, false, false) != -1);
    }

    /**
     * 设置指定的SmartLocale
     * 
     * @param locale
     * @return
     */
    public boolean setSmartLocale(String locale) {
        if (locale == null || locale == "") {
            Assert.assertTrue("设置Locale的国家不能为空", false);
        }
        String currentLocale = xmlUtil.getSmartLocale();
        if (locale.equals(currentLocale)) {
            return true;
        }
        visitUrl("dolphin://developer");
        caseUtil.clickOnText("developer_options_titles", 3, true);
        solo.sleep(Res.integer.time_wait);
        EditText smartLocale = (EditText) caseUtil.getView(null, "et_smart_locale");
        caseUtil.setText(smartLocale, locale);
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnView("btn_save_locale");
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        caseUtil.clickOnText("developer_options_titles", 5, true);
        solo.sleep(10 * 1000);
        return false;
    }

    /**
     * 返回被应用主题的被选择view图像
     * 
     * @return
     */
    public View getViewAppliedTheme() {
        ArrayList<View> allViews = caseUtil.getViews(null, "skin_selection");
        if (allViews.size() == 0) {
            Assert.assertTrue("当前并不在Themes界面", false);
        }
        for (View v : allViews) {
            if (((ImageView) v).getDrawable() != null) {
                return v;
            }
        }
        return null;
    }

    /**
     * 判断当前应用的主题是不是指定的id的主题
     * 
     * @param themeId
     * @return
     */
    public boolean isAppliedTheme(int themeId) {
        return xmlUtil.getThemeId() == themeId;
    }

    /**
     * 根据给定的主题名判断主题是否被应用
     * 
     * @param themeName
     * @return
     */
    public boolean isAppliedTheme(String themeName) {
        View view = getViewAppliedTheme();
        if (view != null) {
            if (((ViewGroup) view.getParent()).getChildCount() == 8) {
                TextView textView = (TextView) ((ViewGroup) view.getParent()).getChildAt(4);
                return textView.getText().toString().contains(themeName);
            } else {
                TextView textView = (TextView) ((ViewGroup) view.getParent()).getChildAt(5);
                return textView.getText().toString().contains(themeName);
            }
        }
        return false;
    }

    /**
     * 在BrowserActivity界面中滑动到SideBar
     * 
     * @param isLeftBar
     *            {@code true}便是滑动到左侧边栏. {@code false}表示滑动到右侧边栏
     */
    public void enterSideBar(boolean isLeftBar) {
        // 小屏幕机型操作特殊,addressbar没有相关按钮
        if (caseUtil.getDisplayRange() == 0) {
            caseUtil.clickOnView("address_open_menu_more");
            solo.sleep(Res.integer.time_wait);
            if (isLeftBar) {
                caseUtil.clickOnText("bookmarks", 0, true);
            } else {
                caseUtil.clickOnText("action_menu_item_control_panel", 0, true);
            }
        } else {
            if (isLeftBar) {
                View leftBar = caseUtil.getViews(null, "left_sidebar").get(0);
                if (leftBar.isShown()) {
                    solo.clickOnView(leftBar);
                } else {
                    caseUtil.slide(null, 1 / 50f, 1 / 6f, 49 / 50f, 1 / 6f, 1f);
                }
            } else {
                View rightBar = caseUtil.getViews(null, "right_sidebar").get(0);
                if (rightBar.isShown()) {
                    solo.clickOnView(rightBar);
                } else {
                    caseUtil.slide(null, 49 / 50f, 1 / 6f, 1 / 50f, 1 / 6f, 1f);
                }
            }
        }
        solo.sleep(Res.integer.time_change_activity);
    }

    /**
     * 画一个指定的图形<br>
     * 这里集合了常用的几种图形
     * 
     * @param view
     *            所画的图形在哪个{@link View} 中
     * @param pattern
     *            图形的样式,对应参数值请查看Case列表
     */
    public void drawGesture(View view, String pattern) {
        switch (pattern) {
        case "star":
            caseUtil.draw(view, new float[] { 1 / 2f, 1 / 5f, 1 / 5f, 4 / 5f, 4 / 5f, 2 / 5f,
                    1 / 5f, 2 / 5f, 4 / 5f, 4 / 5f, 1 / 2f, 1 / 5f }, 1f);
            break;
        case "F":
            caseUtil.draw(view, new float[] { 3 / 5f, 1 / 4f, 1 / 2f, 1 / 4f, 1 / 2f, 3 / 4f,
                    2 / 5f, 3 / 4f }, 1f);
            break;
        case "G":
            caseUtil.draw(view, new float[] { 3 / 4f, 1 / 4f, 1 / 4f, 1 / 4f, 1 / 4f, 3 / 4f,
                    3 / 4f, 3 / 4f, 3 / 4f, 1 / 2f, 1 / 2f, 1 / 2f, 1 / 2f, 5 / 8f }, 1f);
            break;
        case "L":
            caseUtil.draw(view, new float[] { 2 / 5f, 1 / 4f, 2 / 5f, 3 / 4f, 3 / 5f, 3 / 4f }, 1f);
            break;
        default:
            Assert.assertTrue("请指定一个已经存在的图形", false);
            break;
        }
    }

    /**
     * 是否是INT版本
     * 
     * @return 是INT版本则返回{@code true} 否则返回{@code false}
     */
    public boolean isINTPackage() {
        if (caseUtil.packageName.equals("mobi.mgeek.TunnyBrowser")) {
            return true;
        }
        return false;
    }

    /**
     * @param tag
     *            0:FullScreen 1：Tabs 2:Night 3:Desktop 4:Incognito 5:No image
     * @return
     */
    public View getControlPanelView(int tag) {
        View view = caseUtil.getViewByIndex(solo.getView("list_installed_plugin"), new int[] { 0,
                0, tag });
        if (view instanceof ViewGroup) {
            view = ((ViewGroup) view).getChildAt(0);
        }
        return view;
    }

    /**
     * 在进入右侧边栏的情况下，打开/关闭全屏、tabs、night、desktop、incognito或no image
     * 
     * @param toOpen
     *            {@code true}打开;{@code false}关闭
     * @param tag
     *            0:FullScreen 1：Tabs 2:Night 3:Desktop 4:Incognito 5:No image
     */
    public void setControlPanelView(boolean toOpen, int tag) {
        View view = getControlPanelView(tag);
        if (toOpen != view.isSelected()) {
            solo.clickOnView(view);
        } else {
            solo.goBack();
        }
    }

    /**
     * 进入右侧边栏，并打开/关闭全屏、tabs、night、desktop、incognito或no image
     * 
     * @param toOpen
     *            {@code true}打开;{@code false}关闭
     * @param tag
     *            0:FullScreen 1：Tabs 2:Night 3:Desktop 4:Incognito 5:No image
     */
    public void clickOnControlPanel(boolean toOpen, int tag) {
        enterSideBar(false);
        setControlPanelView(toOpen, tag);
        solo.sleep(Res.integer.time_wait);
    }

    public void setCheckBox(View v, boolean enable) {
        boolean checked = false;
        if (v instanceof CheckBox) {
            checked = ((CheckBox) v).isChecked();
            if (checked != enable) {
                solo.clickOnView(v);
            }
        }
    }

    public boolean isCheckBox(View v) {
        boolean checked = false;
        if (v instanceof CheckBox) {
            checked = ((CheckBox) v).isChecked();
        }
        return checked;
    }

    /**
     * 返回是否使用了内核
     * 
     * @return {@code true} 正在使用,默认值为{@code false}
     */
    public boolean isWebkit() {
        boolean flag = false;
        try {
            Class<?> sWebViewFactoryClass = Class
                    .forName("com.dolphin.browser.core.WebViewFactory");
            Method sIsUsingWebkitMethod = sWebViewFactoryClass
                    .getDeclaredMethod("isUsingDolphinWebkit");
            sIsUsingWebkitMethod.setAccessible(true);
            flag = (Boolean) sIsUsingWebkitMethod.invoke(null);
        } catch (ClassNotFoundException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (NoSuchMethodException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (IllegalAccessException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (InvocationTargetException e) {
            Assert.assertTrue(e.getMessage(), false);
        }
        return flag;
    }
}