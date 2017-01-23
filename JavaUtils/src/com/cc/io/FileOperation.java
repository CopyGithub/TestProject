package com.cc.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileOperation {
    private final String charsetName = System.getProperty("file.encoding");

    /**
     * 删除指定路径下的所有文件和目录
     * 
     * @param file
     *            文件
     * @return {@code true} 所有文件都正常删除{@code false} 有一个文件删除报错都是{@code false}
     */
    public boolean fileDel(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        for (File child : file.listFiles()) {
            if (!fileDel(child)) {
                return false;
            }
        }
        return file.delete();
    }

    /**
     * 重命名文件或目录
     * 
     * @param file
     * @param name
     * @return
     * @throws SecurityException
     */
    public boolean fileRename(File file, String name) throws SecurityException {
        String oldName = file.getName();
        if (name != null && name.length() > 0) {
            if (file.isFile() && oldName.indexOf(".") > 0) {
                String suffix = oldName.substring(oldName.lastIndexOf("."));
                if (suffix.length() > 1) {
                    name = name.endsWith(suffix) ? name : (name + suffix);
                }
            }
            file.renameTo(new File(file.getParent() + File.separator + name));
            return true;
        }
        return false;
    }

    /**
     * 创建文件或目录
     * 
     * @param file
     *            文件
     * @param dir
     *            是否为目录, {@code true} 表示创建目录, {@code false} 表示创建文件
     * @param delete
     *            是否删除已存在的目录
     * @return
     */
    public boolean fileCreate(File file, boolean dir, boolean delete) {
        boolean flag = true;
        if (file.exists()) {
            if (delete || (dir != file.isDirectory())) {
                flag = fileDel(file);
                if (!flag) {
                    return false;
                }
            } else {
                return true;
            }
        }
        if (dir) {
            flag = file.mkdirs();
        } else {
            if (!file.getParentFile().exists()) {
                flag = file.getParentFile().mkdirs();
                if (!flag) {
                    return false;
                }
            }
            try {
                flag = file.createNewFile();
            } catch (IOException e) {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 复制或移动文件和目录
     * 
     * @param fromFile
     * @param destFile
     *            如果目前文件和源文件类型不一致时,会被删除,以便文件复制能够正常进行
     * @param delete
     *            {@code true} 删除目标位置的原有文件或文件夹{@code false} 表示覆盖目标位置原有的文件或文件夹
     * @return {@code true} 表示复制成功,{@code false} 表示任意一步失败
     */
    public boolean fileCopy(File fromFile, File destFile, boolean delete) {
        if (!fromFile.exists()) {
            return false;
        }
        boolean flag = fileCreate(destFile, fromFile.isDirectory(), delete);
        if (!flag) {
            return false;
        }
        if (fromFile.isFile()) {
            flag = fileDel(destFile);
            if (!flag) {
                return false;
            }
            FileChannel in = null;
            FileChannel out = null;
            try {
                FileInputStream inputStream = new FileInputStream(fromFile);
                FileOutputStream outputStream = new FileOutputStream(destFile);
                in = inputStream.getChannel();
                out = outputStream.getChannel();
                in.transferTo(0, in.size(), out);
                inputStream.close();
                outputStream.close();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            for (File file : fromFile.listFiles()) {
                flag = fileCopy(file,
                        new File(destFile.getAbsoluteFile() + File.separator + file.getName()),
                        false);
                if (!flag) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 读取文件字节流
     * 
     * @param file
     * @return
     */
    public byte[] readBytes(File file) {
        FileInputStream fis = null;
        byte[] buffer = null;
        try {
            fis = new FileInputStream(file);
            buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            return buffer;
        } catch (IOException e) {
            return e.getMessage().getBytes();
        }
    }

    /**
     * 读取文件中的内容并转化为String文本
     * 
     * @param file
     * @param charsetName
     *            字符集，如"utf-8"
     * @return
     * @throws UnsupportedEncodingException
     */
    public String readText(File file, String charsetName) throws UnsupportedEncodingException {
        return new String(readBytes(file), charsetName);
    }

    /**
     * 写入{@link Buffer}到文件中
     * 
     * @param buffer
     * @param file
     * @param append
     *            是否追加,{@code true} 表示追加,[{@code false} 表示覆写
     * @param num
     *            重复写入流的次数,用于创建大文件
     * @throws IOException
     */
    public void writeByte(byte[] buffer, File file, boolean append, long num) throws IOException {
        FileOutputStream fos = null;
        fos = new FileOutputStream(file, append);
        while (num-- > 0) {
            fos.write(buffer);
        }
        fos.close();
    }

    /**
     * 写文本内容到文件中
     * 
     * @param content
     * @param file
     * @param append
     *            是否追加,{@code true} 表示追加,[{@code false} 表示覆写
     * @param charsetName
     *            字符集，如"utf-8"
     * @param num
     *            重复写入的次数,用于创建大文件，默认为1
     * @throws IOException
     */
    public void writeText(String content, File file, boolean append, String charsetName, long num)
            throws IOException {
        byte[] buffer = content.getBytes(charsetName);
        writeByte(buffer, file, append, num);
    }

    /**
     * Zip压缩文件
     * 
     * @param zipOutputStream
     * @param file
     * @param name
     * @throws IOException
     */
    private void zip(ZipOutputStream zipOutputStream, File file, String name) throws IOException {
        ZipEntry zipEntry = null;
        if (file.isFile()) {
            zipEntry = new ZipEntry(name);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(readBytes(file));
        } else {
            File[] files = file.listFiles();
            for (File child : files) {
                zip(zipOutputStream, child, name + File.separator + child.getName());
            }
        }
    }

    /**
     * Zip压缩文件
     * 
     * @param zipPath
     *            压缩后的路径
     * @param file
     *            需要压缩的文件
     * @return
     */
    public boolean zipCompress(String zipPath, File file) {
        if (!file.exists()) {
            return false;
        }
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipPath),
                    Charset.forName(charsetName));
            zip(zipOutputStream, file, file.getName());
            zipOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解压zip文件
     * 
     * @param unZipPath
     * @param file
     * @return
     * @throws ZipException
     * @throws IOException
     */
    public boolean zipDecompress(String unZipPath, File file) throws ZipException, IOException {
        if (!file.exists()) {
            return false;
        }
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file),
                Charset.forName(charsetName));
        byte[] buffer = null;
        int temp = 0;
        ZipEntry zipEntry = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.isDirectory()) {
                continue;
            }
            File destFile = new File(unZipPath + File.separator + zipEntry.getName());
            fileCreate(destFile, false, true);
            FileOutputStream fos = new FileOutputStream(destFile);
            buffer = new byte[4096];
            while ((temp = zipInputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, temp);
            }
            fos.close();
        }
        zipInputStream.close();
        return true;
    }

}