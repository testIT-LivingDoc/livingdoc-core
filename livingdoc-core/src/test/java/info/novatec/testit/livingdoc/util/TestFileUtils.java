package info.novatec.testit.livingdoc.util;

import java.io.File;
import java.io.IOException;


public class TestFileUtils {

    public static String REPORTS_DIR_NAME = "test-reports";

    public static File createTempReportsDirectory() {
        return createTempDirectory(REPORTS_DIR_NAME);
    }

    public static File createTempDirectory(String directory) {
        try {
            File tempDir = File.createTempFile("dirs", "");
            if ( ! tempDir.delete()) {
                tempDir = new File(tempDir.getAbsolutePath() + "dir");
            }
            File reportTempDir = new File(tempDir.getAbsolutePath(), directory);
            if (reportTempDir.mkdirs()) {
                return reportTempDir;
            }
            throw new RuntimeException("Cannot create temporary reports folder " + reportTempDir.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Cannot create temporary reports folder", e);
        }
    }
}
