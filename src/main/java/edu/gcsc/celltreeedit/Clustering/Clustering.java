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
        dp.setBackground(Color.WHITE);
        JFrame frame = new JFrame();
        frame.setSize(1000,800);
        frame.setTitle(outputFilename);
        frame.setVisible(true);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.WHITE);
        JLabel label1 = new JLabel(outputFilename);
        contentPane.add(label1, BorderLayout.NORTH);
        contentPane.add(dp, BorderLayout.CENTER);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        if (saveOutput) {
            Container content = frame.getContentPane();
            BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();

            content.printAll(g2d);

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
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }
}
