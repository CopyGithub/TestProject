package com.framework.common;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.java.common.Utils;

public class UploadData {
    private static final String UPLOAD = "上传所有数据";
    private static final String UNUPLOAD = "不上传";

    // public boolean selectUploadData(String url, String param) {
    // Utils javaUtils = new Utils();
    // ArrayList<String> selectList = new ArrayList<String>();
    // selectList.add(UPLOAD);
    // selectList.add(UNUPLOAD);
    // String select = javaUtils.selectInput(selectList, false);
    // String result = "";
    // if (UPLOAD.equals(select)) {
    // result = javaUtils.sendPost(url, param);
    // try {
    // JSONObject jsonObject = new JSONObject(result);
    // if (jsonObject.getInt("returnCode") == 200) {
    // System.out.println("上传成功");
    // return true;
    // } else {
    // System.out.println("上传失败:" + result);
    // }
    // } catch (JSONException e) {
    // }
    // }
    // return false;
    // }
}
