package edu.gcsc.celltreeedit.CodeEridTest;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.ClusterAnalysis.ClusteringAnalyzer;
import edu.gcsc.celltreeedit.Clustering.Clustering;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClusteringTest {
    // topology of clustering correct?
    // calculated distance correct?
    // limitCluster works correctly?

    @Test
    public void clusteringTest() {
        // create cluster with matrix and names
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(distanceMatrix, names);
        clusteringTestRec(cluster);

        List<Cluster> limitedClusters = ClusteringAnalyzer.limitClusterBySize(cluster, 3);
        assertEquals("clstr#2", limitedClusters.get(2).getName());
        assertEquals("clstr#1", limitedClusters.get(1).getName());
        assertEquals("C", limitedClusters.get(0).getName());

    }

    private void clusteringTestRec(Cluster cluster) {

        switch (cluster.getName()) {
            case "clstr#4":
                assertEquals(5, cluster.countLeafs());
                assertEquals(4, cluster.getDistanceValue(), 0d);
                assertEquals(cluster.getChildren().get(0).getName(), ("C"));
                assertEquals(cluster.getChildren().get(1).getName(), ("clstr#3"));
                break;
            case "clstr#3":
                assertEquals(4, cluster.countLeafs());
                assertEquals(3, cluster.getDistanceValue(), 0d);
                assertEquals(cluster.getChildren().get(0).getName(), ("clstr#1"));
                assertEquals(cluster.getChildren().get(1).getName(), ("clstr#2"));
                break;
            case "clstr#2":
                assertEquals(2, cluster.countLeafs());
                assertEquals(2, cluster.getDistanceValue(), 0d);
                assertEquals(cluster.getChildren().get(0).getName(), ("D"));
                assertEquals(cluster.getChildren().get(1).getName(), ("E"));
                break;
            case "clstr#1":
                assertEquals(2, cluster.countLeafs());
                assertEquals(1, cluster.getDistanceValue(), 0d);
                assertEquals(cluster.getChildren().get(0).getName(), ("A"));
                assertEquals(cluster.getChildren().get(1).getName(), ("B"));
                break;
            default:
                break;
        }
        if (cluster.getChildren().size() == 2) {
            clusteringTestRec(cluster.getChildren().get(0));
            clusteringTestRec(cluster.getChildren().get(1));
        }
    }


    private double[][] distanceMatrix = new double[][]{
            {0d, 1d, 4d, 3d, 3d},
            {1d, 0d, 4d, 3d, 3d},
            {4d, 4d, 0d, 4d, 4d},
            {3d, 3d, 4d, 0d, 2d},
            {3d, 3d, 4d, 2d, 0d}

    };

    private String[] names = new String[]{
            "A", "B", "C", "D", "E"
    };
}
