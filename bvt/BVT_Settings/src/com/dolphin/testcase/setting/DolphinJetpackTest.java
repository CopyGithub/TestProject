package com.dolphin.testcase.setting;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Communication;
import com.adolphin.common.Res;
import com.adolphin.common.Communication.Operate;
import com.adolphin.common.Communication.Operator;
import com.adolphin.common.Communication.Status;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber(value = "DOLINT-9999")
@TestClass("验证DolphinJetpack关闭")
public class DolphinJetpackTest extends BaseTest {
    public void testDolphinJetpack() {
        uiUtil.skipWelcome();
        Communication communication = new Communication();
        communication.classname = this.getClass().getSimpleName();
        communication.operator = Operator.CASE;
        communication.operate = Operate.RESTART;
        communication.status = Status.WAIT;
        communication.communicateWithFramework();
        if (Status.OK.equals(communication.status)) {
            uiUtil.enterSetting(false);
            solo.clickOnText("Dolphin Jetpack");
            solo.sleep(Res.integer.time_wait);
            solo.clickOnButton(1);
        }
        uiUtil.enterSetting(false);
        uiUtil.assertSearchText("use_dolphin_webkit", null, -1, true, false, 2, new int[] { 1, 0 });
    }
}
