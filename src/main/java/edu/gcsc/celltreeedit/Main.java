package edu.gcsc.celltreeedit;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.Clustering.Clustering;
import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.Lucene.CLI;
import edu.gcsc.celltreeedit.Lucene.LuceneIndexWriter;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;
import edu.gcsc.celltreeedit.ClusterAnalysis.HungarianAlgorithm;
import edu.gcsc.celltreeedit.ClusterAnalysis.RelPartitioningErrorTable;
import edu.gcsc.celltreeedit.TEDCalculation.CellTreeEditDistance;
import javafx.util.Pair;

import javax.swing.*;
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
                preprocessSWCDirectory(appProperties);
                break;
            case 1:
                queryLucene(appProperties);
                break;
            case 2:
                queryByFileDialog(appProperties);
                break;
            case 3:
                queryByUniqueMetadata(appProperties);
                break;
            case 4:
                calculateTEDMatrixOnly(appProperties);
                break;
            case 5:
                calculateTEDMatrixAndDendrogram(appProperties);
                break;
            case 6:
                calculateDendrogramsForTEDMatrices(appProperties);
                break;
            case 7:
                analyzeClusteringOfTEDMatrices(appProperties);
                break;
            case 8:
                doWhateverIsInMyFunctionBody(appProperties);
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }


    private static Pair<double[][], String[]> calculateTEDMatrixOnly(AppProperties appProperties) throws IOException {
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

    private static void calculateTEDMatrixAndDendrogram(AppProperties appProperties) throws IOException {
        Pair<double[][], String[]> result = calculateTEDMatrixOnly(appProperties);
        showDendrogram(appProperties, result);
    }

    private static void calculateDendrogramsForTEDMatrices(AppProperties appProperties) throws IOException {

        List<Pair<double[][], String[]>> results = Utils.readMatricesFromTxt();
        for (Pair<double[][], String[]> result : results) {
            showDendrogram(appProperties, result);
        }
    }

    private static void showDendrogram(AppProperties appProperties, Pair<double[][], String[]> result) throws IOException {
        String[] oldFileNames = result.getValue();
        String[] newFileNames = renameFileNamesToUniqueMetadataNames(oldFileNames, appProperties);
//            Utils.printToTxt(currentResult.getKey(), newFileNames, appProperties.getOutputDirectory(), "Matrix_fileNamesAdjusted.txt");
        // create cluster with matrix and adjusted names
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getKey(), newFileNames);
        // generate dendrogram
        clustering.showCluster(cluster);
        showFileNameMapping(oldFileNames, newFileNames);
    }

    private static void showFileNameMapping(String[] oldFileNames, String[] newFileNames) {
        JFrame frame = new JFrame();
        Tables fileNameMapping = new Tables(newFileNames, oldFileNames, new String[]{"Names", "original FileNames"});
        frame.add(fileNameMapping);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,350);
        frame.setVisible(true);
        frame.setTitle("FileName-Mapping");
    }

    private static String[] renameFileNamesToUniqueMetadataNames(String[] oldFileNames, AppProperties appProperties) throws IOException {
        String[] newFileNames = new String[oldFileNames.length];
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        UniqueMetadata uniqueMetadata;
        NeuronMetadataR neuronMetadataR;
        // create unique metadata of files
        for (int i = 0; i < oldFileNames.length; i++) {
            neuronMetadataR = neuronMetadata.get(oldFileNames[i]);
            uniqueMetadata = UniqueMetadata.addNeuronMetadata(neuronMetadataR);
            // uniqueMetadataId, archive, neuronId
            newFileNames[i] = uniqueMetadata.getUniqueMetadataId() + ", " + neuronMetadataR.getArchive() + ", " + neuronMetadataR.getNeuronId();
        }
        return newFileNames;
    }

    /**
     * Analyzes Clusterings of multiple labels and displays the results.
     *
     * @param appProperties
     * @throws IOException
     */
    private static void analyzeClusteringOfTEDMatrices(AppProperties appProperties) throws IOException {

        List<Pair<double[][], String[]>> result = Utils.readMatricesFromTxt();

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        for (Pair<double[][], String[]> currentResult : result) {
            double[][] matrix = currentResult.getKey();
            String[] fileNames = currentResult.getValue();

            // Create uniqueMetadata from Filenames to know original clusters of filenames and their number
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
            // put in List for stiff ordering
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
            Map<Integer, Integer> assignment = putAssignmentInMap(hungarian.findOptimalAssignment());
            System.out.println("Donedonedonedone");

            // calculate overall absolute partitioning error
            int overallAbsPartitioningError = calculateAbsPartitioningError(partitioningErrorMatrix, assignment);

            // TODO: get number of Neurons with uniqueMetadata A and number of Neurons with uniqueMetadata B
            // calculate relative partitioning errors and save in lower left matrix
            float[][] relPartitioningErrors = calculateRelPartitioningErrors(partitioningErrorMatrix, assignment, uniqueMetadataObjects);

            // display lower left matrix with javax Swing colorcoded as in heumann, wittum paper

            showRelPartitioningErrors(relPartitioningErrors, uniqueMetadataObjects);
        }
    }

    private static void showRelPartitioningErrors(float[][] relPartitioningErrors, List<UniqueMetadata> uniqueMetadataObjects) {
        JFrame frame = new JFrame();
        RelPartitioningErrorTable relPartitioningErrorTable = new RelPartitioningErrorTable(relPartitioningErrors, uniqueMetadataObjects);
        frame.add(relPartitioningErrorTable);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //labels.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,350);
        frame.setVisible(true);
        frame.setTitle("relative Partitioning Errors");
    }

    private static Map<Integer, Integer> putAssignmentInMap(int[][] assignment) {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < assignment.length; i++) {
            result.put(i, assignment[i][1]);
        }
        return result;
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
        for (Cluster cluster : leafsOfCluster) {
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

    private static int calculateAbsPartitioningError(int[][] partitioningErrorMatrix, Map<Integer, Integer> assignment) {
        int absPartitioningError = 0;
        for (int i = 0; i < assignment.size(); i++) {
            int j = assignment.get(i);
            absPartitioningError += partitioningErrorMatrix[i][j];
        }
        return absPartitioningError;
    }

    private static float[][] calculateRelPartitioningErrors(int[][] partitioningErrorMatrix, Map<Integer, Integer> assignment, List<UniqueMetadata> uniqueMetadataObjects) {
        int size = assignment.size() - 1;
        float[][] relPartitioningErrors = new float[size][size];
        // index of row-set
        for (int i = 0; i < size; i++) {
            // index of col-set
            for (int j = 0; j <= i; j++) {
                int row = i + 1;
                relPartitioningErrors[i][j] = 2 * ((float) partitioningErrorMatrix[row][assignment.get(row)] + (float) partitioningErrorMatrix[j][assignment.get(j)]) / ((float) uniqueMetadataObjects.get(row).getNoOfNeurons() + (float) uniqueMetadataObjects.get(j).getNoOfNeurons());
            }
        }
        return relPartitioningErrors;
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

        // i is used for leaf-clusters. leafs have keys below 0 thus they are sorted at the beginning of limitedClusters
        int i = -1;
        if (cluster.isLeaf()) {
            limitedClusters.put(i, cluster);
            i--;
        } else {
            limitedClusters.put(getClusterNumberFromName(cluster.getName()), cluster);
        }

        for (int numberOfClusters = 1; numberOfClusters < size; numberOfClusters++) {
            int clusterNumber = limitedClusters.lastKey();
            if (!limitedClusters.get(clusterNumber).isLeaf()) {
                List<Cluster> childClusters = limitedClusters.lastEntry().getValue().getChildren();
                limitedClusters.remove(clusterNumber);
                // check if child 0 is leaf
                if (childClusters.get(0).isLeaf()) {
                    limitedClusters.put(i, childClusters.get(0));
                    i--;
                } else {
                    limitedClusters.put(getClusterNumberFromName(childClusters.get(0).getName()), childClusters.get(0));
                }
                // check if child 1 is leaf
                if (childClusters.get(1).isLeaf()) {
                    limitedClusters.put(i, childClusters.get(1));
                    i--;
                } else {
                    limitedClusters.put(getClusterNumberFromName(childClusters.get(1).getName()), childClusters.get(1));
                }
            } else {
                throw new RuntimeException("Not enough clusters available to get desired number of clusters.");
            }
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
//    private static void queryByUniqueMetadata(AppProperties appProperties) throws IOException {
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
//        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
//    }

    // for querying a predefined combination
    private static void queryByUniqueMetadata(AppProperties appProperties) throws IOException {
        System.out.println("inside queryByUniqueMetadata");
        int noOfNeuronsPerType = 37;

        // define which uniqueMetadata Types shall be used
        Set<UniqueMetadata> selectedUniqueMetadata = new HashSet<>();
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "granule")), new HashSet<>(Arrays.asList("dentate gyrus", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA3", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Glia", "astrocyte")), new HashSet<>(Arrays.asList("CA1", "hippocampus")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("stellate", "interneuron")), new HashSet<>(Arrays.asList("amygdala", "basolateral amygdala complex")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 2")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 3")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5a")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("principal cell", "pyramidal")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 5b")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Horizontal", "interneuron", "neurogliaform")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 1")), "rat", "", ""));
        selectedUniqueMetadata.add(new UniqueMetadata(new HashSet<>(Arrays.asList("Star", "pyramidal", "interneuron")), new HashSet<>(Arrays.asList("somatosensory", "neocortex", "layer 4")), "rat", "", ""));

        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());
        // add all metadata to UniqueMetadata
        for (String neuronMetadataRKey : neuronMetadata.keySet()) {
            UniqueMetadata.addNeuronMetadata(neuronMetadata.get(neuronMetadataRKey));
        }

//         select neurons depending on typeCount and input-variables
        List<String> selectedNeuronNames = new ArrayList<>();
        int k = 1;
        for (UniqueMetadata uniqueMetadata : selectedUniqueMetadata) {
            // select noOfNeuronsPerType neurons randomly
            selectedNeuronNames.addAll(pickNRandom(UniqueMetadata.getUniqueMetadataMap().get(uniqueMetadata).getNeuronNames(), noOfNeuronsPerType));
            System.out.println(uniqueMetadata.getSpecies() + ";" + String.join(", ", uniqueMetadata.getBrainRegion()) + ";" + String.join(", ", uniqueMetadata.getCellTypes()) + ";" + uniqueMetadata.getNoOfNeurons() + ";" + uniqueMetadata.getArchives().size());
            k += 1;
        }

        // write to json
        JsonUtils.writeJSON(selectedNeuronNames, appProperties.getOutputDirectory());
    }

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
