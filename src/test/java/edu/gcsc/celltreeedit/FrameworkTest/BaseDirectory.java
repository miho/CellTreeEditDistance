package edu.gcsc.celltreeedit.FrameworkTest;

import java.io.File;

public class BaseDirectory {
    private static File baseDirectory = new File("/media/exdisk/Sem06/BA/ProgramData/Test");

    public static File getBaseDirectory() {
        return baseDirectory;
    }

    public static File getWorkingDirectory() {
        return new File(baseDirectory.getPath() + "/WorkingDirectory");
    }
}
