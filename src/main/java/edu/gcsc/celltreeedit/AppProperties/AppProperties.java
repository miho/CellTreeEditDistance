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

    // Default-Values, might get replaced by CommandLineArguments
    int calcType;
    File baseDirectory = new File("");
    File destinationDirectory = new File("");
    File jsonFile = new File("");
    String jsonName = "swcFiles.json";
    String matrixName = "DistanceMatrix.txt";

    // nur getter, keine setter um nachträgliche änderung zu verhindern. getter für dataDirectory etc aber keine variable -> im getter zusammenbasteln. package-private machen! --> einfaches ändern in CommandLineParsing
    public int getCalcType() {
        return calcType;
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public File getDataDirectory() {
        return (baseDirectory.getPath().equals("")) ? new File(baseDirectory.getPath() + "/Data") : new File("");
    }

    public File getMetadataDirectory() {
        return (baseDirectory.getPath().equals("")) ? new File(baseDirectory.getPath() + "/Data/Metadata") : new File("");
    }

    public File getSwcFileDirectory() {
        return (baseDirectory.getPath().equals("")) ? new File(baseDirectory.getPath() + "/Data/SWCFiles") : new File("");
    }

    public File getWorkingDirectory() {
        return (baseDirectory.getPath().equals("")) ? new File(baseDirectory.getPath() + "/WorkingDir") : new File("");
    }

    public File getOutputDirectory() {
        return (baseDirectory.getPath().equals("")) ? new File(baseDirectory.getPath() + "/Output") : new File("");
    }

    public File getDestinationDirectory() { return destinationDirectory; }

    public File getJsonFile() {
        return jsonFile;
    }

    public String getJsonName() { return jsonName; }

    public String getMatrixName() { return matrixName; }
}


