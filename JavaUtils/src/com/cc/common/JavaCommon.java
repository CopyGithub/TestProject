package com.cc.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class JavaCommon {

    /**
     * 睡眠当前线程指定的{@code time} 毫秒数
     * 
     * @param time
     *            单位毫秒
     */
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 将{@link String}数组转化为一个{@link Arraylist}
     * 
     * @param strings
     * @return
     */
    public ArrayList<String> toArrayList(String[] strings) {
        ArrayList<String> arrays = new ArrayList<String>();
        for (int i = 0; i < strings.length; i++) {
            arrays.add(strings[i]);
        }
        return arrays;
    }

    /**
     * 移除数组中指定的元素
     * 
     * @param origin
     *            原始数组
     * @param index
     *            需要移除的元素索引
     * @return
     */
    public int[] removeElement(int[] origin, int index) {
        int[] result = new int[origin.length - 1];
        int flag = 0;
        for (int i = 0; i < result.length; i++) {
            if (i == index) {
                flag = 1;
            }
            result[i] = origin[i + flag];
        }
        return result;
    }

    /**
     * 计算字符串的MD5值
     * 
     * @param string
     * @return
     */
    public String MD5(String string) {
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = string.getBytes();
        digester.update(bytes);
        StringBuffer des = new StringBuffer();
        String tmp = null;
        byte[] byteDigest = digester.digest();
        for (int i = 0; i < byteDigest.length; i++) {
            tmp = (Integer.toHexString(byteDigest[i] & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * 将输入流中的结果读取到{@link ArrayList} 中
     * 
     * @author chchen
     *
     */
    class ReadInputStream implements Runnable {
        private ArrayList<String> out;
        private InputStream inputStream;

        /**
         * 
         * @param out
         *            存放输入流的结果
         * @param inputStream
         *            输入流
         */
        public ReadInputStream(final ArrayList<String> out, InputStream inputStream) {
            this.out = out;
            this.inputStream = inputStream;
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }

        public void run() {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    out.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行本地命令,并将执行结果写入到{@link ArrayList}中
     * 
     * @param out
     *            存放执行结果,为{@code null} 时不记录
     * @param command执行的命令
     * @param timeout
     *            等待时间,单位为秒
     * @return 是否正常执行完成
     */
    public boolean runtimeExec(ArrayList<String> out, String command, int timeout) {
        ArrayList<String> backupOut = new ArrayList<String>();
        Process process = null;
        boolean result = false;
        try {
            process = Runtime.getRuntime().exec(command);
            backupOut = out != null ? out : backupOut;
            new ReadInputStream(backupOut, process.getInputStream()).start();
            new ReadInputStream(backupOut, process.getErrorStream()).start();
            result = process.waitFor(timeout, TimeUnit.SECONDS);
            new JavaCommon().sleep(1000);// 增加多线程执行后的等待
        } catch (IOException | InterruptedException e) {
            return false;
        }
        return result;
    }

    /**
     * 获取所有在线设备序列号列表
     * 
     * @return
     */
    public ArrayList<String> getAllDevices() {
        String cmd = "adb devices";
        ArrayList<String> result = new ArrayList<String>();
        runtimeExec(result, cmd, 10);
        ArrayList<String> devices = new ArrayList<String>();
        for (String string : result) {
            String[] device = string.trim().split("\\t");
            if (device.length == 2 && device[1].equals("device")) {
                devices.add(device[0].trim());
            }
        }
        return devices;
    }
}
