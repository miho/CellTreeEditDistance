package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.Clustering.PassingDataToELKI;
import edu.gcsc.celltreeedit.Utils;
import javafx.util.Pair;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ClusteringTest {

    //TODO: fails because clustering from R is not exactly the same as the one from ELKI
//    @Test
//    public void clusteringELKITest() throws FileNotFoundException {
//
//        // read distanceMatrix
//        Pair<double[][], String[]> resultTED = Utils.readMatrixFromTxt(new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/distanceMatrix01.txt"));
//        double[][] distanceMatrix = resultTED.getKey();
//        String[] fileNames = resultTED.getValue();
//        // create cluster from matrix and names
//        List<Set<String>> clusterResult = PassingDataToELKI.createClustering(distanceMatrix, fileNames, 2);
//        // read result from R
//        List<Set<String>> rResult = Utils.readRArrayFromTxt(new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/r_result01.txt"));
//        Set<Set<String>> clusterResultSet = new HashSet<>(clusterResult);
//        Set<Set<String>> rResultSet = new HashSet<>(rResult);
//        checkClustering(clusterResultSet, rResultSet);
//
//
//        // read distanceMatrix
//        resultTED = Utils.readMatrixFromTxt(new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/distanceMatrix03.txt"));
//        distanceMatrix = resultTED.getKey();
//        fileNames = resultTED.getValue();
//        // create cluster from matrix and names
//        clusterResult = PassingDataToELKI.createClustering(distanceMatrix, fileNames, 13);
//        // read result from R
//        rResult = Utils.readRArrayFromTxt(new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/r_result03.txt"));
//        clusterResultSet = new HashSet<>(clusterResult);
//        rResultSet = new HashSet<>(rResult);
//        checkClustering(clusterResultSet, rResultSet);
//
//
//
////        File[][] files = new File[4][2];
////        File distanceMatrixFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/distanceMatrix01.txt");
////        File rResultFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/r_result01.txt");
////        files[0][0] = distanceMatrixFile;
////        files[0][1] = rResultFile;
////
////        distanceMatrixFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/distanceMatrix02.txt");
////        rResultFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/r_result02.txt");
////        files[1][0] = distanceMatrixFile;
////        files[1][1] = rResultFile;
////
////        distanceMatrixFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/distanceMatrix03.txt");
////        rResultFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/r_result03.txt");
////        files[2][0] = distanceMatrixFile;
////        files[2][1] = rResultFile;
////
////        distanceMatrixFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/distanceMatrix04.txt");
////        rResultFile = new File("/media/exdisk/Sem06/BA/ProgramData/Test/TestClustering/r_result04.txt");
////        files[3][0] = distanceMatrixFile;
////        files[3][1] = rResultFile;
//
//
//    }

    private void checkClustering( Set<Set<String>> clusterResultSet, Set<Set<String>> rResultSet) {
//        assertEquals(clusterResultSet, rResultSet);

        // assert clusters contain same names
        for (Set<String> cluster : clusterResultSet) {
            boolean equal = false;
            for (Set<String> cluster2 : rResultSet) {
                if (cluster.equals(cluster2)) {
                    equal = true;
                }
            }
            assertTrue(equal);
        }
    }

//    @Test
//    public void clusteringTest() {
//        // create cluster with matrix and names
//        Clustering clustering = Clustering.getInstance();
//        Cluster cluster = clustering.createCluster(distanceMatrix, names);
//        clusteringTestRec(cluster);
//
//        List<Cluster> limitedClusters = ClusteringAnalyzer.limitClusterBySize(cluster, 3);
//        assertEquals("clstr#2", limitedClusters.get(2).getName());
//        assertEquals("clstr#1", limitedClusters.get(1).getName());
//        assertEquals("C", limitedClusters.get(0).getName());
//
//    }
//
//    private void clusteringTestRec(Cluster cluster) {
//
//        switch (cluster.getName()) {
//            case "clstr#4":
//                assertEquals(5, cluster.countLeafs());
//                assertEquals(4, cluster.getDistanceValue(), 0d);
//                assertEquals(cluster.getChildren().get(0).getName(), ("C"));
//                assertEquals(cluster.getChildren().get(1).getName(), ("clstr#3"));
//                break;
//            case "clstr#3":
//                assertEquals(4, cluster.countLeafs());
//                assertEquals(3, cluster.getDistanceValue(), 0d);
//                assertEquals(cluster.getChildren().get(0).getName(), ("clstr#1"));
//                assertEquals(cluster.getChildren().get(1).getName(), ("clstr#2"));
//                break;
//            case "clstr#2":
//                assertEquals(2, cluster.countLeafs());
//                assertEquals(2, cluster.getDistanceValue(), 0d);
//                assertEquals(cluster.getChildren().get(0).getName(), ("D"));
//                assertEquals(cluster.getChildren().get(1).getName(), ("E"));
//                break;
//            case "clstr#1":
//                assertEquals(2, cluster.countLeafs());
//                assertEquals(1, cluster.getDistanceValue(), 0d);
//                assertEquals(cluster.getChildren().get(0).getName(), ("A"));
//                assertEquals(cluster.getChildren().get(1).getName(), ("B"));
//                break;
//            default:
//                break;
//        }
//        if (cluster.getChildren().size() == 2) {
//            clusteringTestRec(cluster.getChildren().get(0));
//            clusteringTestRec(cluster.getChildren().get(1));
//        }
//    }

    private double[][] distanceMatrix = new double[][]{
            {0d, 1d, 4d, 3d, 3d},
            {1d, 0d, 4d, 3d, 3d},
            {4d, 4d, 0d, 4d, 4d},
            {3d, 3d, 4d, 0d, 2d},
            {3d, 3d, 4d, 2d, 0d}

    };

    private String[] names = new String[]{
            "a", "b", "c", "d", "e"
    };
}
