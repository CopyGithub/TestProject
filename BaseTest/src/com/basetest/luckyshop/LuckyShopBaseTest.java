package com.basetest.luckyshop;

import com.android.common.Utils;
import com.basetest.CaseUtil;
import com.basetest.Res;
import com.robotium.solo.Solo;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

@SuppressWarnings("rawtypes")
public class LuckyShopBaseTest extends ActivityInstrumentationTestCase2 {
    protected Solo solo;
    protected Utils utils;
    protected CaseUtil caseUtil;
    protected UIUtil uiUtil;
    protected com.java.common.Utils javaUtils = new com.java.common.Utils();
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
    public LuckyShopBaseTest() {
        super(launcherActivityClass);
    }

    @Override
    protected void setUp() {
        instrumentation = getInstrumentation();
        solo = new Solo(instrumentation, getActivity());
        utils = new Utils(instrumentation.getTargetContext());
        caseUtil = new CaseUtil(instrumentation, solo);
        uiUtil = new UIUtil(instrumentation, solo);
    }

    @Override
    protected void tearDown() {
        solo.finishOpenedActivities();
    }
}
