package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.TEDCalculation.NodeData;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCostModel;
import edu.gcsc.celltreeedit.TEDCalculation.TreeCreator;
import org.junit.Test;

import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Erid on 23.04.2018.
 */
public class CellTreeEditDistanceTest {

    @Test
    public void compareSameTree(){
        try {
            FileInputStream f1=new FileInputStream(new File("...\\1k.swc"));
            FileInputStream f2=new FileInputStream(new File("...\\1k.swc"));
            TreeCreator t1= new TreeCreator(f1);
            TreeCreator t2= new TreeCreator(f2);
            Node<NodeData> node1=t1.createTree(1);
            Node<NodeData> node2=t2.createTree(1);
            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(node1, node2);
            assertEquals(0,result,1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compareTreeToNull(){
        try {
            FileInputStream f1=new FileInputStream(new File("...\\1k.swc"));
            TreeCreator t1= new TreeCreator(f1);
            Node<NodeData> node1=t1.createTree(1);
            Node<NodeData> node2=new Node<NodeData>(new NodeData(1));
            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(node1, node2);
            assertEquals(4.0,result,1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compareTreeToTree1(){
        try {
            FileInputStream f1=new FileInputStream(new File("...\\1k.swc"));
            FileInputStream f2=new FileInputStream(new File("...\\1k_2.swc"));
            TreeCreator t1= new TreeCreator(f1);
            TreeCreator t2= new TreeCreator(f2);
            Node<NodeData> node1=t1.createTree(1);
            Node<NodeData> node2=t2.createTree(1);
            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(node1, node2);
            assertEquals(0.0,result,1.0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compareTrees(){
        try{
            FileInputStream f1= new FileInputStream(new File("...\\1k.swc"));
            FileInputStream f2= new FileInputStream(new File("...\\1k_3.swc"));
            TreeCreator t1= new TreeCreator(f1);
            TreeCreator t2= new TreeCreator(f2);
            Node<NodeData> node1=t1.createTree(1);
            Node<NodeData> node2=t2.createTree(1);
            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(node1, node2);
            assertEquals(1.0,result,1.0);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

}
