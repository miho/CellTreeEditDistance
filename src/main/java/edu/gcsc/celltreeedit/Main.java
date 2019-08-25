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
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        TEDResult result = cellTreeEditDistance.compareFilesFromFiles(files, 9);
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
            for (int i = 1; i < 23; i += 1) {
                Date date = new Date();
                System.out.println(dateFormat.format(date) + " selected Label " + i);
                cellTreeEditDistance = new CellTreeEditDistance();
                result = cellTreeEditDistance.compareFilesFromFiles(files, i);
                Utils.printMatrixToTxt(result.getDistanceMatrix(), result.getFileNames(), appProperties.getOutputDirectory(), "Matrix_" + FilenameUtils.removeExtension(jsonFile.getName()) + "_Label" + i + ".txt");
            }
        }


        // calculate distances of 1000 of these SWC-Files
//        Date date = new Date();
//        System.out.println(dateFormat.format(date) + " 1000 mostCommon");
//        cellTreeEditDistance = new CellTreeEditDistance();
//        result = cellTreeEditDistance.compareFilesFromFiles(JsonUtils.parseJsonToFiles(new File("/media/exdisk/Sem06/BA/Testlaeufe/swcFiles_mostCommon_1000_40Types_25Each.json")), 9);
//        Utils.printMatrixToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), "Matrix_mostCommon_1000_40Types_25Each.txt");
    }
}
