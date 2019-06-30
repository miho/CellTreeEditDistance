package edu.gcsc.celltreeedit;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Erid on 23.04.2018.
 */
public class LabelTest {
    @Test
    public void correctLabelImport(){
        try {
            FileInputStream f1=new FileInputStream(new File("...\\1k.swc"));
            TreeCreator t1= new TreeCreator(f1);
            t1.createTreeStructure(0);
            assertEquals(5, t1.getNodeList().size());

            int i0= t1.getNodeList().get(0).getNodeData().getIndex().get(0);
            assertEquals(1,i0);

            int i1= t1.getNodeList().get(1).getNodeData().getIndex().get(0);
            assertEquals(1,i1);

            int i2= t1.getNodeList().get(2).getNodeData().getIndex().get(0);
            assertEquals(1,i2);

            int i3= t1.getNodeList().get(3).getNodeData().getIndex().get(0);
            assertEquals(6,i3);

            int i4= t1.getNodeList().get(4).getNodeData().getIndex().get(0);
            assertEquals(6,i4);

            int i2_1= t1.getNodeList().get(2).getNodeData().getIndex().get(1);
            assertEquals(3,i2_1);

            int i2_2= t1.getNodeList().get(2).getNodeData().getIndex().get(2);
            assertEquals(4,i2_2);

            int i2_3= t1.getNodeList().get(2).getNodeData().getIndex().get(3);
            assertEquals(5,i2_3);

            int i3_1= t1.getNodeList().get(3).getNodeData().getIndex().get(1);
            assertEquals(7,i3_1);

            int i3_2= t1.getNodeList().get(3).getNodeData().getIndex().get(2);
            assertEquals(8,i3_2);

            int i4_1= t1.getNodeList().get(4).getNodeData().getIndex().get(1);
            assertEquals(10,i4_1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel1(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(1);
            assertEquals(1, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel2(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);


            t1.createTree(2);
            assertEquals(1/3, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel3(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);


            t1.createTree(3);
            assertEquals(3, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
            assertEquals(1, t1.getNodeList().get(1).getNodeData().getLabel(),1.0);
            assertEquals(1, t1.getNodeList().get(2).getNodeData().getLabel(),1.0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel4(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(4);
            assertEquals(4, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel5(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(5);
            assertEquals(4, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void correctLabel6(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(6);
            assertEquals(16, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel7(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(7);
            assertEquals(4/3, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel8(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(8);
            assertEquals(12*3.142+10*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel9(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(9);
            assertEquals(8*3.142+2*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel10(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(10);
            assertEquals(28*3.142+26*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel11(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(11);
            assertEquals(12/28, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel12(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(12);
            assertEquals(10*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel13(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(13);
            assertEquals(4*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel14(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(14);
            assertEquals(26*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void correctLabel15(){
        try {
            FileInputStream f1 = new FileInputStream(new File("...\\labelsTest.swc"));
            TreeCreator t1 = new TreeCreator(f1);
            t1.createTree(15);
            assertEquals(10/26*3.142, t1.getNodeList().get(0).getNodeData().getLabel(),1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
