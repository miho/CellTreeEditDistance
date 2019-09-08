package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.Cluster;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.TEDResult;
import edu.gcsc.celltreeedit.Tables;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class DendrogramCreator {

    public static void createDendrogram(TEDResult result, File metadataDirectory, File outputDirectory, String outputFilename, boolean replaceDendrogramNames, boolean saveOutput) throws IOException {
        String[] fileNames = result.getFileNames();

        if (replaceDendrogramNames) {
            fileNames = createFilenameMapping(fileNames, metadataDirectory, outputDirectory, outputFilename, saveOutput);
        }

        // create cluster with matrix and adjusted names
        Clustering clustering = Clustering.getInstance();
        Cluster cluster = clustering.createCluster(result.getDistanceMatrix(), fileNames);
        // generate dendrogram
        clustering.createDendrogram(cluster, outputDirectory, outputFilename, saveOutput);
    }

    private static String[] createFilenameMapping(String[] oldFileNames, File metadataDirectory, File outputDirectory, String outputFilename, boolean saveOutput) throws IOException {
        Pair<String[], String[]> renameResult = renameFileNamesToUniqueMetadataNames(oldFileNames, metadataDirectory);
        String[] newFileNames = renameResult.getKey();
        String[] neuronMetadataNames = renameResult.getValue();
        outputFilename = FilenameUtils.removeExtension(outputFilename) + "_FilenameMapping";

        Tables fileNameMapping = new Tables(newFileNames, oldFileNames, neuronMetadataNames, new String[]{"shown Names", "original FileNames", "neuronMetadata Names"});
        if (saveOutput) {
            fileNameMapping.printTable(outputDirectory, outputFilename);
        } else {
            JFrame frame = new JFrame();
            Container contentPane = frame.getContentPane();
            contentPane.setLayout(new BorderLayout());
            frame.setSize(1000,1000);
            frame.setVisible(true);
            frame.setTitle(outputFilename);

            JLabel label1 = new JLabel(outputFilename);
            contentPane.add(label1, BorderLayout.NORTH);

//            JPanel rowPane = new JPanel();
//            frame.add(rowPane);
            contentPane.add(fileNameMapping, BorderLayout.CENTER);
        }

        return newFileNames;
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
