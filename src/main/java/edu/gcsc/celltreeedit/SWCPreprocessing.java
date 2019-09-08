package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import eu.mihosoft.vswcreader.SWCSegment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * SWC Preprocessing. This class provides methods to preprocess a SWC-Directory. It moves NonCNGDirectories, Duplicates, NonMetadataFiles and ErrorFiles.
 *
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
