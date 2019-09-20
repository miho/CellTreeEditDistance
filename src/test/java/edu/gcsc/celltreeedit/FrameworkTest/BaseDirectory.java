package edu.gcsc.celltreeedit.FrameworkTest;

import java.io.File;

public class BaseDirectory {
    private static File testDirectory = new File("/media/exdisk/Sem06/BA/ProgramData/Test");

    public static File getTestDirectory() {
        return testDirectory;
    }

    public static File getWorkingDirectory() {
        return new File(testDirectory.getPath() + "/WorkingDirectory");
    }
}
