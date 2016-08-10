
package com.dolphin.testcase.share;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-482
 * <p>
 * 脚本描述: 验证Share界面可以正确关闭
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-482")
@TestClass("验证Share界面可以正确关闭")
public class CloseShareTest extends BaseTest {

    public void testCloseShare() {
        uiUtil.skipWelcome();
        // 进入Share界面
        uiUtil.enterShare(2);
        // 验证：Share界面关闭, 返回到进入Share之前的界面
        closeShare(true);

        // 再次进入Share界面
        uiUtil.enterShare(2);
        // 验证：Share界面关闭, 返回到进入Share之前的界面
        closeShare(false);

    }

    private void closeShare(boolean isClickCancel) {
        if (isClickCancel) {
            solo.clickOnView(solo.getView("button2"));
        } else {
            solo.goBack();
        }
        assertTrue("'Share' interface is not closed...", solo.waitForDialogToClose());
    }

}