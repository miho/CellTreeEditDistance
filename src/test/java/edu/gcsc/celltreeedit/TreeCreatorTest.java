package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


/**
 * Created by Erid on 13.04.2018.
 */
public class TreeCreatorTest {

    @Test
    public void helpArrays(){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File("TestSWCFiles/1k.swc"));
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
            FileInputStream fileInputStream=new FileInputStream(new File("TestSWCFiles/1k_2.swc"));
            TreeCreator t1= new TreeCreator(fileInputStream);
            int[] firstChild={-1,2,16,4,5,6,7,8,9,14,11,12,13,15,-1,-1,-1};
            int[] nextSibling={-1,-1,3,-1,-1,-1,-1,10,-1,-1,-1,-1,-1,-1,-1,-1,-1};
            assertArrayEquals(firstChild,t1.getFirstChild());
            assertArrayEquals(nextSibling,t1.getNextSibling());

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Test
    public void treeStructure(){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File("TestSWCFiles/1k.swc"));
            TreeCreator t1= new TreeCreator(fileInputStream);
            t1.createTreeStructure(0);
            assertEquals(t1.getNodeList().size(),5);
            assertEquals(t1.getNodeList().get(0).getNodeCount(),5);
            assertEquals(t1.getNodeList().get(0).getChildren().get(0).getNodeData().getIndex().size(), 2);
            assertEquals(t1.getNodeList().get(0).getChildren().get(1).getNodeData().getIndex().size(), 5);
            assertEquals(t1.getNodeList().get(1).getChildren().size(), 0);
            assertEquals(t1.getNodeList().get(2).getChildren().get(0).getNodeData().getIndex().size(), 4);
            assertEquals(t1.getNodeList().get(2).getChildren().get(1).getNodeData().getIndex().size(), 5);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}