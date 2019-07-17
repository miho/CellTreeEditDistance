package edu.gcsc.celltreeedit.ClusterAnalysis;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.Clustering.Clustering;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import javafx.util.Pair;

import javax.swing.*;
import java.util.*;

public class ClusteringAnalyzer {

    public static void analyzeClusteringOfTEDResult(Pair<double[][], String[]> result, Map<String, NeuronMetadataR> neuronMetadata) {
        double[][] matrix = result.getKey();
        String[] fileNames = result.getValue();

        // Create uniqueMetadata from Filenames to know original clusters of filenames and their number
        UniqueMetadataContainer uniqueMetadataContainer = new UniqueMetadataContainer();
        NeuronMetadataR neuronMetadataR;
        // create unique metadata of filenames in matrix
        for (int i = 0; i < fileNames.length; i++) {
            neuronMetadataR = neuronMetadata.get(fileNames[i]);
            uniqueMetadataContainer.addNeuronMetadata(neuronMetadataR);
        }
        // put in List for fixed ordering
        List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects = new ArrayList<>(uniqueMetadataContainer.getUniqueMetadataMap().keySet());
        int noOfUniqueMetadata = uniqueMetadataObjects.size();

        // Calculate Cluster
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(matrix, fileNames);
        // get clusters according to size of uniqueMetadata
        List<Cluster> limitedClusters = limitClusterBySize(cluster, noOfUniqueMetadata);

        // calculate partitioning error and match clusters to uniqueMetadata
        // HungarianDouble takes DistanceMatrix of values
        // go through each uniqueMetadataObject. Calculate partitioning error for all clusters
        int[][] partitioningErrorMatrix = calculatePartitioningErrorMatrix(uniqueMetadataObjects, limitedClusters, uniqueMetadataContainer.getFileNameToUniqueMetadataMap());
        int length = partitioningErrorMatrix.length;
        int[][] partitioningErrorMatrixCopy = new int[length][];
        for (int i = 0; i < length; i++) {
            partitioningErrorMatrixCopy[i] = partitioningErrorMatrix[i].clone();
        }
        HungarianAlgorithm hungarian = new HungarianAlgorithm(partitioningErrorMatrixCopy);
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

    private static void showRelPartitioningErrors(float[][] relPartitioningErrors, List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects) {
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
        int[][] partitioningErrorMatrix = new int[noOfUniqueMetadata][noOfUniqueMetadata];
        for (int i = 0; i < noOfUniqueMetadata; i++) {
            List<Cluster> leafsOfCluster = findLeafsOfCluster(clusters.get(i));
            for (int j = 0; j < noOfUniqueMetadata; j++) {
                partitioningErrorMatrix[j][i] = calculatePartitioningError(uniqueMetadataObjects.get(j), leafsOfCluster, fileNamesUniqueMetadataMap);
            }
        }
        return partitioningErrorMatrix;
    }

    private static int calculatePartitioningError(UniqueMetadataContainer.UniqueMetadata uniqueMetadata, List<Cluster> leafsOfCluster, Map<String, UniqueMetadataContainer.UniqueMetadata> fileNamesUniqueMetadataMap) {
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

    private static float[][] calculateRelPartitioningErrors(int[][] partitioningErrorMatrix, Map<Integer, Integer> assignment, List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects) {
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
