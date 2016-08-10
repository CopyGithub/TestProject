package com.framework.common;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.dexbacked.value.DexBackedArrayEncodedValue;
import org.jf.dexlib2.dexbacked.value.DexBackedStringEncodedValue;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableBooleanEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableIntEncodedValue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ApkInfo {
    public int apkId = Math.abs(new Random().nextInt());;// 唯一ID,随机生成
    public String filePath = "";// 文件路径
    public long fileSize;// 文件大小
    // APK公有属性
    public String appName = "";// 程序名(默认)
    public int versionCode;// 版本号
    public String versionName = "";// 版本名
    public String packageName = "";// 包名
    public int minSdkVersion;// 最低适配版本
    public int targetSdkVersion;// 目标适配版本
    public ArrayList<String> usesPermissions = new ArrayList<String>();// 用户权限
    public String launchableActivity = "";
    // 测试APK特有属性
    public String targetPackageName;// 测试apk的目标apk包名
    public String instrumentName;// 测试apk的测试框架名
    // APK类信息
    public ArrayList<TestClass> classes = new ArrayList<TestClass>();
    private com.java.common.Utils javaUtils = new com.java.common.Utils();

    public ApkInfo(String filePath) throws Exception {
        this.filePath = filePath;
        File file = new File(filePath);
        if (file.exists()) {
            fileSize = file.length();
        } else {
            throw new Exception("Apk路径" + filePath + "不存在");
        }
        fileSize = new File(filePath).length();
        parseApkBasicInfo();
    }

    /**
     * 解析命令{@code aapt d badging命令获取的信息}
     */
    private void aaptBadging() {
        ArrayList<String> out = new ArrayList<String>();
        String aapt = String.format("aapt d badging %s", filePath);
        javaUtils.runtimeExec(out, aapt, 30);
        for (String string : out) {
            if (string.contains("application-label:")) {
                appName = string.replace("application-label:", "").replace("\'", "");
            } else if (string.contains("launchable-activity: name=")) {
                string = string.replace("launchable-activity: name='", "");
                launchableActivity = string.substring(0, string.indexOf("\'"));
            }
        }
    }

    /**
     * 解析命令{@code aapt d xmltree命令获取的信息}
     */
    private void aaptXmltree() {
        ArrayList<String> out = new ArrayList<String>();
        String aapt = String.format("aapt d xmltree %s %s", filePath, "AndroidManifest.xml");
        javaUtils.runtimeExec(out, aapt, 30);
        String node = "";
        for (int i = 0; i < out.size(); i++) {
            String string = out.get(i);
            if (string.isEmpty()) {
                continue;
            }
            string = replace(string);
            String[] split = string.split("=");
            if (split.length < 2) {
                node = split[0];
                continue;
            }
            if (split[0].equals("android:versionCode") && node.equals("manifest")) {
                versionCode = Integer.parseInt(split[1].substring(2), 16);
            } else if (split[0].equals("android:versionName") && node.equals("manifest")) {
                versionName = split[1];
            } else if (split[0].equals("package") && node.equals("manifest")) {
                packageName = split[1];
            } else if (split[0].equals("android:minSdkVersion") && node.equals("uses-sdk")) {
                minSdkVersion = Integer.parseInt(split[1].substring(2), 16);
            } else if (split[0].equals("android:targetSdkVersion") && node.equals("uses-sdk")) {
                targetSdkVersion = Integer.parseInt(split[1].substring(2), 16);
            } else if (split[0].equals("android:name") && node.equals("uses-permission")) {
                usesPermissions.add(split[1]);
            } else if (split[0].equals("android:name") && node.equals("instrumentation")) {
                instrumentName = split[1];
            } else if (split[0].equals("android:targetPackage") && node.equals("instrumentation")) {
                targetPackageName = split[1];
            }
        }
    }

    private void parseApkBasicInfo() {
        aaptBadging();
        aaptXmltree();
    }

    private String replace(String string) {
        while (string.indexOf("(") != -1) {
            string = string.substring(0, string.indexOf("("))
                    + string.substring(string.indexOf(")") + 1, string.length());
        }
        return string.replace("\"", "").trim().substring(3);
    }

    public static class TestClass {
        public String className;// 测试类完整名
        public String classSimpleName;// 测试类简名
        public String[] annotationNumber;// 测试类中注解TestNumber值
        public String annotationClass;// 测试类中注解TestClass值
        public boolean annotationReinstall = false;// 测试类中注解是否有reinstall
        boolean annotationSmokeTest = false;// 测试类中注解是否有SmokeTest
        public Integer[] annotationOrder; // 测试类中注解ClassOrder值
    }

    /**
     * 通过解包来解析apk的包信息
     * 
     * @param testClasses
     * @param excuteType
     */
    public void parseApkAdvancedInfo(String[] testClasses, String excuteType) {
        DexFile dexFile = null;
        try {
            dexFile = DexFileFactory.loadDexFile(filePath, 15);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (testClasses != null) {
            for (String string : testClasses) {
                TestClass testClass = new TestClass();
                testClass.classSimpleName = string;
                classes.add(testClass);
            }
        }
        boolean flag = classes.size() == 0 ? true : false;
        int count = 0;
        for (ClassDef classDef : dexFile.getClasses()) {
            if (flag) {
                TestClass testClass = new TestClass();
                parseTestClass(testClass, classDef);
                if (testClass.annotationNumber != null) {
                    testClass.className = getClassName(classDef.getType());
                    testClass.classSimpleName = getClassSimpleName(classDef.getType());
                    classes.add(testClass);
                }
            } else {
                if (count == testClasses.length) {
                    break;
                }
                String simpleName = getClassSimpleName(classDef.getType());
                for (TestClass testClass : classes) {
                    if (testClass.classSimpleName.equals(simpleName)) {
                        parseTestClass(testClass, classDef);
                        if (testClass.annotationNumber != null) {
                            testClass.className = getClassName(classDef.getType());
                        }
                        count++;
                        break;
                    }
                }
            }
        }
        if (excuteType.equals(Res.SMOKE)) {
            getSmokeClasses();
        }
        // 根据ClassOrder排序
        sortClasses();
    }

    private void sortClasses() {
        ArrayList<TestClass> hasOrderClasses = new ArrayList<TestClass>();
        ArrayList<TestClass> noOrderClasses = new ArrayList<TestClass>();
        // 区分出有无ClassOrder的两组TestClass集合
        divideClasses(hasOrderClasses, noOrderClasses);
        // 对有ClassOrder的TestClass进行排序
        sortOrderClasses(hasOrderClasses);

        // 添加到classes
        classes.clear();
        classes.addAll(hasOrderClasses);
        classes.addAll(noOrderClasses);
    }

    private void sortOrderClasses(ArrayList<TestClass> hasOrderClasses) {
        hasOrderClasses.sort(new Comparator<TestClass>() {

            @Override
            public int compare(TestClass arg0, TestClass arg1) {
                int result = arg0.annotationOrder[0].compareTo(arg1.annotationOrder[0]);
                if (result == 0) {
                    return arg0.annotationOrder[1].compareTo(arg1.annotationOrder[1]);
                }
                return result;
            }

        });
    }

    /**
     * 获得有、无ClassOrder的两组TestClass集合
     * 
     * @param hasOrderClasses
     * @param noOrderClasses
     * @return
     */
    private void divideClasses(ArrayList<TestClass> hasOrderClasses,
            ArrayList<TestClass> noOrderClasses) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).annotationOrder == null) {
                // 没有ClassOrder
                noOrderClasses.add(classes.get(i));
            } else {
                // 有ClassOrder
                hasOrderClasses.add(classes.get(i));
            }
            classes.remove(i);
            i--;
        }
    }

    private void getSmokeClasses() {
        int count = classes.size();
        for (int i = 0; i < count; i++) {
            if (!classes.get(i).annotationSmokeTest) {
                classes.remove(i);
                i--;
                count--;
            }
        }
    }

    private String getClassSimpleName(String className) {
        className = className.replace(";", "").trim();
        return className.substring(className.lastIndexOf("/") + 1);
    }

    private String getClassName(String className) {
        return className.replace(";", "").trim().replace("/", ".").substring(1);
    }

    private void parseTestClass(TestClass testClass, ClassDef classDef) {
        Set<? extends Annotation> annotations = classDef.getAnnotations();
        if (annotations == null) {
            return;
        }
        for (Annotation annotation : annotations) {
            if (!annotation.getType().contains(Res.TEST_ANNOTATION)) {
                continue;
            }
            String simpleName = getClassSimpleName(annotation.getType());
            switch (simpleName) {
            case "TestNumber":
                testClass.annotationNumber = (String[]) getAnnotationValues(annotation);
                break;
            case "TestClass":
                testClass.annotationClass = (String) getAnnotationValues(annotation);
                break;
            case "Reinstall":
                testClass.annotationReinstall = true;
                break;
            case "SmokeTest":
                testClass.annotationSmokeTest = true;
                break;
            case "ClassOrder":
                testClass.annotationOrder = (Integer[]) getAnnotationValues(annotation);
                break;
            default:
                break;
            }
        }
    }

    private Object getAnnotationValues(Annotation annotation) {
        Set<? extends AnnotationElement> map = annotation.getElements();
        for (AnnotationElement annotationElement : map) {
            EncodedValue encodedValue = annotationElement.getValue();
            // String
            if (encodedValue instanceof DexBackedStringEncodedValue) {
                return ((DexBackedStringEncodedValue) encodedValue).getValue();
            }
            // int
            if (encodedValue instanceof ImmutableIntEncodedValue) {
                return ((ImmutableIntEncodedValue) encodedValue).getValue();
            }
            // boolean
            if (encodedValue instanceof ImmutableBooleanEncodedValue) {
                return ((ImmutableBooleanEncodedValue) encodedValue).getValue();
            }
            // String[]/Integer[]
            if (encodedValue instanceof DexBackedArrayEncodedValue) {
                List<? extends EncodedValue> encodedValues = ((DexBackedArrayEncodedValue) encodedValue)
                        .getValue();
                Object[] objects = null;
                String[] strings = new String[encodedValues.size()];
                Integer[] ints = new Integer[encodedValues.size()];
                for (int i = 0; i < encodedValues.size(); i++) {
                    if (encodedValues.get(i) instanceof DexBackedStringEncodedValue) {
                        strings[i] = ((DexBackedStringEncodedValue) encodedValues.get(i))
                                .getValue();
                        objects = strings;
                    }
                    if (encodedValues.get(i) instanceof ImmutableIntEncodedValue) {
                        ints[i] = ((ImmutableIntEncodedValue) encodedValues.get(i)).getValue();
                        objects = ints;
                    }
                }
                return objects;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String content = "";
        content += "fileSize:" + fileSize + "\r\n";
        content += "versionCode:" + versionCode + "\r\n";
        content += "versionName:" + versionName + "\r\n";
        content += "packageName:" + packageName + "\r\n";
        content += "minSdkVersion:" + minSdkVersion + "\r\n";
        content += "targetSdkVersion:" + targetSdkVersion + "\r\n";
        content += "usesPermissions:\r\n";
        for (String usesPermission : usesPermissions) {
            content += "  " + usesPermission + "\r\n";
        }
        return content;
    }
}