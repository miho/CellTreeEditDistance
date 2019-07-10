package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;

import javax.swing.*;

public final class Clustering {

    // Clustering is a Singleton
    private static final Clustering INSTANCE = new Clustering();

    public static Clustering getInstance() {
        return INSTANCE;
    }

    private Clustering() {}


    public Cluster createCluster(double[][] matrix, String[] fileNames) {
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        return alg.performClustering(matrix, fileNames,
                new AverageLinkageStrategy());
    }

    public void showCluster(Cluster cluster) {
        DendrogramPanel dp = new DendrogramPanel();
        dp.setModel(cluster);
        JFrame frame = new JFrame();
        frame.add(dp);
        frame.setSize(950,350);
        frame.setVisible(true);
        frame.setTitle("Dendrogram");
    }



}
