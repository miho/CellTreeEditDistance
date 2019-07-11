package edu.gcsc.celltreeedit.AppProperties;

import java.io.File;

/**
 * Contains basic properties of the application like calcType, basedirectory of project, Directory of JsonFile etc
 */
public final class AppProperties {

    private static final AppProperties INSTANCE = new AppProperties();

    public static AppProperties getInstance() {
        return INSTANCE;
    }

    private AppProperties() { }


    // Ziel: mit defaultwerten füllen und evtl durch CommandlineArgumente ersetzen
    // Default-Values
    int calcType = 0;
    File baseDirectory = new File("/media/exdisk/Sem06/BA/ProgramData");
    File jsonPath = new File("/media/exdisk/Sem06/BA/ProgramData/swcFiles.json");
    boolean show = true;
    String matrixExportName = "DistanceMatrix.txt";

    // nur getter, keine setter um nachträgliche änderung zu verhindern. getter für dataDirectory etc aber keine variable -> im getter zusammenbasteln. package-private machen! --> einfaches ändern in CommandLineParsing
    public int getCalcType() {
        return calcType;
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public File getDataDirectory() {
        return new File(baseDirectory.getPath() + "/Data");
    }

    public File getMetadataDirectory() {
        return new File(baseDirectory.getPath() + "/Data/Metadata");
    }

    public File getSwcFileDirectory() {
        return new File(baseDirectory.getPath() + "/Data/SWCFiles");
    }

    public File getWorkingDirectory() { return new File(baseDirectory.getPath() + "/WorkingDir"); }

    public File getOutputDirectory() {
        return new File(baseDirectory.getPath() + "/Output");
    }

    public File getJsonPath() {
        return jsonPath;
    }

    public boolean isShow() {
        return show;
    }

    public String getMatrixExportName() {
        return matrixExportName;
    }
}


