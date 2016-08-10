package com.dolphin.android.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

public final class FileUtil {

    public static final int S_IRWXU = 00700; // rwx u
    public static final int S_IRUSR = 00400; // r-- u
    public static final int S_IWUSR = 00200; // -w- u
    public static final int S_IXUSR = 00100; // --x u

    public static final int S_IRWXG = 00070; // rwx g
    public static final int S_IRGRP = 00040;
    public static final int S_IWGRP = 00020;
    public static final int S_IXGRP = 00010;

    public static final int S_IRWXO = 00007; // rwx o
    public static final int S_IROTH = 00004;
    public static final int S_IWOTH = 00002;
    public static final int S_IXOTH = 00001;

    private FileUtil() {
    }

    public static int setPermissions(String file, int mode) {
        return setPermissions(file, mode, -1, -1);
    }

    public static int setPermissions(String file, int mode, int uid, int gid) {
        Class<?>[] parameterTypes = new Class<?>[] { String.class, int.class, int.class, int.class };
        Object[] parameters = new Object[] { file, mode, uid, gid };
        return (Integer) invokeStaticMethod("android.os.FileUtils", "setPermissions",
                parameterTypes, parameters);
    }

    public static Object invokeStaticMethod(String className, String methodName,
            Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Class<?> c = Class.forName(className);
            Method method = c.getMethod(methodName, parameterTypes);
            if (method != null) {
                method.setAccessible(true);
                return method.invoke(c, parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveToFile(File file, String text) {
        saveToFile(file, text, false, "utf-8");
    }

    public static void saveToFile(File file, String text, boolean append) {
        saveToFile(file, text, append, "utf-8");
    }

    public static void saveToFile(File file, String text, String encoding) {
        saveToFile(file, text, false, encoding);
    }

    public static void saveToFile(File file, String text, boolean append, String encoding) {
        if (file == null || TextUtils.isEmpty(text)) {
            return;
        }
        ensureParent(file);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file, append), encoding);
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static byte[] readFileBytes(File file) {
        FileInputStream fis = null;
        byte[] bytes = null;
        try {
            fis = new FileInputStream(file);
            bytes = IOUtils.toByteArray(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return bytes;
    }

    public static String readFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        String text = null;
        if (existFile(file)) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                text = IOUtils.toString(fis, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(fis);
            }
        }
        return text;
    }

    public static boolean existFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    public static void ensureParent(File file) {
        if (null != file) {
            final File parentFile = file.getParentFile();
            if (null != parentFile && !parentFile.exists()) {
                parentFile.mkdirs();
            }
        }
    }

}
