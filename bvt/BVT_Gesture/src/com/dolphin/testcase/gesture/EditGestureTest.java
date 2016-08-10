package com.dolphin.testcase.gesture;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-603
 * <p>
 * 脚本描述: 点击Gesture list中手势:编辑
 * 
 * @author sjguo
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-603")
@TestClass("验证Gesture list中手势可正常编辑")
public class EditGestureTest extends BaseTest {
    private String urlName = "facebook.com";

    public void testEditGesture() {
        uiUtil.skipWelcome();
        // 当前为"GESTURE & SONAR"界面
        uiUtil.enterGestureAndSonarSettings(false);
        // 验证：进入"DRAW A GESTURE"界面,并弹出toast和提示
        checkCreatedSuccess();
        // 验证：打开网页:facebook.com
        checkNewGesture();
    }

    private void checkNewGesture() {
        // 返回浏览器主页
        for (int i = 0; i < 3; i++) {
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
        uiUtil.enterGestureAndSonar(false);
        // 画新手势
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.sleep(4 * 1000);// 不需要打开,只是验证url
        caseUtil.slideDireciton(null, false, 1 / 4f, 1f);
        assertTrue("网页未打开", uiUtil.checkURL(urlName));
    }

    private void checkCreatedSuccess() {
        solo.clickOnText(urlName);
        solo.sleep(Res.integer.time_wait);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.clickOnView(solo.getView("done"));
        solo.sleep(Res.integer.time_wait);
        assertTrue("DRAW A GESTURE didn't appear.",
                caseUtil.waitForText("gesture_pad_tips", 0, true, true, 20 * 1000, false));
        assertTrue("Toast 'Gesture was created successfully.' didn't appear.",
                caseUtil.waitForText("save_success", 0, true, true, 20 * 1000, false));
        assertTrue("'Try out your new Gesture' didn't appear.",
                caseUtil.waitForText("gesture_have_a_try", 0, true, true, 20 * 1000, false));
    }
}