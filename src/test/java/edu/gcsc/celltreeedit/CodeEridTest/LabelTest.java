package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.NodeData;
import edu.gcsc.celltreeedit.TreeCostModel;
import edu.gcsc.celltreeedit.TreeCreator;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LabelTest {

    // each row represents the labels of a node
    private Double[][] correctLabels = new Double[][]{
            { 1d, 0.142857142857143, 0d, 0d, 20.6838908114423, 0d, 0d, 1d, 0d, 0d, 195945673.142389, 0d, 0d, 1d, 0d, 0d, 741241459.754624, 0d, 0d, 1d, 0d, 2.094010344666110 },
            { 1d, 0.142857142857143, 0.3, 0.3, 0.3, 0.0145040409821753, 0.0145040409821753, 0.0145040409821753, 26358327.730844, 26358327.730844, 26358327.730844, 0.134518549494533, 0.134518549494533, 0.134518549494533, 246453756.091057, 246453756.091057, 246453756.091057, 0.332487818709765, 0.332487818709765, 0.332487818709765, 0.0355597051189898, 0d },
            { 1d, 0.142857142857143, 3.67375357771614, 3.67375357771614, 18.7122120252056, 0.177614241498694, 0.177614241498694, 0.904675633602457, 114101690.973535, 114101690.973535, 114101830.000492, 0.582312888790459, 0.582312888790459, 0.582313598308327, 247393754.824245, 247393754.824245, 247393952.262515, 0.333755959773406, 0.333755959773406, 0.333756226135018, 0.153933228466884, 1.26873150309129 },
            { 1d, 0.142857142857143, 2.58069758011279, 6.25445115782893, 2.58069758011279, 0.124768478215189, 0.302382719713883, 0.124768478215189, 17.1068261791087, 114101708.080362, 17.1068261791087, 0.0000000873039241171588, 0.582312976094383, 0.0000000873039241171588, 23.6700817032565, 247393778.494327, 23.6700817032565, 0.0000000319330245114624, 0.33375599170643, 0.0000000319330245114624, 0.000000023078614875066, 0d },
            { 1d, 0.142857142857143, 2.5068815092496, 6.18063508696575, 2.5068815092496, 0.121199707158713, 0.298813948657407, 0.121199707158713, 68.1501617511492, 114101759.123697, 68.1501617511492, 0.000000347801309711117, 0.582313236591769, 0.000000347801309711117, 92.3403854670799, 247393847.16463, 92.3403854670799, 0.000000124575311124188, 0.333756084348717, 0.000000124575311124188, 0.0000000919405692359804, 0d },
            { 1d, 0.142857142857143, 9.9508793581271, 13.6246329358432, 9.9508793581271, 0.481093206729861, 0.658707448228555, 0.481093206729861, 53.7699683197907, 114101744.743504, 53.7699683197907, 0.000000274412634162723, 0.582313163203093, 0.000000274412634162723, 81.4278029248644, 247393836.252048, 81.4278029248644, 0.000000109853276355885, 0.333756069626682, 0.000000109853276355885, 0.0000000725404220341242, 0d },
            { 1d, 0.142857142857143, 1.67167878623671, 1.67167878623671, 1.67167878623671, 0.0808203254153679, 0.0808203254153679, 0.0808203254153679, 55485515.4110531, 55485515.4110531, 55485515.4110531, 0.28316785219714, 0.28316785219714, 0.28316785219714, 247393751.401052, 247393751.401052, 247393751.401052, 0.333755955155217, 0.333755955155217, 0.333755955155217, 0.0748548461245282, 0d }

    };

    private Double[][] correctLabels02 = new Double[][]{
            { 1d, 0.142857142857143, 0d, 0d, 30d, 0d, 0d, 1d, 0d, 0d, 5299.86680660598, 0d, 0d, 1d, 0d, 0d, 2611.25516150189, 0d, 0d, 1d, 0d, 0.475882249660417 },
            { 1d, 0.142857142857143, 3d, 3d, 3d, 0.1, 0.1, 0.1, 1407.43350880823, 1407.43350880823, 1407.43350880823, 0.265560165975104, 0.265560165975104, 0.265560165975104, 644.202705564797, 644.202705564797, 644.202705564797, 0.246702319659285, 0.246702319659285, 0.246702319659285, 0.538987353498893, 0d },
            { 1d, 0.142857142857143, 6d, 6d, 18d, 0.2, 0.2, 0.6, 1759.29188601028, 1759.29188601028, 2045.17681748696, 0.33195020746888, 0.33195020746888, 0.385892116182573, 832.698264780185, 832.698264780185, 1066.39115076208, 0.318888125931458, 0.318888125931458, 0.408382591821754, 0.673734191873616, 0.475882249660417 },
            { 1d, 0.142857142857143, 3d, 9d, 3d, 0.1, 0.3, 0.1, 87.9645943005142, 1847.2564803108, 87.9645943005142, 0.016597510373444, 0.348547717842324, 0.016597510373444, 67.9630403948339, 900.661305175019, 67.9630403948339, 0.026026962587503, 0.344915088518961, 0.026026962587503, 0.0336867095936808, 0d },
            { 1d, 0.142857142857143, 6d, 12d, 6d, 0.2, 0.4, 0.2, 109.955742875643, 1869.24762888593, 109.955742875643, 0.020746887966805, 0.352697095435685, 0.020746887966805, 97.7668051922222, 930.465069972407, 97.7668051922222, 0.0374405407152898, 0.356328666646748, 0.0374405407152898, 0.042108386992101, 0d },
            { 1d, 0.142857142857143, 3d, 9d, 3d, 0.1, 0.3, 0.1, 87.9645943005142, 1847.2564803108, 87.9645943005142, 0.016597510373444, 0.348547717842324, 0.016597510373444, 67.9630403948339, 900.661305175019, 67.9630403948339, 0.026026962587503, 0.344915088518961, 0.026026962587503, 0.0336867095936808, 0d },
            { 1d, 0.142857142857143, 9d, 9d, 9d, 0.3, 0.3, 0.3, 1847.2564803108, 1847.2564803108, 1847.2564803108, 0.348547717842324, 0.348547717842324, 0.348547717842324, 900.661305175019, 900.661305175019, 900.661305175019, 0.344915088518961, 0.344915088518961, 0.344915088518961, 0.707420901467296, 0d }

    };
    // labelValues were calculated using Libre Office Calc. Precision after decimal separator for big numbers is lower.
    private double[] labelDelta = new double[]{
            1d, 0.000000000000001, 0.0000000000001, 0.0000000000001, 0.0000000000001, 0.000000000000001, 0.000000000000001, 0.000000000000001, 0.000001, 0.000001, 0.000001, 0.000000000000001, 0.000000000000001, 0.000000000000001, 0.000001, 0.00001, 0.000001, 0.000000000000001, 0.000000000000001, 0.000000000000001, 0.000000000000001, 0.00000000000001
    };

    @Test
    public void checkLabels() throws IOException {
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest01.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabel(root, i, 0, labelDelta[i-1]);
        }
    }

    // currentNode and index in preorder
    private int checkLabel(Node<NodeData> currentNode, int labelId, int nodeIndex, double delta) {
        assertEquals(this.correctLabels[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), delta);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return nodeIndex;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            nodeIndex += 1;
            nodeIndex = checkLabel(childNode, labelId, nodeIndex, delta);
        }
        // index of last child
        return nodeIndex;
    }

    @Test
    public void checkLabels02() throws IOException {
        for (int i = 1; i < 23; i++) {
            System.out.println(i);
            FileInputStream f = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest02.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
            checkLabel02(root, i, 0, labelDelta[i-1]);
        }
    }

    // currentNode and index in preorder
    private int checkLabel02(Node<NodeData> currentNode, int labelId, int nodeIndex, double delta) {
        assertEquals(this.correctLabels02[nodeIndex][labelId - 1], currentNode.getNodeData().getLabel(), delta);

        // if currentNode has no children return own index
        if (currentNode.getChildren() == null) {
            return nodeIndex;
        }
        // recursively go through children and return highest index
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            nodeIndex += 1;
            nodeIndex = checkLabel02(childNode, labelId, nodeIndex, delta);
        }
        // index of last child
        return nodeIndex;
    }

    //TODO: Implement Test
    @Test
    public void checkLabelForSingleBranchNeuron() throws IOException {
        for (int i = 1; i < 23; i++) {
            FileInputStream f = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest02.swc"));
            TreeCreator t = new TreeCreator(f);
            Node<NodeData> root = t.createTree(i);
//            checkLabel(root, i, 0, labelDelta[i-1]);
        }
    }
}
