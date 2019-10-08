/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
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

    public void createCluster(float[][] distanceMatrix, String[] filenames) {
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        this.rootCluster = alg.performClustering(distanceMatrix, filenames,
                new WardLinkageStrategy());
    }

    void showDendrogram(String titleName, List<ClusterColorRegex> clusterColorRegexes) {
        this.createDendrogram(new File(""), titleName, false, clusterColorRegexes);
    }

    void saveDendrogram(File outputDirectory, String outputFilename, List<ClusterColorRegex> clusterColorRegexes) {
        this.createDendrogram(outputDirectory, outputFilename, true, clusterColorRegexes);
    }

    private void createDendrogram(File outputDirectory, String outputFilename, boolean saveOutput, List<ClusterColorRegex> clusterColorRegexes) {
        outputFilename = (outputFilename.isEmpty() || outputFilename.equals("Dendrogram")) ? "Dendrogram" : FilenameUtils.removeExtension(outputFilename) + "_Dendrogram";
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
            contentPane.setSize(500, 800);
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
