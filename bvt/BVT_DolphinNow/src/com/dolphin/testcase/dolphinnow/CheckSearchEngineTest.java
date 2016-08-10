package com.dolphin.testcase.dolphinnow;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.robotium.solo.Solo;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号: DOLINT-788
 * <p>
 * 脚本描述: 验证搜索引擎能正常切换
 * 
 * @author sjguo
 * 
 */
@TestNumber(value = "DOLINT-788")
@TestClass("验证搜索引擎能正常切换")
public class CheckSearchEngineTest extends BaseTest {

    public void testCheckSwitchEngine() {
        if (uiUtil.isINTPackage()) {
            return;
        }
        uiUtil.skipWelcome();
        uiUtil.setSmartLocale("ru_RU");

        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(Res.integer.time_wait);
        caseUtil.slideDireciton(null, true, 0f, 1f);
        /**
         * 搜索引擎列表里面引擎的排列为4*N，引擎的图标显示清晰，界面UI正常 <br>
         * 与地址栏搜索引擎列表一致
         */
        checkEngineList(false);

        // 验证：引擎能正常切换
        checkSwitchEngine("Google");

        // 验证：横屏对功能无影响
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.sleep(Res.integer.time_wait);
        checkEngineList(true);
        checkSwitchEngine("Yahoo");
    }

    private void checkSwitchEngine(String string) {
        solo.clickOnText(string);
        solo.sleep(Res.integer.time_wait);
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        // 获得当前搜索引擎
        assertTrue("未正确切换搜索引擎", string.equalsIgnoreCase(getCurrentEngine()));
    }

    // 验证：引擎列表
    private void checkEngineList(boolean isLandscape) {
        View parent = solo.getView("card_page_root");
        View engine = caseUtil.getViewByIndex(parent, new int[] { 0, 0, 0 });
        solo.clickOnView(engine);
        solo.sleep(Res.integer.time_wait);

        ArrayList<View> views = utils.getChildren(solo.getView("search_engine_gridview"), false);
        if (isLandscape) {
            utils.ubietyOfViews(views, 5, true, true, true);
        } else {
            utils.ubietyOfViews(views, 4, true, true, true);
        }
        uiUtil.compareSearchEngineListWithOrigin(views, true);
    }

    /**
     * 通过XML获取当前搜索引擎
     * 
     * @return
     */
    private String getCurrentEngine() {
        String origin = new com.java.common.Utils().readText("search/search_category");
        String currentEngine = "";
        try {
            JSONArray ja = new JSONArray(origin);
            assertFalse("没有正确获取到预置数据,可能数据结构有变动", ja.length() == 0);
            JSONObject jObject = ja.getJSONObject(0);
            currentEngine = jObject.getString("default_engine");

        } catch (JSONException e) {
            // Nothings
        }
        return currentEngine;
    }

}