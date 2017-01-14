package com.basetest;

import com.android.common.Utils;
import com.robotium.solo.Solo;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

public class BaseTest extends ActivityInstrumentationTestCase2 {
    protected Solo solo;
    protected Utils utils;
    protected com.java.common.Utils javaUtils = new com.java.common.Utils();
    protected CaseUtil caseUtil;
    protected Instrumentation instrumentation;

    private static Class<?> launcherActivityClass;
    static {
        try {
            launcherActivityClass = Class.forName("");
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
    }

    @Override
    protected void tearDown() {
        solo.finishOpenedActivities();
    }
}
