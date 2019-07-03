package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.NodeData;
import edu.gcsc.celltreeedit.TreeCreator;
import eu.mihosoft.ext.apted.node.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LabelTest {

    // each row represents the labels of a node
    private Double[][] correctLabels = new Double[][]{
            { 1d, 0.142857142857143, 0d, 0d, 20.6838908114423, 0d, 0d, 1d,  0d, 0d, 195945673.142389, 0d, 0d, 1d,  0d, 0d, 741241459.754624, 0d, 0d, 1d,  0d, 0.0965252012732848 },
            { 1d, 0.142857142857143, 0.3, 0.3, 0.3, 0.0145040409821753, 0.0145040409821753, 0.0145040409821753, 26358327.730844, 26358327.730844, 26358327.730844, 0.134518549494533, 0.134518549494533, 0.134518549494533, 246453756.091057, 246453756.091057, 246453756.091057, 0.332487818709765, 0.332487818709765, 0.332487818709765, 0.106950399737894, 0d },
            { 1d, 0.142857142857143, 3.67375357771614, 3.67375357771614, 18.7122120252056, 0.177614241498694, 0.177614241498694, 0.904675633602457, 114101690.973535, 114101690.973535, 114101830.000492, 0.582312888790459, 0.582312888790459, 0.582313598308327, 247393754.824245, 247393754.824245, 247393952.262515, 0.333755959773406, 0.333755959773406, 0.333756226135018, 1.32150937914998, 0.508787150285017 },
            { 1d, 0.142857142857143, 2.58069758011279, 6.25445115782893, 2.58069758011279, 0.124768478215189, 0.302382719713883, 0.124768478215189, 17.1068261791087, 114101708.080362, 17.1068261791087, 0.0000000873039241171588, 0.582312976094383, 0.0000000873039241171588, 23.6700817032565, 247393778.494327, 23.6700817032565, 0.0000000319330245114624, 0.33375599170643, 0.0000000319330245114624, 0.72271935490426, 0d },
            { 1d, 0.142857142857143, 2.5068815092496, 6.18063508696575, 2.5068815092496, 0.121199707158713, 0.298813948657407, 0.121199707158713, 68.1501617511492, 114101759.123697, 68.1501617511492, 0.000000347801309711117, 0.582313236591769, 0.000000347801309711117, 92.3403854670799, 247393847.16463, 92.3403854670799, 0.000000124575311124188, 0.333756084348717, 0.000000124575311124188, 1.06913253949538, 0d },
            { 1d, 0.142857142857143, 9.9508793581271, 13.6246329358432, 9.9508793581271, 0.481093206729861, 0.658707448228555, 0.481093206729861, 53.7699683197907, 114101744.743504, 53.7699683197907, 0.000000274412634162723, 0.582313163203093, 0.000000274412634162723, 81.4278029248644, 247393836.252048, 81.4278029248644, 0.000000109853276355885, 0.333756069626682, 0.000000109853276355885, 0.660339176403982, 0d },
            { 1d, 0.142857142857143, 1.67167878623671, 1.67167878623671, 1.67167878623671, 0.0808203254153679, 0.0808203254153679, 0.0808203254153679, 55485515.4110531, 55485515.4110531, 55485515.4110531, 0.28316785219714, 0.28316785219714, 0.28316785219714, 247393751.401052, 247393751.401052, 247393751.401052, 0.333755955155217, 0.333755955155217, 0.333755955155217, 1.05543554647027, 0d }

    };

    @Test
    public void checkLabels() throws IOException {

        for (int i = 18; i < 21; i++) {
            FileInputStream f = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabel(root, i, 0);
        }


//        FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest.swc"));
//        TreeCreator t2 = new TreeCreator(f2);
//        node1 = t2.createTree(2);
//        checkLabel_2(node1, 0);
//
//        FileInputStream f3 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest.swc"));
//        TreeCreator t3 = new TreeCreator(f3);
//        node1 = t3.createTree(3);
//        checkLabel_3(node1, 0);
//
//        FileInputStream f4 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest.swc"));
//        TreeCreator t4 = new TreeCreator(f4);
//        node1 = t4.createTree(4);
//        checkLabel_4(node1, 0);

    }

    // currentNode and index in preorder
    private int checkLabel(Node<NodeData> currentNode, int labelId, int nodeIndex) {
        assertEquals(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), 0.0000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return nodeIndex;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            nodeIndex += 1;
            nodeIndex = checkLabel(childNode, labelId,nodeIndex);
        }
        // index of last child
        return nodeIndex;
    }

    // currentNode and index in preorder
    private int checkLabel_1(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][0], currentNode.getNodeData().getLabel(), 0.0d);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_1(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_2(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][1], currentNode.getNodeData().getLabel(), 0.00000000000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_2(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_3(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][2], currentNode.getNodeData().getLabel(), 0.000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_3(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_4(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][3], currentNode.getNodeData().getLabel(), 0.000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_4(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_5(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][4], currentNode.getNodeData().getLabel(), 0.000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_5(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_6(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][5], currentNode.getNodeData().getLabel(), 0.000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_6(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_7(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][6], currentNode.getNodeData().getLabel(), 0.000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_7(childNode, x);
        }
        // index of last child
        return x;
    }

    private int checkLabel_8(Node<NodeData> currentNode, int x) {
        assertEquals(this.correctLabels[x][7], currentNode.getNodeData().getLabel(), 0.000001);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return x;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            x += 1;
            x = checkLabel_8(childNode, x);
        }
        // index of last child
        return x;
    }
}
