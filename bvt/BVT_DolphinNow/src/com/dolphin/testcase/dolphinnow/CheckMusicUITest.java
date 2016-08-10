package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-811
 * <p>
 * 脚本描述: 验证Music卡片的展示
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-811")
@TestClass("验证Music卡片的展示")
public class CheckMusicUITest extends BaseTest {
    private View parent;

    public void testCheckMusicUI() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        caseUtil.slideDireciton(null, true, 0f, 1f);
        solo.sleep(Res.integer.time_wait);

        // 验证：Music卡片显示如下：
        // 验证：1.卡片名称Music，右边是刷新icon（icon为顺时针方向箭头的圆圈）
        uiUtil.assertCardText("music", 2, true);

        // 验证：2.5个音乐文件，每个音乐文件格式显示为：排名+歌手图片+音乐名+歌手名+播放icon+下载icon
        // （若音乐/歌手名太长，则结尾用...代替）(TODO)
        parent = solo.getView("card_page_root");
        checkMusicFiles();
        // 验证：3.Top（一团火的icon）和Collection(音乐符的icon)
        checkTopIcons();

        // 验证：4.More （字体颜色和当前主题色一致）
        uiUtil.assertCardMore(2);
    }

    private void checkTopIcons() {
        // TODO
    }

    private void checkMusicFiles() {
        ArrayList<View> musics = utils.getChildren(
                caseUtil.getViewByIndex(parent, new int[] { 2, 1, 0 }), false);
        assertTrue("不是5个音乐文件", musics.size() == 5);
        for (int i = 0; i < musics.size(); i++) {
            View image = caseUtil.getViewByIndex(musics.get(i), new int[] { 0 });
            View name = caseUtil.getViewByIndex(musics.get(i), new int[] { 1, 0 });
            View singer = caseUtil.getViewByIndex(musics.get(i), new int[] { 1, 1 });
            ImageView download = (ImageView) caseUtil
                    .getViewByIndex(musics.get(i), new int[] { 2 });
            ImageView play = (ImageView) caseUtil.getViewByIndex(musics.get(i), new int[] { 3 });
            ArrayList<View> views = new ArrayList<View>();
            views.add(image);
            views.add(name);
            views.add(download);
            views.add(play);
            utils.ubietyOfViews(views, 0, false, false, false);
            assertTrue("位置不正确", utils.ubietyOfView(name, singer, false, false, false) != -1);
            // 下载icon和播放icon (TODO)
        }
    }
}