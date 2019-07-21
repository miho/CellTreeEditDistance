package edu.gcsc.celltreeedit.Clustering;

import de.lmu.ifi.dbs.elki.algorithm.AbstractAlgorithm;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.AGNES;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.AnderbergHierarchicalClustering;
import de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.extraction.CutDendrogramByNumberOfClusters;
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
import de.lmu.ifi.dbs.elki.logging.LoggingConfiguration;
import de.lmu.ifi.dbs.elki.utilities.ELKIBuilder;

import java.util.*;

public class PassingDataToELKI {

    public static List<Set<String>> createClustering(double[][] distanceMatrix, String[] fileNames, int noOfClusters) {
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

        List<Set<String>> result = new ArrayList<>();
        List<String> resultFileNames;
        for (Cluster cluster : clustering.getToplevelClusters()) {
            resultFileNames = new ArrayList<>();
            for (DBIDIter iter = cluster.getIDs().iter(); iter.valid(); iter.advance()) {
                resultFileNames.add(fileNames[ids.getOffset(iter)]);
            }
            result.add(new HashSet<>(resultFileNames));
        }

        return result;
    }
}
