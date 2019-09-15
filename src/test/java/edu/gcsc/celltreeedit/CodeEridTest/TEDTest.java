package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCostModel;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TEDTest {

    private float deltaBySize(float value, float epsilon) {
        String scientificNotation = String.format("%1.1e", value);
        int integerPlaces = Integer.parseInt(scientificNotation.substring(4));
        return epsilon * Float.parseFloat("1e" + (integerPlaces));
    }

    @Test
    public void checkTEDSameTrees() throws IOException {
        for (int i = 1; i < 23; i++) {
            FileInputStream f1 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01.CNG.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);
            FileInputStream f2 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01.CNG.swc"));
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
            FileInputStream f1 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01.CNG.swc"));
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
            assertEquals(expectedResultFloat, result, deltaBySize(expectedResultFloat, 1e-6f));


            // change order of trees
            apted = new APTED<>(new TreeCostModel());
            result = apted.computeEditDistance(root2, root1);
            expectedResult = 0d;
            for (Node<NodeData> node: t1.getNodeList()) {
                expectedResult += node.getNodeData().getLabel();
            }
            expectedResultFloat = (float) expectedResult;
            assertEquals(expectedResultFloat, result, deltaBySize(expectedResultFloat, 1e-6f));
        }
    }

    @Test
    public void checkTEDTreesCreatedManually() {
        double label1 = 0.452;
        double label2 = 1.1111;
        Node<NodeData> rootDefault = createTree(label1, label2, 0);
        Node<NodeData> rootLeafRenamed = createTree(label1, label2, 1);
        Node<NodeData> rootLeafMissing = createTree(label1, label2, 2);
        Node<NodeData> rootLeafInserted = createTree(label1, label2, 3);

        Node<NodeData> rootInnerRenamed = createTree(label1, label2, 4);
        Node<NodeData> rootInnerMissing = createTree(label1, label2, 5);
        Node<NodeData> rootInnerInserted = createTree(label1, label2, 6);


        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        float result = apted.computeEditDistance(rootDefault, rootLeafRenamed);
        float testResult = (float)(Math.abs(label2 - label1));
        assertEquals(testResult, result, deltaBySize(testResult, 1e-6f));
        System.out.println("correctResult: " + testResult);
        System.out.println("actuallResult: " + result);

        apted = new APTED<>(new TreeCostModel());
        result = apted.computeEditDistance(rootDefault, rootLeafMissing);
        testResult = (float)(label1);
        assertEquals(testResult, result, deltaBySize(testResult, 1e-6f));
        System.out.println("correctResult: " + testResult);
        System.out.println("actuallResult: " + result);

        apted = new APTED<>(new TreeCostModel());
        result = apted.computeEditDistance(rootDefault, rootLeafInserted);
        testResult = (float)(label1);
        assertEquals(testResult, result, deltaBySize(testResult, 1e-6f));
        System.out.println("correctResult: " + testResult);
        System.out.println("actuallResult: " + result);

        apted = new APTED<>(new TreeCostModel());
        result = apted.computeEditDistance(rootDefault, rootInnerRenamed);
        testResult = (float)(Math.abs(label2 - label1));
        assertEquals(testResult, result, deltaBySize(testResult, 1e-6f));
        System.out.println("correctResult: " + testResult);
        System.out.println("actuallResult: " + result);

        apted = new APTED<>(new TreeCostModel());
        result = apted.computeEditDistance(rootDefault, rootInnerMissing);
        testResult = (float)(label1);
        assertEquals(testResult, result, deltaBySize(testResult, 1e-6f));
        System.out.println("correctResult: " + testResult);
        System.out.println("actuallResult: " + result);

        apted = new APTED<>(new TreeCostModel());
        result = apted.computeEditDistance(rootDefault, rootInnerInserted);
        testResult = (float)(label1);
        assertEquals(testResult, result, deltaBySize(testResult, 1e-6f));
        System.out.println("correctResult: " + testResult);
        System.out.println("actuallResult: " + result);
    }

    private Node<NodeData> createTree(double label1, double label2, int option) {

        // create first tree manually
        NodeData nodeData = new NodeData(1);
        Node<NodeData> root = new Node<>(nodeData);
        // set labels manually
        root.getNodeData().setLabel(label1);
        
        nodeData = new NodeData(1);
        Node<NodeData> childL = new Node<>(nodeData);
        root.addChild(childL);
        childL.getNodeData().setLabel(label1);
        nodeData = new NodeData(1);
        Node<NodeData> childLL = new Node<>(nodeData);
        childL.addChild(childLL);
        childLL.getNodeData().setLabel(label1);
        nodeData = new NodeData(1);
        Node<NodeData> childLR = new Node<>(nodeData);
        childL.addChild(childLR);
        childLR.getNodeData().setLabel(label1);


        nodeData = new NodeData(1);
        Node<NodeData> childM = new Node<>(nodeData);
        nodeData = new NodeData(1);
        Node<NodeData> childMM = new Node<>(nodeData);

        switch (option) {
            case 4:
                // create both Nodes M and MM but MM with label2
                root.addChild(childM);
                childM.getNodeData().setLabel(label1);

                childM.addChild(childMM);
                childMM.getNodeData().setLabel(label2);
                break;
            case 5:
                // do not create childM. put childMM directly under root
                root.addChild(childMM);
                childMM.getNodeData().setLabel(label1);
                break;
            case 6:
                // create both Nodes M and MM but MM with label2
                root.addChild(childM);
                childM.getNodeData().setLabel(label1);

                nodeData = new NodeData(1);
                Node<NodeData> childIns = new Node<>(nodeData);
                childM.addChild(childIns);
                childIns.getNodeData().setLabel(label1);

                childIns.addChild(childMM);
                childMM.getNodeData().setLabel(label1);
                break;
            default:
                // create both Nodes M and MM
                root.addChild(childM);
                childM.getNodeData().setLabel(label1);

                childM.addChild(childMM);
                childMM.getNodeData().setLabel(label1);
                break;

        }

        nodeData = new NodeData(1);
        Node<NodeData> childMML = new Node<>(nodeData);
        childMM.addChild(childMML);
        childMML.getNodeData().setLabel(label1);
        nodeData = new NodeData(1);
        Node<NodeData> childMMM = new Node<>(nodeData);
        childMM.addChild(childMMM);
        childMMM.getNodeData().setLabel(label1);

        nodeData = new NodeData(1);
        Node<NodeData> childMMR = new Node<>(nodeData);
        switch (option) {
            case 1:
                childMM.addChild(childMMR);
                childMMR.getNodeData().setLabel(label2);
                break;
            case 2:
                break;
            case 3:
                childMM.addChild(childMMR);
                childMMR.getNodeData().setLabel(label1);
                nodeData = new NodeData(1);
                Node<NodeData> childIns = new Node<>(nodeData);
                childMM.addChild(childIns);
                childIns.getNodeData().setLabel(label1);
                break;
            default:
                childMM.addChild(childMMR);
                childMMR.getNodeData().setLabel(label1);
                break;
        }
        
        nodeData = new NodeData(1);
        Node<NodeData> childR = new Node<>(nodeData);
        root.addChild(childR);
        childR.getNodeData().setLabel(label1);
        
        return root;
    }


    // --------------------------------------- NOT COMPLETELY IMPLEMENTED ----------------------------------------------
    @Test
    public void checkTEDSameTrees1lengthChanged() throws IOException {

        int noOfNodes = 7;

        double[] v1 = new double[]{-2.1, -1, 0.1};
        double r1 = 1.6;

        double[] v2 = new double[]{-4.2, -1, -1.4};
        double r2 = 1.3;

        double[] v2New = new double[]{-4.2, -2, -1.4};
        double r2New = 1.3;

        double oldLengthOfSegment = Math.sqrt((v1[0]-v2[0]) * (v1[0]-v2[0]) + (v1[1]-v2[1]) * (v1[1]-v2[1]) + (v1[2]-v2[2]) * (v1[2]-v2[2]));
        double newLengthOfSegment = Math.sqrt((v1[0]-v2New[0]) * (v1[0]-v2New[0]) + (v1[1]-v2New[1]) * (v1[1]-v2New[1]) + (v1[2]-v2New[2]) * (v1[2]-v2New[2]));
        double diffLengthOfSegment = Math.abs(newLengthOfSegment - oldLengthOfSegment);

        double oldVolumeOfSegment = oldLengthOfSegment*Math.PI/3*(r1*r1+r1*r2+r2*r2);
        double newVolumeOfSegment = newLengthOfSegment*Math.PI/3*(r1*r1+r1*r2New+r2New*r2New);
        double diffVolumeOfSegment = Math.abs(newVolumeOfSegment - oldVolumeOfSegment);

        double oldSurfaceOfSegment = (r1+r2)*Math.PI*Math.sqrt((r1-r2)*(r1-r2)+oldLengthOfSegment*oldLengthOfSegment);
        double newSurfaceOfSegment = (r1+r2New)*Math.PI*Math.sqrt((r1-r2New)*(r1-r2New)+newLengthOfSegment*newLengthOfSegment);
        double diffSurfaceOfSegment = Math.abs(newSurfaceOfSegment - oldSurfaceOfSegment);

        double[] node4 = new double[]{-2.1, -1.0, 0.1};
        double[] node5Old = new double[]{-4.2, -1.0, -1.4};
        double[] node5New = new double[]{-4.2, -2.0, -1.4};
        double[] node7 = new double[]{-2.8, -2.9, 1.0};
        double[] node8 = new double[]{-10.2, 4.0, 3.0};
        double[] v5Old = MathArrays.ebeSubtract(node5Old, node4);
        double[] v5New = MathArrays.ebeSubtract(node5New, node4);
        double[] v7 = MathArrays.ebeSubtract(node7, node4);
        double[] v8 = MathArrays.ebeSubtract(node8, node4);
        double oldAngleOfSegment = (Math.cos(MathArrays.cosAngle(v5Old, v7)) + Math.cos(MathArrays.cosAngle(v5Old, v8)) + Math.cos(MathArrays.cosAngle(v7, v8))) / 3;
        double newAngleOfSegment = (Math.cos(MathArrays.cosAngle(v5New, v7)) + Math.cos(MathArrays.cosAngle(v5New, v8)) + Math.cos(MathArrays.cosAngle(v7, v8))) / 3;
        double diffAngleOfSegment = Math.abs(newAngleOfSegment - oldAngleOfSegment);

        float result;
        double testTED;

        List<Integer> implementedLabelsTest = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 9, 10, 15, 16, 22));
        for (int i : implementedLabelsTest) {
            FileInputStream f1 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01_simpleTree.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);

            FileInputStream f2 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01_simpleTree_1lengthChanged.swc"));
            TreeCreator t2 = new TreeCreator(f2);
            Node<NodeData> root2 = t2.createTree(i);

            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            result = apted.computeEditDistance(root1, root2);

            testTED = 0;
            switch (i) {
                case 1:
                case 2:
                    // must be 0 because same topology
                    testTED = 0d;
                    break;
                case 3:
                    // must be diffLengthOfSegment because only label of this node has to be renamed by the difference
                    testTED = diffLengthOfSegment;
                    break;
                case 4:
                    // must be diffLengthOfSegment because only this length to soma has changed
                    testTED = diffLengthOfSegment;
                    break;
                case 5:
                    // must be number of nodes (including node itself) that are ancestors of node with changed length * diffLengthOfSegment. because ancestor labels changed as well
                    testTED = 3 * diffLengthOfSegment;
                    break;
                case 9:
                    // must be diffVolumeOfSegment because only this volume to soma has changed
                    testTED = diffVolumeOfSegment;
                    break;
                    // case 10 would not work because difference is to small and would be lost because of floatingpoint precision
                case 15:
                    // must be diffSurfaceOfSegment because only this surface to soma has changed
                    testTED = diffSurfaceOfSegment;
                    break;
                case 22:
                    // must be diffAngleOfSegment because only angle of node 4 has changed
                    testTED = diffAngleOfSegment;
                    break;
                default:
                    break;
            }
            float testTEDFloat = (float) testTED;
            System.out.println(i);
            System.out.println("correctValue: " + testTED);
            System.out.println("actualValue: " + result);
            System.out.println("delta: " + deltaBySize(testTEDFloat, 1e-4f));
            assertEquals(testTEDFloat, result, deltaBySize(testTEDFloat, 1e-4f));
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
            FileInputStream f1 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01_superSimpleTree.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);

            FileInputStream f2 = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/TEDTest01_superSimpleTree_1NodeInserted.swc"));
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
                case 4:
                    // cost for node insert
                    testTED = lengthOfSegment;
                    break;
                case 9:
                case 10:
                    // cost for node insert
                    testTED = volumeOfSegment;
                    break;
                case 15:
                case 16:
                    // cost for node insert
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
