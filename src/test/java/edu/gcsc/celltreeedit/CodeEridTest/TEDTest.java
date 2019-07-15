package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCostModel;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TEDTest {

    // - TED --> Fälle Bild prüfen (für alle Labels: Baum-leer, gleiche Bäume, fast gleiche Bäume (zwei Knoten mit kleinen labels mehr, löschen mittlerer Knoten führt zu anderem Baum, hinzufügen mittlerer Knoten führt zu anderem Baum, umbenennen mittlerer Knoten führt zu anderem baum)


    private double deltaBySize(double value) {
        double epsilon = 1e-8;
        int integerPlaces = Double.toString(value).indexOf('.');
        return epsilon * Double.parseDouble("1e" + integerPlaces + 1);
    }

    @Test
    public void checkTEDSameTrees() throws IOException {
        for (int i = 1; i < 23; i++) {
            FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01.CNG.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);
            FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01.CNG.swc"));
            TreeCreator t2 = new TreeCreator(f2);
            Node<NodeData> root2 = t2.createTree(i);

            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(root1, root2);
            assertEquals(0f, result, 0f);
        }
    }

    @Test
    public void checkTEDTreeEmpty() throws IOException {
        for (int i = 1; i < 23; i++) {
            System.out.println("Label: " + i);
            FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01.CNG.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);
            Node<NodeData> root2 = new Node<>(new NodeData(0d));

            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(root1, root2);
            double expectedResult = 0d;
            for (Node<NodeData> node: t1.getNodeList()) {
                expectedResult += node.getNodeData().getLabel();
            }
            float expectedResultFloat = (float) expectedResult;
            System.out.println("expectedResult: " + expectedResult);
            System.out.println("actualResult: " + result);
            assertEquals(expectedResultFloat, result, deltaBySize(expectedResult));


            // change order of trees
            apted = new APTED<>(new TreeCostModel());
            result = apted.computeEditDistance(root2, root1);
            expectedResult = 0d;
            for (Node<NodeData> node: t1.getNodeList()) {
                expectedResult += node.getNodeData().getLabel();
            }
            expectedResultFloat = (float) expectedResult;
            System.out.println("expectedResult: " + expectedResult);
            System.out.println("actualResult: " + result);
            assertEquals(expectedResultFloat, result, deltaBySize(expectedResult));
        }
    }


    @Test
    public void checkTEDSameTreesLeafInserted() throws IOException {

        double lengthOfSegment = 1;
        double volumeOfSegment = Math.PI * 1 * 1 * lengthOfSegment;
        double surfaceOfSegment = 2 * Math.PI * 1 * lengthOfSegment;

//        double lengthOfT = 20.6838908114423 + lengthOfSegment;
//        double volumeOfT = 195945673.142389 + volumeOfSegment;
//        double surfaceOfT = 741241459.754624 + surfaceOfSegment;

        double lengthOfT = 2 * lengthOfSegment;
        double volumeOfT = 2 * volumeOfSegment;
        double surfaceOfT = 2 * surfaceOfSegment;

        float result;
        double testTED;

        for (int i = 1; i < 23; i++) {
            System.out.println(i);
            FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01_superSimpleTree.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);

            FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01_superSimpleTree_1NodeInserted.swc"));
            TreeCreator t2 = new TreeCreator(f2);
            Node<NodeData> root2 = t2.createTree(i);

            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            result = apted.computeEditDistance(root1, root2);

            testTED = 0;
            switch (i) {
                case 1:
                    testTED = 1d;
                    break;
                case 2:
                    testTED = 1d / 4d;
                    break;
                case 3:
                    testTED = lengthOfSegment;
                    break;
                case 4:
                    testTED = lengthOfSegment;
                    break;
                case 5:
                    testTED = lengthOfSegment;
                    break;
                case 6:
                    testTED = lengthOfSegment / lengthOfT;
                    break;
                case 7:
                    testTED = lengthOfSegment / lengthOfT;
                    break;
                case 8:
                    testTED = lengthOfSegment / lengthOfT;
                    break;
                case 9:
                    testTED = volumeOfSegment;
                    break;
                case 10:
                    testTED = volumeOfSegment;
                    break;
                case 11:
                    testTED = volumeOfSegment;
                    break;
                case 12:
                    testTED = volumeOfSegment / volumeOfT;
                    break;
                case 13:
                    testTED = volumeOfSegment / volumeOfT;
                    break;
                case 14:
                    testTED = volumeOfSegment / volumeOfT;
                    break;
                case 15:
                    testTED = surfaceOfSegment;
                    break;
                case 16:
                    testTED = surfaceOfSegment;
                    break;
                case 17:
                    testTED = surfaceOfSegment;
                    break;
                case 18:
                    testTED = surfaceOfSegment / surfaceOfT;
                    break;
                case 19:
                    testTED = surfaceOfSegment / surfaceOfT;
                    break;
                case 20:
                    testTED = surfaceOfSegment / surfaceOfT;
                    break;
                case 21:
                    testTED = volumeOfSegment / surfaceOfT;
                    break;
                case 22:
                    testTED = 0d;
                    break;
                }
            assertEquals((float) testTED, result, 0f);
        }
    }

    private int calculateNoOfNodes(Node<NodeData> node) {
        int noOfNodes = 1;
        for (Node<NodeData> child : node.getChildren()) {
            noOfNodes += calculateNoOfNodes(child);
        }
        return noOfNodes;
    }

    @Test
    public void checkTEDSameTreesLeafRenamed() throws IOException {

        double lengthOfSegment = 1;
        double volumeOfSegment = Math.PI * 1 * 1 * lengthOfSegment;
        double surfaceOfSegment = 2 * Math.PI * 1 * lengthOfSegment;

//        double lengthOfT = 20.6838908114423 + lengthOfSegment;
//        double volumeOfT = 195945673.142389 + volumeOfSegment;
//        double surfaceOfT = 741241459.754624 + surfaceOfSegment;

        double lengthOfT = 3 * lengthOfSegment;
        double volumeOfT = 3 * volumeOfSegment;
        double surfaceOfT = 3 * surfaceOfSegment;

        float result;
        double testTED;

        for (int i = 1; i < 23; i++) {
            System.out.println(i);
            FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01_superSimpleTree.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);

            FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01_superSimpleTree_1NodeRenamed.swc"));
            TreeCreator t2 = new TreeCreator(f2);
            Node<NodeData> root2 = t2.createTree(i);

            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            result = apted.computeEditDistance(root1, root2);

            testTED = 0;
            switch (i) {
                case 1:
                    testTED = 0d;
                    break;
                case 2:
                    testTED = 0d;
                    break;
                case 3:
                    testTED = lengthOfSegment;
                    break;
                case 4:
                    testTED = lengthOfSegment;
                    break;
                case 5:
                    testTED = lengthOfSegment;
                    break;
                case 6:
                    testTED = lengthOfSegment / lengthOfT;
                    break;
                case 7:
                    testTED = lengthOfSegment / lengthOfT;
                    break;
                case 8:
                    testTED = lengthOfSegment / lengthOfT;
                    break;
                case 9:
                    testTED = volumeOfSegment;
                    break;
                case 10:
                    testTED = volumeOfSegment;
                    break;
                case 11:
                    testTED = volumeOfSegment;
                    break;
                case 12:
                    testTED = volumeOfSegment / volumeOfT;
                    break;
                case 13:
                    testTED = volumeOfSegment / volumeOfT;
                    break;
                case 14:
                    testTED = volumeOfSegment / volumeOfT;
                    break;
                case 15:
                    testTED = surfaceOfSegment;
                    break;
                case 16:
                    testTED = surfaceOfSegment;
                    break;
                case 17:
                    testTED = surfaceOfSegment;
                    break;
                case 18:
                    testTED = surfaceOfSegment / surfaceOfT;
                    break;
                case 19:
                    testTED = surfaceOfSegment / surfaceOfT;
                    break;
                case 20:
                    testTED = surfaceOfSegment / surfaceOfT;
                    break;
                case 21:
                    testTED = volumeOfSegment / surfaceOfT;
                    break;
                case 22:
                    testTED = 0d;
                    break;
            }
            assertEquals((float) testTED, result, 0f);
        }
    }
}
