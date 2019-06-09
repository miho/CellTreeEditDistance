package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;
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
 * SWC Test. This class provides a method to Test Reading SWC-Files. If it failes it moves the Files in a subdirectory
 * 'errorFiles'
 *
 */
public class SWCTest {

    private String baseDirectory;

    private Set<String> swcFilenames = new HashSet<>();
    private Set<String> alreadyReadFilenames = new HashSet<>();
    private Map<String, NeuronMetadataRImpl> neuronMetadata;

    public Set<String> preprocessSWCDirectory(Map<String, NeuronMetadataRImpl> neuronMetadata, File swcDirectory) {

        // choose Files that shall be checked
//        File directory = Utils.chooseDirectory();
        File directory = swcDirectory;
        try {
            this.baseDirectory = directory.getCanonicalPath();
            System.out.println(this.baseDirectory);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.neuronMetadata = neuronMetadata;
        this.moveNonCNGDirectories(directory);
        this.moveDuplicateFiles(directory);
        this.moveErrorFiles(directory);

        return swcFilenames;
    }

    private void moveNonMetadata(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                this.swcFilenames.add(Utils.removeSWCFileExtensions(subFile.getName()));
                if (!this.neuronMetadata.containsKey(Utils.removeSWCFileExtensions(subFile.getName()))) {
                    this.moveFile(subFile, "/NonMetadata");
                }
            } else {
                if (subFile.getName().equals("NonMetadata") || subFile.getName().equals("NonCNGFiles") || subFile.getName().equals("DuplicateFiles") || subFile.getName().equals("ErrorFiles")) {
                    continue;
                }
                this.moveNonMetadata(subFile);
            }
        }
    }

    private void moveNonCNGDirectories(File directory) {
        File[] archives = directory.listFiles();
        if (archives == null) {
            return;
        }
        for (File archive: archives) {
            if (archive.getName().equals("NonMetadata") || archive.getName().equals("NonCNGFiles") || archive.getName().equals("DuplicateFiles") || archive.getName().equals("ErrorFiles")) {
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

    private void moveDuplicateFiles(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                this.swcFilenames.add(Utils.removeSWCFileExtensions(subFile.getName()));
                if (!this.alreadyReadFilenames.contains(Utils.removeSWCFileExtensions(subFile.getName()))) {
                    this.alreadyReadFilenames.add(Utils.removeSWCFileExtensions(subFile.getName()));
                } else {
                    this.moveFile(subFile, "/DuplicateFiles");
                }
            } else {
                if (subFile.getName().equals("NonMetadata") || subFile.getName().equals("NonCNGFiles") || subFile.getName().equals("DuplicateFiles") || subFile.getName().equals("ErrorFiles")) {
                    continue;
                }
                this.moveDuplicateFiles(subFile);
            }
        }
    }

    private void moveErrorFiles(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                this.swcFilenames.add(Utils.removeSWCFileExtensions(subFile.getName()));
//                try to create a SWCSegment from file
                try {
                    FileInputStream f = new FileInputStream(subFile);
                    SWCSegment.fromStream(f);
                } catch (Exception ex) {
                    if (ex instanceof RuntimeException) {
                        this.moveFile(subFile, "/ErrorFiles");
                    } else {
                        ex.printStackTrace();
                    }
                }
            } else {
                if (subFile.getName().equals("NonMetadata") || subFile.getName().equals("NonCNGFiles") || subFile.getName().equals("DuplicateFiles") || subFile.getName().equals("ErrorFiles")) {
                    continue;
                }
                this.moveErrorFiles(subFile);
            }
        }
    }

    private void moveFile(File file, String toDir) {
        // move file
        try {
            // get file directory of file
            String relativeFileDir = file.getCanonicalPath().substring(0, file.getCanonicalPath().lastIndexOf(file.getName())).replaceFirst(this.baseDirectory, "");
            String newFileDir = this.baseDirectory + toDir + relativeFileDir;
            new File(newFileDir).mkdirs();
            // move file to defined directory
            Files.move(Paths.get(file.getCanonicalPath()), Paths.get(newFileDir + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Subfile: " + file.getCanonicalPath() + " is moved to " + newFileDir + file.getName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
