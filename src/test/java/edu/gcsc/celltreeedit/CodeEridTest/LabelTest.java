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

    @Test
    public void checkLabels() throws IOException {
        FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/label1Test.swc"));
        TreeCreator t1 = new TreeCreator(f1);
        Node<NodeData> node1 = t1.createTree(1);
        checkLabel_1(node1);

        FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/label1Test.swc"));
        TreeCreator t2 = new TreeCreator(f2);
        node1 = t2.createTree(2);
        checkLabel_2(node1);

    }

    private void checkLabel_1(Node<NodeData> currentNode) {
        assertEquals(currentNode.getNodeData().getLabel(), 1.0, 0.0d);
        if (currentNode.getChildren() == null) {
            return;
        }
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            checkLabel_1(childNode);
        }
    }

    private void checkLabel_2(Node<NodeData> currentNode) {
        double expected = 0.142857142857143;
        assertEquals(expected, currentNode.getNodeData().getLabel(), 0.000000000000001);
        if (currentNode.getChildren() == null) {
            return;
        }
        for (Node<NodeData> childNode: currentNode.getChildren()) {
            checkLabel_2(childNode);
        }
    }
}
