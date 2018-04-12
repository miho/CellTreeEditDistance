package edu.gcsc.celltreeedit;

import distance.APTED;
import node.Node;

import javax.swing.*;
import java.io.*;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
            Matrix.compare(ChooseFiles.choose());
            // falls die files nicht von JfileChoose kommen sollten, dann die Files direkt an die Methode übergeben.

     //   FileInputStream f= new FileInputStream(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1k.SWC"));
      //  FileInputStream f2= new FileInputStream(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\LA31_07_15_10_39_191516-1SpalaxMelDAPI_ov10_Z85_sdx20_3.CNG.SWC"));

       // TestBaum t1 = new TestBaum();
      //  TestBaum t2 = new TestBaum();

       // Node<NodeData> one = t1.createTree(f);
        //Node<NodeData> two = t2.createTree(f2);

// Initialise APTED.
      //  APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
// Execute APTED.
        //float result = apted.computeEditDistance(one, two);
       // System.out.println(result);
       // System.out.println(one.getNodeCount());
        //System.out.println(two.getNodeCount());

    }
}

