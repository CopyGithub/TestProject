package com.dolphin.updatecase.speeddial;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adolphin.common.BaseTest;
import com.test.annotation.TestClass;
import com.test.annotation.TestNumber;

@TestNumber("test_010")
@TestClass("SpeedDial_满一屏")
public class AddManySpeedDialTest extends BaseTest {

    public void testAddManySpeedDial() {
        uiUtil.skipWelcome();
        String[] contentBeforeUpdate = { "新浪", "test0", "test1", "test2", "test3", "test4",
                "test5", "test6", "test7", "test8", "test9", "test10", "test11", "test12",
                "test13", "" };
        ArrayList<String> compareStrings = new ArrayList<String>();
        for (int i = 0; i < contentBeforeUpdate.length; i++) {
            compareStrings.add(contentBeforeUpdate[i]);
        }

        ArrayList<String> allContent = getSpeedContent();
        assertTrue("最后一个快速访问后面未显示+号", getSpeedContent().get(allContent.size() - 1).equals(""));
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < allContent.size(); i++) {
            if (i >= allContent.indexOf("新浪")) {
                strings.add(allContent.get(i));
            }
        }
        assertTrue("Speed dial排列顺序与升级前不一致，或者存在丢失或更改", strings.equals(compareStrings));
    }

    private ArrayList<String> getSpeedContent() {
        ArrayList<String> strings = new ArrayList<String>();
        View workspace = caseUtil.getViewByIndex(solo.getView("frame"), new int[] { 0, 1, 0 });
        if (workspace instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) workspace).getChildCount(); i++) {
                ViewGroup space = (ViewGroup) ((ViewGroup) workspace).getChildAt(i);
                ArrayList<String> childStrings = getTitles(space);
                for (int j = 0; j < childStrings.size(); j++) {
                    strings.add(childStrings.get(j));
                }
            }
        } else {
            ViewGroup space = (ViewGroup) caseUtil.getViewByIndex(workspace, new int[] { 0 });
            strings = getTitles(space);
        }
        return strings;

    }

    private ArrayList<String> getTitles(ViewGroup space) {
        View cuView = null;
        ArrayList<String> strings = new ArrayList<String>();
        for (int j = space.getChildCount() - 1; j >= 0; j--) {
            cuView = space.getChildAt(j);
            if (cuView.getClass().getSimpleName().equals("HomeShortcutIcon")) {
                TextView title = (TextView) ((ViewGroup) cuView).getChildAt(1);
                String text = title.getText().toString();
                strings.add(text);
            }
        }
        return strings;
    }
}
