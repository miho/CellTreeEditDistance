package edu.gcsc.celltreeedit;

import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;


/**
 * Created by Erid on 16.02.2018.
 * <p>
 * The class implements a file chooser, which can choose a number of Swc-files to compare or a whole folder.
 */
public class Utils {

    private static List<File> swcFiles;

    /**
     * @return a list of files which were selected
     */
    public static File[] chooseSWCFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //accept files and directories as input
        FileNameExtensionFilter swc = new FileNameExtensionFilter("SWC", "SWC");
        fileChooser.addChoosableFileFilter(swc);                            // filter on swc files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only swc files
        fileChooser.setMultiSelectionEnabled(true);                       // accept multiple files as input
        fileChooser.showOpenDialog(null);

        if (fileChooser.getSelectedFile().isFile())
            return fileChooser.getSelectedFiles();
        else {
            File folder = new File(fileChooser.getSelectedFile().getAbsolutePath());
            return folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".swc");
                }
            });
        }
    }

    /**
     * @return a list of json-files which were selected
     */
    public static File[] chooseJSONFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //accept files and directories as input
        FileNameExtensionFilter json = new FileNameExtensionFilter("json", "json");
        fileChooser.addChoosableFileFilter(json);                            // filter on swc files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only swc files
        fileChooser.setMultiSelectionEnabled(true);                       // accept multiple files as input
        fileChooser.showOpenDialog(null);

        if (fileChooser.getSelectedFile().isFile())
            return fileChooser.getSelectedFiles();
        else {
            File folder = new File(fileChooser.getSelectedFile().getAbsolutePath());
            return folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });
        }
    }

    public static File incrementFileNameIfNecessary(File destinationDirectory, String fileName) {
        destinationDirectory.mkdirs();
        File file = new File(destinationDirectory.getAbsolutePath() + "/" + fileName);
        // increment fileName if necessary
        int count = 1;
        while (file.exists()) {
            file = new File(destinationDirectory.getAbsolutePath() + "/" + FilenameUtils.removeExtension(fileName) + count + "." + FilenameUtils.getExtension(fileName));
            count++;
        }
        return file;
    }

    public static void printMatrixToTxt(double[][] results, String[] filenames, File outputDirectory, String matrixName) {
        File file = incrementFileNameIfNecessary(outputDirectory, matrixName);
        try {
            FileWriter export = new FileWriter(file.getPath());
            BufferedWriter br = new BufferedWriter(export);

            for (int i = 0; i < results.length; i++) {
                br.write(filenames[i] + ";");
                for (int j = 0; j < results.length; j++) {
                    if (j < results.length - 1)
                        br.write(results[i][j] + ";");
                    else
                        br.write(results[i][j] + "");
                }
                br.newLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Matrix saved to: " + file.getPath());
    }


    public static List<Pair<double[][], String[]>> readMatricesFromTxt() throws IOException {
        // choose distancematrix files
        File[] matrixFiles = chooseTXTFiles();

        List<Pair<double[][], String[]>> result = new ArrayList<>();
        for (File matrixFile: matrixFiles) {
            // read first line to get dimensions of matrix
            Scanner scanner = new Scanner(matrixFile);
            String line = scanner.nextLine();
            String[] splittedline = line.split(";");
            int size = splittedline.length - 1;
            double[][] matrix = new double[size][size];
            String[] filenames = new String[size];
            filenames[0] = splittedline[0];
            for (int i = 1; i < splittedline.length; i++) {
                matrix[0][i - 1] = Double.parseDouble(splittedline[i]);
            }

            // read cell by cell to build matrix and filenames-array
            int i = 1;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                splittedline = line.split(";");
                filenames[i] = splittedline[0];
                for (int j = 1; j < splittedline.length; j++) {
                    matrix[i][j - 1] = Double.parseDouble(splittedline[j]);
                }
                i++;
            }
            scanner.close();
            result.add(new Pair<>(matrix, filenames));
        }
        return result;
    }

    /**
     * @return a txt-file
     */
    private static File[] chooseTXTFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //accept files and directories as input
        FileNameExtensionFilter json = new FileNameExtensionFilter("txt", "txt");
        fileChooser.addChoosableFileFilter(json);                            // filter on txt files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only txt files
        fileChooser.setMultiSelectionEnabled(true);                       // accept only single file as input
        fileChooser.showOpenDialog(null);

        return fileChooser.getSelectedFiles();
    }

    public static File chooseDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //accept only directories as input
        fileChooser.showOpenDialog(null);
        return fileChooser.getSelectedFile();
    }

    public static String removeSWCFileExtensions(String filename) {
        if (filename.toLowerCase().endsWith(".cng.swc")) {
            filename = filename.substring(0, filename.toLowerCase().lastIndexOf(".cng.swc"));
        } else if (filename.toLowerCase().endsWith(".ims.swc")) {
            filename = filename.substring(0, filename.toLowerCase().lastIndexOf(".ims.swc"));
        } else if (filename.toLowerCase().endsWith(".swc")) {
            filename = filename.substring(0, filename.toLowerCase().lastIndexOf(".swc"));
        } else {
            System.out.println("wrong fileending: " + filename);
        }
        return filename;
    }

    public static List<File> getFilesForNeuronNames(List<String> selectedNeuronNames, File swcFileDirectory) {
        swcFiles = new ArrayList<>();
        Set<String> neuronNamesToFind = new HashSet<>(selectedNeuronNames);
        getFilesForNeuronNamesRec(swcFileDirectory, neuronNamesToFind);
        return swcFiles;
    }

    private static void getFilesForNeuronNamesRec(File directory, Set<String> neuronNamesToFind) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile : subFiles) {
            if (subFile.isFile()) {
                String fileNameWithoutSWCFileExtension = Utils.removeSWCFileExtensions(subFile.getName());
                if (neuronNamesToFind.contains(fileNameWithoutSWCFileExtension)) {
                    swcFiles.add(subFile);
                }
            } else {
                if (subFile.getName().equals("00_Ignore")) {
                    continue;
                }
                // recursively check next directory
                getFilesForNeuronNamesRec(subFile, neuronNamesToFind);
            }
        }
    }

    public static String[] getNeuronNamesForFiles(File[] files) {
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = removeSWCFileExtensions(files[i].getName());
        }
        return fileNames;
    }
}
