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

import javax.swing.*;
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
        System.out.println(appProperties.getCalcType());
        System.out.println(appProperties.getBaseDirectory());
        System.out.println(appProperties.getJsonDirectory());
        System.out.println(appProperties.getDataDirectory());
        System.out.println(appProperties.getMatrixExportName());
        System.out.println(appProperties.getMatrixExportDirectory());

//        switch (appProperties.getCalcType()) {
//            case 0:
//                calculateCompletely();
//                break;
//            case 1:
//                calculateMatrixOnly();
//                break;
//            case 2:
//                queryLucene();
//                break;
//            case 3:
//                preprocessSWCDirectory();
//                break;
//            default:
//                System.out.println("calcType not valid");
//                break;
//        }

//        CellTreeEditDistance matrix = new CellTreeEditDistance();
//        if (!jsonDirectory.getName().equals("")) {
//            Set<String> swcFileNames = Utils.parseJsonToFileNames(jsonDirectory);
//            matrix.compareFilesFromFilenames(swcFileNames, swcDirectory, 9);
//        } else {
//            matrix.compareFilesFromChoose(9);
//        }
    }

    private static void calculateCompletely() throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(new File("/media/exdisk/Sem06/BA/Data/Neuromorpho/Metadata"));

        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        boolean argumentGiven = false;
        if (argumentGiven) {
//            cellTreeEditDistance.compareFilesFromFilenames();
        } else {
//            doSomething
        }
        // calculate clustering
        // generate dendrogram
    }

    private static void calculateMatrixOnly() {
        // check if swc-directory is given
        // calculate matrix
    }

    private static void queryLucene() {
        // let user query metadata through lucene
        // let user export names into json file
    }

    private static void preprocessSWCDirectory() throws IOException {
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataRImpl> neuronMetadata = neuronMetadataMapper.mapFromDirectory(new File("/media/exdisk/Sem06/BA/Data/Neuromorpho/Metadata"));

        // preprocess SWC-Directory
        File swcDirectory = new File("/media/exdisk/Sem06/BA/Data/Neuromorpho/SWC-Files/00_All");
        SWCPreprocessing swcPreprocessing = new SWCPreprocessing();
        swcPreprocessing.preprocessSWCDirectory(neuronMetadata, swcDirectory);
    }

//    private static boolean hasValuedParam(final String cmdArg,
//                                          final AppParameter parameter) {
//        return cmdArg.startsWith(parameter.paramName) &&
//                cmdArg.length() > parameter.valueOffset;
//    }
//
//    private static boolean extractBoolean(final String cmdArg, final AppParameter parameter) {
//        return Boolean.parseBoolean(cmdArg.substring(parameter.valueOffset));
//    }
//
//    private static int extractInt(final String cmdArg, final AppParameter parameter) {
//        return Integer.parseInt(cmdArg.substring(parameter.valueOffset));
//    }
//
//    private static String extractString(final String cmdArg, final AppParameter parameter) {
//        return cmdArg.substring(parameter.valueOffset);
//    }

}
