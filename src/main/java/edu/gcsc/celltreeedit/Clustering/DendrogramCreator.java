package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.visualization.ClusterColorRegex;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataMapper;
import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import edu.gcsc.celltreeedit.NeuronMetadata.UniqueMetadataContainer;
import edu.gcsc.celltreeedit.TEDResult;
import edu.gcsc.celltreeedit.Tables;
import edu.gcsc.celltreeedit.Utils;
import javafx.util.Pair;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that helps to create a Dendrogram using Clustering-library. Contains the logic when to show, save dendrograms or whether filenames are replaced.
 */
public class DendrogramCreator {

    public static void createDendrogram(TEDResult result, File outputDirectory, String outputFilename, boolean saveOutput, List<ClusterColorRegex> clusterColorRegexes) {
        // create cluster with matrix and adjusted names
        Clustering clustering = new Clustering();
        clustering.createCluster(result.getDistanceMatrix(), result.getFileNames());

        if (saveOutput) {
            clustering.saveDendrogram(outputDirectory, outputFilename, clusterColorRegexes);
        } else {
            clustering.showDendrogram(outputFilename, clusterColorRegexes);
        }
    }

    public static void createDendrogram(TEDResult result, File metadataDirectory, File outputDirectory, String outputFilename, boolean replaceDendrogramNames, boolean saveOutput, List<ClusterColorRegex> clusterColorRegexes) throws IOException {
        String[] fileNames = result.getFileNames();
        outputFilename = (outputFilename.isEmpty()) ? "Dendrogram" : FilenameUtils.removeExtension(outputFilename);

        if (replaceDendrogramNames || saveOutput) {
            NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
            Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(metadataDirectory);
            String metadataFilename = outputFilename.replaceAll("_Label..", "");
            if (replaceDendrogramNames && saveOutput) {
                fileNames = Utils.printMetadataForFilenames(fileNames, neuronMetadata, outputDirectory, metadataFilename);
            } else if (!replaceDendrogramNames && saveOutput) {
                Utils.printMetadataForFilenames(fileNames, neuronMetadata, outputDirectory, metadataFilename);
            } else {
                fileNames = createFilenameMapping(fileNames, neuronMetadata, outputFilename);
            }
        }

        // create cluster with matrix and adjusted names
        Clustering clustering = new Clustering();
        clustering.createCluster(result.getDistanceMatrix(), fileNames);

        if (saveOutput) {
            clustering.saveDendrogram(outputDirectory, outputFilename, clusterColorRegexes);
        } else {
            clustering.showDendrogram(outputFilename, clusterColorRegexes);
        }
    }

    private static String[] createFilenameMapping(String[] oldFileNames, Map<String, NeuronMetadataR> neuronMetadata, String tablename) {
        Pair<String[], String[]> renameResult = renameFileNamesToUniqueMetadataNames(oldFileNames, neuronMetadata);
        String[] newFileNames = renameResult.getKey();
        String[] neuronMetadataNames = renameResult.getValue();
        tablename = (tablename.isEmpty()) ? "FilenameMapping" : tablename + "_FilenameMapping";

        Tables fileNameMapping = new Tables(newFileNames, oldFileNames, neuronMetadataNames, new String[]{"shown Names", "original FileNames", "neuronMetadata"});
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        frame.setSize(1000,1000);
        frame.setVisible(true);
        frame.setTitle(tablename);

        JLabel label1 = new JLabel(tablename);
        contentPane.add(label1, BorderLayout.NORTH);
        contentPane.add(fileNameMapping, BorderLayout.CENTER);

        return newFileNames;
    }

    private static Pair<String[], String[]> renameFileNamesToUniqueMetadataNames(String[] oldFileNames, Map<String, NeuronMetadataR> neuronMetadata) {
        String[] newFileNames = new String[oldFileNames.length];
        String[] neuronMetadataNames = new String[oldFileNames.length];

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
