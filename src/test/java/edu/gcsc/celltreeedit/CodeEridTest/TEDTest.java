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

    @Test
    public void checkTEDZero() throws IOException {
        FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest01_T1.swc"));
        TreeCreator t1 = new TreeCreator(f1);
        Node<NodeData> root1 = t1.createTree(1);
        FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/TEDTest03_T2.swc"));
        TreeCreator t2 = new TreeCreator(f2);
        Node<NodeData> root2 = t2.createTree(1);

        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        float result = apted.computeEditDistance(root1, root2);
        assertEquals(0f, result,0f);
    }

    @Test
    public void checkTEDTreeEmpty() throws IOException {
        for (int i = 1; i < 23; i++) {
            System.out.println("Label: " + i);
            FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest01.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            Node<NodeData> root1 = t1.createTree(i);
            Node<NodeData> root2 = new Node<>(new NodeData(0d));

            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(root1, root2);
            Float expectedResult = 0f;
            for (Node<NodeData> node: t1.getNodeList()) {
                expectedResult += (float) node.getNodeData().getLabel();
            }
            System.out.println("expectedResult: " + expectedResult);
            System.out.println("actualResult: " + result);
            assertEquals(expectedResult, result,0.00001);
        }

    }
}
