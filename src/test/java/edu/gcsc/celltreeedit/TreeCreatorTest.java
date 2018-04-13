package edu.gcsc.celltreeedit;

import distance.APTED;
import node.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import static org.junit.Assert.*;


/**
 * Created by Erid on 13.04.2018.
 */
public class TreeCreatorTest {

    FileInputStream f1;
    FileInputStream f2;

    @Test
    public void createTree() throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file=new File(classLoader.getResource(""));
        assertEquals("ll",file.getAbsolutePath());
       // f1=new FileInputStream(new File("C:\\Users\\Erid\\Documents\\GitHub\\CellTreeEditDistance\\src\\test\\resources\\1k.SWC"));
       // File file2=new File(getClass().getClassLoader().getResource("1g.swc").toURI);
       // f2=new FileInputStream(new File("C:\\\\Users\\\\Erid\\\\Dropbox\\\\Dokumente\\\\Informatik-UNI\\\\SoSe2017\\\\Bachelorarbeit\\\\files\\\\1k.SWC"));
      //  TreeCreator t1 = new TreeCreator();
      //  TreeCreator t2 = new TreeCreator();
      //  Node<NodeData> one = t1.createTree(f1,1);
       // Node<NodeData> two = t2.createTree(f2,1);
        //APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        //float result = apted.computeEditDistance(one, two);
        //assertEquals (0.0,result,1.0);
    }

    @Test
    public void setNodeLabel() throws Exception {
    }

    @Test
    public void fromString() throws Exception {
    }

}