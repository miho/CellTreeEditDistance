package edu.gcsc.celltreeedit.Clustering;

import com.apporiented.algorithm.clustering.*;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;
import edu.gcsc.celltreeedit.Utils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Clustering {

    // Thread-save Singleton
    private static final Clustering INSTANCE = new Clustering();

    public static Clustering getInstance() {
        return INSTANCE;
    }

    private Clustering() {}

    public Cluster createCluster(double[][] matrix, String[] fileNames) {
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        return alg.performClustering(matrix, fileNames,
                new WardLinkageStrategy());
    }

    public void createDendrogram(Cluster cluster, File outputDirectory, String outputFilename, boolean saveOutput) {
        outputFilename = FilenameUtils.removeExtension(outputFilename) + "_Dendrogram";
        DendrogramPanel dp = new DendrogramPanel();
        dp.setModel(cluster);
        JFrame frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        JLabel label1 = new JLabel(outputFilename);
        frame.add(label1);
        frame.add(dp);
        frame.setSize(1024,768);
        frame.setTitle(outputFilename);
        frame.setVisible(true);

        if (saveOutput) {
            Container content = frame.getContentPane();
            BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();

            content.printAll(g2d);

            g2d.dispose();

            try {
                File fileToSave = Utils.incrementFileNameIfNecessary(outputDirectory, outputFilename + ".png");
                ImageIO.write(img, "png", fileToSave);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            frame.dispose();
        } else {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
