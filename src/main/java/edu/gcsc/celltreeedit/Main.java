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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    private static AppProperties appProperties;

    public static void main(String[] args) throws IOException {

        File file = new File("/media/exdisk/ABC/swcFileName.swc");
        System.out.println(file.getName());

        CommandLineParsing.parseArguments(args);
        appProperties = AppProperties.getInstance();

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
                calculateTEDMatrixOnly();
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


    private static Pair<double[][], String[]> calculateTEDMatrixOnly() throws IOException {
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        Pair<double[][], String[]> result;
        if (appProperties.getJsonDirectory().getPath().equals("")) {
            result = cellTreeEditDistance.compareFilesFromDirectory(appProperties.getSwcFileDirectory(), 9);
        } else {
            result = cellTreeEditDistance.compareFilesFromFilenames(JsonUtils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), appProperties.getMatrixExportName());
        return result;
    }

    private static void calculateTEDMatrixAndDendrogram() throws IOException {
        Pair<double[][], String[]> result = calculateTEDMatrixOnly();
        DendrogramCreator.showDendrogram(result);
    }

    private static void calculateDendrogramsForTEDMatrices() throws IOException {
        List<Pair<double[][], String[]>> results = Utils.readMatricesFromTxt();
        for (Pair<double[][], String[]> result : results) {
            DendrogramCreator.showDendrogram(result);
        }
    }

    /**
     * Analyzes Clusterings of multiple labels and displays the results.
     *
     * @throws IOException
     */
    private static void analyzeClusteringOfTEDMatrices() throws IOException {

        List<Pair<double[][], String[]>> results = Utils.readMatricesFromTxt();

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        for (Pair<double[][], String[]> result : results) {
            ClusteringAnalyzer.analyzeClusteringOfTEDResult(result, neuronMetadata);
        }
    }

    private static void queryLucene() throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        File indexDirectory = new File(appProperties.getWorkingDirectory() + "/LuceneIndex");
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(indexDirectory);
        luceneIndexWriter.createIndex(neuronMetadata);
        System.out.println("lucene index created!");
        CLI.startCLI(indexDirectory, appProperties.getOutputDirectory());

    }


    // for querying the mostCommon neurontypes
//    private static void queryByUniqueMetadata() throws IOException {
//        // TODO: put in AppProperties and make adjustable from commandline?
//        int noOfTypes = 40;
//        int noOfNeuronsPerType = 25;
//
//        // put metadata in hashMap
//        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
//        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());
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

    // for querying a predefined combination
    private static void queryByUniqueMetadata() throws IOException {
        System.out.println("inside queryByUniqueMetadata");
        int noOfNeuronsPerType = 37;

        // define which uniqueMetadata Types shall be used
        UniqueMetadataContainer selectedUniqueMetadataContainer = new UniqueMetadataContainer();
        Set<UniqueMetadataContainer.UniqueMetadata> selectedUniqueMetadata = new HashSet<>();
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "granule")), new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("stellate", "interneuron")), new HashSet<>(Arrays.asList("amygdala", "basolateral amygdala complex")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 2")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 3")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5a")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5b")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Horizontal", "interneuron", "neurogliaform")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 1")), "rat", "", ""));
        selectedUniqueMetadata.add(selectedUniqueMetadataContainer.createUniqueMetadataObject(new HashSet<>(Arrays.asList("Star", "pyramidal", "interneuron")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        UniqueMetadataContainer uniqueMetadataContainer = new UniqueMetadataContainer();
        // add all metadata to UniqueMetadata
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            uniqueMetadataContainer.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }

//         select neurons depending on typeCount and input-variables
        List<String> selectedNeuronNames = new ArrayList<>();
        for (UniqueMetadataContainer.UniqueMetadata uniqueMetadata : selectedUniqueMetadata) {
            // select noOfNeuronsPerType neurons randomly
            selectedNeuronNames.addAll(pickNRandom(uniqueMetadataContainer.getUniqueMetadataMap().get(uniqueMetadata).getNeuronNames(), noOfNeuronsPerType));
            System.out.println(uniqueMetadata.getSpecies() + ";" + String.join(", ", uniqueMetadata.getBrainRegion()) + ";" + String.join(", ", uniqueMetadata.getCellTypes()) + ";" + uniqueMetadata.getNoOfNeurons() + ";" + uniqueMetadata.getArchives().size());
        }

        // write to json
//        JsonUtils.writeToJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

    private static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private static void queryByFileDialog() throws IOException {
        File[] files = Utils.choose();
        List<String> selectedNeuronNames = Arrays.stream(files).map(file -> Utils.removeSWCFileExtensions(file.getName())).collect(Collectors.toList());

        // write to json
//        JsonUtils.writeToJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

    private static void preprocessSWCDirectory() throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        // preprocess SWC-Directory
        File swcDirectory = new File("/media/exdisk/Sem06/BA/Data/SWC-Files/00_All");
        SWCPreprocessing swcPreprocessing = new SWCPreprocessing();
        swcPreprocessing.preprocessSWCDirectory(neuronMetadata, swcDirectory);
    }


    // method to do some custom things which program should not be able to do in the end
    private static void doWhateverIsInMyFunctionBody() throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        CellTreeEditDistance cellTreeEditDistance;
        Pair<double[][], String[]> result;
        // calculate distances of Selection of SWC-Files for every possible label
        Set<String> fileNames = JsonUtils.parseJsonToFileNames(new File("/media/exdisk/Sem06/BA/Testlaeufe/swcFiles_Selected_481_13Types_37Each.json"));
        for (int i = 1; i < 23; i += 1) {
            Date date = new Date();
            System.out.println(dateFormat.format(date) + " selected Label " + i);
            cellTreeEditDistance = new CellTreeEditDistance();
            result = cellTreeEditDistance.compareFilesFromFilenames(fileNames, appProperties.getSwcFileDirectory(), i);
            Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), "Matrix_Selected_481_13Types_37Each_Label" + i + ".txt");
        }

        // calculate distances of 1000 of these SWC-Files
        Date date = new Date();
        System.out.println(dateFormat.format(date) + " 1000 mostCommon");
        cellTreeEditDistance = new CellTreeEditDistance();
        result = cellTreeEditDistance.compareFilesFromFilenames(JsonUtils.parseJsonToFileNames(new File("/media/exdisk/Sem06/BA/Testlaeufe/swcFiles_mostCommon_1000_40Types_25Each.json")), appProperties.getSwcFileDirectory(), 9);
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), "Matrix_mostCommon_1000_40Types_25Each.txt");
    }
}
