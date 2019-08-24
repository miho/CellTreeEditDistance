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
import java.text.Collator;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class DendrogramCreator {

    public static void showDendrogram(Pair<double[][], String[]> result, File metadataDirectory, boolean replaceDendrogramNames) throws IOException {
        String[] fileNames = result.getValue();
        String[] neuronMetadataNames = new String[fileNames.length];

        if (replaceDendrogramNames) {
            Pair<String[], String[]> renameResult = renameFileNamesToUniqueMetadataNames(fileNames, metadataDirectory);
            fileNames = renameResult.getKey();
            neuronMetadataNames = renameResult.getValue();
        }

        // create cluster with matrix and adjusted names
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getKey(), fileNames);
        // generate dendrogram
        clustering.showCluster(cluster);
        if (replaceDendrogramNames) {
            showFileNameMapping(result.getValue(), fileNames, neuronMetadataNames);
        }
    }

    private static void showFileNameMapping(String[] oldFileNames, String[] newFileNames, String[] neuronMetadataNames) {
        JFrame frame = new JFrame();
        Tables fileNameMapping = new Tables(newFileNames, oldFileNames, neuronMetadataNames, new String[]{"shown Names", "original FileNames", "neuronMetadata Names"});
        frame.add(fileNameMapping);
        frame.setSize(1000,1000);
        frame.setVisible(true);
        frame.setTitle("FileName-Mapping");
    }

    private static Pair<String[], String[]> renameFileNamesToUniqueMetadataNames(String[] oldFileNames, File metadataDirectory) throws IOException {
        String[] newFileNames = new String[oldFileNames.length];
        String[] neuronMetadataNames = new String[oldFileNames.length];
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
            neuronMetadataNames[i] = neuronMetadataR.getSpecies() + " | " + neuronMetadataR.getBrainRegion().stream().sorted().collect(Collectors.joining(", ")) + " | " + neuronMetadataR.getCellType().stream().sorted().collect(Collectors.joining(", "));
        }
        return new Pair<>(newFileNames, neuronMetadataNames);
    }
}
