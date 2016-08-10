package com.framework.testcase;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.framework.common.Res;
import com.framework.common.Utils;

public class Communication {
    protected String classname;// 对应的case类名
    protected Operator operator; // 操作者
    protected Operate operate; // 操作
    protected Status status; // 状态
    protected int count;// 同一类名的通信次数
    protected com.java.common.Utils javaUtils = new com.java.common.Utils();
    protected String device;

    enum Operator {
        CASE, FRAMEWORK
    }

    enum Operate {
        RESTART, PULLDATA
    }

    enum Status {
        WAIT, OK, FAIL, FINISHED
    }

    /**
     * 读JSON
     * 
     * @param adb
     */
    protected void readJsonValues(String adb) {
        String json = javaUtils.readText(getJsonPath(adb));
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

    /**
     * 改写JSON
     * 
     * @param adb
     */
    protected void setJsonValues(String adb) {
        String jsonPath = getJsonPath(adb);
        String json = javaUtils.readText(jsonPath);
        JSONObject jObject = new JSONObject(json);
        try {
            JSONArray jsonArray = jObject.getJSONArray(classname);
            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
            jsonObject2.put("operator", operator.toString());
            jsonObject2.put("operate", operate.toString());
            jsonObject2.put("status", status.toString());
            jsonObject2.put("count", count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        javaUtils.writeText(jObject.toString(), jsonPath, false);
    }

    protected String getJsonPath(String adb) {
        return getJsonFolder(adb) + File.separator + "test.json";
    }

    protected String getJsonFolder(String adb) {
        device = adb.substring(7).trim();
        return Res.DIR_PC_JSON + device;
    }

    /**
     * 处理case的通信
     * 
     * @param adb
     */
    protected void communicateWithCase(String adb) {
        String jsonPath = getJsonPath(adb);
        if (new File(jsonPath).exists()) {
            readJsonValues(adb);
            if ((Status.WAIT.equals(status))) {
                System.out.println("存在正确的待处理的JSON！！！！！！！！！！！！！！！！！！！");
                if (Operator.FRAMEWORK.equals(operator)) {
                    // 框架处理操作
                    switch (operate) {
                    case PULLDATA:
                        // TODO
                        System.out.println("pullData");
                        break;
                    default:
                        break;
                    }
                    boolean isOperateSuccess = true;
                    if (isOperateSuccess) {
                        // status从WAIT改为OK
                        status = Status.OK;
                    } else {
                        status = Status.FAIL;
                    }
                } else {
                    status = Status.OK;
                }
                setJsonValues(adb);
                pushJsonToPhone(adb);
            }
        }
    }

    /**
     * 将JSON文件push到手机
     * 
     * @param adb
     */
    protected void pushJsonToPhone(String adb) {
        ArrayList<String> result = new ArrayList<String>();
        String cmd = new Utils().cmdPush(adb, getJsonFolder(adb), Res.DIR_PHONE_JSON);
        javaUtils.runtimeExec(result, cmd, 30);
        javaUtils.printArrayString(result);
    }

    /**
     * 将JSON文件pull到电脑
     * 
     * @param adb
     */
    protected void pullJsonToPC(String adb) {
        ArrayList<String> result = new ArrayList<String>();
        String cmd = new Utils().cmdPull(adb, Res.DIR_PHONE_JSON, getJsonFolder(adb));
        javaUtils.runtimeExec(result, cmd, 30);
    }

    /**
     * 删除电脑端,手机端的JSON文件
     * 
     * @param adb
     */
    protected void deleteJsonFiles(String adb) {
        // 删除JSON(电脑端)
        javaUtils.deleteFileOrDir(getJsonFolder(adb));
        // 删除JSON(手机端)
        ArrayList<String> result = new ArrayList<String>();
        javaUtils.runtimeExec(result, new Utils().cmdDelete(adb, Res.DIR_PHONE_JSON), 30);
        javaUtils.printArrayString(result);
    }
}
