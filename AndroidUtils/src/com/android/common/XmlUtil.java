package com.android.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class XmlUtil {
    private Context context;

    public XmlUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取SharedPreferences
     * 
     * @param fileName
     * @return
     */
    private SharedPreferences getPreferences(String fileName) {
        return context.getSharedPreferences(fileName, Context.MODE_APPEND);
    }

    /**
     * 获取特定文件特定key的boolean值
     * 
     * @param fileName
     *            sharePreferences文件名
     * @param key
     *            键值对的key值
     * @return 返回获取的值, 默认值为{@code false}
     */
    public boolean getSharedPreferencesValues(String fileName, String key) {
        return getPreferences(fileName).getBoolean(key, false);
    }

    /**
     * 获取特定文件特定key的int值
     * 
     * @param fileName
     *            sharePreferences文件名
     * @param key
     *            键值对的key值
     * @return 返回获取的值, 默认值为{@code -1}
     */
    public int getSharedPreferencesValuesInt(String fileName, String key) {
        return getPreferences(fileName).getInt(key, -1);
    }

    /**
     * 获取特定文件特定key的String值
     * 
     * @param fileName
     *            sharePreferences文件名
     * @param key
     *            键值对的key值
     * @return 返回获取的值, 默认值为{@code ""}
     */
    public String getSharedPreferencesValuesString(String fileName, String key) {
        return getPreferences(fileName).getString(key, "");
    }

    /**
     * 修改boolean值的SharePreferences内容
     * 
     * @param fileName
     *            sharePreferences文件名
     * @param key
     *            键值对的key值
     * @param value
     *            设定的值
     * @return 是否设置成功{@code true} 表示成功,{@code false} 表示失败
     */
    public boolean setSharedPreferencesValues(String fileName, String key, boolean value) {
        Editor editor = getPreferences(fileName).edit();
        editor.putBoolean(key, value);
        editor.commit();
        return getSharedPreferencesValues(fileName, key) == value;
    }

    /**
     * 修改int值的SharePreferences内容
     * 
     * @param fileName
     *            sharePreferences文件名
     * @param key
     *            键值对的key值
     * @param value
     *            设定的值
     * @return 是否设置成功{@code true} 表示成功,{@code false} 表示失败
     */
    public boolean setSharedPreferencesValuesInt(String fileName, String key, int value) {
        Editor editor = getPreferences(fileName).edit();
        editor.putInt(key, value);
        editor.commit();
        return getSharedPreferencesValuesInt(fileName, key) == value;
    }

    /**
     * 修改String值的SharePreferences内容
     * 
     * @param fileName
     * @param key
     * @param value
     * @return
     */
    public boolean setSharedPreferencesValuesString(String fileName, String key, String value) {
        Editor editor = getPreferences(fileName).edit();
        editor.putString(key, value);
        editor.commit();
        return getSharedPreferencesValuesString(fileName, key) == value;
    }
}
