package info.novatec.testit.livingdoc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;


public class IOUtilsc {
    public static void copyFile(File srcFile, File destFile) throws IOException {
        InputStream reader = new FileInputStream(srcFile);
        OutputStream out = new FileOutputStream(destFile);
        try {
            byte[] buffer = new byte[2048];
            int n = 0;
            while ( - 1 != ( n = reader.read(buffer) )) {
                out.write(buffer, 0, n);
            }
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(reader);
        }
    }

    public static String uniquePath(String name, String ext) throws IOException {
        File file = File.createTempFile(name, ext);
        String path = file.getAbsolutePath();
        file.delete();

        return path;
    }
}
