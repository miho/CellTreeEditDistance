package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.TEDCalculation.TEDClusterResult;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
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

    //##################################################################################################################
    // Functions for file choosing
    /**
     * @return an array of swc-files from filedialog
     */
    public static File[] chooseSWCFiles() {
        return chooseFiles("swc");
    }

    /**
     * @return an array of json-files from filedialog
     */
    public static File[] chooseJSONFiles() {
        return chooseFiles("json");
    }

    /**
     * @return an array of txt-files from filedialog
     */
    public static File[] chooseTxtFiles() {
        return chooseFiles("txt");
    }

    private static File[] chooseFiles(String extensionFilter) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionFilter, extensionFilter);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.showOpenDialog(jFrame);
        File[] chosenFiles = fileChooser.getSelectedFiles();
        jFrame.dispose();
        return chosenFiles;
    }

    //##################################################################################################################
    // IO-Functions for reading or writing files

    public static File printMatrixToTxt(double[][] results, String[] filenames, File outputDirectory, String matrixName) {
        matrixName = (matrixName.isEmpty()) ? "Matrix" : FilenameUtils.removeExtension(matrixName);
        File file = incrementFileNameIfNecessary(outputDirectory, matrixName + ".txt");
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
        return file;
    }

    public static File printClusterMatrixToTxt(TEDClusterResult tedClusterResult, File fileInput, File outputDirectory, String matrixName) {
        matrixName = (matrixName.isEmpty()) ? "Matrix" : FilenameUtils.removeExtension(matrixName);
        double[][] result = tedClusterResult.getMatrix();
        int noOfRows = result.length;
        int noOfCols = result[0].length;
        File file = incrementFileNameIfNecessary(outputDirectory, matrixName + ".txt");
        try {
            FileWriter export = new FileWriter(file.getPath());
            BufferedWriter br = new BufferedWriter(export);
            br.write("# " + fileInput.getName());
            br.newLine();
            br.write("iteration=" + tedClusterResult.getIteration());
            br.newLine();
            br.write("row=" + tedClusterResult.getRow());
            br.newLine();
            br.write("col=" + tedClusterResult.getCol());
            br.newLine();
            for (int i = 0; i < noOfRows; i++) {
                for (int j = 0; j < noOfCols; j++) {
                    if (j < noOfCols - 1)
                        br.write(result[i][j] + ";");
                    else
                        br.write(result[i][j] + "");
                }
                br.newLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Matrix saved to: " + file.getPath());
        return file;
    }

    public static void reassembleClusterMatrixToTxt(File[] matrixFiles, File[] files, File outputDirectory, String matrixname) throws IOException {

        int filesLength = files.length;
        String[] filenames = Utils.getNeuronnamesForFiles(files);

        // write result from little matrix into big matrix depending on row, col, filesLength
        double[][] reassembledMatrix = new double[filesLength][filesLength];

        for (File matrixFile : matrixFiles) {
            TEDClusterResult tedClusterResult = readClusterMatrixFromTxt(matrixFile);
            double[][] result = tedClusterResult.getMatrix();
            int row = tedClusterResult.getRow();
            int col = tedClusterResult.getCol();
            // go through result m is row
            for (int m = 0; m < result.length; m++) {
                int rowToWrite = row + m;
                // go through columns of result. first column is empty so skip it
                for (int n = 1; n < result[0].length; n++) {
                    if (result[m][n] == 0d) {
                        continue;
                    }
                    // col is already the column of the first comparison
                    int colToWrite = col + n - 1;
                    // is colToWrite within matrix boundaries? if not begin at the first column again
                    if (colToWrite >= filesLength) {
                        colToWrite -= filesLength;
                        reassembledMatrix[colToWrite][rowToWrite] = result[m][n];
                    } else {
                        reassembledMatrix[rowToWrite][colToWrite] = result[m][n];
                    }
                }
            }
        }
        printMatrixToTxt(reassembledMatrix, filenames, outputDirectory, matrixname);
    }

    public static TEDClusterResult readClusterMatrixFromTxt(File matrixFile) throws IOException {

        Scanner scanner = new Scanner(matrixFile);
        String line = scanner.nextLine();
        while (line.startsWith("#")) {
            line = scanner.nextLine();
        }
        int iteration = Integer.parseInt(line.replace("iteration=", ""));
        line = scanner.nextLine();
        int row = Integer.parseInt(line.replace("row=", ""));
        line = scanner.nextLine();
        int col = Integer.parseInt(line.replace("col=", ""));
        line = scanner.nextLine();
        String[] splittedline = line.split(";");
        int size = splittedline.length;
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            matrix[0][i] = Double.parseDouble(splittedline[i]);
        }
        int i = 1;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            splittedline = line.split(";");
            for (int j = 0; j < splittedline.length; j++) {
                matrix[i][j] = Double.parseDouble(splittedline[j]);
            }
            i++;
        }
        scanner.close();
        double[][] newmatrix = new double[i][size];
        for (int k = 0; k < newmatrix.length; k++) {
            newmatrix[k] = matrix[k];
        }
        return new TEDClusterResult(iteration, row, col, newmatrix);
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

    public static void printTableToTxt(JTable table, File outputDirectory, String filename) {
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

    //##################################################################################################################
    // Filename-Functions

    public static File incrementFileNameIfNecessary(File outputDirectory, String fileName) {
        outputDirectory.mkdirs();
        File file = new File(outputDirectory.getAbsolutePath() + "/" + fileName);
        // increment fileName if necessary
        int count = 1;
        while (file.exists()) {
            file = new File(outputDirectory.getAbsolutePath() + "/" + FilenameUtils.removeExtension(fileName) + "_" + count + "." + FilenameUtils.getExtension(fileName));
            count++;
        }
        return file;
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

    public static List<File> removeSwcFileDirectoryFromPaths(List<File> files, File swcFileDirectory) {
        List<File> newFiles = new ArrayList<>();
        for (File file : files) {
            newFiles.add(new File(file.getAbsolutePath().replace(swcFileDirectory.getAbsolutePath() + "/", "")));
        }
        return newFiles;
    }

    public static String renameMatrixOutput(String appPropertiesName) {
        return (appPropertiesName.isEmpty() || appPropertiesName.equals("Matrix")) ? "Matrix" : appPropertiesName + "_Matrix";
    }

    public static String renameMatrixOutputWithJson(String appPropertiesName, String jsonName) {
        return (appPropertiesName.isEmpty()) ? FilenameUtils.removeExtension(jsonName) + "_Matrix" : appPropertiesName + "_Matrix";
    }


    //##################################################################################################################
    // Functions for changing Filenames to Neuronnames or otherwise

    // TODO: TESTEN
    public static List<File> getFilesForNeuronnames(List<String> selectedNeuronNames, File swcFileDirectory) {
        swcFiles = new ArrayList<>();
        Set<String> neuronNamesToFind = new HashSet<>(selectedNeuronNames);
        getFilesForNeuronnamesRec(swcFileDirectory, neuronNamesToFind);
        return swcFiles;
    }

    private static void getFilesForNeuronnamesRec(File directory, Set<String> neuronNamesToFind) {
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
                getFilesForNeuronnamesRec(subFile, neuronNamesToFind);
            }
        }
    }

    public static String[] getNeuronnamesForFiles(File[] files) {
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = removeSWCFileExtensions(files[i].getName());
        }
        return fileNames;
    }


    //##################################################################################################################
    // Other functions

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
