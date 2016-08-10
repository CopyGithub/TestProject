package com.framework.performance;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.java.common.Utils;

class PerformanceReport {
    protected UUID uuid = UUID.randomUUID();
    protected String productCode = "";// 产品代号, 既apk包名
    protected String productName = "";// 产品名, 既apk的名字
    protected int versionCode = 0;// 版本号
    protected String versionName = "";// 版本名
    protected String extraInfo = "";// 额外的信息, 如是否带内核
    protected String[] performanceType;// 测试的性能点
    protected String caseName;// 测试实例名
    protected String caseExtraInfo = "";// 具体实例的额外信息
    protected String[] deviceInfo;// 设备信息组,和Res中的设备信息对应
    protected int num = 0;// 测试次数
    // 测试结果
    protected ArrayList<Data> datas = new ArrayList<Data>();// 原始值及处理的相关数据
    protected float averageValue = 0;// 算数平均值
    protected float median = 0f;// 中位数
    protected float peakVal = 0f;// 峰值

    private final Utils javaUtils = new Utils();

    /**
     * 将结果报告转化为json格式
     * 
     * @return
     */
    protected JSONObject convertJSONArray() {
        JSONObject root = new JSONObject();
        root.put("productCode", productCode);
        root.put("productName", productName);
        root.put("versionCode", versionCode);
        root.put("versionName", versionName);
        root.put("extraInfo", extraInfo);
        root.put("testTypeCode", performanceType[1]);

        JSONArray testInfoArray = new JSONArray();
        JSONObject testInfo = new JSONObject();
        testInfo.put("testModelName", caseName);
        testInfo.put("deviceType", deviceInfo[2]);
        testInfo.put("uuid", uuid);
        testInfo.put("caseExtraInfo", caseExtraInfo);
        testInfo.put("averageValue", String.valueOf(averageValue));
        testInfo.put("median", String.valueOf(median));

        JSONArray testDataArray = new JSONArray();
        for (Data data : datas) {
            JSONObject testData = new JSONObject();

            JSONArray dataArray = new JSONArray();
            for (int[] originalData : data.originalDatas) {
                JSONObject dataObject = new JSONObject();
                dataObject.put("time", originalData[0]);
                dataObject.put("dataValue", originalData[1]);
                dataArray.put(dataObject);
            }
            testData.put("testTimes", data.testNo);
            testData.put("data", dataArray);
            testDataArray.put(testData);
        }

        testInfo.put("testData", testDataArray);
        testInfoArray.put(testInfo);
        root.put("testInfo", testInfoArray);
        return root;
    }

    /**
     * 整理指定目录下的所有txt格式的json文件
     * 
     * @param dir
     *            指定的目录
     * @return 返回符合上传服务器标准的json格式
     */
    protected String tidyJson(String dir) {
        JSONObject root = new JSONObject();
        JSONArray productArray = new JSONArray();
        File dirFile = new File(dir);
        for (File file : dirFile.listFiles()) {
            if (!file.getName().endsWith(".txt")) {
                continue;
            }
            String jsonString = javaUtils.readText(file.getAbsolutePath());
            JSONObject jsonObject = new JSONObject(jsonString);
            if (productArray.length() == 0) {
                productArray.put(jsonObject);
                continue;
            }
            for (int i = 0; i < productArray.length(); i++) {
                JSONObject product = productArray.getJSONObject(i);
                if (product.getString("productCode").equals(jsonObject.getString("productCode"))
                        && product.getInt("versionCode") == jsonObject.getInt("versionCode")
                        && product.getString("extraInfo").equals(jsonObject.getString("extraInfo"))
                        && product.getString("testTypeCode")
                                .equals(jsonObject.getString("testTypeCode"))) {
                    JSONArray testInfoArray = product.getJSONArray("testInfo");
                    testInfoArray.put(jsonObject.getJSONArray("testInfo").getJSONObject(0));
                } else {
                    productArray.put(jsonObject);
                }
            }
        }
        root.put("otherData", productArray);
        return root.toString();
    }
}
