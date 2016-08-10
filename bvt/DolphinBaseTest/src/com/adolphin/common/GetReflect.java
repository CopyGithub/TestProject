package com.adolphin.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import android.content.res.Resources;

import com.adolphin.common.BaseTest;

/**
 * 用于获取最新版本的R文件映射关系,使用时复制到任意Case中执行,需要在GetR方法前添加test
 * <p>
 * 结果文件放置在sdcard根目录下的aa.txt中
 * 
 * @author chchen
 * 
 */
public class GetReflect extends BaseTest {
    private File file = null;
    private FileWriter fw = null;
    private Class<?> clzString = null;
    private Resources resources = null;

    public void GetR() throws IOException, IllegalArgumentException, ClassNotFoundException,
            IllegalAccessException {
        uiUtil.skipWelcome();
        file = new File(utils.externalStorageDirectory + "/" + "aa.txt");
        if (file.exists()) {
            file.delete();
        }
        fw = new FileWriter(file);
        resources = solo.getInstrumentation().getTargetContext().getResources();
        writeFile("string", false);
        writeFile("array", true);
        writeFile("drawable", false);
        fw.close();
    }

    private void writeFile(String rName, boolean isArray) throws IOException,
            ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        fw.append("\r\n" + rName + "部分~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\r\n");
        clzString = Class.forName(caseUtil.packageName + ".R$" + rName);
        Field[] fields = clzString.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int value = 0;
            value = field.getInt(clzString);
            String valueHe = Integer.toHexString(value);
            String name = field.getName();
            String message = "";
            try {
                if (isArray) {
                    String[] array = resources.getStringArray(value);
                    for (int j = 0; j < array.length; j++) {
                        message = message + j + "." + array[j] + ";";
                    }
                } else {
                    message = resources.getString(value);
                }
            } catch (Exception e) {
                message = e.toString();
            }
            fw.append(valueHe + ":" + name + ":" + message);
            fw.append("\r\n");
        }
    }
}