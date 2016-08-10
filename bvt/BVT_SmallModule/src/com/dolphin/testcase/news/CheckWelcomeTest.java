package com.dolphin.testcase.news;

import android.widget.ImageView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.ImageUtil;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: <br>
 * 1.DOLINT-341 <br>
 * 2.DOLINT-342 <br>
 * <p>
 * 脚本描述: <br>
 * 1.验证首次启动INT版本,显示用户照片切换动画 <br>
 * 2.验证首次启动Express版本,显示隐私条款开始界面 <br>
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-341,DOLINT-342")
@TestClass("验证首次启动INT/Express版本,显示相应的开始界面")
@Reinstall
public class CheckWelcomeTest extends BaseTest {
    public void testCheckWelcome() {
        if (uiUtil.isINTPackage()) {
            // 验证首次启动INT版本后的切换
            testWelcome(true);
        } else {
            // 验证首次启动Express版本后的开始界面
            testWelcome(false);
        }

    }

    private void testWelcome(boolean isInt) {
        // 验证：若为int版本，则第一步需验证显示用户照片切换动画
        if (isInt) {
            init();
            // 判断是否直接显示隐私条款开始界面(非英文环境下)(此时需跳过判断用户照片)
            if (!solo.searchText(caseUtil.getTextByRId("splash_end_screen_text2"))) {
                // 英文环境下，分别验证切换动画的三张用户照片
                checkPhotos("tutorial/drawables/tutorial_fast.jpg", new int[] { 1, 0 });
                caseUtil.slideDireciton(null, true, 1f, 1f);
                checkPhotos("tutorial/drawables/tutorial_beautiful.jpg", new int[] { 1, 0 });
                caseUtil.slideDireciton(null, true, 1f, 1f);
                checkPhotos("tutorial/drawables/tutorial_responsive.jpg", new int[] { 1, 0 });
                caseUtil.slideDireciton(null, true, 1f, 1f);
            }

        }

        // 验证：显示隐私条款开始界面
        String termsInfo = caseUtil.getTextByRId("terms_info");
        int i = termsInfo.indexOf("<br/>");
        String string = termsInfo.substring(0, i);
        String termsOfUse = caseUtil.getTextByRId("terms_of_service");
        assertTrue("未显示隐私条款开始界面", solo.searchText(string) || solo.searchText(termsOfUse));

        // 点击START
        solo.clickOnText(caseUtil.getTextByRId("splash_end_screen_text2"));
        solo.sleep(Res.integer.time_wait);

        // 验证：进入浏览器主页
        assertTrue("未进入浏览器主页",
                solo.getCurrentActivity().getClass().getSimpleName().equals("BrowserActivity"));
    }

    private void checkPhotos(String fileName, int[] index) {
        ImageUtil imageUtil = new ImageUtil(solo);
        ImageView iv = (ImageView) caseUtil.getViewByIndex(solo.getView("preloading_tips"), index);
        Boolean f = imageUtil.compareImageView(iv, false, 0, fileName);
        assertTrue("用户照片不一致", f);
    }

    private void init() {
        // 滑动页面到最左端，保证其不自动切换动画
        int i = 0;
        while (i < 2) {
            caseUtil.slideDireciton(null, true, 0f, 1f);
            i++;
            solo.sleep(Res.integer.time_wait);
        }
    }

}