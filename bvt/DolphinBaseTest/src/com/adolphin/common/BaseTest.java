package com.adolphin.common;

import com.android.common.Utils;
import com.robotium.solo.Solo;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

@SuppressWarnings("rawtypes")
public class BaseTest extends ActivityInstrumentationTestCase2 {
    protected Solo solo;
    protected Utils utils;
    protected com.java.common.Utils javaUtils = new com.java.common.Utils();
    protected UIUtil uiUtil;
    protected CaseUtil caseUtil;
    protected Instrumentation instrumentation;

    private static Class launcherActivityClass;
    static {
        try {
            launcherActivityClass = Class.forName(Res.string.launcher_activity_classname);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public BaseTest() {
        super(launcherActivityClass);
    }

    @Override
    protected void setUp() {
        instrumentation = getInstrumentation();
        solo = new Solo(instrumentation, getActivity());
        utils = new Utils(instrumentation.getTargetContext());
        caseUtil = new CaseUtil(solo);
        uiUtil = new UIUtil(solo);
    }

    @Override
    protected void tearDown() {
        solo.sleep(Res.integer.time_wait);
        solo.takeScreenshot(Res.string.take_screen);
        solo.sleep(4 * 1000);
        solo.finishOpenedActivities();
    }
}