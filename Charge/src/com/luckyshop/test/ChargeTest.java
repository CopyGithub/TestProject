package com.luckyshop.test;

import com.basetest.Res;
import com.basetest.luckyshop.LuckyShopBaseTest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ChargeTest extends LuckyShopBaseTest {
    private final static String CHARGE_ACTIVITY = "com.aqsdk.pay.ChargeActivity";

    public void testCharge() {
        uiUtil.skipWelcome();
        View bottom = solo.getView("id/bottom_frame");
        View mybutton = caseUtil.getViewByIndex(bottom, new int[] { 0, 4 });
        solo.clickOnView(mybutton);
        solo.sleep(Res.integer.time_change_activity);
        TextView recharge = (TextView) caseUtil.getView("id/recharge");
        if (recharge == null) {
            login();
            recharge = (TextView) caseUtil.getView("id/recharge");
        }
        solo.clickOnView(recharge);
        solo.sleep(Res.integer.time_change_activity);
        assertPay();
    }

    private void login() {
        caseUtil.clickOnView("id/login_button");
        solo.sleep(Res.integer.time_change_activity);
        EditText phone = (EditText) solo.getView("id/phone_edit");
        EditText password = (EditText) solo.getView("id/password_edit");
        caseUtil.setText(phone, "10027345848");
        caseUtil.setText(password, "123456");
        TextView button = (TextView) caseUtil.getView("id/login_button");
        solo.sleep(Res.integer.time_change_activity);
        solo.clickOnView(button);
        solo.sleep(Res.integer.time_change_activity);
    }

    private void assertPay() {
        ListView payList = (ListView) caseUtil.getViewByIndex("id/payMethodListView", 0);
        Button pay = (Button) solo.getView("id/chargeButton");
        int childNumber = payList.getChildCount();
        assertTrue("没有支付方式～～～或者网络加载缓慢", childNumber > 0);
        for (int i = 0; i < childNumber; i++) {
            if (!isChargeActivity()) {
                solo.goBackToActivity(CHARGE_ACTIVITY);
                solo.sleep(Res.integer.time_change_activity);
            }
            View payCategory = payList.getChildAt(i);
            int[] location = new int[2];
            payCategory.getLocationOnScreen(location);
            if (location[1] > caseUtil.getDisplaySize(false)[1] - 400) {
                caseUtil.slideDireciton(null, false, 0.7f, 1.0f);
            }
            solo.clickOnView(payCategory);
            solo.sleep(Res.integer.time_wait);
            TextView textView = (TextView) caseUtil.getViewByIndex(payCategory,
                    new int[] { 1, 0, 0 });
            solo.clickOnView(pay);
            solo.sleep(Res.integer.time_change_activity);
            if (!isChargeActivity()) {
                Log.e("aaa", "第" + i + "行支付：" + textView.getText() + "的跳转正常");
                solo.goBack();
            } else {
                Log.e("aaa", "第" + i + "行支付：" + textView.getText() + "的跳转不正常");
            }
            solo.sleep(Res.integer.time_change_activity);
        }
    }

    private boolean isChargeActivity() {
        String currentActivityName = solo.getCurrentActivity().getLocalClassName();
        Log.e("aaa", "current name:" + currentActivityName);
        return CHARGE_ACTIVITY.equals(currentActivityName);
    }
}