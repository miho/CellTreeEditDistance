package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import javafx.util.Pair;

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
    private static AppProperties appProperties = AppProperties.getInstance();

    /**
     * @return a list of files which were selected
     */
    public static File[] choose() {
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
            File[] selectedFiles = folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".swc");
                }
            });
            return selectedFiles;
        }
    }

    /**
     * @return a list of json-files which were selected
     */
    public static File[] chooseJson() {
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
            File[] selectedFiles = folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });
            return selectedFiles;
        }
    }

    public static void printToTxt(double[][] results, String[] filenames, File exportDirectory, String fileName) {
//        System.out.println("Please choose the directory where you want to save the file");
//        JFileChooser save= new JFileChooser();
//        save.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        FileNameExtensionFilter text = new FileNameExtensionFilter("Text file (*.txt)","txt");
//        save.addChoosableFileFilter(text);
//        save.setAcceptAllFileFilterUsed(false);
//        save.showSaveDialog(null);
//        String path=save.getSelectedFile().getAbsolutePath();
        exportDirectory.mkdirs();
        String path = exportDirectory.getAbsolutePath() + "/" + fileName;

        try {
            FileWriter export = new FileWriter(path);
            BufferedWriter br = new BufferedWriter(export);
            // br.write("#;");
            //for(int i=0;i<results.length;i++){                              //kopfzeile
            //  if(i<results.length-1)
            //    br.write(filenames[i]+";");
            //else
            //  br.write(filenames[i]);
            //}
            //br.newLine();
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
        System.out.print("Done!");
    }


    public static List<Pair<double[][], String[]>> readMatricesFromTxt() throws IOException {
        // choose distancematrix files
        File[] matrixFiles = chooseTxt();

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
    public static File[] chooseTxt() {
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

    public static List<File> getFilesForNeuronNames(List<String> selectedNeuronNames) {
        swcFiles = new ArrayList<>();
        Set<String> neuronNamesToFind = new HashSet<>(selectedNeuronNames);
        getFilesForNeuronNamesRec(appProperties.getSwcFileDirectory(), neuronNamesToFind);
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
