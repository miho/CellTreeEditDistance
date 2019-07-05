package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.NodeData;
import edu.gcsc.celltreeedit.TreeCostModel;
import edu.gcsc.celltreeedit.TreeCreator;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TEDTest {

    @Test
    public void checkTEDZhangShasha() throws IOException {
        FileInputStream f1 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest03_T1.swc"));
        TreeCreator t1 = new TreeCreator(f1);
        Node<NodeData> root1 = t1.createTree(1);
        FileInputStream f2 = new FileInputStream(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Test/labelTest03_T2.swc"));
        TreeCreator t2 = new TreeCreator(f2);
        Node<NodeData> root2 = t2.createTree(1);

        APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        float result = apted.computeEditDistance(root1, root2);
        assertEquals(0f, result,0f);
    }
}
