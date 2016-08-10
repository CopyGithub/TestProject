package com.dante;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Assert;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class CheckWhenToGetSmartLocale extends UiAutomatorTestCase {
    private final String remote_locale = "remote_locale";
    private final String filePath = "/data/data/com.dolphin.browser.express.web/shared_prefs/localization";

    public void testCheckWhenToGetSmartLocale() {
        getFile();
    }

    private void writeSharedPrefsString(String key, String value) {
        FileWriter fw;
        try {
            fw = new FileWriter(getFile());
            String aa = readSharedPrefsString();
            key = "name=\"" + key + "\"";
            String map = "<map>";
            int index = aa.indexOf(key);
            if (index == -1) {
                index = aa.indexOf(map);
                fw.append("", index + map.length() + 1, 0);
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.append("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\r");
                fw.append("<map>\r");
                fw.append("</map>\r");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private String readSharedPrefsString() {
        String result = "";
        try {
            FileInputStream fis = new FileInputStream(getFile());
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            result = new String(buffer);
            fis.close();
        } catch (FileNotFoundException e) {
            Assert.assertTrue("读取文件未找到", false);
        } catch (IOException e) {
            Assert.assertTrue("读取文件异常", false);
        }
        return result;
    }
}
