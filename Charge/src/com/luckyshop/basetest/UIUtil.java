package com.luckyshop.basetest;

import com.basetest.CaseUtil;
import com.basetest.Res;
import com.robotium.solo.Solo;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.os.SystemClock;

public class UIUtil {
    private Solo solo;
    private CaseUtil caseUtil;

    public UIUtil(Instrumentation instrumentation, Solo solo) {
        this.solo = solo;
        caseUtil = new CaseUtil(instrumentation, solo);
    }

    /**
     * 启动跳过开始的welcome界面
     */
    public void skipWelcome() {
        solo.sleep(Res.integer.time_change_activity);
        long endTime = SystemClock.uptimeMillis() + 30 * 1000;
        // 小于50则还在welcome界面
        int[] display = caseUtil.getDisplaySize(false);
        while (endTime > SystemClock.uptimeMillis()) {
            if (solo.getViews().size() > 50) {
                break;
            }
            for (int i = 0; i < 3; i++) {
                caseUtil.slideDireciton(null, true, 0.9f, 1.0f);
                solo.sleep(1000);
            }
            solo.clickOnScreen(display[0] / 2, display[1] / 2);
            solo.sleep(Res.integer.time_change_activity);
        }
        if (endTime <= SystemClock.uptimeMillis()) {
            Assert.assertTrue("skip welcome超时失败", false);
        }
        if (caseUtil.searchViewById("id/bg", true)) {
            caseUtil.clickOnView("id/close");
            solo.sleep(Res.integer.time_change_activity);
        }
        solo.sleep(Res.integer.time_wait);
    }
}