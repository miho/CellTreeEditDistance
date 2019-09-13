package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.*;
import com.apporiented.algorithm.clustering.visualization.ClusterColorRegex;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;
import edu.gcsc.celltreeedit.Utils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Can be used to create a Cluster from a DistanceMatrix and show the result in a dendrogram
 */
public class Clustering {

    public Cluster getRootCluster() {
        return rootCluster;
    }

    private Cluster rootCluster;

    public void createCluster(double[][] distanceMatrix, String[] filenames) {
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        this.rootCluster = alg.performClustering(distanceMatrix, filenames,
                new WardLinkageStrategy());
    }

    public void showDendrogram(String outputFilename, List<ClusterColorRegex> clusterColorRegexes) {
        this.createDendrogram(new File(""), outputFilename, false, clusterColorRegexes);
    }

    public void saveDendrogram(File outputDirectory, String outputFilename, List<ClusterColorRegex> clusterColorRegexes) {
        this.createDendrogram(outputDirectory, outputFilename, true, clusterColorRegexes);
    }

    private void createDendrogram(File outputDirectory, String outputFilename, boolean saveOutput, List<ClusterColorRegex> clusterColorRegexes) {
        outputFilename = FilenameUtils.removeExtension(outputFilename) + "_Dendrogram";
        DendrogramPanel dp = new DendrogramPanel();
        dp.setClusterColorRegexes(clusterColorRegexes);
        dp.setModel(this.rootCluster);
        dp.setBackground(Color.WHITE);

        JFrame frame = new JFrame();
        frame.setSize(1000, 800);
        frame.setTitle(outputFilename);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.WHITE);

        JLabel label1 = new JLabel(outputFilename);
        contentPane.add(label1, BorderLayout.NORTH);
        contentPane.add(dp, BorderLayout.CENTER);

//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }

        if (saveOutput) {
            contentPane.setSize(1000, 800);
            contentPane.doLayout();
            BufferedImage img = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = img.createGraphics();

            contentPane.printAll(g2d);

            g2d.dispose();

            try {
                File fileToSave = Utils.incrementFileNameIfNecessary(outputDirectory, outputFilename + ".png");
                ImageIO.write(img, "png", fileToSave);
                System.out.println("Dendrogram saved to: " + fileToSave);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        } else {
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
