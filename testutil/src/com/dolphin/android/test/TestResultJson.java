package com.dolphin.android.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

public class TestResultJson {
    private static final String JSON_CLASS_NAME = "class_name";
    private static final String JSON_FULL_NAME = "class_full_name";
    private static final String JSON_TYPE = "type";
    private static final String JSON_MESSAGE = "message";
    private static final String JSON_TIME = "time";
    private static final String JSON_STACK = "stack";

    private static final String JSON_RESULT_TYPE = "result_type";

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAILED = 1;

    private final File mJsonFile;
    private JSONObject mResultJson;

    public TestResultJson(File file) {
        mJsonFile = file;
        if (mJsonFile.exists()) {
            mJsonFile.delete();
        }
    }

    public void onStart() throws ParserConfigurationException, SAXException, IOException {
        mResultJson = createNewResultJson();
    }

    private JSONObject createNewResultJson() {
        JSONObject result = new JSONObject();
        return result;
    }

    public void onFinish() throws TransformerException {
        OutputStreamWriter writer = null;
        try {
            File parent = mJsonFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            writer = new OutputStreamWriter(new FileOutputStream(mJsonFile), "utf-8");
            writer.write(mResultJson.toString());
        } catch (UnsupportedEncodingException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    public void appendSuccess(String clsName, String testMethod, long time) {
        final JSONObject success = mResultJson;
        try {
            success.put(JSON_CLASS_NAME, clsName);
            success.put(JSON_FULL_NAME, testMethod);
            success.put(JSON_TIME, time);
            success.put(JSON_RESULT_TYPE, RESULT_SUCCESS);
        } catch (JSONException e) {
        }
    }

    public void appendFailed(String clsName, String testMethod, String message, String type,
            String stack) {
        try {
            final JSONObject obj = mResultJson;
            obj.put(JSON_CLASS_NAME, clsName);
            obj.put(JSON_FULL_NAME, testMethod);
            obj.put(JSON_MESSAGE, message);
            obj.put(JSON_TYPE, type);
            obj.put(JSON_STACK, stack);
            obj.put(JSON_RESULT_TYPE, RESULT_FAILED);
            // replace the last failed message.
        } catch (JSONException e) {
        }
    }
}
