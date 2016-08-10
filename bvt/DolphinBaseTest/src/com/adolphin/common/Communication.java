package com.adolphin.common;

import java.io.File;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.common.Utils;

public class Communication {
    public String classname;// 对应的case类名
    public Operator operator; // 操作者
    public Operate operate; // 操作
    public Status status; // 状态
    public int count = 1;// 同一类名的通信次数,1开始

    com.java.common.Utils javaUtils = new com.java.common.Utils();
    private String filePath = new Utils(null).externalStorageDirectory + "/backup/JSON";
    private String jsonPath = filePath + "/test.json";

    public enum Operator {
        CASE, FRAMEWORK
    }

    public enum Operate {
        RESTART, PULLDATA
    }

    public enum Status {
        WAIT, OK, FAIL, FINISHED
    }

    /**
     * 写JSON
     */
    public void writeJsonFile() {
        JSONObject jsonObject = new JSONObject();
        if (new File(jsonPath).exists()) {
            String json = javaUtils.readText(jsonPath);
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            new File(filePath).mkdir();
            javaUtils.createFileOrDir(jsonPath, false, false);
        }
        try {
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("operator", operator.toString());
            jsonObject2.put("operate", operate.toString());
            jsonObject2.put("status", status.toString());
            jsonObject2.put("count", count);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject2);
            jsonObject.put(classname, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        javaUtils.writeText(jsonObject.toString(), jsonPath, false);
    }

    /**
     * 读JSON
     */
    public void readJsonValues() {
        String json = javaUtils.readText(jsonPath);
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jsonArray = jObject.getJSONArray(classname);
            operator = Operator.valueOf(jsonArray.getJSONObject(0).getString("operator"));
            operate = Operate.valueOf(jsonArray.getJSONObject(0).getString("operate"));
            status = Status.valueOf(jsonArray.getJSONObject(0).getString("status"));
            count = jsonArray.getJSONObject(0).getInt("count");
        } catch (JSONException e) {
            // Nothings
        }
    }

    public boolean isJsonExist() {
        return new File(jsonPath).exists();
    }

    /**
     * case与框架进行通信
     */
    public void communicateWithFramework() {
        if (Operator.FRAMEWORK.equals(operator)) {
            // 需框架进行操作
            writeJsonFile();
            while (true) {
                javaUtils.sleep(1000);
                readJsonValues();
                if (Status.OK.equals(status)) {
                    break;
                }
                if (Status.FAIL.equals(status)) {
                    Assert.assertTrue("操作未成功", false);
                }
            }
        } else {
            // 需case进行操作
            if (isJsonExist()) {
                Communication communication2 = new Communication();
                communication2.classname = classname;
                communication2.readJsonValues();
                if (count == communication2.count) {
                    operate = communication2.operate;
                    status = communication2.status;
                    return;
                }
            }
            writeJsonFile();
            while (true) {
                javaUtils.sleep(1000);
                readJsonValues();
                if (Status.OK.equals(status)) {
                    break;
                }
            }
        }
    }
}
