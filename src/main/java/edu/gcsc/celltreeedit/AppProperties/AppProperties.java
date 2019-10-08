/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
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
    File directoryInput = new File("");
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

    public File getDirectoryInput() { return directoryInput; }

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


