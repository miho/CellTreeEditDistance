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
            assertEquals(expectedResultFloat, result, deltaBySize(expectedResult));


            // change order of trees
            apted = new APTED<>(new TreeCostModel());
            result = apted.computeEditDistance(root2, root1);
            expectedResult = 0d;
            for (Node<NodeData> node: t1.getNodeList()) {
                expectedResult += node.getNodeData().getLabel();
            }
            expectedResultFloat = (float) expectedResult;
            assertEquals(expectedResultFloat, result, deltaBySize(expectedResult));
        }
    }

    @Test
    public void checkTEDSuperSimpleTreesCreatedManually() {

        double labelValue1 = 1d/3;
        double labelValue2 = 1d/2;

        // create first tree manually
        NodeData nodeData = new NodeData(1);
        Node<NodeData> root1 = new Node<>(nodeData);
        nodeData = new NodeData(1);
        Node<NodeData> leftChild1 = new Node<>(nodeData);
        root1.addChild(leftChild1);
        nodeData = new NodeData(1);
        Node<NodeData> rightChild1 = new Node<>(nodeData);
        root1.addChild(rightChild1);
        // set labels manually
        root1.getNodeData().setLabel(labelValue1);
        leftChild1.getNodeData().setLabel(labelValue1);
        rightChild1.getNodeData().setLabel(labelValue1);

        // create second tree manually
        nodeData = new NodeData(1);
        Node<NodeData> root2 = new Node<>(nodeData);
        nodeData = new NodeData(1);
        Node<NodeData> leftChild2 = new Node<>(nodeData);
        root2.addChild(leftChild2);

        // set labels manually
        root2.getNodeData().setLabel(labelValue2);
        leftChild2.getNodeData().setLabel(labelValue2);


        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        double result = apted.computeEditDistance(root2, root1);

        assertEquals(labelValue1, result, 0d);
    }

    @Test
    public void checkTEDSameTrees1lengthChanged() throws IOException {

        double diffLengthOfSegment = 1;
        double diffVolumeOfSegment = Math.PI * 1 * 1 * diffLengthOfSegment;
        double diffSurfaceOfSegment = 2 * Math.PI * 1 * diffLengthOfSegment;

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
                    testTED = diffLengthOfSegment;
                    break;
                case 4:
                    testTED = diffLengthOfSegment;
                    break;
//                case 5:
//                    testTED
                case 9:
                    testTED = diffVolumeOfSegment;
                    break;
                case 10:
                    testTED = diffVolumeOfSegment;
                    break;
                case 15:
                    testTED = diffSurfaceOfSegment;
                    break;
                case 16:
                    testTED = diffSurfaceOfSegment;
                    break;
                case 22:
                    testTED = 0d;
                    break;
            }
            if (i == 1 || i == 2 || i == 3 || i == 4 || i == 9 || i == 10 || i == 15 || i == 16 || i == 22) {
                assertEquals((float) testTED, result, 0f);
            }
        }
    }

    @Test
    public void checkTEDSameTreesLeafInserted() throws IOException {

        double lengthOfSegment = 1;
        double volumeOfSegment = Math.PI * 1 * 1 * lengthOfSegment;
        double surfaceOfSegment = 2 * Math.PI * 1 * lengthOfSegment;

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
                    // all nodes have to be renamed and one node with label 1/4 has to be inserted
                    testTED = 1d/4 + 3* (1d/3 - 1d/4);
                    break;
                case 3:
                    testTED = lengthOfSegment;
                    break;
                case 4:
                    testTED = lengthOfSegment;
                    break;
                case 9:
                    testTED = volumeOfSegment;
                    break;
                case 10:
                    testTED = volumeOfSegment;
                    break;
                case 15:
                    testTED = surfaceOfSegment;
                    break;
                case 16:
                    testTED = surfaceOfSegment;
                    break;
                case 22:
                    testTED = 0d;
                    break;
            }
            if (i == 1 || i == 2 || i == 3 || i == 4 || i == 9 || i == 10 || i == 15 || i == 16 || i == 22) {
                assertEquals((float) testTED, result, 0f);
            }
        }
    }
}
