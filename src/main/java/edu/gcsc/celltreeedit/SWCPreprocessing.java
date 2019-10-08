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
package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import eu.mihosoft.vswcreader.SWCSegment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * SWC Preprocessing. This class provides methods to preprocess a SWC-Directory. It moves NonCNGDirectories, Duplicates, NonMetadataFiles and ErrorFiles.
 */
public class SWCPreprocessing {

    private File swcDirectory;
    private Set<String> alreadyReadFilenames = new HashSet<>();
    private Map<String, NeuronMetadataR> neuronMetadata;

    public void preprocessSWCDirectory(Map<String, NeuronMetadataR> neuronMetadata, File swcDirectory) {

        this.swcDirectory = swcDirectory;
        System.out.println("> Preprocessing the following directory: " + swcDirectory.getAbsolutePath() + "\n");
        this.neuronMetadata = neuronMetadata;
        this.moveNonCNGDirectories(swcDirectory);
        this.moveDuplicatesAndNonMetadataAndErrorFiles(swcDirectory);
        System.out.println("\n> Preprocessing finished");
    }

    private void moveNonCNGDirectories(File directory) {
        File[] archives = directory.listFiles();
        if (archives == null) {
            return;
        }
        for (File archive: archives) {
            if (archive.getName().equals("00_Ignore")) {
                continue;
            }
            File[] subDirectories = archive.listFiles();
            if (subDirectories == null) {
                break;
            }
            for (File subDirectory: subDirectories) {
                if (!subDirectory.getName().equals("CNG version")) {
                    this.moveFile(subDirectory, "/NonCNGFiles");
                }
            }
        }
    }

    private void moveDuplicatesAndNonMetadataAndErrorFiles(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        Arrays.sort(subFiles);
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                String filenameWithoutSWCFileExtension = Utils.removeSWCFileExtensions(subFile.getName());
                if (this.alreadyReadFilenames.contains(filenameWithoutSWCFileExtension)) {
                    this.moveFile(subFile, "/DuplicateFiles");
                    continue;
                } else {
                    this.alreadyReadFilenames.add(filenameWithoutSWCFileExtension);
                }
                if (!this.neuronMetadata.containsKey(filenameWithoutSWCFileExtension)) {
                    this.moveFile(subFile, "/NonMetadata");
                    continue;
                }
                if (this.fileHasError(subFile)) {
                    this.moveFile(subFile, "/ErrorFiles");
                }
            } else {
                if (subFile.getName().equals("00_Ignore")) {
                    continue;
                }
                // recursively check next directory
                this.moveDuplicatesAndNonMetadataAndErrorFiles(subFile);
            }
        }
    }

    private boolean fileHasError(File file) {
        // try to create a SWCSegment from file
        try {
            FileInputStream f = new FileInputStream(file);
            SWCSegment.fromStream(f);
        } catch (Exception ex) {
            if (ex instanceof RuntimeException) {
                return true;
            } else {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private void moveFile(File file, String toSubDir) {
        // move file to subdirectory
        try {
            // get file directory of file
            String relativeFileDir = file.getCanonicalPath().substring(0, file.getCanonicalPath().lastIndexOf(file.getName())).replaceFirst(this.swcDirectory.getCanonicalPath(), "");
            String newFileDir = this.swcDirectory.getCanonicalPath() + "/00_Ignore" + toSubDir + relativeFileDir;
            new File(newFileDir).mkdirs();
            // move file to defined directory
            Files.move(Paths.get(file.getCanonicalPath()), Paths.get(newFileDir + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File: " + file.getCanonicalPath() + "  Moved to: " + newFileDir + file.getName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
