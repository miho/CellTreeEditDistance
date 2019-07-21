package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.*;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;
import de.lmu.ifi.dbs.elki.algorithm.AbstractAlgorithm;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.CLINK;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.extraction.CutDendrogramByNumberOfClusters;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.KMeansLloyd;
import de.lmu.ifi.dbs.elki.algorithm.clustering.kmeans.initialization.RandomUniformGeneratedInitialMeans;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.model.KMeansModel;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.ids.DBIDRange;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.SquaredEuclideanDistanceFunction;
import de.lmu.ifi.dbs.elki.logging.LoggingConfiguration;
import de.lmu.ifi.dbs.elki.utilities.ELKIBuilder;
import de.lmu.ifi.dbs.elki.utilities.random.RandomFactory;

import javax.swing.*;
import java.util.ArrayList;

public final class Clustering {

    // Thread-save Singleton
    private static final Clustering INSTANCE = new Clustering();

    public static Clustering getInstance() {
        return INSTANCE;
    }

    private Clustering() {}

    public void createELKICluster() {

    }



//    public void createWekaCluster(double[][] matrix, String[] fileNames) throws Exception {
//        ArrayList<Attribute> attributes = new ArrayList<>();
//        attributes.add(new Attribute("distance"));
//
//        Instances dataset = new Instances("TestInstances", );
//        HierarchicalClusterer hierarchicalClusterer = new HierarchicalClusterer();
////        hierarchicalClusterer.setDistanceFunction(new EuclideanDistance());
//        hierarchicalClusterer.setOptions(new String[]{"-N", "2", "-L", "WARD", "-P"});
//        hierarchicalClusterer.setPrintNewick(true);
//        hierarchicalClusterer.buildClusterer();
//    }

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
