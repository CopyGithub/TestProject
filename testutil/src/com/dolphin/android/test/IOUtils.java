
package com.dolphin.android.test;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.util.zip.ZipFile;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class IOUtils {

    private static final String TAG = IOUtils.class.getSimpleName();

    private static final int EOF = -1;

    private static final int BUFFER_SIZE = 1024;

    public interface ProgressListenrer {

        public void progress(long current, long total);

    }

    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     * 
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws java.io.IOException If any error occurs during the copy.
     */
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = in.read(buffer)) != EOF) {
            out.write(buffer, 0, read);
        }
    }

    public static long copyStream(InputStream in, OutputStream out, long total,
            ProgressListenrer l) throws IOException {
        int read = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        long current = 0;
        while ((read = in.read(buffer)) != EOF) {
            out.write(buffer, 0, read);
            current += read;
            if (l != null) {
                l.progress(current, total);
            }
        }
        return current;
    }

    /**
     * Closes the specified stream.
     * 
     * @param stream The stream to close.
     */
    public static void closeQuietly(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    public static void closeQuietly(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    public static void closeQuietly(ZipFile zipFile) {
        if (zipFile != null) {
            try {
                zipFile.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    public static String loadContent(final InputStream stream) throws IOException {
        return toString(stream, null);
    }

    /**
     * Convert an {@link InputStream} to String.
     * 
     * @param stream the stream that contains data.
     * @param encoding the encoding of the data.
     * @return the result string.
     * @throws IOException an I/O error occurred.
     */
    public static String toString(final InputStream stream, String encoding)
            throws IOException {
        if (null == stream) {
            throw new IllegalArgumentException("stream may not be null.");
        }
        if (TextUtils.isEmpty(encoding)) {
            encoding = System.getProperty("file.encoding", "utf-8");
        }
        final InputStreamReader reader = new InputStreamReader(stream, encoding);
        final StringWriter writer = new StringWriter();
        final char[] buffer = new char[4 * BUFFER_SIZE];
        int charRead = reader.read(buffer);
        while (charRead > 0) {
            writer.write(buffer, 0, charRead);
            charRead = reader.read(buffer);
        }
        return writer.toString();
    }
    
    public static String loadFromAssets(Context context, String fileName) {
        if(TextUtils.isEmpty(fileName)){
            return "";
        }
        try {
            return toString(context.getAssets().open(fileName), "utf-8");
        }
        catch (Exception e) {
            return "";
        }
    }
    
    public static String loadContent(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return toString(stream, "utf-8");
        } catch (Exception e) {
        } catch(OutOfMemoryError ee){
        }finally {
            closeQuietly(stream);
        }
        return null;
    }

    public static byte[] toByteArray(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = null;
        try {
            copyStream(in, out);
            data = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(out);
        }
        return data;
    }
}
