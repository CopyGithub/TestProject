package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-801
 * <p>
 * 脚本描述: 验证video卡片的显示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-801")
@TestClass("验证video卡片的显示")
public class CheckVideoUITest extends BaseTest {
    private View parent;

    public void testCheckVideoUI() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");
        caseUtil.slideDireciton(null, true, 0f, 1f);
        // 验证：Video卡片显示如下：
        // 验证：1.卡片名称Video，右边是刷新icon（icon为顺时针方向箭头的圆圈）
        uiUtil.assertCardText("video", 3, true);

        // 验证：2.Banner视频图片（图片上有视频名称）
        parent = solo.getView("card_page_root");
        checkBanner();

        // 验证：3.三个视频，每个视频格式显示为：图片（上面显示视频时长）+视频名称+播放数量
        checkVideos();

        // 验证：4.More （字体颜色和当前主题色一致）
        uiUtil.assertCardMore(3);
    }

    private void checkVideos() {
        ArrayList<View> videos = utils.getChildren(
                caseUtil.getViewByIndex(parent, new int[] { 3, 1, 2 }), false);
        assertTrue("不是3个视频", videos.size() == 3);
        for (int i = 0; i < videos.size(); i++) {
            View image = caseUtil.getViewByIndex(videos.get(i), new int[] { 0 });
            View playTime = caseUtil.getViewByIndex(videos.get(i), new int[] { 1 });
            View name = caseUtil.getViewByIndex(videos.get(i), new int[] { 2, 0 });
            View playCount = caseUtil.getViewByIndex(videos.get(i), new int[] { 2, 1 });
            boolean flag1 = utils.ubietyOfView(image, playTime, true, false, false) != -1;
            boolean flag2 = utils.ubietyOfView(image, name, true, false, false) != -1;
            boolean flag3 = utils.ubietyOfView(image, playCount, true, false, false) != -1;
            boolean flag4 = utils.ubietyOfView(name, playCount, false, false, false) != -1;
            assertTrue("位置不正确", flag1 && flag2 && flag3 && flag4);
        }
    }

    private void checkBanner() {
        // TODO 图片上有视频名称
        // View banner = caseUtil.getViewByIndex(parent, new int[] { 3, 1, 0 });
        // assertTrue("", true);

    }
}