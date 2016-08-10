package com.dolphin.updatecase.updatefunction;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_038")
@TestClass("升级后功能检查_speed dial操作")
public class SpeedDialOperateTest extends BaseTest {

    public void testSpeedDial() {
        uiUtil.skipWelcome();
        // 验证：点击百度一下，speed dial加载正常
        solo.clickOnView(uiUtil.getSpeedDialByName("百度一下"));
        assertTrue("点击百度一下，speed dial加载不正常", uiUtil.checkURL("www.baidu.com"));
        solo.goBack();

        // 验证：长按百度一下，选择发送至桌面，发送成功
        View baidu = caseUtil.getViewByText("百度一下", -1, true, true, true);
        caseUtil.dragViewToView(baidu,
                caseUtil.getViewByText("send_to_home", 0, true, false, false));

        // 验证：长按百度一下，选择删除
        caseUtil.dragViewToView(uiUtil.getSpeedDialByName("百度一下"), getDeleteView());
        assertTrue("百度一下删除不成功", null == uiUtil.getSpeedDialByName("百度一下"));

        // 修改文件夹，Folder改为change
        solo.clickOnText("Folder");
        solo.clickOnView(solo.getView("id/folder_name"));
        solo.hideSoftKeyboard();
        solo.clearEditText(0);
        solo.enterText(0, "change");
        solo.clickOnView(solo.getView("id/title_bg"));

        // 验证：Folder文件修改成功change
        assertTrue("fold文件未修改成功change",
                null != caseUtil.getViewByText("change", -1, true, true, true));
    }

    private View getDeleteView() {
        if (caseUtil.getDisplayRotation() == 0) {
            return solo.getView("button_drop_target_text", 2);
        } else {
            return solo.getView("button_drop_target_text", 1);
        }
    }
}
