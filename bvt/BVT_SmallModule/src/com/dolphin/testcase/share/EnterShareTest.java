package com.dolphin.testcase.share;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-480
 * <p>
 * 脚本描述: 验证可以通过各种入口进入Share界面
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-480")
@TestClass("验证可以通过各种入口进入Share界面")
public class EnterShareTest extends BaseTest {

    public void testEnterShare() {
        uiUtil.skipWelcome();

        // 进入Share界面
        uiUtil.enterShare(1);

        // 关闭Share界面,Share界面关闭
        checkShareClosed();

        // 进入Share界面
        uiUtil.enterShare(2);

        // 关闭Share界面,Share界面关闭
        checkShareClosed();

        // 进入Share界面
        uiUtil.enterShare(3);
    }

    private void checkShareClosed() {
        solo.clickOnView(solo.getView("button2"));
        assertTrue("'Share' interface is not closed...", solo.waitForDialogToClose());
    }
}