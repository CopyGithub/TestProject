package com.dolphin.testcase.gesture;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;

/**
 * 脚本编号: DOLINT-599
 * <p>
 * 脚本描述: 创建新手势:不与预置手势重复
 * <p>
 * 未完成
 * 
 * @author sjguo
 * 
 */

public class AddGestureAndRedoTest extends BaseTest {
    private String urlName = Res.string.url_downloadtest;

    public void testAddGestureAndRedo() {
        uiUtil.skipWelcome();
        init();
        // 验证画板区域(TODO)

        // 1.点击"Add"按钮 -> 创建Gesture界面点击"Redo"按钮
        // 验证：画板区域内容清除,显示空白

        // 2.画任意手势(如:1)
        // 验证：画板区域显示所画手势轨迹1

        // 3.点击"Redo"按钮
        // 验证：画板区域清空

        // 4.画任意手势（如:2）
        // 验证：画板区域显示所画手势轨迹2

    }

    private void init() {
        // 已打开任意网页界面
        uiUtil.visitUrl(urlName);
        solo.sleep(Res.integer.time_wait);
        // 当前为"GESTURE & SONAR"界面
        uiUtil.enterGestureAndSonarSettings(false);
    }
}
