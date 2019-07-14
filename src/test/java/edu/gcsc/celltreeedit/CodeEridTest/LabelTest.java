package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import eu.mihosoft.ext.apted.node.Node;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LabelTest {

    // each row represents the labels of a node
    private Double[][] correctLabels = new Double[][]{
            {1d, 0.142857142857143, 0d, 0d, 20.6838908114423, 0d, 0d, 1d, 0d, 0d, 195945673.142389, 0d, 0d, 1d, 0d, 0d, 741241459.754624, 0d, 0d, 1d, 0d, 2.094010344666110},
            {1d, 0.142857142857143, 0.3, 0.3, 0.3, 0.0145040409821753, 0.0145040409821753, 0.0145040409821753, 26358327.730844, 26358327.730844, 26358327.730844, 0.134518549494533, 0.134518549494533, 0.134518549494533, 246453756.091057, 246453756.091057, 246453756.091057, 0.332487818709765, 0.332487818709765, 0.332487818709765, 0.0355597051189898, 0d},
            {1d, 0.142857142857143, 3.67375357771614, 3.67375357771614, 18.7122120252056, 0.177614241498694, 0.177614241498694, 0.904675633602457, 114101690.973535, 114101690.973535, 114101830.000492, 0.582312888790459, 0.582312888790459, 0.582313598308327, 247393754.824245, 247393754.824245, 247393952.262515, 0.333755959773406, 0.333755959773406, 0.333756226135018, 0.153933228466884, 1.26873150309129},
            {1d, 0.142857142857143, 2.58069758011279, 6.25445115782893, 2.58069758011279, 0.124768478215189, 0.302382719713883, 0.124768478215189, 17.1068261791087, 114101708.080362, 17.1068261791087, 0.0000000873039241171588, 0.582312976094383, 0.0000000873039241171588, 23.6700817032565, 247393778.494327, 23.6700817032565, 0.0000000319330245114624, 0.33375599170643, 0.0000000319330245114624, 0.000000023078614875066, 0d},
            {1d, 0.142857142857143, 2.5068815092496, 6.18063508696575, 2.5068815092496, 0.121199707158713, 0.298813948657407, 0.121199707158713, 68.1501617511492, 114101759.123697, 68.1501617511492, 0.000000347801309711117, 0.582313236591769, 0.000000347801309711117, 92.3403854670799, 247393847.16463, 92.3403854670799, 0.000000124575311124188, 0.333756084348717, 0.000000124575311124188, 0.0000000919405692359804, 0d},
            {1d, 0.142857142857143, 9.9508793581271, 13.6246329358432, 9.9508793581271, 0.481093206729861, 0.658707448228555, 0.481093206729861, 53.7699683197907, 114101744.743504, 53.7699683197907, 0.000000274412634162723, 0.582313163203093, 0.000000274412634162723, 81.4278029248644, 247393836.252048, 81.4278029248644, 0.000000109853276355885, 0.333756069626682, 0.000000109853276355885, 0.0000000725404220341242, 0d},
            {1d, 0.142857142857143, 1.67167878623671, 1.67167878623671, 1.67167878623671, 0.0808203254153679, 0.0808203254153679, 0.0808203254153679, 55485515.4110531, 55485515.4110531, 55485515.4110531, 0.28316785219714, 0.28316785219714, 0.28316785219714, 247393751.401052, 247393751.401052, 247393751.401052, 0.333755955155217, 0.333755955155217, 0.333755955155217, 0.0748548461245282, 0d}

    };

    private Double[][] correctLabels02 = new Double[][]{
            {1d, 0.142857142857143, 0d, 0d, 30d, 0d, 0d, 1d, 0d, 0d, 5299.86680660598, 0d, 0d, 1d, 0d, 0d, 2611.25516150189, 0d, 0d, 1d, 0d, 0.475882249660417},
            {1d, 0.142857142857143, 3d, 3d, 3d, 0.1, 0.1, 0.1, 1407.43350880823, 1407.43350880823, 1407.43350880823, 0.265560165975104, 0.265560165975104, 0.265560165975104, 644.202705564797, 644.202705564797, 644.202705564797, 0.246702319659285, 0.246702319659285, 0.246702319659285, 0.538987353498893, 0d},
            {1d, 0.142857142857143, 6d, 6d, 18d, 0.2, 0.2, 0.6, 1759.29188601028, 1759.29188601028, 2045.17681748696, 0.33195020746888, 0.33195020746888, 0.385892116182573, 832.698264780185, 832.698264780185, 1066.39115076208, 0.318888125931458, 0.318888125931458, 0.408382591821754, 0.673734191873616, 0.475882249660417},
            {1d, 0.142857142857143, 3d, 9d, 3d, 0.1, 0.3, 0.1, 87.9645943005142, 1847.2564803108, 87.9645943005142, 0.016597510373444, 0.348547717842324, 0.016597510373444, 67.9630403948339, 900.661305175019, 67.9630403948339, 0.026026962587503, 0.344915088518961, 0.026026962587503, 0.0336867095936808, 0d},
            {1d, 0.142857142857143, 6d, 12d, 6d, 0.2, 0.4, 0.2, 109.955742875643, 1869.24762888593, 109.955742875643, 0.020746887966805, 0.352697095435685, 0.020746887966805, 97.7668051922222, 930.465069972407, 97.7668051922222, 0.0374405407152898, 0.356328666646748, 0.0374405407152898, 0.042108386992101, 0d},
            {1d, 0.142857142857143, 3d, 9d, 3d, 0.1, 0.3, 0.1, 87.9645943005142, 1847.2564803108, 87.9645943005142, 0.016597510373444, 0.348547717842324, 0.016597510373444, 67.9630403948339, 900.661305175019, 67.9630403948339, 0.026026962587503, 0.344915088518961, 0.026026962587503, 0.0336867095936808, 0d},
            {1d, 0.142857142857143, 9d, 9d, 9d, 0.3, 0.3, 0.3, 1847.2564803108, 1847.2564803108, 1847.2564803108, 0.348547717842324, 0.348547717842324, 0.348547717842324, 900.661305175019, 900.661305175019, 900.661305175019, 0.344915088518961, 0.344915088518961, 0.344915088518961, 0.707420901467296, 0d}
    };

    private Random rand = new Random();
    private int maxDepth = 20;
    private double[] nodeOffsets = new double[]{2, 1, 1, 1};
    private int nodeNumber;
    private BufferedWriter br;


    @Test
    public void checkLabels() throws IOException {
        for (int i = 1; i < 22; i++) {
            FileInputStream f = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest01.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabel(root, i, 0);
        }
    }

    // currentNode and index in preorder
    private int checkLabel(Node<NodeData> currentNode, int labelId, int nodeIndex) {
//        System.out.println("correct: " + this.correctLabels[nodeIndex][labelId - 1]);
//        System.out.println("actual : " + currentNode.getNodeData().getLabel());
//        assertTrue(Utils.doublesAlmostEqual(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), 1e-12, 5));
//        System.out.println(currentNode.getNodeData().getLabel() + ", " + deltaBySize(currentNode.getNodeData().getLabel()));
        assertEquals(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), deltaBySize(currentNode.getNodeData().getLabel()));

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

    private double deltaBySize(double value) {
        double epsilon = 1e-15;
        int integerPlaces = Double.toString(value).indexOf('.');
        return epsilon * Double.parseDouble("1e" + integerPlaces + 1);
    }

    @Test
    public void checkLabelsProgramatically() throws IOException {
        createTreeAndSWCFile();

        for (int i = 1; i < 22; i++) {
            FileInputStream f = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/WorkingDir/programaticSWCFile.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabelProgramatically(root, i, 0);
        }
    }

    // currentNode and index in preorder
    private int checkLabelProgramatically(Node<NodeData> currentNode, int labelId, int nodeIndex) {

//        System.out.println("correct: " + this.correctLabels[nodeIndex][labelId - 1]);
//        System.out.println("actual : " + currentNode.getNodeData().getLabel());
//        assertTrue(Utils.doublesAlmostEqual(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), 1e-12, 5));
//        System.out.println(currentNode.getNodeData().getLabel() + ", " + deltaBySize(currentNode.getNodeData().getLabel()));
        assertEquals(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), deltaBySize(currentNode.getNodeData().getLabel()));

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

    private void createTreeAndSWCFile() {
        // write root node in swcFile
        // create random Object with seed
        // randomly create number of children

        this.rand.setSeed(1L);
        TestNode root = new TestNode();
        int depth = 0;
        createTreeRec(root, depth + 1);
        // traverse tree inorder to set number of ancestors
        setNoOfAncestorsAndNoOfDecendants(root, 0);
        printTreeToSWC(root);
    }

    private void createTreeRec(TestNode parentNode, int depth) {
        if (depth >= this.maxDepth) {
            return;
        }
        for (int i = 0; i <= getRandomNumberOfChildren(); i++) {
            TestNode child = parentNode.addChild(new TestNode());
            createTreeRec(child, depth + 1);
        }
    }

    private int getRandomNumberOfChildren() {
        int number = this.rand.nextInt(11);
        if (number < 3) {
            return 0;
        } else if (number < 9) {
            return 1;
        } else {
            return 2;
        }
    }

    private int setNoOfAncestorsAndNoOfDecendants(TestNode node, int noOfAncestors) {
        node.setNoOfAncestors(noOfAncestors);

        List<TestNode> children = node.getChildren();
        int decendants = 0;
        for (TestNode child : children) {
            decendants += setNoOfAncestorsAndNoOfDecendants(child, noOfAncestors + 1);
        }
        node.setNoOfDecendents(decendants);
        return decendants + 1;
    }


    private void printTreeToSWC(TestNode node) {
        File file = new File("/media/exdisk/Sem06/BA/ProgramData/WorkingDir/programaticSWCFile.swc");
        try {
            FileWriter export = new FileWriter(file.getPath());
            this.br = new BufferedWriter(export);

            this.nodeNumber = 0;
            writeLinesPreOrder(node, nodeOffsets[0], nodeOffsets[1], nodeOffsets[2], nodeOffsets[3], -1);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("SWC-File saved to: " + file.getPath());
    }

    private void writeLinesPreOrder(TestNode node, double x, double y, double z, double r, int parentNodeNumber) throws IOException {
        this.nodeNumber++;
        this.br.write(this.nodeNumber + " " + "3 " + x + " " + y + " " + z + " " + r + " " + parentNodeNumber);
        parentNodeNumber = this.nodeNumber;
        this.br.newLine();
        int noOfChildren = node.getChildren().size();
        switch (noOfChildren) {
            case 0:
                break;
            case 1:
                writeLinesPreOrder(node.getChildren().get(0), x + nodeOffsets[0], y + nodeOffsets[1], z + nodeOffsets[2], nodeOffsets[3], parentNodeNumber);
                break;
            case 2:
                writeLinesPreOrder(node.getChildren().get(0), x + nodeOffsets[0], y + nodeOffsets[1], z + nodeOffsets[2], nodeOffsets[3], parentNodeNumber);
                writeLinesPreOrder(node.getChildren().get(1), x + nodeOffsets[1], y + nodeOffsets[0], z + nodeOffsets[2], nodeOffsets[3], parentNodeNumber);
        }
    }


}
