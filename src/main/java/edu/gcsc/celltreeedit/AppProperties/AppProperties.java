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
    int label = 18;
    int rows;
    int iteration;
    File baseDirectory = new File("");
    File metadataDirectory = new File("");
    File swcFileDirectory = new File("");
    File workingDirectory = new File("");
    File outputDirectory = new File("");
    File fileInput = new File("");
    String nameOutput = "";
    boolean renameDendrogram = false;
    boolean saveOutput = false;

    // only getter to prevent overwriting
    public int getCalcType() {
        return calcType;
    }

    public int getLabel() {
        return label;
    }

    public int getRows() { return rows; }

    public int getIteration() { return iteration; }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public File getMetadataDirectory() {
        return metadataDirectory;
    }

    public File getSwcFileDirectory() {
        return swcFileDirectory;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public File getFileInput() {
        return fileInput;
    }

    public String getNameOutput() {
        return nameOutput;
    }

    public boolean isRenameDendrogram() {
        return renameDendrogram;
    }

    public boolean isSaveOutput() {
        return saveOutput;
    }
}


