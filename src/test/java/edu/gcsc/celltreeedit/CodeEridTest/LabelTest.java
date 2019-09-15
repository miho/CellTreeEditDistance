package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import edu.gcsc.celltreeedit.Utils;
import eu.mihosoft.ext.apted.node.Node;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LabelTest {

    // each row represents the labels of a node
    private Double[][] correctLabels = new Double[][]{
            {1d, 0.142857142857143, 0d, 0d, 20.6838908114423, 0d, 0d, 1d, 0d, 0d, 195945673.142389, 0d, 0d, 1d, 0d, 0d, 741241459.754624, 0d, 0d, 1d, 0d, 1.879754456481810},
            {1d, 0.142857142857143, 0.3, 0.3, 0.3, 0.0145040409821753, 0.0145040409821753, 0.0145040409821753, 26358327.730844, 26358327.730844, 26358327.730844, 0.134518549494533, 0.134518549494533, 0.134518549494533, 246453756.091057, 246453756.091057, 246453756.091057, 0.332487818709765, 0.332487818709765, 0.332487818709765, 0.0355597051189898, 0d},
            {1d, 0.142857142857143, 3.67375357771614, 3.67375357771614, 18.7122120252056, 0.177614241498694, 0.177614241498694, 0.904675633602457, 114101690.973535, 114101690.973535, 114101830.000492, 0.582312888790459, 0.582312888790459, 0.582313598308327, 247393754.824245, 247393754.824245, 247393952.262515, 0.333755959773406, 0.333755959773406, 0.333756226135018, 0.153933228466884, 1.41041380528349},
            {1d, 0.142857142857143, 2.58069758011279, 6.25445115782893, 2.58069758011279, 0.124768478215189, 0.302382719713883, 0.124768478215189, 17.1068261791087, 114101708.080362, 17.1068261791087, 0.0000000873039241171588, 0.582312976094383, 0.0000000873039241171588, 23.6700817032565, 247393778.494327, 23.6700817032565, 0.0000000319330245114624, 0.33375599170643, 0.0000000319330245114624, 0.000000023078614875066, 0d},
            {1d, 0.142857142857143, 2.5068815092496, 6.18063508696575, 2.5068815092496, 0.121199707158713, 0.298813948657407, 0.121199707158713, 68.1501617511492, 114101759.123697, 68.1501617511492, 0.000000347801309711117, 0.582313236591769, 0.000000347801309711117, 92.3403854670799, 247393847.16463, 92.3403854670799, 0.000000124575311124188, 0.333756084348717, 0.000000124575311124188, 0.0000000919405692359804, 0d},
            {1d, 0.142857142857143, 9.9508793581271, 13.6246329358432, 9.9508793581271, 0.481093206729861, 0.658707448228555, 0.481093206729861, 53.7699683197907, 114101744.743504, 53.7699683197907, 0.000000274412634162723, 0.582313163203093, 0.000000274412634162723, 81.4278029248644, 247393836.252048, 81.4278029248644, 0.000000109853276355885, 0.333756069626682, 0.000000109853276355885, 0.0000000725404220341242, 0d},
            {1d, 0.142857142857143, 1.67167878623671, 1.67167878623671, 1.67167878623671, 0.0808203254153679, 0.0808203254153679, 0.0808203254153679, 55485515.4110531, 55485515.4110531, 55485515.4110531, 0.28316785219714, 0.28316785219714, 0.28316785219714, 247393751.401052, 247393751.401052, 247393751.401052, 0.333755955155217, 0.333755955155217, 0.333755955155217, 0.0748548461245282, 0d}

    };

    private Random rand = new Random();
    private int maxDepth;
    private double[] nodeOffsets;
    private int nodeNumber;
    private BufferedWriter br;

    private int noOfTestNodes;
    private double lengthOfT;
    private double volumeOfT;
    private double surfaceOfT;

    private MaxUlps[] maxUlpsCheckLabels = new MaxUlps[22];
    private MaxUlps[] maxUlpsCheckLabelsProgramatically = new MaxUlps[22];

    @Test
    public void checkLabels() throws IOException {
        for (int i = 1; i < 23; i++) {
            maxUlpsCheckLabels[i-1] = new MaxUlps();
        }
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(new File(BaseDirectory.baseDirectory.getPath() + "/Test/labelTest01.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabel(root, i, 0);
        }
        System.out.println("checkLabels");
        System.out.println("Label;noOfUlps;correctValue;wrongValue");
        for (int i = 1; i < 23; i++) {
            MaxUlps maxUlps = maxUlpsCheckLabels[i-1];
            System.out.println(i + ";" + maxUlps.noOfUlps + ";" + maxUlps.correctValue + ";" + maxUlps.wrongValue);
        }
    }

    // currentNode and index in preorder
    private int checkLabel(Node<NodeData> currentNode, int labelId, int nodeIndex) {
        assertTrue(Utils.doublesAlmostEqual(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), 0d, 33));
        maxUlpsCheckLabels[labelId - 1].updateMaxUlps(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel());
        if (labelId == 10) {
            System.out.println(this.correctLabels[nodeIndex][labelId - 1] + " ||| " + currentNode.getNodeData().getLabel());
        }

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return nodeIndex;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode : currentNode.getChildren()) {
            nodeIndex += 1;
            nodeIndex = checkLabel(childNode, labelId, nodeIndex);
        }
        // index of last child
        return nodeIndex;
    }

    @Test
    public void checkLabelsProgramatically() throws IOException {

        for (int i = 1; i < 23; i++) {
            maxUlpsCheckLabelsProgramatically[i-1] = new MaxUlps();
        }

        TestNode testRoot = createTreeAndSWCFile(1L, 15, new double[]{1.329d, -2.7812d, 0.43d, 3.76d});
        File savedFile = new File(BaseDirectory.baseDirectory.getPath() + "/WorkingDir/programaticSWCFile.swc");
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(savedFile);
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabelProgramatically(testRoot, root, i);
        }

        testRoot = createTreeAndSWCFile(3001L, 15, new double[]{1d, 1d, 1d, 1d});
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(savedFile);
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabelProgramatically(testRoot, root, i);
        }

        testRoot = createTreeAndSWCFile(73L, 15, new double[]{-2.43d, -8.92893d, 29.3344d, 28d});
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(savedFile);
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabelProgramatically(testRoot, root, i);
        }

        testRoot = createTreeAndSWCFile(54L, 15, new double[]{0.0000001, 0.0000001, 0.0000001, 0.0000001});
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(savedFile);
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabelProgramatically(testRoot, root, i);
        }
        System.out.println("checkLabels");
        System.out.println("Label;noOfUlps;correctValue;wrongValue");
        for (int i = 1; i < 23; i++) {
            MaxUlps maxUlps = maxUlpsCheckLabelsProgramatically[i-1];
            System.out.println(i + ";" + maxUlps.noOfUlps + ";" + maxUlps.correctValue + ";" + maxUlps.wrongValue);
        }
        savedFile.delete();
    }

    // currentNode and index in preorder
    private void checkLabelProgramatically(TestNode testNode, Node<NodeData> node, int labelId) {
        double testLabel = 0;
        switch (labelId) {
            case 1:
                testLabel = 1d;
                break;
            case 2:
                testLabel = 1d / noOfTestNodes;
                break;
            case 3:
                testLabel = calculateLengthOfSegment() * testNode.getNoOfIncludedSegments();
                break;
            case 4:
                testLabel = calculateLengthOfSegment() * testNode.getNoOfAncestors();
                break;
            case 5:
                testLabel = calculateLengthOfSegment() * testNode.getNoOfDecendents();
                break;
            case 6:
                testLabel = (this.lengthOfT == 0d) ? 0d : calculateLengthOfSegment() * testNode.getNoOfIncludedSegments() / this.lengthOfT;
                break;
            case 7:
                testLabel = (this.lengthOfT == 0d) ? 0d : calculateLengthOfSegment() * testNode.getNoOfAncestors() / this.lengthOfT;
                break;
            case 8:
                testLabel = (this.lengthOfT == 0d) ? 0d : calculateLengthOfSegment() * testNode.getNoOfDecendents() / this.lengthOfT;
                break;
            case 9:
                testLabel = calculateVolumeOfSegment() * testNode.getNoOfIncludedSegments();
                break;
            case 10:
                testLabel = calculateVolumeOfSegment() * testNode.getNoOfAncestors();
                break;
            case 11:
                testLabel = calculateVolumeOfSegment() * testNode.getNoOfDecendents();
                break;
            case 12:
                testLabel = (this.volumeOfT == 0d) ? 0d : calculateVolumeOfSegment() * testNode.getNoOfIncludedSegments() / this.volumeOfT;
                break;
            case 13:
                testLabel = (this.volumeOfT == 0d) ? 0d : calculateVolumeOfSegment() * testNode.getNoOfAncestors() / this.volumeOfT;
                break;
            case 14:
                testLabel = (this.volumeOfT == 0d) ? 0d : calculateVolumeOfSegment() * testNode.getNoOfDecendents() / this.volumeOfT;
                break;
            case 15:
                testLabel = calculateSurfaceOfSegment() * testNode.getNoOfIncludedSegments();
                break;
            case 16:
                testLabel = calculateSurfaceOfSegment() * testNode.getNoOfAncestors();
                break;
            case 17:
                testLabel = calculateSurfaceOfSegment() * testNode.getNoOfDecendents();
                break;
            case 18:
                testLabel = (this.surfaceOfT == 0d) ? 0d : calculateSurfaceOfSegment() * testNode.getNoOfIncludedSegments() / this.surfaceOfT;
                break;
            case 19:
                testLabel = (this.surfaceOfT == 0d) ? 0d : calculateSurfaceOfSegment() * testNode.getNoOfAncestors() / this.surfaceOfT;
                break;
            case 20:
                testLabel = (this.surfaceOfT == 0d) ? 0d : calculateSurfaceOfSegment() * testNode.getNoOfDecendents() / this.surfaceOfT;
                break;
            case 21:
                testLabel = (this.surfaceOfT == 0d) ? 0d : calculateVolumeOfSegment() * testNode.getNoOfIncludedSegments() / this.surfaceOfT;
                break;
            case 22:
                if (testNode.getChildren().size() == 0) {
                    testLabel = 0d;
                } else {
                    double[] v1 = new double[]{nodeOffsets[0], nodeOffsets[1], nodeOffsets[2]};
                    double[] v2 = new double[]{nodeOffsets[2], nodeOffsets[0], nodeOffsets[1]};
                    double cosAngle = MathArrays.cosAngle(v1, v2);
                    if (Utils.doublesAlmostEqual(Math.abs(cosAngle), 1d, 0d, 1)) {
                        testLabel = (cosAngle > 0) ? 0d : Math.PI;
                    } else {
                        testLabel = Math.acos(cosAngle);
                    }
                }
                break;
            default:
                System.out.println("Something went wrong. LabelId not correct: " + labelId);
                assertTrue(false);
                break;
        }

        Set<Integer> deltaClass01 = new HashSet<>(Arrays.asList(6, 7, 12, 13, 18, 19));
        if (deltaClass01.contains(labelId)) {
            assertTrue(Utils.doublesAlmostEqual(testLabel, node.getNodeData().getLabel(), 0d, 355));
        } else {
            assertTrue(Utils.doublesAlmostEqual(testLabel, node.getNodeData().getLabel(), 0d, 37));
        }
        maxUlpsCheckLabelsProgramatically[labelId - 1].updateMaxUlps(testLabel, node.getNodeData().getLabel());

        // nunber of Children must be the same, prevent topology is false
        assertEquals(testNode.getChildren().size(), node.getChildren().size());
        int noOfChildren = node.getChildren().size();
        for (int i = 0; i < noOfChildren; i++) {
            checkLabelProgramatically(testNode.getChildren().get(i), node.getChildren().get(i), labelId);
        }
    }

    private class MaxUlps {
        private int noOfUlps = -1;
        private double correctValue = 0d;
        private double wrongValue = 0d;

        MaxUlps() { }

        private void updateMaxUlps(double correctValue, double wrongValue) {
            double bigger = Math.max(correctValue, wrongValue);
            double dif = Math.abs(correctValue - wrongValue);
            int noOfUlps = (int) Math.round(Math.ceil(dif / Math.ulp(bigger)));
            if (noOfUlps > this.noOfUlps) {
                this.noOfUlps = noOfUlps;
                this.correctValue = correctValue;
                this.wrongValue = wrongValue;
            }
        }
    }


    // ---------------------- Creation and Calculation for checkLabelsProgramatically ----------------------------------
    private int calculateNoOfTestNodes(TestNode node) {
        int noOfTestNodes = 1;
        for (TestNode child : node.getChildren()) {
            noOfTestNodes += calculateNoOfTestNodes(child);
        }
        return noOfTestNodes;
    }

    private double calculateLengthOfSegment() {
        double x = nodeOffsets[0];
        double y = nodeOffsets[1];
        double z = nodeOffsets[2];
        return Math.sqrt(x * x + y * y + z * z);
    }

    private double calculateVolumeOfSegment() {
        return calculateLengthOfSegment() * Math.PI * nodeOffsets[3] * nodeOffsets[3];
    }

    private double calculateSurfaceOfSegment() {
        return calculateLengthOfSegment() * 2 * Math.PI * nodeOffsets[3];
    }

    private TestNode createTreeAndSWCFile(Long seed, int maxDepth, double[] nodeOffsets) throws IOException {
        // write root node in swcFile
        // create random Object with seed
        // randomly create number of children

        File file = new File(BaseDirectory.baseDirectory.getPath() + "/WorkingDir/programaticSWCFile.swc");

        // stuff for writing to swc-File
        FileWriter export = new FileWriter(file.getPath());
        this.br = new BufferedWriter(export);
        this.nodeNumber = 1;
        this.nodeOffsets = nodeOffsets;
        double x = this.nodeOffsets[0];
        double y = this.nodeOffsets[1];
        double z = this.nodeOffsets[2];
        double r = this.nodeOffsets[3];
        int parentNodeNumber = 1;

        // stuff for tree-creation
        this.rand.setSeed(seed);

        // print swcLines for root and its direct children
        printSwcLine(x, y, z, r, -1);
        int noOfSinlgeNodes = getRandomNumberOfSingleNodes();
        for (int i = 0; i < noOfSinlgeNodes; i++) {
            this.nodeNumber += 1;
            x += this.nodeOffsets[0];
            y += this.nodeOffsets[1];
            z += this.nodeOffsets[2];
            printSwcLine(x, y, z, r, parentNodeNumber);
            parentNodeNumber = this.nodeNumber;
        }

        TestNode root = new TestNode();
        // only noOfSingleNodes because absolute root node does have an own segment
        root.setNoOfIncludedSegments(noOfSinlgeNodes);
        int depth = 0;
        this.maxDepth = maxDepth;

        // createTreeRec called with values of parentNode
        createTreeRec(root, depth, x, y, z, r, parentNodeNumber, false);
        createTreeRec(root, depth, x, y, z, r, parentNodeNumber, true);

        br.close();

        // traverse tree inorder to set number of ancestors
        setNoOfAncestorsAndNoOfDecendants(root, 0);

        this.noOfTestNodes = calculateNoOfTestNodes(root);
        this.lengthOfT = root.getNoOfDecendents() * calculateLengthOfSegment();
        this.volumeOfT = root.getNoOfDecendents() * calculateVolumeOfSegment();
        this.surfaceOfT = root.getNoOfDecendents() * calculateSurfaceOfSegment();
        return root;
    }

    private void printSwcLine(double x, double y, double z, double r, int parentNodeNumber) throws IOException {
        this.br.write(this.nodeNumber + " " + "3 " + x + " " + y + " " + z + " " + r + " " + parentNodeNumber);
        this.br.newLine();
    }

    private void createTreeRec(TestNode parentNode, int depth, double x, double y, double z, double r, int parentNodeNumber, boolean issecondChild) throws IOException {
        depth += 1;
        if (depth > this.maxDepth) {
            return;
        }

        int noOfSingleNodes = getRandomNumberOfSingleNodes();
        for (int i = 0; i < noOfSingleNodes; i++) {
            this.nodeNumber += 1;
            if (issecondChild) {
                x += this.nodeOffsets[2];
                y += this.nodeOffsets[0];
                z += this.nodeOffsets[1];
            } else {
                x += this.nodeOffsets[0];
                y += this.nodeOffsets[1];
                z += this.nodeOffsets[2];
            }
            printSwcLine(x, y, z, r, parentNodeNumber);
            parentNodeNumber = this.nodeNumber;
        }

        this.nodeNumber += 1;
        if (issecondChild) {
            x += this.nodeOffsets[2];
            y += this.nodeOffsets[0];
            z += this.nodeOffsets[1];
        } else {
            x += this.nodeOffsets[0];
            y += this.nodeOffsets[1];
            z += this.nodeOffsets[2];
        }
        printSwcLine(x, y, z, r, parentNodeNumber);
        parentNodeNumber = this.nodeNumber;

        TestNode node = parentNode.addChild(new TestNode());
        node.setNoOfIncludedSegments(noOfSingleNodes + 1);

        boolean isBranch = getEndOrBranch();
        if (isBranch) {
            // createTreeRec called with values of parentNode
            createTreeRec(node, depth, x, y, z, r, parentNodeNumber, false);
            createTreeRec(node, depth, x, y, z, r, parentNodeNumber, true);
        }
    }

    private int getRandomNumberOfSingleNodes() {
        int noOfSingleNodes = 0;
        while (this.rand.nextInt(3) < 2) {
            noOfSingleNodes += 1;
        }
        return noOfSingleNodes;
    }

    private boolean getEndOrBranch() {
        int number = this.rand.nextInt(8);
        return (number > 1);
    }

    // noOfAncestors is sum of all segments before
    private int setNoOfAncestorsAndNoOfDecendants(TestNode node, int noOfAncestors) {
        noOfAncestors += node.getNoOfIncludedSegments();
        node.setNoOfAncestors(noOfAncestors);

        List<TestNode> children = node.getChildren();
        int decendants = node.getNoOfIncludedSegments();
        for (TestNode child : children) {
            decendants += setNoOfAncestorsAndNoOfDecendants(child, noOfAncestors);
        }
        node.setNoOfDecendents(decendants);
        // decendants are number of all segments from children AND own segments
        return decendants;
    }

}
