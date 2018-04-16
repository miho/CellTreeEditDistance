package edu.gcsc.celltreeedit;

import distance.APTED;
import eu.mihosoft.vswcreader.SWCSegment;
import node.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by Erid on 13.04.2018.
 */
public class TreeCreatorTest {

    /**
     * es kann  nicht von resources direktory gelesen werden. nullpoionterexception kommt
     */
    FileInputStream fi1;
    FileInputStream fi2;
    File f1=new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1k.SWC");
    File f2=new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1k_2.SWC");
    File f3=new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1g.SWC");
    File f4=new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1g_2.SWC");
    File f5=new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\sg.SWC");
    File f6=new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\LA31_07_15_10_39_191516-1SpalaxMelDAPI_ov10_Z85_sdx20_3.CNG.SWC");
    //File f7=new File(getClass().getClassLoader().getResource("1k").getFile());
    @Test
    public void createTree() throws Exception {
        fi1=new FileInputStream(f1);
        fi2=new FileInputStream(f2);
        TreeCreator t1 = new TreeCreator();
        TreeCreator t2 = new TreeCreator();
        Node<NodeData> one = t1.createTree(fi1,1);
        Node<NodeData> two = t2.createTree(fi2,1);
        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        float result = apted.computeEditDistance(one, two);
        assertEquals (3.0,result,1.0);
    }

    @Test
    public void createTree2() throws Exception {
        fi1=new FileInputStream(f1);
        fi2=new FileInputStream(f3);
        TreeCreator t1 = new TreeCreator();
        TreeCreator t2 = new TreeCreator();
        Node<NodeData> one = t1.createTree(fi1,1);
        Node<NodeData> two = t2.createTree(fi2,1);
        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        float result = apted.computeEditDistance(one, two);
        assertEquals (660.0,result,1.0);
    }

    @Test
    public void createTree3() throws Exception {
        fi1=new FileInputStream(f3);
        fi2=new FileInputStream(f4);
        TreeCreator t1 = new TreeCreator();
        TreeCreator t2 = new TreeCreator();
        Node<NodeData> one = t1.createTree(fi1,1);
        Node<NodeData> two = t2.createTree(fi2,1);
        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        float result = apted.computeEditDistance(one, two);
        assertEquals (32.0,result,1.0);
    }


    /**
     * example
     * @throws Exception
     */
    @Test
    public void setNodeData() throws Exception {
        fi1 = new FileInputStream(f1);
        List<Node<NodeData>> nodeList = new ArrayList<>();

        try {
            List<SWCSegment> swcSegments = SWCSegment.fromStream(fi1);
            int size = swcSegments.size();
            int[] parents = {-1, 1, 1, 3, 4, 5, 4, 5, 6, 6, 10};
            for (int i = 0; i < size; i++) {
                Node<NodeData> treeNode = new Node<>(new NodeData(swcSegments.get(i).getIndex(),
                        swcSegments.get(i).getType(),
                        swcSegments.get(i).getPos().getX(),
                        swcSegments.get(i).getPos().getY(),
                        swcSegments.get(i).getPos().getZ(),
                        swcSegments.get(i).getR(),
                        swcSegments.get(i).getParent()));
                nodeList.add(treeNode);
            }
            for(int j=0; j<size;j++){
                assertEquals(parents[j],nodeList.get(j).getNodeData().getParent());
            }
        }catch (IOException e){

        }
    }
    /**
     * number of nodes starting at root --- swc segment number -1
     * @throws Exception
     */
    @Test
    public void getNodesNumb() throws Exception {
        fi1=new FileInputStream(f1);
        TreeCreator t1= new TreeCreator();
        Node<NodeData> node= t1.createTree(fi1,1);
        assertEquals(10,node.getNodeCount(),1.0);

    }

    /**
     * number of nodes starting at root --- swc segment number -1
     * @throws Exception
     */
    @Test
    public void getNodesNumb2() throws Exception {
        fi1=new FileInputStream(f3);
        TreeCreator t1= new TreeCreator();
        Node<NodeData> node= t1.createTree(fi1,1);
        assertEquals(670,node.getNodeCount(),1.0);

    }

}