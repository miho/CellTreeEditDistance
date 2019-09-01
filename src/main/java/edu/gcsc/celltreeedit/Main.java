package edu.gcsc.celltreeedit;

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
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    private static AppProperties appProperties = AppProperties.getInstance();

    /**
     * Make sure structure of BaseDirectory is correct.
     * Structure of BaseDirectory:
     * ProgramData
     *      /Data
     *          /Metadata
     *          /SWCFiles
     *      /Output
     *      /Test
     *          /TestClustering
     *          /TestSWCFiles
     *          /TestWorkingDir
     *      /WorkingDir
     *      /someSWCFileForTEDCalculation.json
     *
     * case=0: preprocess SWC-Directory.
     *          BaseDirectory must be given.
     * case=1: query Metadata with Lucene and create .json file.
     *          BaseDirectory must be given. json-filename optional.
     * case=2: choose some files from filedialog, which should be used for TED-calculation. creates .json file.
     *          destinationDirectory must be given. json-filename optional. CURRENTLY NOT SUPPORTED!
     * case=3: query Metadata by unique metadata. UniqueMetadata must be adjusted inside of the program in main-function. create .json file.
     *          BaseDirectory must be given. json-filename optional.
     * case=4: calculate TED-matrix.
     *          directory to json-file must be given. matrix-name optional. json-file must be located directly inside /ProgramData
     * case=5: calculate TED-matrix and create dendrogram.
     *          directory to json-file must be given. matrix-name optional. json-file must be located directly inside /ProgramData
     * case=6: calculate Dendrograms for TED-matrices which have already been calculated.
     *          BaseDirectory must be given.
     * case=7: analyze clusterings of TED-matrices. shows relative partitioning errors of the result.
     *          BaseDirectory must be given.
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
                queryByFileDialog();
                break;
            case 3:
                queryByUniqueMetadata();
                break;
            case 4:
                calculateTEDMatrix();
                break;
            case 5:
                calculateTEDMatrixAndDendrogram();
                break;
            case 6:
                calculateDendrogramsForTEDMatrices();
                break;
            case 7:
                analyzeClusteringOfTEDMatrices();
                break;
            case 8:
                doWhateverIsInMyFunctionBody();
                break;
            case 9:
                doWhateverIsInMyFunctionBody02();
                break;
            case 10:
                printMetadataForJson();
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }

    /**
     * Starts preprocessing of SWCDirectory
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
        CLI.startCLI(indexDirectory, appProperties.getBaseDirectory(), appProperties.getOutputDirectory(), appProperties.getSwcFileDirectory(), appProperties.getJsonName());
    }

    /** TODO: DELETE??
     * Lets User choose either a directory containing swc-Files or multiple swc-Files. SWC-File-paths are written to json-File.
     * @throws IOException if json-file could not be written to hard drive
     */
    private static void queryByFileDialog() throws IOException {
        System.out.println("> Starting file dialog");
        List<File> files = Utils.chooseSWCFiles();
        // write to json
        JsonUtils.writeToJSON(files,"queried by file dialog", appProperties.getDestinationDirectory(), appProperties.getJsonName());
    }

    // TODO: Inhalt auslagern??, uniqueMetadata Anzahl der Neuronen stimmt nicht
    // for querying a predefined combination
    private static void queryByUniqueMetadata() throws IOException {
        System.out.println("Starting query by predefined UniqueMetadata\n");
        int noOfNeuronsPerType = 143;

        // define which uniqueMetadata-groups shall be used
        UniqueMetadataContainer predefinedUniqueMetadataContainer = new UniqueMetadataContainer();
        Set<UniqueMetadataContainer.UniqueMetadata> predefinedUniqueMetadata = new HashSet<>();
        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("hippocampus")), "rat", "", ""));
        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "granule")), new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), "rat", "", ""));

//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "granule")), new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("stellate", "interneuron")), new HashSet<>(Arrays.asList("amygdala", "basolateral amygdala complex")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 2")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 3")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5a")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5b")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Horizontal", "interneuron", "neurogliaform")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 1")), "rat", "", ""));
//        predefinedUniqueMetadata.add(predefinedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Star", "pyramidal", "interneuron")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));

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
            System.out.println("species: " + uniqueMetadata.getSpecies() + "; " + "brainRegion" + String.join(", ", uniqueMetadata.getBrainRegion()) + "; " + "cellTypes: " + String.join(", ", uniqueMetadata.getCellTypes()) + "; " + "noOfNeurons: " + existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNoOfNeurons() + "; " + "noOfArchives: " + existingUniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getArchives().size());
        }

        List<File> selectedNeuronFiles = Utils.getFilesForNeuronNames(selectedNeuronNames, appProperties.getSwcFileDirectory());
        // write to json
        JsonUtils.writeToJSON(selectedNeuronFiles, "queried by uniqueMetadata", appProperties.getSwcFileDirectory(), appProperties.getOutputDirectory(), appProperties.getJsonName());
    }

    private static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private static TEDResult calculateTEDMatrix() throws IOException {
        System.out.println("> Starting TED calculation\n");
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        File[] files = JsonUtils.parseJsonToFiles(appProperties.getJsonFile());
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
        }
        TEDResult result = cellTreeEditDistance.compareFilesFromFiles(files, 22);
        Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), appProperties.getMatrixName());
        return result;
    }

    private static void calculateTEDMatrixAndDendrogram() throws IOException {
        System.out.println("> Starting TED calculation and Dendrogram calculation\n");
        TEDResult result = calculateTEDMatrix();
        DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), result.getName(), appProperties.isReplaceDendrogramNames(), appProperties.isSaveOutput());
    }

    private static void calculateDendrogramsForTEDMatrices() throws IOException {
        System.out.println("> Starting Dendrogram calculation for TEDMatrices");
        List<TEDResult> results = Utils.readMatricesFromTxt();
        for (TEDResult result : results) {
            DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), result.getName(), appProperties.isReplaceDendrogramNames(), appProperties.isSaveOutput());
        }
    }

    /**
     * Analyzes Clusterings of multiple labels and displays the results.
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

    // for querying the mostCommon neurontypes
//    private static void queryByUniqueMetadata() throws IOException {
//        // TODO: put in AppProperties and make adjustable from commandline?
//        int noOfTypes = 40;
//        int noOfNeuronsPerType = 25;
//
//        // put metadata in hashMap
//        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
//        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapExistingFromDirectory(appProperties.getMetadataDirectory());
//        // add all metadata to UniqueMetadata
//        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
//            UniqueMetadata.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
//        }
//        List<UniqueMetadata> sortedUniqueMetadata = new ArrayList<>(UniqueMetadata.getUniqueMetadataMap().keySet());
////         sort uniqueMetadata
//        sortedUniqueMetadata.sort(Comparator.comparingInt(UniqueMetadata::getNoOfNeurons).reversed());
//
////         select neurons depending on typeCount and input-variables
//        List<String> selectedNeuronNames = new ArrayList<>();
//        int k = 1;
//        for (UniqueMetadata uniqueMetadata : sortedUniqueMetadata) {
//            if (k > noOfTypes) {
//                break;
//            }
//            // select noOfNeuronsPerType neurons randomly
//            selectedNeuronNames.addAll(pickNRandom(uniqueMetadata.getNeuronNames(), noOfNeuronsPerType));
//            System.out.println(uniqueMetadata.getSpecies() + ";" + String.join(", ", uniqueMetadata.getBrainRegion()) + ";" + String.join(", ", uniqueMetadata.getCellTypes()) + ";" + uniqueMetadata.getNoOfNeurons() + ";" + uniqueMetadata.getArchives().size());
//            k += 1;
//        }
//
//        // write to json
//        JsonUtils.writeToJSON(selectedNeuronNames, appProperties.getOutputDirectory());
//    }

    // method to do some custom things which program should not be able to do in the end
    private static void doWhateverIsInMyFunctionBody() throws IOException {
        System.out.println("> Starting doWhateverIsInMyFunctionBody()");

//        File[] files = Utils.chooseSWCFiles().toArray(new File[1]);
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
//        // calculate distances of Selection of SWC-Files for every possible label
//        File[] files = JsonUtils.parseJsonToFiles(new File("/media/exdisk/Sem06/BA/ProgramData/swcFiles.json"));
//        for (int i = 0; i < files.length; i++) {
//            files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
//        }
            String labelNumber;
            for (int i = 1; i < 23; i += 1) {
                Date date = new Date();
                System.out.println(dateFormat.format(date) + " selected Label " + i);
                cellTreeEditDistance = new CellTreeEditDistance();
                result = cellTreeEditDistance.compareFilesFromFiles(files, i);
                if (i < 10) {
                    labelNumber = "0" + i;
                } else {
                    labelNumber = Integer.toString(i);
                }
                Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), "Matrix_" + FilenameUtils.removeExtension(jsonFile.getName()) + "_Label" + labelNumber + ".txt");
            }

        }


        // calculate distances of 1000 of these SWC-Files
//        Date date = new Date();
//        System.out.println(dateFormat.format(date) + " 1000 mostCommon");
//        cellTreeEditDistance = new CellTreeEditDistance();
//        result = cellTreeEditDistance.compareFilesFromFiles(JsonUtils.parseJsonToFiles(new File("/media/exdisk/Sem06/BA/Testlaeufe/swcFiles_mostCommon_1000_40Types_25Each.json")), 9);
//        Utils.printMatrixToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), "Matrix_mostCommon_1000_40Types_25Each.txt");
    }

    private static void doWhateverIsInMyFunctionBody02() throws IOException {
        System.out.println("> Starting doWhateverIsInMyFunctionBody02()");

        Map<String, String> filenameMapping01A = new HashMap<String, String>() {{
            put("0003-4_4", "1, Pyramidal");
            put("DYNC1I1_knockdown", "2, Pyramidal");
            put("01-03-c1", "3, Pyramidal");
            put("cell49-CA3", "4, Pyramidal");
            put("19-4-c2", "5, Pyramidal");
            put("27-02-c3", "6, Pyramidal");
            put("0002-8_2", "7, Pyramidal");
            put("11--Exp", "8, Pyramidal");
            put("0001-2_4", "9, Pyramidal");
            put("15-02-13_cell1_Ventral", "10, Pyramidal");
            put("20--Exp", "11, Pyramidal");
            put("EPOcCA3c_45c", "12, Pyramidal");
            put("BDLHD-HP5-j-2", "13, Pyramidal");
            put("sh2-1-1", "14, Pyramidal");
            put("sh1-20x-2", "15, Pyramidal");
            put("0002-2_2", "16, Pyramidal");
            put("052714C_37", "17, Pyramidal");
            put("0006-3_4", "18, Pyramidal");
            put("c12363", "19, Pyramidal");
            put("K-09", "20, Pyramidal");
            put("R493B1", "21, Pyramidal");
            put("HC-Q1B", "22, Pyramidal");
            put("K-Slide-6-section-3-neuron-2-CA1-REVISED-profile", "23, Pyramidal");
            put("EPOcCA3c_41a", "24, Pyramidal");
            put("EPOcCA3c_41b", "25, Pyramidal");
            put("slice-5_6", "1, NOT-Pyramidal");
            put("slice-8_4", "2, NOT-Pyramidal");
            put("B4-CA1-L-E63x1zACR2", "3, NOT-Pyramidal");
            put("C3-CA1-L-A63x1zACR5", "4, NOT-Pyramidal");
            put("082203a", "5, NOT-Pyramidal");
            put("CA3cell-2_1", "6, NOT-Pyramidal");
            put("SLICE-1_32", "7, NOT-Pyramidal");
            put("GFP_05", "8, NOT-Pyramidal");
            put("N2C1-1-3012_Maximumintensityprojection", "9, NOT-Pyramidal");
            put("CCK-basket-16-12-09-1a", "10, NOT-Pyramidal");
            put("A3-CA1-R-A63x1zACR5", "11, NOT-Pyramidal");
            put("cell-5ca3_1", "12, NOT-Pyramidal");
            put("Hilus-02_2", "13, NOT-Pyramidal");
            put("CA1-19_2", "14, NOT-Pyramidal");
            put("cell-11_1", "15, NOT-Pyramidal");
            put("A1-CA1-L-D63x1zACR2", "16, NOT-Pyramidal");
            put("No17-APV", "17, NOT-Pyramidal");
            put("A3-CA1-L-D63x1zACR1", "18, NOT-Pyramidal");
            put("DG-outer-mol-01_1", "19, NOT-Pyramidal");
            put("A3-CA1-L-B63x1zACR1", "20, NOT-Pyramidal");
            put("124-5L", "21, NOT-Pyramidal");
            put("B4-CA1-L-C63x1zACR1", "22, NOT-Pyramidal");
            put("DG-outer-mol_10", "23, NOT-Pyramidal");
            put("35dpi_ipsi_infra_18", "24, NOT-Pyramidal");
            put("32-SR-HiDe", "25, NOT-Pyramidal");
        }};

        Map<String, String> filenameMapping02A = new HashMap<String, String>() {{
            put("109-1_5_6_CA1_R1_N2_CG", "1, CA1");
            put("CA1-cell-3_4", "2, CA1");
            put("c9236e", "3, CA1");
            put("109-5_5_6_L2_CA1_N2_CG", "4, CA1");
            put("0001-2_1", "5, CA1");
            put("108-3_5_2_CA1_R1_N3_CG", "6, CA1");
            put("BDLHD-HP3-e-3", "7, CA1");
            put("5--Exp", "8, CA1");
            put("0003-2_3", "9, CA1");
            put("0005_4", "10, CA1");
            put("108-7_6_7_L1_CA1_N1_CG", "11, CA1");
            put("CA1-02_5", "12, CA1");
            put("109-5_5_5_R1_CA1_N2_CG", "13, CA1");
            put("108-3_5_1_CA1_R2_N1_CG", "14, CA1");
            put("B4-CA1-L-D63x1zCell3ACR", "15, CA1");
            put("5--Con", "16, CA1");
            put("B1-CA1-R-I63x1zACR4", "17, CA1");
            put("052914C_10", "18, CA1");
            put("110-4_6_8_CA1_L1_N1_CG", "19, CA1");
            put("pc4d", "20, CA1");
            put("CA1-10_3", "21, CA1");
            put("BDL-HP3-2-j-1", "22, CA1");
            put("CA1-08", "23, CA1");
            put("CA1-11_3", "24, CA1");
            put("CA1-W-P15-2", "25, CA1");
            put("l60b", "1, CA3");
            put("HC-T1", "2, CA3");
            put("c62563", "3, CA3");
            put("116_post", "4, CA3");
            put("A2_CA3", "5, CA3");
            put("HC-S3", "6, CA3");
            put("HC-R2", "7, CA3");
            put("EPOcCA3c_37a", "8, CA3");
            put("EPOcCA3c_22a", "9, CA3");
            put("CA3cell-5", "10, CA3");
            put("HC-Q6", "11, CA3");
            put("HC-D3", "12, CA3");
            put("EPOcCA3c_21b", "13, CA3");
            put("HC-R8", "14, CA3");
            put("ca3cell-5_3", "15, CA3");
            put("CA3-17_2", "16, CA3");
            put("CA3_5", "17, CA3");
            put("HC-U1A", "18, CA3");
            put("CA3-01_6", "19, CA3");
            put("CA3_11", "20, CA3");
            put("EPOcCA3c_37c", "21, CA3");
            put("CA303_1", "22, CA3");
            put("cell-5ca3_1", "23, CA3");
            put("HC-R7", "24, CA3");
            put("CA3-20_3", "25, CA3");
            put("n251", "1, Interneuron");
            put("n255", "2, Interneuron");
            put("BC5", "3, Interneuron");
            put("HIPP1_1", "4, Interneuron");
            put("5-27-11cell1-nonFS-BC", "5, Interneuron");
            put("20120504-1-6", "6, Interneuron");
            put("20111228-6-9", "7, Interneuron");
            put("3-17-2012-reconstruction_60x", "8, Interneuron");
            put("1223-10-11", "9, Interneuron");
            put("20111229-6-7", "10, Interneuron");
            put("n253", "11, Interneuron");
            put("n260", "12, Interneuron");
            put("10-6-2014cell2", "13, Interneuron");
            put("20120112-1-2", "14, Interneuron");
            put("n254", "15, Interneuron");
            put("11-4-2014-cell1", "16, Interneuron");
            put("BC6", "17, Interneuron");
            put("TML", "18, Interneuron");
            put("20111230-1-2", "19, Interneuron");
            put("HIPP3", "20, Interneuron");
            put("20111214-2-6-7", "21, Interneuron");
            put("6-18-2014-cell1", "22, Interneuron");
            put("PIN-reconstruction", "23, Interneuron");
            put("n257", "24, Interneuron");
            put("BC3_1", "25, Interneuron");
            put("04n11023-vehicle", "1, Dentate Granule");
            put("dox9g_roi-i", "2, Dentate Granule");
            put("No37-APV", "3, Dentate Granule");
            put("728882", "4, Dentate Granule");
            put("n516", "5, Dentate Granule");
            put("n28-r01-03-sl1", "6, Dentate Granule");
            put("040726TTX1", "7, Dentate Granule");
            put("Mature_contra_infra_05", "8, Dentate Granule");
            put("28dpi_ipsi_supra_07", "9, Dentate Granule");
            put("n220", "10, Dentate Granule");
            put("805881", "11, Dentate Granule");
            put("No9-Veh", "12, Dentate Granule");
            put("77dpi_ipsi_infra_08", "13, Dentate Granule");
            put("dox29g_roi2-i", "14, Dentate Granule");
            put("28dpi_ipsi_supra_20", "15, Dentate Granule");
            put("con10g_roi-i", "16, Dentate Granule");
            put("77dpi_contra_infra_06", "17, Dentate Granule");
            put("4299202", "18, Dentate Granule");
            put("No56-Veh", "19, Dentate Granule");
            put("Mature_contra_supra_08", "20, Dentate Granule");
            put("77dpi_contra_infra_01", "21, Dentate Granule");
            put("dox29g_roi1-1", "22, Dentate Granule");
            put("35dpi_ipsi_supra_21", "23, Dentate Granule");
            put("No12-APV", "24, Dentate Granule");
            put("n29-r01-03-sl3", "25, Dentate Granule");
        }};

        Map<String, String> filenameMapping01B = new HashMap<String, String>() {{
            put("107-8_4_9_CA1_R1_N1_CG", "1, Pyramidal");
            put("HC-P2", "2, Pyramidal");
            put("l24a", "3, Pyramidal");
            put("n402", "4, Pyramidal");
            put("sh1-2-20x-3", "5, Pyramidal");
            put("CA2-W-P13", "6, Pyramidal");
            put("107-6_5_6_CA1_R1_N1_CG", "7, Pyramidal");
            put("A1_CA1", "8, Pyramidal");
            put("0002-3", "9, Pyramidal");
            put("EPOcCA3c_45a", "10, Pyramidal");
            put("29-andreae", "11, Pyramidal");
            put("109-4_6_2_L1_CA1_N1_CG", "12, Pyramidal");
            put("EPOcCA3c_39d", "13, Pyramidal");
            put("0002-4_2", "14, Pyramidal");
            put("108-8_5_7_CA1_L1_N1_CG", "15, Pyramidal");
            put("0001-8_1", "16, Pyramidal");
            put("I040913C7SEC8_2D", "17, Pyramidal");
            put("108-6_7_5_CA1_R1_N1_CG", "18, Pyramidal");
            put("0001-7", "19, Pyramidal");
            put("012417B_ECA", "20, Pyramidal");
            put("HC-S3", "21, Pyramidal");
            put("107-8_4_10_CA1_R2_N2_CG", "22, Pyramidal");
            put("107-2_5_4_CA1_L2_N2_CG", "23, Pyramidal");
            put("Con-HP5-2-e", "24, Pyramidal");
            put("n417", "25, Pyramidal");
            put("Hilus-1", "1, NOT-Pyramidal");
            put("1223-10-11", "2, NOT-Pyramidal");
            put("BICD2_20", "3, NOT-Pyramidal");
            put("040318018", "4, NOT-Pyramidal");
            put("DG-gran_10", "5, NOT-Pyramidal");
            put("A3-CA1-L-C63x1zACR8", "6, NOT-Pyramidal");
            put("DEV133T3", "7, NOT-Pyramidal");
            put("slice-3_3", "8, NOT-Pyramidal");
            put("n38-r06-09-sl4", "9, NOT-Pyramidal");
            put("20111230-1-2", "10, NOT-Pyramidal");
            put("n512", "11, NOT-Pyramidal");
            put("slice-1_9", "12, NOT-Pyramidal");
            put("CA3cell-1_1", "13, NOT-Pyramidal");
            put("04n11024-vehicle1", "14, NOT-Pyramidal");
            put("100803a", "15, NOT-Pyramidal");
            put("11-4-2014-cell1", "16, NOT-Pyramidal");
            put("G5_roi_i", "17, NOT-Pyramidal");
            put("H906-2", "18, NOT-Pyramidal");
            put("CA1-12_2", "19, NOT-Pyramidal");
            put("sr3", "20, NOT-Pyramidal");
            put("slice-1_3", "21, NOT-Pyramidal");
            put("n224", "22, NOT-Pyramidal");
            put("CA3-16", "23, NOT-Pyramidal");
            put("dox24g_roi-i", "24, NOT-Pyramidal");
            put("CA3-16_3", "25, NOT-Pyramidal");
        }};

        Map<String, String> filenameMapping02B = new HashMap<String, String>() {{
            put("C2-CA1-L-C63x1zCell3ACR", "1, CA1");
            put("CA1-12_1", "2, CA1");
            put("052814D_1", "3, CA1");
            put("0001-4_3", "4, CA1");
            put("API1-2", "5, CA1");
            put("B1-CA1-L-A63x1zACR3", "6, CA1");
            put("CA1-17_3", "7, CA1");
            put("C3-CA1-L-A63x1zACR4", "8, CA1");
            put("A2-CA1-R-B63x1zACR5", "9, CA1");
            put("0002-6_3", "10, CA1");
            put("CA1-14_3", "11, CA1");
            put("109-4_5_8_R1_CA1_N1_CG", "12, CA1");
            put("n416", "13, CA1");
            put("107-4_4_10_CA1_R1_N1_CG", "14, CA1");
            put("API1_4", "15, CA1");
            put("cell1-3-CA1", "16, CA1");
            put("BAS2_1", "17, CA1");
            put("109-6_5_6_R1_CA1_N1_CG", "18, CA1");
            put("052814C_6", "19, CA1");
            put("19-2-c4", "20, CA1");
            put("neuron", "21, CA1");
            put("0001_5", "22, CA1");
            put("B4-CA1-L-A63x1zACR1", "23, CA1");
            put("A1-CA1-L-E63x1zACR1", "24, CA1");
            put("30-01-13_cell2_Dorsal", "25, CA1");
            put("Ca3Sham-19-1-09_22-1", "1, CA3");
            put("cell8zr", "2, CA3");
            put("CA3-16_2", "3, CA3");
            put("c82063", "4, CA3");
            put("HC-T3B", "5, CA3");
            put("960924b", "6, CA3");
            put("CA3-03rad", "7, CA3");
            put("HC-H3", "8, CA3");
            put("CA3-14_1", "9, CA3");
            put("HC-U3", "10, CA3");
            put("CA3-2", "11, CA3");
            put("CA3-radiata-tracing_1", "12, CA3");
            put("EPOcCA3c_23a", "13, CA3");
            put("CA3-01", "14, CA3");
            put("EPOcCA3c_39e", "15, CA3");
            put("EPOcCA3c_42d", "16, CA3");
            put("HC-B3", "17, CA3");
            put("HC-E5", "18, CA3");
            put("EPOcCA3c_41c", "19, CA3");
            put("cell-3-ca3_1", "20, CA3");
            put("CA3_15", "21, CA3");
            put("CA3-12_3", "22, CA3");
            put("c10861", "23, CA3");
            put("A10_CA3", "24, CA3");
            put("cell-3-ca3", "25, CA3");
            put("NGFC", "1, Interneuron");
            put("1228-1-2", "2, Interneuron");
            put("BC2", "3, Interneuron");
            put("10-28-2014-cell7_20X", "4, Interneuron");
            put("HIPP2_1", "5, Interneuron");
            put("3-12-2014-reconstruction", "6, Interneuron");
            put("6-17-2014_20Xpaired2cells-second", "7, Interneuron");
            put("DIN-reconstruction", "8, Interneuron");
            put("20120117-4-6", "9, Interneuron");
            put("BC1", "10, Interneuron");
            put("n250", "11, Interneuron");
            put("BC4", "12, Interneuron");
            put("HICAP", "13, Interneuron");
            put("AAC", "14, Interneuron");
            put("12-14-2012-recon_60X", "15, Interneuron");
            put("n261", "16, Interneuron");
            put("1226-11-12", "17, Interneuron");
            put("20120516-16-18", "18, Interneuron");
            put("n258", "19, Interneuron");
            put("n256", "20, Interneuron");
            put("20130609-1-4-PV", "21, Interneuron");
            put("n252", "22, Interneuron");
            put("n262", "23, Interneuron");
            put("6-17-2014_20Xpaired2cells-first", "24, Interneuron");
            put("BC1_1", "25, Interneuron");
            put("21dpi_ipsi_supra_07", "1, Dentate Granule");
            put("21dpi_contra_infra_03", "2, Dentate Granule");
            put("28dpi_ipsi_supra_14", "3, Dentate Granule");
            put("n26-r01-02-sl3", "4, Dentate Granule");
            put("77dpi_ipsi_supra_07", "5, Dentate Granule");
            put("Mature_ipsi_infra_01", "6, Dentate Granule");
            put("G10_roi_i", "7, Dentate Granule");
            put("77dpi_ipsi_infra_04", "8, Dentate Granule");
            put("DA111026_C2", "9, Dentate Granule");
            put("No45-Veh", "10, Dentate Granule");
            put("124893b", "11, Dentate Granule");
            put("dox21g_roi1-i", "12, Dentate Granule");
            put("35dpi_ipsi_infra_19", "13, Dentate Granule");
            put("21dpi_ipsi_infra_06", "14, Dentate Granule");
            put("77dpi_ipsi_supra_06", "15, Dentate Granule");
            put("124-5R", "16, Dentate Granule");
            put("28dpi_ipsi_supra_16", "17, Dentate Granule");
            put("con15g_roi-i", "18, Dentate Granule");
            put("28dpi_contra_infra_02", "19, Dentate Granule");
            put("No09-Veh", "20, Dentate Granule");
            put("77dpi_contra_infra_05", "21, Dentate Granule");
            put("35dpi_contra_supra_01", "22, Dentate Granule");
            put("n510", "23, Dentate Granule");
            put("Mature_contra_infra_01", "24, Dentate Granule");
            put("dox19g_roi-i", "25, Dentate Granule");
        }};

        List<TEDResult> results = Utils.readMatricesFromTxt();
        for (TEDResult result : results) {
            if (result.getName().startsWith("Matrix_swcFiles_Testcase01_a")) {
                renameFiles(filenameMapping01A, result.getFileNames());
            } else if (result.getName().startsWith("Matrix_swcFiles_Testcase01_b")) {
                renameFiles(filenameMapping01B, result.getFileNames());
            } else {
                if (Pattern.matches("Matrix_swcFiles_Testcase0._a_\\w*", result.getName())) {
                    renameFiles(filenameMapping02A, result.getFileNames());
                } else if (Pattern.matches("Matrix_swcFiles_Testcase0._b_\\w*", result.getName())) {
                    renameFiles(filenameMapping02B, result.getFileNames());
                }
            }
        }
        for (TEDResult result : results) {
            DendrogramCreator.createDendrogram(result, appProperties.getMetadataDirectory(), appProperties.getOutputDirectory(), result.getName(), false, true);
        }
    }

    private static void renameFiles(Map<String, String> filenameMap, String[] fileNames) {
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = filenameMap.get(fileNames[i]);
            if (fileNames[i] == null) {
                throw new RuntimeException();
            }
        }
    }

    public static void printMetadataForJson() throws IOException {

        // input: jsonFiles
        // output: file with Filenames to metadata mapping
        // filename, archive, species, brainRegions, cellTypes, ageClassification, minAge, maxAge, physicalIntegrity, domain, attributes, protocol, reconstructionSoftware, experimentCondition, filesize
        File[] jsonFiles = Utils.chooseJSONFiles();

        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapExistingFromMetadataDirectory(appProperties.getMetadataDirectory(), appProperties.getSwcFileDirectory());

        for (File jsonFile : jsonFiles) {
            System.out.println("\nJsonFile: " + jsonFile.getName());
            File[] files = JsonUtils.parseJsonToFiles(jsonFile);
            for (int i = 0; i < files.length; i++) {
                files[i] = new File(appProperties.getSwcFileDirectory() + "/" + files[i].getPath());
            }

            try {
                File file = new File(FilenameUtils.removeExtension(jsonFile.toString()) + "_Metadata" + ".csv");
                FileWriter export = new FileWriter(file.getAbsolutePath());
                BufferedWriter br = new BufferedWriter(export);
                br.write("neuronId"+ "; " + "filename" + "; " + "archive" + "; " + "species" + "; " + "brainRegions" + "; " + "cellTypes" + "; " + "ageClassification" + "; " + "minAge" + "; " + "maxAge" + "; " + "physicalIntegrity" + "; " + "domain" + "; " + "attributes" + "; " + "protocol" + "; " + "reconstructionSoftware" + "; " + "experimentCondition" + "; " + "filesize");
                br.newLine();

                for (File neuronFile: files) {
                    String filename = FilenameUtils.removeExtension(FilenameUtils.removeExtension(neuronFile.getName()));
                    NeuronMetadataR neuronMetadataObject = neuronMetadata.get(filename);
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
                    String formattedSize = String.format("%.2f", (double) FileUtils.sizeOf(neuronFile)/(1024*1024));
                    br.write(neuronMetadataObject.getNeuronId() + "; " + filename + "; " + neuronMetadataObject.getArchive() + "; " + neuronMetadataObject.getSpecies() + "; " + brainRegions.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", ")) + "; " + cellTypes.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", ")) + "; " + neuronMetadataObject.getAgeClassification() + "; " + neuronMetadataObject.getMinAge() + "; " + neuronMetadataObject.getMaxAge() + "; " + neuronMetadataObject.getPhysicalIntegrity() + "; " + neuronMetadataObject.getDomain() + "; " + neuronMetadataObject.getAttributes() + "; " + neuronMetadataObject.getProtocol() + "; " + neuronMetadataObject.getReconstructionSoftware() + "; " + experimentConditions.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", ")) + "; " + formattedSize + "MB");
                    br.newLine();
                }
                br.close();
                System.out.println("File saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
