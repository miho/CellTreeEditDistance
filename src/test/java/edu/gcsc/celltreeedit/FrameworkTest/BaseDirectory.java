package edu.gcsc.celltreeedit.FrameworkTest;

import java.io.File;

public class BaseDirectory {
    private static File baseDirectory = new File("ProgramData");

    public static File getBaseDirectory() {
        return baseDirectory;
    }

    public static File getWorkingDirectory() {
        return new File(baseDirectory.getPath() + "/WorkingDirectory");
    }
}
