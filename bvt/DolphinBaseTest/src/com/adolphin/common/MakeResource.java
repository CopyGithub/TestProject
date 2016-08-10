package com.adolphin.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import com.adolphin.common.BaseTest;

/**
 * 用于获取最新版本的R文件映射关系,并生成RDolphin.java文件
 * <p>
 * 使用时复制到任意Case中执行,需要在MakeR方法前添加test
 * <p>
 * 结果文件放置在sdcard根目录下的RDolphin.java中
 * 
 * @author chchen
 * 
 */
public class MakeResource extends BaseTest {
    private File file = null;
    private FileWriter fw = null;
    private Class<?> clzString = null;

    public void MakeR() throws IOException, IllegalArgumentException, ClassNotFoundException,
            IllegalAccessException {
        uiUtil.skipWelcome();
        file = new File(utils.externalStorageDirectory + "/" + "RDolphin.java");
        if (file.exists()) {
            file.delete();
        }
        fw = new FileWriter(file);
        writeRFormatBefore();
        writeFile("drawable", false);
        writeRFormatAfter();
        fw.close();
    }

    private void writeFile(String rName, boolean isArray) throws IOException,
            ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        clzString = Class.forName(caseUtil.packageName + ".R$" + rName);
        Field[] fields = clzString.getDeclaredFields();
        fw.append("\r\n");
        fw.append("    public static final class " + rName + " {\r\n");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            int value = 0;
            value = field.getInt(clzString);
            String valueHe = Integer.toHexString(value);
            String name = field.getName();
            fw.append("        public final static int " + name + " = 0x" + valueHe + ";\r\n");
        }
        fw.append("    }\r\n");
    }

    private void writeRFormatBefore() throws IOException {
        fw.append("package com.adolphin.common;\r\n");
        fw.append("\r\n");
        fw.append("public final class RDolphin {\r\n");
    }

    private void writeRFormatAfter() throws IOException {
        fw.append("}");
    }
}