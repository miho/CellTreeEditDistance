package edu.gcsc.celltreeedit;

import com.apporiented.algorithm.clustering.visualization.ClusterColorRegex;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.ClusterAnalysis.ClusteringAnalyzer;
import edu.gcsc.celltreeedit.Clustering.DendrogramCreator;
import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.Lucene.CLI;
import edu.gcsc.celltreeedit.Lucene.LuceneIndexWriter;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.TEDCalculation.CellTreeEditDistance;
import edu.gcsc.celltreeedit.TEDCalculation.TEDClusterResult;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
import javafx.scene.control.Cell;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    private static AppProperties appProperties = AppProperties.getInstance();

    private static final List<ClusterColorRegex> clusterColorRegexes = Arrays.asList(
            new ClusterColorRegex(Pattern.compile("^[123456],.*"), Color.BLACK),
            new ClusterColorRegex(Pattern.compile("^.*"), new Color(35, 106, 185)),
            new ClusterColorRegex(Pattern.compile("^(11|12|13|14|15|16),.*"), new Color(186, 26, 70)),
            new ClusterColorRegex(Pattern.compile("^[x],.*"), new Color(68, 141, 118)),
            new ClusterColorRegex(Pattern.compile("^([789]|10),.*"), new Color(118, 60, 118)),
            new ClusterColorRegex(Pattern.compile("^6,.*"), Color.GRAY),
            new ClusterColorRegex(Pattern.compile("^7,.*"), new Color(160, 100, 0))
    );

    /**
     * Make sure structure of BaseDirectory is correct.
     * Structure of BaseDirectory:
     * ProgramData
     * /Data
     * /Metadata
     * /SWCFiles
     * /Output
     * /Test
     * /TestClustering
     * /TestSWCFiles
     * /TestWorkingDir
     * /WorkingDir
     * /someSWCFileForTEDCalculation.json
     * <p>
     * case=0: preprocess SWC-Directory.
     * BaseDirectory must be given.
     * case=1: query Metadata with Lucene and create .json file.
     * BaseDirectory must be given. json-filename optional.
     * case=2: choose some files from filedialog, which should be used for TED-calculation. creates .json file.
     * destinationDirectory must be given. json-filename optional. CURRENTLY NOT SUPPORTED!
     * case=3: query Metadata by unique metadata. UniqueMetadata must be adjusted inside of the program in main-function. create .json file.
     * BaseDirectory must be given. json-filename optional.
     * case=4: calculate TED-matrix.
     * directory to json-file must be given. matrix-name optional. json-file must be located directly inside /ProgramData
     * case=5: calculate TED-matrix and create dendrogram.
     * directory to json-file must be given. matrix-name optional. json-file must be located directly inside /ProgramData
     * case=6: calculate Dendrograms for TED-matrices which have already been calculated.
     * BaseDirectory must be given.
     * case=7: analyze clusterings of TED-matrices. shows relative partitioning errors of the result.
     * BaseDirectory must be given.
     * case=8: do whatever is defined in the function-body. used for development
     *
     * @param args
     * @throws IOException
     * @throws ParseException
     */
    public static void main(String[] args) throws IOException, ParseException {


        CommandLineParsing.parseArguments(args);

        switch (appProperties.getCalcType()) {
            case 0:
                preprocessSWCDirectory();
                break;
            case 1:
                queryLucene();
                break;
            case 2:
                queryByUniqueMetadata();
                break;
            case 3:
                calculateTEDMatrix();
                break;
            case 4:
                calculateTEDMatrixAndDendrogram();
                break;
            case 5:
                calculateDendrogram();
                break;
            case 6:
                calculateTEDMatricesForJsonFilesWithAllLabels();
                break;
            case 7:
                calculateDendrogramsForTEDMatrices();
                break;
            case 8:
                calculateTEDMatrixAndDendrogramByFileDialog();
                break;
            case 9:
                analyzeClusteringOfTEDMatrices();
                break;
            case 10:
                printMetadataOfJsonFiles();
                break;
            case 11:
                copySWCFilesOfJsonFilesToOutput();
                break;
            case 12:
                calculateTEDMatrixOnCluster();
                break;
            case 13:
                reassembleClusterMatricesToTxt();
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }

    /**
     * Starts preprocessing of SWCDirectory
     *
     * @throws IOException if no json-files containing neuronMetadata are found
     */
    private static void preprocessSWCDirectory() throws IOException {
        System.out.println("> Starting preprocessing \n");
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(appProperties.getMetadataDirectory());

        // preprocess SWC-Directory
        SWCPreprocessing swcPreprocessing = new SWCPreprocessing();
        swcPreprocessing.preprocessSWCDirectory(neuronMetadata, appProperties.getSwcFileDirectory());
    }

    /**
     * Starts CLI for querying SWC-Files using Lucene
     *
     * @throws IOException if no json-files containing neuronMetadata are found
     */
    private static void queryLucene() throws IOException {
        System.out.println("> Starting Lucene");
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());

        File indexDirectory = new File(appProperties.getWorkingDirectory() + "/LuceneIndex");
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(indexDirectory);
        luceneIndexWriter.createIndex(neuronMetadata);
        CLI.startCLI(indexDirectory, appProperties.getOutputDirectory(), appProperties.getSwcFileDirectory(), appProperties.getNameOutput());
    }


    // for querying a predefined combination
    private static void queryByUniqueMetadata() throws IOException {
        System.out.println("> Starting query by predefined UniqueMetadata\n");
        int noOfNeuronsPerType = 143;

        // define which uniqueMetadata-groups shall be used
        UniqueMetadataContainer predefinedUniqueMetadataContainer = new UniqueMetadataContainer();
        Set<UniqueMetadataContainer.UniqueMetadata> predefinedUniqueMetadata = new HashSet<>();
        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject("", "", "rat", new HashSet<>(Arrays.asList("hippocampus")), new HashSet<>(Arrays.asList("principal cell", "pyramidal"))));
        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject("", "", "rat", new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), new HashSet<>(Arrays.asList("principal cell", "granule"))));

        // get existing neuronMetadata and put it in existingUniqueMetadataContainer --> neuronNames for each uniqueMetadata-group --> select neurons randomly from uniqueMetadata-group
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());
        UniqueMetadataContainer existingUniqueMetadataContainer = new UniqueMetadataContainer();
        // add all existing neuronMetadata to UniqueMetadata
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            existingUniqueMetadataContainer.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }

//         select neurons depending on typeCount and input-variables
        List<String> selectedNeuronNames = new ArrayList<>();
        for (UniqueMetadataContainer.UniqueMetadata uniqueMetadata : predefinedUniqueMetadata) {
            // select noOfNeuronsPerType neurons randomly
            selectedNeuronNames.addAll(pickNRandom(existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNeuronNames(), noOfNeuronsPerType));
            System.out.println("species: " + uniqueMetadata.getSpecies() + "; " + "brainRegion" + String.join(", ", uniqueMetadata.getBrainRegions()) + "; " + "cellTypes: " + String.join(", ", uniqueMetadata.getCellTypes()) + "; " + "noOfNeurons: " + existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNoOfNeurons() + "; " + "noOfArchives: " + existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getArchives().size());
        }

        List<File> selectedNeuronFiles = Utils.getFilesForNeuronnames(selectedNeuronNames, appProperties.getSwcFileDirectory());
        // write to json
        JsonUtils.writeToJSON(selectedNeuronFiles, "queried by uniqueMetadata", appProperties.getSwcFileDirectory(), appProperties.getOutputDirectory(), appProperties.getNameOutput());
    }

    private static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private static void calculateTEDMatrix() throws IOException {
        System.out.println("> Starting TED calculation\n");
        TEDResult result = calculateTEDMatrixForJson();
        String nameOutput = Utils.renameMatrixOutputWithJson(appProperties.getNameOutput(), appProperties.getFileInput().getName());
        Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), nameOutput);
    }

    private static TEDResult calculateTEDMatrixForJson() throws IOException {
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        File[] files = JsonUtils.parseJsonToFiles(appProperties.getFileInput());
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
        }
        return cellTreeEditDistance.compareFilesFromFiles(files, appProperties.getLabel());
    }

    private static void calculateTEDMatrixAndDendrogram() throws IOException {
        System.out.println("> Starting TED calculation and Dendrogram calculation\n");
        TEDResult result = calculateTEDMatrixForJson();
        String nameOutput = Utils.renameMatrixOutputWithJson(appProperties.getNameOutput(), appProperties.getFileInput().getName());
        if (appProperties.isSaveOutput()) {
            nameOutput = FilenameUtils.removeExtension(Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), nameOutput).getName());
        }
        DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), nameOutput, appProperties.isRenameDendrogram(), appProperties.isSaveOutput(), clusterColorRegexes);
    }


    private static void calculateDendrogram() throws IOException {
        System.out.println("> Starting Dendrogram calculation\n");
        TEDResult result = Utils.readMatrixFromTxt(appProperties.getFileInput());
        String nameOutput = (appProperties.getNameOutput().isEmpty()) ? result.getName() : appProperties.getNameOutput();
        DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), nameOutput, appProperties.isRenameDendrogram(), appProperties.isSaveOutput(), clusterColorRegexes);
    }

    // method to do some custom things which program should not be able to do in the end
    private static void calculateTEDMatricesForJsonFilesWithAllLabels() throws IOException {
        System.out.println("> Starting TED calculation for Json-Files with all labels");

        File[] jsonFiles = Utils.chooseJSONFiles();

        for (File jsonFile : jsonFiles) {
            System.out.println("\nJsonFile: " + jsonFile.getName());
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);
            for (int i = 0; i < files.length; i++) {
                files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            CellTreeEditDistance cellTreeEditDistance;
            TEDResult result;
            String labelNumber;
            for (int i = 1; i < 25; i += 1) {
                Date date = new Date();
                System.out.println(dateFormat.format(date) + " selected Label " + i);
                cellTreeEditDistance = new CellTreeEditDistance();
                result = cellTreeEditDistance.compareFilesFromFiles(files, i);
                if (i < 10) {
                    labelNumber = "0" + i;
                } else {
                    labelNumber = Integer.toString(i);
                }
                Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), FilenameUtils.removeExtension(jsonFile.getName()) + "_Matrix_Label" + labelNumber + ".txt");
            }
        }
    }

    private static void calculateDendrogramsForTEDMatrices() throws IOException {
        System.out.println("> Starting Dendrogram calculation for TEDMatrices");

        List<TEDResult> results = Utils.readMatricesFromTxt();
        for (TEDResult result : results) {
            DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), result.getName(), appProperties.isRenameDendrogram(), appProperties.isSaveOutput(), clusterColorRegexes);
        }
    }

    private static void calculateTEDMatrixAndDendrogramByFileDialog() {
        System.out.println("> Starting TED calculation and Dendrogram calculation by file-dialog");
        // Files in File[] speichern
        File[] files = Utils.chooseSWCFiles();
        File outputDirectory = new File("/" + FilenameUtils.getPath(files[0].getAbsolutePath()));
        String nameOutput = Utils.renameMatrixOutput(appProperties.getNameOutput());
        // calculateTED
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        TEDResult result = cellTreeEditDistance.compareFilesFromFiles(files, appProperties.getLabel());
        if (appProperties.isSaveOutput()) {
            nameOutput = FilenameUtils.removeExtension(Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), outputDirectory, nameOutput).getName());
        }
        DendrogramCreator.createDendrogram(result, outputDirectory, nameOutput, appProperties.isSaveOutput(), clusterColorRegexes);
    }

    /**
     * Analyzes Clusterings of multiple matrices and displays the results.
     * TODO: Test and improve
     *
     * @throws IOException
     */
    private static void analyzeClusteringOfTEDMatrices() throws IOException {
        System.out.println("> Starting Analysis of Clustering for TEDMatrices");

        List<TEDResult> results = Utils.readMatricesFromTxt();

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(appProperties.getMetadataDirectory());

        for (TEDResult result : results) {
            ClusteringAnalyzer.analyzeClusteringOfTEDResult(result, neuronMetadata);
        }
    }


    public static void printMetadataOfJsonFiles() throws IOException {
        System.out.println("> Starting print Metadata for Json-files");
        // input: jsonFiles
        // output: file with Filenames to metadata mapping
        // filename, archive, species, brainRegions, cellTypes, ageClassification, minAge, maxAge, physicalIntegrity, domain, attributes, protocol, reconstructionSoftware, experimentCondition, filesize
        File[] jsonFiles = Utils.chooseJSONFiles();

        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());

        for (File jsonFile : jsonFiles) {
            System.out.println("\nJsonFile: " + jsonFile.getName());
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);
            String[] filenames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                filenames[i] = Utils.removeSWCFileExtensions(files[i].getName());
            }
            Utils.printMetadataForFilenames(filenames, neuronMetadata, appProperties.getOutputDirectory(), FilenameUtils.removeExtension(jsonFile.getName()));
        }
    }

    public static void copySWCFilesOfJsonFilesToOutput() throws IOException {
        System.out.println("> Starting copy of SWC-Files from Json to Output-directory");
        File[] jsonFiles = Utils.chooseJSONFiles();
        String outputDirectory;
        for (File jsonFile : jsonFiles) {
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);

            for (int i = 0; i < files.length; i++) {
                files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
            }
            // get directory name from jsonFileName
            // create Directory
            String newFolder = FilenameUtils.removeExtension(FilenameUtils.getName(jsonFile.getName()));
            outputDirectory = appProperties.getOutputDirectory().getAbsolutePath() + "/" + newFolder;
            new File(outputDirectory).mkdirs();

            FileWriter export = new FileWriter(outputDirectory + "/" + "relativePaths");
            BufferedWriter br = new BufferedWriter(export);

            for (int i = 0; i < files.length; i++) {
                // copy File to directory
                Utils.copyFile(files[i], outputDirectory);
                br.write("./" + newFolder + "/" + files[i].getName() + " ");
            }
            br.close();
        }
    }

    public static void calculateTEDMatrixOnCluster() throws IOException {
        System.out.println("> Starting calculation of TEDMatrix on Cluster");

        File[] files = JsonUtils.parseJsonToFiles(appProperties.getFileInput());
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files, appProperties.getRows(), appProperties.getIteration(), appProperties.getLabel(), appProperties.getSwcFileDirectory());

        Utils.printClusterMatrixToTxt(tedClusterResult, appProperties.getFileInput(), appProperties.getOutputDirectory(), "Clustermatrix_" + appProperties.getIteration());
    }

    public static void reassembleClusterMatricesToTxt() throws IOException {
        File[] files = JsonUtils.parseJsonToFiles(appProperties.getFileInput());
        Utils.reassembleClusterMatrixToTxt(appProperties.getDirectoryInput(), files, appProperties.getOutputDirectory(), "ReassembledMatrix");
    }
}
