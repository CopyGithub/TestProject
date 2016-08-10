package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-803
 * <p>
 * 脚本描述: 验证video卡片视频可以播放
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-803")
@TestClass("验证video卡片视频可以播放")
public class CheckVideosGoTest extends BaseTest {

    public void testCheckVideosGo() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");
        caseUtil.slideDireciton(null, true, 0f, 1f);
        // 验证：分别点击Video模块的三个视频，跳转到对应video详情页面
        View parent = solo.getView("card_page_root");
        ArrayList<View> videos = utils.getChildren(
                caseUtil.getViewByIndex(parent, new int[] { 3, 1, 2 }), false);
        for (int i = 0; i < videos.size(); i++) {
            int size = solo.getViews().size();
            TextView name = (TextView) caseUtil.getViewByIndex(videos.get(i), new int[] { 2, 0 });
            solo.clickOnView(name);
            solo.sleep(Res.integer.time_wait);
            assertTrue("Network is not available", uiUtil.waitForWebPageFinished());
            solo.sleep(Res.integer.time_wait);
            assertTrue("第" + (i + 1) + "个未跳到对应视频详情页面", solo.getViews().size() != size);
            solo.goBack();
            solo.sleep(Res.integer.time_wait);
        }
    }
}