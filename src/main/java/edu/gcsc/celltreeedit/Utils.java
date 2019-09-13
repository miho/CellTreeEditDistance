package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


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
    public static List<File> chooseSWCFiles() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //accept files and directories as input
        FileNameExtensionFilter swc = new FileNameExtensionFilter("SWC", "SWC");
        fileChooser.addChoosableFileFilter(swc);                            // filter on swc files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only swc files
        fileChooser.setMultiSelectionEnabled(true);                       // accept multiple files as input
        fileChooser.showOpenDialog(jFrame);

        File[] chosenFiles;

        if (fileChooser.getSelectedFile().isFile())
            chosenFiles = fileChooser.getSelectedFiles();
        else {
            File folder = new File(fileChooser.getSelectedFile().getAbsolutePath());
            chosenFiles = folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".swc");
                }
            });
        }
        jFrame.dispose();
        return (chosenFiles != null) ? Arrays.asList(chosenFiles) : new ArrayList<>();
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
            file = new File(destinationDirectory.getAbsolutePath() + "/" + FilenameUtils.removeExtension(fileName) + "_" + count + "." + FilenameUtils.getExtension(fileName));
            count++;
        }
        return file;
    }

    public static void printMatrixToTxt(double[][] results, String[] filenames, File outputDirectory, String matrixName) {
        File file = incrementFileNameIfNecessary(outputDirectory, FilenameUtils.removeExtension(matrixName) + ".txt");
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
        System.out.println("Matrix saved to: " + file.getPath());
    }


    public static List<TEDResult> readMatricesFromTxt() throws IOException {
        // choose distancematrix files
        File[] matrixFiles = chooseTxtFiles();

        List<TEDResult> result = new ArrayList<>();
        for (File matrixFile: matrixFiles) {
            result.add(readMatrixFromTxt(matrixFile));
        }
        return result;
    }

    public static TEDResult readMatrixFromTxt(File matrixFile) throws FileNotFoundException {
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
        return new TEDResult(matrix, filenames, FilenameUtils.removeExtension(matrixFile.getName()));
    }

    public static List<Set<String>> readRArrayFromTxt(File arrayFile) throws FileNotFoundException {
        // skip first line
        Scanner scanner = new Scanner(arrayFile);
        String line = scanner.nextLine();

        // read cell by cell to build array
        int size = 2;
        String[] splittedline;
        Map<Integer, Set<String>> clusterMap = new HashMap<>();
//        List<Set<String>> clusters = new ArrayList<>();
        String name;
        int clusterId;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            splittedline = line.split(";");
            name = splittedline[0];
            clusterId = Integer.parseInt(splittedline[1]);
            if (clusterMap.containsKey(clusterId)) {
                clusterMap.get(clusterId).add(name);
            } else {
                clusterMap.put(clusterId, new HashSet<>(Arrays.asList(name)));
            }
        }
        scanner.close();
        return new ArrayList<>(clusterMap.values());
    }

    /**
     * @return a txt-file
     */
    private static File[] chooseTxtFiles() {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); //accept files and directories as input
        FileNameExtensionFilter json = new FileNameExtensionFilter("txt", "txt");
        fileChooser.addChoosableFileFilter(json);                            // filter on txt files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only txt files
        fileChooser.setMultiSelectionEnabled(true);                       // accept only single file as input
        fileChooser.showOpenDialog(jFrame);


        File[] chosenFiles;

        if (fileChooser.getSelectedFile().isFile())
            chosenFiles = fileChooser.getSelectedFiles();
        else {
            File folder = new File(fileChooser.getSelectedFile().getAbsolutePath());
            chosenFiles = folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
        }
        jFrame.dispose();
        return (chosenFiles != null) ? chosenFiles : new File[]{};
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

    // TODO: TESTEN
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

    public static boolean doublesAlmostEqual(double a, double b, double maxDiff, double maxUlpsDiff) {
        // Check if the numbers are really close -- needed when comparing numbers near zero as their difference could be greatly bigger than zero
        double absDiff = Math.abs(a - b);
        if (absDiff <= maxDiff)
            return true;

        // Different signs means they do not match.
        if (a < 0 != b < 0)
            return false;

        // find larger number
        a = Math.abs(a);
        b = Math.abs(b);
        double largest = (b > a) ? b : a;
        // get ulp of largest. when difference of doubles is smaller than ulp * allowed number of ulp
        return (absDiff <= Math.ulp(largest) * maxUlpsDiff);
    }

    public static void printTableToTXT(JTable table, File outputDirectory, String filename) {
        int rowSize = table.getRowCount();
        int colSize = table.getColumnCount();

        File file = incrementFileNameIfNecessary(outputDirectory, FilenameUtils.removeExtension(filename) + ".txt");
        try {
            FileWriter export = new FileWriter(file.getPath());
            BufferedWriter br = new BufferedWriter(export);

            for (int i = 0; i < rowSize; i++) {
                for (int j = 0; j < colSize; j++) {
                    if (j < colSize - 1)
                        br.write(table.getValueAt(i, j) + ";");
                    else
                        br.write(table.getValueAt(i, j) + "");
                }
                br.newLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Table saved to: " + file.getPath());
    }

    public static List<File> removeSwcFileDirectoryFromPaths(List<File> files, File swcFileDirectory) {
        List<File> newFiles = new ArrayList<>();
        for (File file : files) {
            newFiles.add(new File(file.getAbsolutePath().replace(swcFileDirectory.getAbsolutePath() + "/", "")));
        }
        return newFiles;
    }

    public static String[] printMetadataForFilenames(String[] filenames, Map<String, NeuronMetadataR> neuronMetadata, File outputDirectory, String metadataFilename) {
        String[] newFileNames = new String[filenames.length];

        UniqueMetadataContainer uniqueMetadataContainer = new UniqueMetadataContainer();
        UniqueMetadataContainer.UniqueMetadata uniqueMetadata;

        try {
            File file = new File(outputDirectory.getAbsolutePath() + "/" + metadataFilename + "_Metadata" + ".csv");
            FileWriter export = new FileWriter(file.getAbsolutePath());
            BufferedWriter br = new BufferedWriter(export);
            br.write("metadataId" + "; " + "neuronId"+ "; " + "filename" + "; " + "archive" + "; " + "species" + "; " + "brainRegions" + "; " + "cellTypes" + "; " + "ageClassification" + "; " + "minAge" + "; " + "maxAge" + "; " + "physicalIntegrity" + "; " + "domain" + "; " + "attributes" + "; " + "protocol" + "; " + "reconstructionSoftware" + "; " + "experimentCondition"); //  + "; " + "filesize"
            br.newLine();

            for (int i = 0; i < filenames.length; i++) {
                String filename = filenames[i];
                NeuronMetadataR neuronMetadataObject = neuronMetadata.get(filename);

                // create unique metadata of files
                uniqueMetadata = uniqueMetadataContainer.addNeuronMetadata(neuronMetadataObject);

                List<String> brainRegions = neuronMetadataObject.getBrainRegion();
                if (brainRegions == null) {
                    brainRegions = new ArrayList<>();
                }
                List<String> cellTypes = neuronMetadataObject.getCellType();
                if (cellTypes == null) {
                    cellTypes = new ArrayList<>();
                }
                List<String> experimentConditions = neuronMetadataObject.getExperimentCondition();
                if (experimentConditions == null) {
                    experimentConditions = new ArrayList<>();
                }
                newFileNames[i] = uniqueMetadata.getUniqueMetadataId() + ", " + neuronMetadataObject.getArchive() + ", " + neuronMetadataObject.getNeuronId();
//                    String formattedSize = String.format("%.2f", (double) FileUtils.sizeOf(swcFile)/(1024*1024));
                br.write(uniqueMetadata.getUniqueMetadataId() + "; " + neuronMetadataObject.getNeuronId() + "; " + filename + "; " + neuronMetadataObject.getArchive() + "; " + neuronMetadataObject.getSpecies() + "; " + brainRegions.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", ")) + "; " + cellTypes.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", ")) + "; " + neuronMetadataObject.getAgeClassification() + "; " + neuronMetadataObject.getMinAge() + "; " + neuronMetadataObject.getMaxAge() + "; " + neuronMetadataObject.getPhysicalIntegrity() + "; " + neuronMetadataObject.getDomain() + "; " + neuronMetadataObject.getAttributes() + "; " + neuronMetadataObject.getProtocol() + "; " + neuronMetadataObject.getReconstructionSoftware() + "; " + experimentConditions.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", "))); //  + "; " + formattedSize + "MB"
                br.newLine();
            }
            br.close();
            System.out.println("File saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFileNames;
    }

    public static void copyFile(File file, String outputDirectory) {
        // move file to subdirectory
        try {
            // copy file to defined directory
            Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(outputDirectory + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File: " + file.getAbsolutePath() + "  Copied to: " + outputDirectory + "/" + file.getName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
