package com.dolphin.testcase.dolphinnow;

import android.view.View;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 1.脚本编号: DOLINT-802 <br>
 * 2.脚本编号: DOLINT-804 <br>
 * <p>
 * 1.脚本描述: 验证点击视频跳转到畅游http://ru.hotube.tv/页面 <br>
 * 2.脚本描述: 验证点击more跳转到视频主页 <br>
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-802,DOLINT-804")
@TestClass("验证点击视频/More,跳转到畅游http://ru.hotube.tv/页面")
public class CheckVideoBannerAndMoreGoTest extends BaseTest {

    public void testCheckVideoBannerAndMoreGo() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        // 验证：点击Video模块第一个banner图片，是否到视频主页
        View parent = solo.getView("card_page_root");
        View banner = caseUtil.getViewByIndex(parent, new int[] { 3, 1, 0 });
        solo.clickOnView(banner);
        solo.sleep(Res.integer.time_wait);
        checkUI();
        // 验证：点击Video模块的More，是否到视频主页
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        TextView more = (TextView) caseUtil.getViewByIndex(parent, new int[] { 3, 3, 1 });
        solo.clickOnView(more);
        solo.sleep(Res.integer.time_wait);
        checkUI();
    }

    private void checkUI() {
        /**
         * 跳转到http://ru.hotube.tv/页面的Топ видео栏， <br>
         * 第一张大图片显示主推video， <br>
         * 图片上左边显示video名称，右边显示video时长， <br>
         * 下面都是左边小图片+右边的video名称和播放数量 <br>
         */
        String urlName = "http://ru.hotube.tv/";
        assertTrue("未跳转到视频主页", uiUtil.checkURL(urlName));
        // 页面内容(TODO)
    }
}