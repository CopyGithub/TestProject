package com.dolphin.testcase.share;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-483
 * <p>
 * 脚本描述: 验证Share界面样式、服务显示正确
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-483")
@TestClass("验证Share界面样式、服务显示正确")
public class CheckShareUITest extends BaseTest {
    View recommondShareGrid;
    View systemShareGrid;
    ArrayList<View> viewsRec;
    ArrayList<View> viewsSys;

    public void testCheckShareUI() {
        uiUtil.skipWelcome();

        uiUtil.enterShare(1);
        // 观察Share界面的样式
        checkShareUI();
        // 观察服务排列方式
        checkShareGrid();

        // 观察服务图标(TODO)
        // 每个服务的图标显示正确: 若系统安装了该服务, 则与系统中的图标一致, 否则从服务器获取

        // 观察服务名称
        checkShareGridName();

    }

    private void checkShareGridName() {
        // 每个服务图标下面显示对应服务的名称, 且名称无拼写错误，格式正确无换行
        for (View view : viewsRec) {
            View icon = caseUtil.getViewByIndex(view, new int[] { 0, 0 });
            View title = caseUtil.getViewByIndex(view, new int[] { 0, 1 });
            assertTrue("Icon is not above the title...",
                    utils.ubietyOfView(icon, title, false, false, false) != -1);
        }
        for (View view : viewsSys) {
            View icon = caseUtil.getViewByIndex(view, new int[] { 0, 0 });
            View title = caseUtil.getViewByIndex(view, new int[] { 0, 1 });
            assertTrue("Icon is not above the title...",
                    utils.ubietyOfView(icon, title, false, false, false) != -1);
        }
    }

    private void checkShareGrid() {
        // 推荐服务显示在界面上方, 按照n行3列, 从左向右排列；系统推荐服务显示在界面下方,
        // 按照3列排列（行数根据服务个数决定）；2类服务之间有分割线
        recommondShareGrid = solo.getView("recommond_share_grid");
        View divider = caseUtil.getViewByIndex(solo.getView("custom"), new int[] { 0, 0, 1 });
        systemShareGrid = solo.getView("system_share_grid");
        ArrayList<View> views = new ArrayList<View>();
        views.add(recommondShareGrid);
        views.add(divider);
        views.add(systemShareGrid);
        utils.ubietyOfViews(views, 1, false, false, false);
        viewsRec = utils.getChildren(recommondShareGrid, false);
        utils.ubietyOfViews(viewsRec, 3, true, true, true);
        viewsSys = utils.getChildren(systemShareGrid, false);
        utils.ubietyOfViews(viewsRec, 3, true, true, true);
    }

    private void checkShareUI() {
        // 界面以弹框的形式显示: 弹框顶部正中显示标题"SHARE", 底部显示"Cancel"按钮
        TextView title = (TextView) caseUtil.getViewByIndex("topPanel", 2);
        Button cancelBtn = (Button) solo.getView("button2");
        assertTrue("Title 'Share' is not in the middle...",
                title.getText().toString().equals(caseUtil.getTextByRId("share")));
        assertTrue("Button is not 'Cancel'...",
                cancelBtn.getText().toString().equals(caseUtil.getTextByRId("cancel")));
    }

}