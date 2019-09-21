package edu.gcsc.celltreeedit;

import java.io.File;

public class BaseDirectory {
    private static File testDirectory = new File("Test/");

    public static File getTestDirectory() {
        return testDirectory;
    }

    public static File getWorkingDirectory() {
        return new File(testDirectory.getPath() + "/WorkingDirectory/");
    }
}
