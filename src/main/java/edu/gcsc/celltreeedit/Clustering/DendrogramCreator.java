package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.Tables;
import javafx.util.Pair;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DendrogramCreator {

    public static void showDendrogram(Pair<double[][], String[]> result, File metadataDirectory) throws IOException {
        String[] oldFileNames = result.getValue();
        String[] newFileNames = renameFileNamesToUniqueMetadataNames(oldFileNames, metadataDirectory);

        // create cluster with matrix and adjusted names
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getKey(), newFileNames);
        // generate dendrogram
        clustering.showCluster(cluster);
        showFileNameMapping(oldFileNames, newFileNames);
    }

    private static void showFileNameMapping(String[] oldFileNames, String[] newFileNames) {
        JFrame frame = new JFrame();
        Tables fileNameMapping = new Tables(newFileNames, oldFileNames, new String[]{"Names", "original FileNames"});
        frame.add(fileNameMapping);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,350);
        frame.setVisible(true);
        frame.setTitle("FileName-Mapping");
    }

    private static String[] renameFileNamesToUniqueMetadataNames(String[] oldFileNames, File metadataDirectory) throws IOException {
        String[] newFileNames = new String[oldFileNames.length];
        // put metadata in hashMap
        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(metadataDirectory);

        UniqueMetadataContainer uniqueMetadataContainer = new UniqueMetadataContainer();
        UniqueMetadataContainer.UniqueMetadata uniqueMetadata;
        NeuronMetadataR neuronMetadataR;
        // create unique metadata of files
        for (int i = 0; i < oldFileNames.length; i++) {
            neuronMetadataR = neuronMetadata.get(oldFileNames[i]);
            uniqueMetadata = uniqueMetadataContainer.addNeuronMetadata(neuronMetadataR);
            // uniqueMetadataId, archive, neuronId
            newFileNames[i] = uniqueMetadata.getUniqueMetadataId() + ", " + neuronMetadataR.getArchive() + ", " + neuronMetadataR.getNeuronId();
        }
        return newFileNames;
    }
}
