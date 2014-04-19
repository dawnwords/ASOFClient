package edu.fudan.se.asof.util;

import java.io.Closeable;
import java.io.FileOutputStream;

/**
 * Created by Dawnwords on 2014/4/18.
 */
public class IOUtil {
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(String path,byte[] bytes) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(out);
        }
    }
}
