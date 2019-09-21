package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import eu.mihosoft.ext.apted.node.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


/**
 * Created by Erid on 13.04.2018.
 */
public class TreeCreatorTest {

    private static File treeCreatorTestDirectory = new File(BaseDirectory.getTestDirectory().getPath() + "/TreeCreatorTest/");

    @Test
    public void helpArrays(){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(treeCreatorTestDirectory.getPath() + "/1k.swc"));
            TreeCreator t1= new TreeCreator(fileInputStream);
            int[] firstChild = {-1, 2, -1, -1, 5, 6, 9, -1, -1, -1, -1, -1};
            int[] nextSibling = {-1, -1, 3, 4, -1, 7, -1, 8, -1, 10, 11, -1};
            assertArrayEquals(firstChild,t1.getFirstChild());
            assertArrayEquals(nextSibling,t1.getNextSibling());

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void helpArrays2(){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(treeCreatorTestDirectory.getPath() + "/1k_2.swc"));
            TreeCreator t1= new TreeCreator(fileInputStream);

            int[] firstChild={-1,2,-1,-1,5,6,9,-1,-1,-1,-1,-1,-1};
            int[] nextSibling={-1,-1,3,4,-1,7,-1,8,-1,10,11,12,-1};
            assertArrayEquals(firstChild,t1.getFirstChild());
            assertArrayEquals(nextSibling,t1.getNextSibling());

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void treeStructure01() throws IOException {
        FileInputStream fileInputStream=new FileInputStream(new File(treeCreatorTestDirectory.getPath() + "/treeStructure01.swc"));
        TreeCreator t1= new TreeCreator(fileInputStream);
        Node<NodeData> node = t1.createTreeStructure(0);
        assertEquals(8, node.getNodeCount());
        assertEquals(2, node.getChildren().size());
        assertEquals(1, node.getNodeData().getIndex().get(0).intValue());
        assertEquals(-1, node.getNodeData().getParent().get(0).intValue());

        Node<NodeData> node2 = node.getChildren().get(0);
        assertEquals(3, node2.getNodeCount());
        assertEquals(2, node2.getChildren().size());
        assertEquals(1, node2.getNodeData().getIndex().get(0).intValue());
        assertEquals(2, node2.getNodeData().getIndex().get(1).intValue());
        assertEquals(3, node2.getNodeData().getIndex().get(2).intValue());
        assertEquals(4, node2.getNodeData().getIndex().get(3).intValue());
        assertEquals(-1, node2.getNodeData().getParent().get(0).intValue());
        assertEquals(1, node2.getNodeData().getParent().get(1).intValue());
        assertEquals(2, node2.getNodeData().getParent().get(2).intValue());
        assertEquals(3, node2.getNodeData().getParent().get(3).intValue());

        Node<NodeData> node3 = node2.getChildren().get(0);
        assertEquals(1, node3.getNodeCount());
        assertEquals(0, node3.getChildren().size());
        assertEquals(4, node3.getNodeData().getIndex().get(0).intValue());
        assertEquals(5, node3.getNodeData().getIndex().get(1).intValue());
        assertEquals(3, node3.getNodeData().getParent().get(0).intValue());
        assertEquals(4, node3.getNodeData().getParent().get(1).intValue());

        Node<NodeData> node4 = node2.getChildren().get(1);
        assertEquals(1, node4.getNodeCount());
        assertEquals(0, node4.getChildren().size());
        assertEquals(4, node4.getNodeData().getIndex().get(0).intValue());
        assertEquals(6, node4.getNodeData().getIndex().get(1).intValue());
        assertEquals(3, node4.getNodeData().getParent().get(0).intValue());
        assertEquals(4, node4.getNodeData().getParent().get(1).intValue());

        Node<NodeData> node5 = node.getChildren().get(1);
        assertEquals(4, node5.getNodeCount());
        assertEquals(3, node5.getChildren().size());
        assertEquals(1, node5.getNodeData().getIndex().get(0).intValue());
        assertEquals(7, node5.getNodeData().getIndex().get(1).intValue());
        assertEquals(-1, node5.getNodeData().getParent().get(0).intValue());
        assertEquals(1, node5.getNodeData().getParent().get(1).intValue());

        Node<NodeData> node6 = node5.getChildren().get(0);
        assertEquals(1, node6.getNodeCount());
        assertEquals(0, node6.getChildren().size());
        assertEquals(7, node6.getNodeData().getIndex().get(0).intValue());
        assertEquals(8, node6.getNodeData().getIndex().get(1).intValue());
        assertEquals(1, node6.getNodeData().getParent().get(0).intValue());
        assertEquals(7, node6.getNodeData().getParent().get(1).intValue());

        Node<NodeData> node7 = node5.getChildren().get(1);
        assertEquals(1, node7.getNodeCount());
        assertEquals(0, node7.getChildren().size());
        assertEquals(7, node7.getNodeData().getIndex().get(0).intValue());
        assertEquals(9, node7.getNodeData().getIndex().get(1).intValue());
        assertEquals(10, node7.getNodeData().getIndex().get(2).intValue());
        assertEquals(1, node7.getNodeData().getParent().get(0).intValue());
        assertEquals(7, node7.getNodeData().getParent().get(1).intValue());
        assertEquals(9, node7.getNodeData().getParent().get(2).intValue());

        Node<NodeData> node8 = node5.getChildren().get(2);
        assertEquals(1, node8.getNodeCount());
        assertEquals(0, node8.getChildren().size());
        assertEquals(7, node8.getNodeData().getIndex().get(0).intValue());
        assertEquals(11, node8.getNodeData().getIndex().get(1).intValue());
        assertEquals(1, node8.getNodeData().getParent().get(0).intValue());
        assertEquals(7, node8.getNodeData().getParent().get(1).intValue());
    }

    @Test
    public void treeStructure02() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(treeCreatorTestDirectory.getPath() + "/treeStructure02.swc"));
        TreeCreator t1 = new TreeCreator(fileInputStream);
        Node<NodeData> node = t1.createTreeStructure(0);
        assertEquals(1, node.getNodeCount());
        assertEquals(0, node.getChildren().size());
        assertEquals(1, node.getNodeData().getIndex().get(0).intValue());
        assertEquals(2, node.getNodeData().getIndex().get(1).intValue());
        assertEquals(3, node.getNodeData().getIndex().get(2).intValue());
        assertEquals(-1, node.getNodeData().getParent().get(0).intValue());
        assertEquals(1, node.getNodeData().getParent().get(1).intValue());
        assertEquals(2, node.getNodeData().getParent().get(2).intValue());
    }

    @Test
    public void treeStructure03() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(treeCreatorTestDirectory.getPath() + "/treeStructure03.swc"));
        TreeCreator t1 = new TreeCreator(fileInputStream);
        Node<NodeData> node = t1.createTreeStructure(0);
        assertEquals(3, node.getNodeCount());
        assertEquals(2, node.getChildren().size());
        assertEquals(1, node.getNodeData().getIndex().get(0).intValue());
        assertEquals(-1, node.getNodeData().getParent().get(0).intValue());

        Node<NodeData> node2 = node.getChildren().get(0);
        assertEquals(1, node2.getNodeCount());
        assertEquals(0, node2.getChildren().size());
        assertEquals(1, node2.getNodeData().getIndex().get(0).intValue());
        assertEquals(2, node2.getNodeData().getIndex().get(1).intValue());
        assertEquals(-1, node2.getNodeData().getParent().get(0).intValue());
        assertEquals(1, node2.getNodeData().getParent().get(1).intValue());

        Node<NodeData> node3 = node.getChildren().get(1);
        assertEquals(1, node3.getNodeCount());
        assertEquals(0, node3.getChildren().size());
        assertEquals(1, node3.getNodeData().getIndex().get(0).intValue());
        assertEquals(3, node3.getNodeData().getIndex().get(1).intValue());
        assertEquals(4, node3.getNodeData().getIndex().get(2).intValue());
        assertEquals(-1, node3.getNodeData().getParent().get(0).intValue());
        assertEquals(1, node3.getNodeData().getParent().get(1).intValue());
        assertEquals(3, node3.getNodeData().getParent().get(2).intValue());
    }

}