package com.dolphin.testcase.history;

import com.adolphin.common.BaseTest;
import com.adolphin.common.Res;
import com.test.annotation.Reinstall;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

/**
 * 脚本编号：DOLINT-1437
 * <p>
 * 脚本描述：验证删除Most visited后所有的Most visited列表数据保持一致
 * 
 * @author jwliu
 * 
 */
@Reinstall
@TestNumber(value = "DOLINT-1437")
@TestClass("验证删除Most visited后所有的Most visited列表数据保持一致")
public class DeleteMostVisitedKeepConsistentTest extends BaseTest {
    private String[] ulrs = { Res.string.url_two, "www.amazon.com", "webapps.dolphin.com/int/", };
    private String[] name = { "Amazon.com", "Web Store", Res.string.testurl_name_2 };

    public void test1Prepare() {
        uiUtil.skipWelcome();
        // 访问网页a,b,c
        uiUtil.visitURL(ulrs);
    }

    public void testDeleteMostVisitedKeepConsistentTest() {
        uiUtil.skipWelcome();
        // 首次安装Dolphin并启动
        // 预置条件：Most visited中已经存在记录c,b,a
        checkMostVisited(name);
        /**
         * 验证：进入左侧边栏的Most visited列表中将记录a删除,左侧边栏的Most visited列表中的记录a消失
         */
        assertTrue("Failed to deleted " + name[2], isDeletedMostVisitedInLeftSide(2, name[2]));
        // 收回左侧边栏，查看地址栏列表中的Most visited列表数据
        /**
         * 验证： 列表中仅显示记录c,b
         */
        assertTrue("The most visited addressbar is not consistent with leftbar most visited ",
                isConsistentInAddressbar(name, name[2]));
    }

    private boolean isConsistentInAddressbar(String names[], String name) {
        solo.goBack();
        solo.sleep(Res.integer.time_wait);
        solo.clickOnView(solo.getView("id/title_bg"));
        solo.sleep(Res.integer.time_wait);
        uiUtil.switchToTargetTabByClicking(3);
        solo.sleep(Res.integer.time_wait);
        for (String s : names) {
            if (name.equals(s)) {
                if (solo.searchText(s)) {
                    return false;
                }
            } else {
                if (!solo.searchText(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isDeletedMostVisitedInLeftSide(int index, String name) {
        caseUtil.longClickAndClickPopIndex(caseUtil.getViewByIndex("id/list", index), 3);
        solo.sleep(Res.integer.time_wait);
        return !solo.searchText(name);
    }

    private void checkMostVisited(String[] checkList) {
        // 等待10s之后出现在most visited
        solo.sleep(1000 * 10);
        uiUtil.enterSideBar(true);
        solo.sleep(Res.integer.time_wait);
        solo.clickOnText(caseUtil.getTextByRId("most_visit_folder_name"));
        solo.sleep(Res.integer.time_wait);
        for (String s : checkList) {
            assertTrue("Failed to find most visit " + s, solo.searchText(s));
            solo.sleep(Res.integer.time_wait);
        }
    }
}