package com.dante;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

import junit.framework.Assert;

public class Constants {
    public static String PACKAGE_NAME = "";

    static {
        String content = "";
        Log.i("pckname", "sd is " + Environment.getExternalStorageDirectory().toString());
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/pckName.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            try {
                while((line = br.readLine()) != null) {
                    content = content + line;
                }
            } catch (IOException e) {
                Assert.assertTrue("Failed to read file.", false);
            }
        } catch (FileNotFoundException e) {
            Assert.assertTrue(Environment.getExternalStorageDirectory().toString() + "/pckName.txt not found!", false);
        }
        try {
            JSONObject obj = new JSONObject(content);
            String name = (String)obj.get("packageName");
            PACKAGE_NAME = name;
        } catch (JSONException e) {
            PACKAGE_NAME = "mobi.mgeek.TunnyBrowser";
        }
    }
}
