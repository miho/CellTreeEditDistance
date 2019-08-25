package edu.gcsc.celltreeedit.ClusterAnalysis;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.Clustering.Clustering;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.TEDResult;
import javafx.util.Pair;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class ClusteringAnalyzer {

    // TODO: improve class for testing issues. return value of function only needed for testing purposes
    public static Pair<double[][], List<UniqueMetadataContainer.UniqueMetadata>> analyzeClusteringOfTEDResult(TEDResult result, Map<String, NeuronMetadataR> neuronMetadata) {
        double[][] matrix = result.getDistanceMatrix();
        String[] fileNames = result.getFileNames();

        // Create uniqueMetadataContainer and add neuronMetadata for filenames to know original clusters of filenames and their number
        UniqueMetadataContainer uniqueMetadataContainer = new UniqueMetadataContainer();
        NeuronMetadataR neuronMetadataR;
        // create unique metadata of filenames in matrix
        for (int i = 0; i < fileNames.length; i++) {
            neuronMetadataR = neuronMetadata.get(fileNames[i]);
            uniqueMetadataContainer.addNeuronMetadata(neuronMetadataR);
        }
        // put in List for fixed ordering which is needed later
        List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects = uniqueMetadataContainer.getUniqueMetadataMap()
                .keySet().stream().sorted(Comparator.comparingInt(UniqueMetadataContainer.UniqueMetadata::getUniqueMetadataId))
                .collect(Collectors.toList());
//        List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects = new ArrayList<>(uniqueMetadataContainer.getUniqueMetadataMap().keySet());
        int noOfUniqueMetadata = uniqueMetadataObjects.size();

        // Calculate Clustering
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(matrix, fileNames);
        // get clusters according to size of uniqueMetadata
        List<Cluster> limitedClusters = limitClusterBySize(cluster, noOfUniqueMetadata);

        // calculate partitioning error and match clusters to uniqueMetadata
        // HungarianDouble takes DistanceMatrix of values
        // for each uniqueMetadataObject calculate partitioning error for all clusters
        // partitioningErrorMatrix has ordering of uniqueMetadataObjects and limitedClusters
        int[][] partitioningErrorMatrix = calculatePartitioningErrorMatrix(uniqueMetadataObjects, limitedClusters, uniqueMetadataContainer.getFileNameToUniqueMetadataMap());

        // make a deep copy of partitioningErrorMatrix because Hungarian algorithm changes the matrix
        int length = partitioningErrorMatrix.length;
        int[][] partitioningErrorMatrixCopy = new int[length][];
        for (int i = 0; i < length; i++) {
            partitioningErrorMatrixCopy[i] = partitioningErrorMatrix[i].clone();
        }
        // use hungarian algorithm to find an optimal matching between uniqueMetadataObjects and Clusters
        HungarianAlgorithm hungarian = new HungarianAlgorithm(partitioningErrorMatrixCopy);
        Map<Integer, Integer> assignment = putAssignmentInMap(hungarian.findOptimalAssignment());

        // calculate overall absolute partitioning error
        int overallAbsPartitioningError = calculateAbsPartitioningError(partitioningErrorMatrix, assignment);

        // TODO: get number of Neurons with uniqueMetadata A and number of Neurons with uniqueMetadata B
        // calculate relative partitioning errors and save in lower left matrix
        double[][] relPartitioningErrors = calculateRelPartitioningErrors(assignment, uniqueMetadataObjects, limitedClusters, uniqueMetadataContainer.getFileNameToUniqueMetadataMap());

        // display lower left matrix with javax Swing colorcoded as in heumann, wittum paper

        showRelPartitioningErrors(relPartitioningErrors, uniqueMetadataObjects);
        return new Pair<>(relPartitioningErrors, uniqueMetadataObjects);
    }

    private static void showRelPartitioningErrors(double[][] relPartitioningErrors, List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects) {
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

    private static int[][] calculatePartitioningErrorMatrix(List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects, List<Cluster> clusters, Map<String, UniqueMetadataContainer.UniqueMetadata> fileNamesUniqueMetadataMap) {
        int noOfUniqueMetadata = uniqueMetadataObjects.size();
        // rows are uniqueMetadataObjects. columns are Clusters
        int[][] partitioningErrorMatrix = new int[noOfUniqueMetadata][noOfUniqueMetadata];
        // first loop to go through clusters
        for (int i = 0; i < noOfUniqueMetadata; i++) {
            for (int j = 0; j < noOfUniqueMetadata; j++) {
                // get all leafs for one specific cluster
                List<Cluster> leafsOfCluster = findLeafsOfCluster(clusters.get(j));
                partitioningErrorMatrix[i][j] = calculatePartitioningError(uniqueMetadataObjects.get(i), leafsOfCluster, fileNamesUniqueMetadataMap);
            }
        }
        return partitioningErrorMatrix;
    }

    private static int calculatePartitioningError(UniqueMetadataContainer.UniqueMetadata uniqueMetadata, List<Cluster> leafsOfCluster, Map<String, UniqueMetadataContainer.UniqueMetadata> fileNamesUniqueMetadataMap) {
        int partitioningError = 0;
        // look at every leaf and count how many of them do not belong to the uniqueMetadataObject
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

    private static double[][] calculateRelPartitioningErrors(Map<Integer, Integer> assignment, List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects, List<Cluster> clusters, Map<String, UniqueMetadataContainer.UniqueMetadata> fileNamesUniqueMetadataMap) {
        // size is 1 row and 1 column smaller because only lower left matrix is relevant. An entry sums up would be symmetric and main diagonal entries would be 0
        int size = assignment.size() - 1;
        double[][] relPartitioningErrors = new double[size][size];
        // index of row of new matrix
        for (int i = 0; i < size; i++) {
            // index of col. only go from 0 to i --> lower left matrix
            for (int j = 0; j <= i; j++) {
                // number of neurons from uniqueMetadataObject A that have been put in B + number of neurons of B put in A
                int deltaAbs = (calculateWronglyClusteredNeurons(uniqueMetadataObjects.get(j), clusters.get(assignment.get(i+1)), fileNamesUniqueMetadataMap) + calculateWronglyClusteredNeurons(uniqueMetadataObjects.get(i+1), clusters.get(assignment.get(j)), fileNamesUniqueMetadataMap));
                // CAN BECOME MORE THAN 1. if clustering is very bad number of wrongly clustered neurons can become as high as sum of neurons of class
                relPartitioningErrors[i][j] = 2* (double) deltaAbs / ((double) uniqueMetadataObjects.get(i+1).getNoOfNeurons() + (double) uniqueMetadataObjects.get(j).getNoOfNeurons());
            }
        }
        return relPartitioningErrors;
    }

    // how many neurons of uniqueMetadataWrong have been put into the cluster of uniqueMetadataCorrect
    private static int calculateWronglyClusteredNeurons(UniqueMetadataContainer.UniqueMetadata uniqueMetadataWrong, Cluster clusterCorrect, Map<String, UniqueMetadataContainer.UniqueMetadata> fileNamesUniqueMetadataMap) {
        int numberOfWronglyClusteredNeurons = 0;
        for (String name : getLeafNamesOfCluster(clusterCorrect)) {
            numberOfWronglyClusteredNeurons += (fileNamesUniqueMetadataMap.get(name).equals(uniqueMetadataWrong)) ? 1 : 0;
        }
        return numberOfWronglyClusteredNeurons;
    }

    private static List<String> getLeafNamesOfCluster(Cluster cluster) {
        List<String> leafNames = new ArrayList<>();
        if (cluster.isLeaf()) {
            return Arrays.asList(cluster.getName());
        }
        for (Cluster childCluster : cluster.getChildren()) {
            leafNames.addAll(getLeafNamesOfCluster(childCluster));
        }
        return leafNames;
    }

    /**
     * returns list of Cluster with number of Clusters limited by size-input. Function uses Cluster-number from name property as Clusternumbers are ordered by their creation.
     *
     * @param cluster
     * @param size
     * @return
     */
    public static List<Cluster> limitClusterBySize(Cluster cluster, int size) {
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
}
