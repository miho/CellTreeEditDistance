package edu.gcsc.celltreeedit;


import node.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Erid on 12.02.2018.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //CellTreeEditDistance matrix=new CellTreeEditDistance();
        //matrix.compareFiles(3);
        FileInputStream i=new FileInputStream(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\beispiel.txt"));



        // Schritt 1 TreCreator Konstruktor aufrufen damit die Daten importiert werden.
        // Schritt 2 den Baum Struktur erzeugen durch aufrufen vom TreeCreator.createTree(label, startknoten)
        // Schritt 3 die Labels berechnen
     //   FileInputStream i=new FileInputStream(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1g_2.SWC"));
       // FileInputStream i2=new FileInputStream(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit\\files\\1k.SWC"));
        TreeCreator treeCreator= new TreeCreator(i);
      //  TreeCreator treeCreator2= new TreeCreator(i2);
          Node<NodeData> t1 = treeCreator.createTree(4,0);
          System.out.println(t1.getChildren().get(1).getNodeData().getLabel());
      //  Node<NodeData> t2 = treeCreator2.createTree(8,0);
        // Initialise APTED.
 //       APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
        // Execute APTED.
   //     float result = apted.computeEditDistance(t1, t2);
     //   System.out.print(result);

        //System.out.println(treeCreator2.getNodeList().get(9).getNodeData().getIndex().get(1)+"*");

        // Vector<Integer> c= treeCreator2.getChildren(6);
        //  System.out.println(c.get(0));
        // System.out.print(t1.getChildren().get(0).getNodeData().getIndex().get(0));
        //   System.out.println(t2.getNodeCount());
          // for(int j=0;j<treeCreator2.getNodeList().size();j++){
            //   Node<NodeData> n=treeCreator2.getNodeList().get(j);
              // System.out.print(j+": ");
              // for(int k=0;k<n.getNodeData().getIndex().size();k++){
                //   System.out.print(n.getNodeData().getIndex().get(k)+" + ");
               //}
               //System.out.println("");
           }


        //System.out.println("liste size 1k "+treeCreator2.getNodeList().size());
        //System.out.println("liste size 1g_2 "+treeCreator.getNodeList().size());


     //   System.out.println(treeCreator2.getNodeList().get(7).getNodeData().getIndex().get(0));
       // System.out.println(treeCreator2.getNodeList().get(7).getNodeData().getIndex().get(1));
        //System.out.println(treeCreator2.getNodeList().get(7).getNodeData().getIndex().get(2));

        //  System.out.println(treeCreator2.getNodeList().get(2).getNodeData().getPosX().get(0));
        //System.out.println(treeCreator2.getNodeList().get(2).getNodeData().getPosX().get(1));
        //System.out.println(treeCreator2.getNodeList().get(2).getNodeData().getPosY().get(0));
        //System.out.println(treeCreator2.getNodeList().get(2).getNodeData().getPosY().get(1));
        //System.out.println(treeCreator2.getNodeList().get(2).getNodeData().getPosZ().get(0));
        //System.out.println(treeCreator2.getNodeList().get(2).getNodeData().getPosZ().get(1));

}

