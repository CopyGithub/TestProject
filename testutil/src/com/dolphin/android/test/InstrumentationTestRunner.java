package com.dolphin.android.test;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

/*
 * Copyright (C) 2007 Hugo Visser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This test runner creates a TEST-all.xml in the files directory of the
 * application under test. The output is compatible with that of the junitreport
 * ant task, the format that is understood by Hudson. Currently this
 * implementation does not implement the all aspects of the junitreport format,
 * but enough for Hudson to parse the test results.
 */
public class InstrumentationTestRunner extends android.test.InstrumentationTestRunner {

    private static final String TAG = InstrumentationTestRunner.class.getSimpleName();

    private static final String JUNIT_XML_FILE = "TEST-all.xml";

    private long mTestStarted;

    private TestResultJson mResultXmlEdit;

    @Override
    public void onCreate(Bundle arguments) {
        Log.v(TAG, "onCreate");
        mResultXmlEdit = new TestResultJson(getOutputFile());
        super.onCreate(arguments);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        try {
            mResultXmlEdit.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    public File getOutputFile() {
        return new File(Environment.getExternalStorageDirectory(), JUNIT_XML_FILE);
    }

    @Override
    public void sendStatus(int resultCode, Bundle results) {
        Log.v(TAG, "sendStatus");
        super.sendStatus(resultCode, results);
        switch (resultCode) {
        case REPORT_VALUE_RESULT_ERROR:
        case REPORT_VALUE_RESULT_FAILURE:
        case REPORT_VALUE_RESULT_OK:
            try {
                recordTestResult(resultCode, results);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            break;
        case REPORT_VALUE_RESULT_START:
            recordTestStart(results);
        default:
            break;
        }
    }

    private void recordTestStart(Bundle results) {
        mTestStarted = System.currentTimeMillis();
    }

    private void recordTestResult(int resultCode, Bundle results) throws IOException {
        long time = System.currentTimeMillis() - mTestStarted;
        String className = results.getString(REPORT_KEY_NAME_CLASS);
        String testMethod = results.getString(REPORT_KEY_NAME_TEST);
        String stack = results.getString(REPORT_KEY_STACK);
        if (resultCode != REPORT_VALUE_RESULT_OK) {
            String message = "";
            String reason = "";
            String stackMessage = "";
            if (stack != null) {
                reason = stack.substring(0, stack.indexOf('\n'));
                int index = reason.indexOf(':');
                if (index > -1) {
                    message = reason.substring(index + 1);
                    reason = reason.substring(0, index);
                }
                stackMessage = stack;
            }
            mResultXmlEdit.appendFailed(className, testMethod, message, reason, stackMessage);
        } else {
            mResultXmlEdit.appendSuccess(className, testMethod, time);
        }
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        Log.v(TAG, "finish");
        try {
            mResultXmlEdit.onFinish();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        super.finish(resultCode, results);
    }
}
