package com.dolphin.testcase.gesture;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-598
 * <p>
 * 脚本描述: 创建新手势:不与预置手势重复:创建成功
 * 
 * @author sjguo
 * 
 */

@Reinstall
@TestNumber(value = "DOLINT-598")
@TestClass("验证自定义新手势创建成功")
public class AddGestureTest extends BaseTest {
    private String urlName = Res.string.url_downloadtest;

    public void testAddGesture() {
        uiUtil.skipWelcome();
        init();
        // 验证：跳转到手势创建成功界面,界面显示正常
        checkCreatedSuccess();
        // 验证：手势创建成功界面关闭,返回"GESTURE & SONAR"界面,同时弹出toast "Matched!"
        checkMatchAndBack();
        // 验证：DRAW A GESTURE界面关闭,网页打开,同时弹出toast
        checkNewGesture();
    }

    private void checkNewGesture() {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        assertTrue("未关闭DRAW A GESTURE界面",
                !caseUtil.searchText("gesture_sonar_settings_title", 0, true, true, false));
        assertTrue("Toast didn't appear.", solo.waitForText("download test"));
        assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
        assertTrue("网页未打开", uiUtil.checkURL(urlName));
    }

    private void checkMatchAndBack() {
        // 画板区域画刚创建的新手势
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        assertTrue("Toast 'Matched!' didn't appear.",
                caseUtil.waitForText("drew_right_gesture_toast", 0, true, true, 20 * 1000, false));
        assertTrue("未进入GESTURE & SONAR界面", caseUtil.waitForText("gesture_sonar_settings_title", 0,
                true, true, 20 * 1000, false));
    }

    private void checkCreatedSuccess() {
        // 点击"Add"按钮 -> 画任意手势（如:O),点击"Done"按钮
        solo.clickOnView(solo.getView("ok"));
        solo.sleep(Res.integer.time_wait);
        uiUtil.drawGesture(caseUtil.getViewByClassName("GestureOverlayView", 0, false), "star");
        solo.clickOnView(solo.getView("done"));
        solo.sleep(Res.integer.time_wait);

        // 验证：跳转到手势创建成功界面,同时弹出toast "Gesture was created successfully."
        assertTrue("Failed to gesture created successfully screen.",
                caseUtil.searchText("save_success", 0, true, true, false));// 验证界面的文本来判断是否进入指定界面
        assertTrue("Toast 'Gesture was created successfully.' didn't appear.",
                caseUtil.waitForText("save_success", 0, true, true, 20 * 1000, false));
        // 验证：界面中显示标题:"DRAW A GESTURE"(居中显示)
        assertTrue("DRAW A GESTURE didn't appear.",
                caseUtil.waitForText("gesture_pad_tips", 0, true, true, 20 * 1000, false));
        // 验证：界面中显示内容Gesture was created successfully.和Try out your new Gesture
        assertTrue("'Gesture was created successfully.' didn't appear.",
                caseUtil.waitForText("save_success", 0, true, true, 20 * 1000, false));
        assertTrue("'Try out your new Gesture' didn't appear.",
                caseUtil.waitForText("gesture_have_a_try", 0, true, true, 20 * 1000, false));
        // 验证：画板区域中动态显示新手势轨迹O(TODO)
    }

    private void init() {
        // 已打开任意网页界面
        uiUtil.visitUrl(urlName);
        solo.sleep(Res.integer.time_wait);
        // 当前为"GESTURE & SONAR"界面
        uiUtil.enterGestureAndSonarSettings(false);
    }
}
