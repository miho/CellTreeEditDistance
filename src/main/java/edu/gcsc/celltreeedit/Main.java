package edu.gcsc.celltreeedit;

//import edu.gcsc.celltreeedit.AppProperties.AppParameter;
import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;
import edu.gcsc.celltreeedit.AppProperties.AppProperties;
import edu.gcsc.celltreeedit.AppProperties.CommandLineParsing;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataRImpl;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        AppProperties appProperties = CommandLineParsing.parseArguments(args);

        // calcType
        // 0 (default) -> calculate everything
        // 1 -> only calc matrix with minimum resources
        // 2 -> choose for json-file
        // 3 -> preprocess swc-files
        switch (appProperties.getCalcType()) {
            case 0:
                calculateCompletely(appProperties);
                break;
            case 1:
                calculateMatrixOnly(appProperties);
                break;
            case 2:
                queryLucene(appProperties);
                break;
            case 3:
                preprocessSWCDirectory(appProperties);
                break;
            default:
                System.out.println("calcType not valid");
                break;
        }
    }

    private static void calculateCompletely(AppProperties appProperties) throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        System.out.println(appProperties.getMetadataDirectory().getPath());
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(appProperties.getMetadataDirectory());

        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        Pair<double[][], String[]> result;
        if (appProperties.getJsonDirectory().getPath().equals("")) {
            result = cellTreeEditDistance.compareFilesFromDirectory(appProperties.getSwcFileDirectory(), 9);
        } else {
            result = cellTreeEditDistance.compareFilesFromFilenames(Utils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getMatrixExportDirectory(), appProperties.getMatrixExportName());
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
            result = cellTreeEditDistance.compareFilesFromFilenames(Utils.parseJsonToFileNames(appProperties.getJsonDirectory()), appProperties.getSwcFileDirectory(), 9);
        }
        Utils.printToTxt(result.getKey(), result.getValue(), appProperties.getMatrixExportDirectory(), appProperties.getMatrixExportName());
    }

    private static void queryLucene(AppProperties appProperties) {
        // let user query metadata through lucene
        // let user export names into json file
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
}
