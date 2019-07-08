package edu.gcsc.celltreeedit;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.Lucene.CLI;
import edu.gcsc.celltreeedit.Lucene.LuceneIndexWriter;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;
import edu.gcsc.celltreeedit.PartitioningError.HungarianAlgorithm;
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

    public static void main(String[] args) throws IOException {

        AppProperties appProperties = CommandLineParsing.parseArguments(args);

        switch (appProperties.getCalcType()) {
            case 0:
                calculateCompletely(appProperties);
                break;
            case 1:
                calculateMatrixOnly(appProperties);
                break;
            case 2:
                analyzeMatrix(appProperties);
                break;
            case 3:
                queryLucene(appProperties);
                break;
            case 4:
                queryByTypeCombination(appProperties);
                break;
            case 5:
                queryByFileDialog(appProperties);
                break;
            case 6:
                preprocessSWCDirectory(appProperties);
                break;
            case 7:
                analyzeClustering(appProperties);
                break;
            case 8:
                doWhateverIsInMyFunctionBody(appProperties);
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }

    private static void calculateCompletely(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        Pair<double[][], String[]> result;
        if (appProperties.getJsonDirectory().getPath().equals("")) {
            result = cellTreeEditDistance.compareFilesFromDirectory(appProperties.getSwcFileDirectory(), 2);
        } else {
            result = cellTreeEditDistance.compareFilesFromFilenames(JsonUtils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), appProperties.getMatrixExportName());
        // calculate clustering
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getKey(), result.getValue());
        // generate dendrogram
        clustering.showCluster(cluster);
    }

    private static void calculateMatrixOnly(AppProperties appProperties) throws IOException {
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        Pair<double[][], String[]> result;
        if (appProperties.getJsonDirectory().getPath().equals("")) {
            result = cellTreeEditDistance.compareFilesFromDirectory(appProperties.getSwcFileDirectory(), 9);
        } else {
            result = cellTreeEditDistance.compareFilesFromFilenames(JsonUtils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getOutputDirectory(), appProperties.getMatrixExportName());
    }

    private static void analyzeMatrix(AppProperties appProperties) throws IOException {

        Pair<double[][], String[]> result = Utils.readMatrixFromTxt();

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        String[] newFileNames = new String[result.getValue().length];

        UniqueMetadata uniqueMetadata;
        NeuronMetadataR neuronMetadataR;
        // create unique metadata of files
        for (int i = 0; i < result.getValue().length; i++) {
            neuronMetadataR = neuronMetadata.get(result.getValue()[i]);
            uniqueMetadata = UniqueMetadata.addNeuronMetadata(neuronMetadataR);
            // uniqueMetadataId, archive, neuronId
            newFileNames[i] = uniqueMetadata.getUniqueMetadataId() + ", " + neuronMetadataR.getArchive() + ", " + neuronMetadataR.getNeuronId();
        }

        Utils.printToTxt(result.getKey(), newFileNames, appProperties.getOutputDirectory(), "Matrix_fileNamesAdjusted.txt");
        // create cluster with matrix and adjusted names
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getKey(), result.getValue());
        // generate dendrogram
        clustering.showCluster(cluster);
    }

    private static void analyzeClustering(AppProperties appProperties) throws IOException {

        Pair<double[][], String[]> result = Utils.readMatrixFromTxt();
        double[][] matrix = result.getKey();
        String[] fileNames = result.getValue();

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        // Create uniqueMetadata from Filenames to know original clusters and their number
        UniqueMetadata uniqueMetadata;
        NeuronMetadataR neuronMetadataR;
        Map<String, UniqueMetadata> fileNamesUniqueMetadataMap = new HashMap<>();
        // create unique metadata of filenames in matrix
        for (int i = 0; i < fileNames.length; i++) {
            neuronMetadataR = neuronMetadata.get(fileNames[i]);
            uniqueMetadata = UniqueMetadata.addNeuronMetadata(neuronMetadataR);
            // save for later use
            fileNamesUniqueMetadataMap.put(fileNames[i], uniqueMetadata);
        }
        List<UniqueMetadata> uniqueMetadataObjects = new ArrayList<>(UniqueMetadata.getUniqueMetadataMap().keySet());
        int noOfUniqueMetadata = uniqueMetadataObjects.size();

        // Calculate Cluster
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(matrix, fileNames);
        // get clusters according to size of uniqueMetadata
        List<Cluster> limitedClusters = limitClusterBySize(cluster, noOfUniqueMetadata);

        // calculate partitioning error and match clusters to uniqueMetadata
        // HungarianDouble takes DistanceMatrix of values
        // go through each uniqueMetadataObject. Calculate partitioning error for all clusters
        int[][] partitioningErrorMatrix = calculatePartitioningErrorMatrix(uniqueMetadataObjects, limitedClusters, fileNamesUniqueMetadataMap);
        HungarianAlgorithm hungarian = new HungarianAlgorithm(partitioningErrorMatrix);
        int[][] assignment = hungarian.findOptimalAssignment();
        System.out.println("Donedonedonedone");

        // calculate relative partitioning errors
        // calculate and show relative partitioning errors as CorrelationPlot

    }

    private static int[][] calculatePartitioningErrorMatrix(List<UniqueMetadata> uniqueMetadataObjects, List<Cluster> clusters, Map<String, UniqueMetadata> fileNamesUniqueMetadataMap) {
        int noOfUniqueMetadata = uniqueMetadataObjects.size();
        int[][] partitioningErrorMatrix = new int[noOfUniqueMetadata][noOfUniqueMetadata];
        for (int i = 0; i < noOfUniqueMetadata; i++) {
            List<Cluster> leafsOfCluster = findLeafsOfCluster(clusters.get(i));
            for (int j = 0; j < noOfUniqueMetadata; j++) {
                partitioningErrorMatrix[j][i] = calculatePartitioningError(uniqueMetadataObjects.get(j), leafsOfCluster, fileNamesUniqueMetadataMap);
            }
        }
        return partitioningErrorMatrix;
    }

    private static int calculatePartitioningError(UniqueMetadata uniqueMetadata, List<Cluster> leafsOfCluster, Map<String, UniqueMetadata> fileNamesUniqueMetadataMap) {
        int partitioningError = 0;
        for (Cluster cluster: leafsOfCluster) {
            if (!fileNamesUniqueMetadataMap.get(cluster.getName()).equals(uniqueMetadata)) {
                partitioningError += 1;
            }
        }
        return partitioningError;
    }

    private static List<Cluster> findLeafsOfCluster(Cluster cluster) {
        if (cluster.isLeaf()) {
            return new ArrayList<>(Arrays.asList(cluster));
        } else {
            List<Cluster> appendedClusters = new ArrayList<>(findLeafsOfCluster(cluster.getChildren().get(0)));
            appendedClusters.addAll(findLeafsOfCluster(cluster.getChildren().get(1)));
            return appendedClusters;
        }
    }

    /**
     * returns list of Cluster with number of Clusters limited by size-input. Function uses Cluster-number from name property as Clusternumbers are ordered by their creation.
     *
     * @param cluster
     * @param size
     * @return
     */
    private static List<Cluster> limitClusterBySize(Cluster cluster, int size) {
        TreeMap<Integer, Cluster> limitedClusters = new TreeMap<>();
        limitedClusters.put(getClusterNumberFromName(cluster.getName()), cluster);

        for (int numberOfClusters = 1; numberOfClusters < size; numberOfClusters++) {
            int clusterNumber = limitedClusters.lastKey();
            List<Cluster> childClusters = limitedClusters.lastEntry().getValue().getChildren();
            limitedClusters.remove(clusterNumber);
            limitedClusters.put(getClusterNumberFromName(childClusters.get(0).getName()), childClusters.get(0));
            limitedClusters.put(getClusterNumberFromName(childClusters.get(1).getName()), childClusters.get(1));
        }
        return new ArrayList<>(limitedClusters.values());
    }

    private static int getClusterNumberFromName(String name) {
        return Integer.parseInt(name.replace("clstr#", ""));
    }

    private static void queryLucene(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        File indexDirectory = new File(appProperties.getWorkingDirectory() + "/LuceneIndex");
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(indexDirectory);
        luceneIndexWriter.createIndex(neuronMetadata);
        System.out.println("lucene index created!");
        CLI.startCLI(indexDirectory, appProperties.getOutputDirectory());

    }


    // for querying the mostCommon neurontypes
    private static void queryByTypeCombination(AppProperties appProperties) throws IOException {
        // TODO: put in AppProperties and make adjustable from commandline?
        int noOfTypes = 40;
        int noOfNeuronsPerType = 25;

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());
        // add all metadata to UniqueMetadata
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            UniqueMetadata.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }
        List<UniqueMetadata> sortedUniqueMetadata = new ArrayList<>(UniqueMetadata.getUniqueMetadataMap().keySet());
//         sort uniqueMetadata
        sortedUniqueMetadata.sort(Comparator.comparingInt(UniqueMetadata::getNoOfNeurons).reversed());

//         select neurons depending on typeCount and input-variables
        List<String> selectedNeuronNames = new ArrayList<>();
        int k = 1;
        for (UniqueMetadata uniqueMetadata : sortedUniqueMetadata) {
            if (k > noOfTypes) {
                break;
            }
            // select noOfNeuronsPerType neurons randomly
            selectedNeuronNames.addAll(pickNRandom(uniqueMetadata.getNeuronNames(), noOfNeuronsPerType));
            System.out.println(uniqueMetadata.getSpecies() + ";" + String.join(", ", uniqueMetadata.getBrainRegion()) + ";" + String.join(", ", uniqueMetadata.getCellTypes()) + ";" + uniqueMetadata.getNoOfNeurons() + ";" + uniqueMetadata.getArchives().size());
            k += 1;
        }

        // write to json
        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

    // for querying a predefined combination
//    private static void queryByTypeCombination(AppProperties appProperties) throws IOException {
//        System.out.println("inside queryByTypeCombination");
//        int noOfTypes = 40;
//        int noOfNeuronsPerType = 37;
//
//        // define which uniqueMetadata Types shall be used
//        Set<UniqueMetadata> selectedUniqueMetadata = new HashSet<>();
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "granule")), new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("stellate", "interneuron")), new HashSet<>(Arrays.asList("amygdala", "basolateral amygdala complex")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 2")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 3")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5a")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5b")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Horizontal", "interneuron", "neurogliaform")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 1")), "rat", "", ""));
//        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Star", "pyramidal", "interneuron")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));
//
//        // put metadata in hashMap
//        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
//        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());
//        // add all metadata to UniqueMetadata
//        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
//            UniqueMetadata.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
//        }
//
////         select neurons depending on typeCount and input-variables
//        List<String> selectedNeuronNames = new ArrayList<>();
//        int k = 1;
//        for (UniqueMetadata uniqueMetadata : selectedUniqueMetadata) {
//            if (k > noOfTypes) {
//                break;
//            }
//            // select noOfNeuronsPerType neurons randomly
//            selectedNeuronNames.addAll(pickNRandom(UniqueMetadata.getUniqueMetadataMap().get(uniqueMetadata).getNeuronNames(), noOfNeuronsPerType));
//            System.out.println(uniqueMetadata.getSpecies() + ";" + String.join(", ", uniqueMetadata.getBrainRegion()) + ";" + String.join(", ", uniqueMetadata.getCellTypes()) + ";" + uniqueMetadata.getNoOfNeurons() + ";" + uniqueMetadata.getArchives().size());
//            k += 1;
//        }
//
//        // write to json
//        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
//    }

    public static List<String> pickNRandom(List<String> lst, int n) {
        List<String> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
    }

    private static void queryByFileDialog(AppProperties appProperties) throws IOException {
        File[] files = Utils.choose();
        List<String> selectedNeuronNames = Arrays.stream(files).map(file -> Utils.removeSWCFileExtensions(file.getName())).collect(Collectors.toList());

        // write to json
        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

    private static void preprocessSWCDirectory(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        // preprocess SWC-Directory
        File swcDirectory = new File("/media/exdisk/Sem06/BA/Data/SWC-Files/00_All");
        SWCPreprocessing swcPreprocessing = new SWCPreprocessing();
        swcPreprocessing.preprocessSWCDirectory(neuronMetadata, swcDirectory);
    }


    // method to do some custom things which program should not be able to do in the end
    private static void doWhateverIsInMyFunctionBody(AppProperties appProperties) throws IOException {
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
