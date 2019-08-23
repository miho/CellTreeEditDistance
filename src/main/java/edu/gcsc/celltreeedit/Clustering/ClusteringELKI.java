package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.Distance;
import de.lmu.ifi.dbs.elki.algorithm.AbstractAlgorithm;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.AGNES;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.AnderbergHierarchicalClustering;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.PointerHierarchyRepresentationResult;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.extraction.CutDendrogramByNumberOfClusters;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.extraction.SimplifiedHierarchyExtraction;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.linkage.WardLinkage;
import de.lmu.ifi.dbs.elki.data.Cluster;
import de.lmu.ifi.dbs.elki.data.Clustering;
import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.database.ids.*;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;
import de.lmu.ifi.dbs.elki.distance.distancefunction.minkowski.SquaredEuclideanDistanceFunction;
import de.lmu.ifi.dbs.elki.evaluation.AutomaticEvaluation;
import de.lmu.ifi.dbs.elki.evaluation.NoAutomaticEvaluation;
import de.lmu.ifi.dbs.elki.evaluation.clustering.extractor.CutDendrogramByNumberOfClustersExtractor;
import de.lmu.ifi.dbs.elki.logging.LoggingConfiguration;
import de.lmu.ifi.dbs.elki.result.AutomaticVisualization;
import de.lmu.ifi.dbs.elki.result.ExportVisualizations;
import de.lmu.ifi.dbs.elki.result.ResultHandler;
import de.lmu.ifi.dbs.elki.utilities.ELKIBuilder;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.ListParameterization;
import de.lmu.ifi.dbs.elki.visualization.visualizers.visunproj.DendrogramVisualization;
import javafx.util.Pair;

import java.util.*;

public class ClusteringELKI {

    // cut clustering for clusterAnalysis
    private static Pair<Clustering<?>, DBIDRange> createCutClustering(double[][] distanceMatrix, int noOfClusters) {
        // Set the logging level to statistics:
        LoggingConfiguration.setStatistics();

        // Adapter to load data from an existing array.
        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(distanceMatrix);
        // Create a database (which may contain multiple relations!)
        Database db = new StaticArrayDatabase(dbc, null);
        // Load the data into the database (do NOT forget to initialize...)
        db.initialize();
        // Relation containing the number vectors:
        Relation<NumberVector> rel = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
        // We know that the ids must be a continuous range:
        DBIDRange ids = (DBIDRange) rel.getDBIDs();
        // K-means should be used with squared Euclidean (least squares):
        SquaredEuclideanDistanceFunction dist = SquaredEuclideanDistanceFunction.STATIC;

        Clustering<?> clustering = new ELKIBuilder<>(CutDendrogramByNumberOfClusters.class) //
                .with(CutDendrogramByNumberOfClusters.Parameterizer.MINCLUSTERS_ID, noOfClusters) //
                .with(AbstractAlgorithm.ALGORITHM_ID, AnderbergHierarchicalClustering.class) //
                .with(AGNES.Parameterizer.LINKAGE_ID, WardLinkage.class)
                .build().run(db);
        return new Pair<>(clustering, ids);
    }

    // complete clustering for dendrogram creation
    public static Pair<Clustering<?>, DBIDRange> createCompleteClustering(double[][] distanceMatrix) {

        // Set the logging level to statistics:
        LoggingConfiguration.setStatistics();

        // Adapter to load data from an existing array.
        DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(distanceMatrix);
        // Create a database (which may contain multiple relations!)
        Database db = new StaticArrayDatabase(dbc, null);
        // Load the data into the database (do NOT forget to initialize...)
        db.initialize();
        // Relation containing the number vectors:
        Relation<NumberVector> rel = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
        // We know that the ids must be a continuous range:
        DBIDRange ids = (DBIDRange) rel.getDBIDs();

        // K-means should be used with squared Euclidean (least squares):
        SquaredEuclideanDistanceFunction dist = SquaredEuclideanDistanceFunction.STATIC;

        AutomaticVisualization visualization = new ELKIBuilder<>(AutomaticVisualization.class)
                .with(AutomaticVisualization.Parameterizer.WINDOW_TITLE_ID, "blabla")
                .build();


        Clustering<?> clustering = new ELKIBuilder<>(SimplifiedHierarchyExtraction.class) //
                .with(CutDendrogramByNumberOfClusters.Parameterizer.MINCLUSTERS_ID, 2)
                .with(AbstractAlgorithm.ALGORITHM_ID, AnderbergHierarchicalClustering.class) //
                .with(AGNES.Parameterizer.LINKAGE_ID, WardLinkage.class)
                .with(DendrogramVisualization.Parameterizer.LAYOUT_ID, "RECTANGULAR")
                .build().run(db);
        System.out.println("extracted");
        return new Pair<>(clustering, ids);
    }

    public static List<Set<String>> calculateCutListOfClusters(double[][] distanceMatrix, String[] fileNames, int noOfClusters) {

        Pair<Clustering<?>, DBIDRange> cutClusteringResult = createCutClustering(distanceMatrix, noOfClusters);
        List<Set<String>> result = new ArrayList<>();
        List<String> resultFileNames;
        for (Cluster cluster : cutClusteringResult.getKey().getToplevelClusters()) {
            resultFileNames = new ArrayList<>();
            for (DBIDIter iter = cluster.getIDs().iter(); iter.valid(); iter.advance()) {
                resultFileNames.add(fileNames[cutClusteringResult.getValue().getOffset(iter)]);
            }
            result.add(new HashSet<>(resultFileNames));
        }

        return result;
    }


    //    private String name;
//    private Cluster parent;
//    private List<Cluster> children;
//    private List<String> leafNames;
//    private Distance distance = new Distance();
//    private static void calculateLBehnkeClustersRec(Cluster cluster, com.apporiented.algorithm.clustering.Cluster lBehnkeCluster) {
//        lBehnkeCluster.setName(cluster.getName());
//        lBehnkeCluster.setDistance(new Distance(cluster));
//    }
//
//    public com.apporiented.algorithm.clustering.Cluster calculateLBehnkeClusters(double[][] distanceMatrix, String[] fileNames) {
//
//        Pair<Clustering<?>, DBIDRange> completeClusteringResult = createCompleteClustering(distanceMatrix);
//        Clustering<?> clustering = completeClusteringResult.getKey();
//
//        for (Cluster cluster : clustering.getToplevelClusters()) {
//            completeClusteringResult.getValue().getOffset(cluster.getIDs().iter());
//            resultFileNames = new ArrayList<>();
//            for (DBIDIter iter = cluster.getIDs().iter(); iter.valid(); iter.advance()) {
//                resultFileNames.add(fileNames[cutClusteringResult.getValue().getOffset(iter)]);
//            }
//            result.add(new HashSet<>(resultFileNames));
//        }
//
//        return result;
//    }
    // public calculateLBehnkeClusters
    // 1. createCompleteClustering
    // 2. change to LBehnke Clusters
    // 3. use DendrogramCreator to show Dendrogram



}
