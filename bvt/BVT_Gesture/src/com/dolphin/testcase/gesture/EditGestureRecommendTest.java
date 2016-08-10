package com.dolphin.testcase.gesture;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-605
 * <p>
 * 脚本描述: 长按Gesture list中手势:Edit:新手势
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-605")
@TestClass("验证编辑Gesture list中的手势为新手势")
public class EditGestureRecommendTest extends BaseTest {
    private String urlName = "facebook.com";

    public void testEditGestureRecommend() {
        uiUtil.skipWelcome();
        // 当前为"GESTURE & SONAR"界面
        uiUtil.enterGestureAndSonarSettings(false);
        // 验证：进入手势编辑界面,画板区域显示当前手势
        checkEditUI();
        // 验证：点击"Redo"按钮,画板清空 (TODO)

        // 验证：弹出"Recommended gestures"弹框
        checkRecommend();

        // 验证：进入"DRAW A GESTURE"界面,并弹出toast和提示
        checkCreatedSuccess();

        // 验证：返回"GESTURE & SONAR"界面,同时弹出toast "Matched!"
        checkMatchAndBack();

        // 验证：打开网页:facebook.com
        checkNewGesture();
    }

    private void checkNewGesture() {
        // 返回上级"DRAW A GESTURE"界面 ,画新手势
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入DRAW A GESTURE界面",
                solo.searchText(caseUtil.getTextByRId("gesture_pad_tips", -1)));
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.sleep(4 * 1000);// 不需要完全打开网页, 只要验证url即可
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        assertTrue("网页未打开", uiUtil.checkURL(urlName));
    }

    private void checkMatchAndBack() {
        // 画板区域画刚创建的新手势
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        assertTrue("Toast 'Matched!' didn't appear.",
                solo.waitForText(caseUtil.getTextByRId("drew_right_gesture_toast", -1)));
        assertTrue("未进入GESTURE & SONAR界面",
                solo.searchText(caseUtil.getTextByRId("gesture_sonar_settings_title", -1)));
    }

    private void checkCreatedSuccess() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.clickOnView(solo.getView("done"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("DRAW A GESTURE didn't appear.",
                solo.waitForText(caseUtil.getTextByRId("gesture_pad_tips", -1)));
        assertTrue("Toast 'Gesture was created successfully.' didn't appear.",
                solo.waitForText(caseUtil.getTextByRId("save_success", -1)));
        assertTrue("'Try out your new Gesture' didn't appear.",
                solo.waitForText(caseUtil.getTextByRId("gesture_have_a_try", -1)));
    }

    private void checkRecommend() {
        solo.clickOnText(caseUtil.getTextByRId("gesture_recommended_use", -1));
        solo.sleep(Res.integer.time_wait);
        assertTrue("未弹出弹框", solo.searchText(caseUtil.getTextByRId("gesture_create_dialog_title", -1)));
    }

    private void checkEditUI() {
        caseUtil.longClickAndClickPopIndex(caseUtil.getViewByText(urlName, -1, false, true, true), 0);
        solo.sleep(Res.integer.time_wait);
        assertTrue("未进入手势编辑界面",
                solo.waitForText(caseUtil.getTextByRId("gesture_create_for_url_title", -1)));
        // 显示当前手势(TODO)
    }
}