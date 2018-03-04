package edu.gcsc.celltreeedit;

import java.io.*;
import distance.APTED;
import node.Node;
import javax.swing.*;

/**
 * Created by Erid on 16.02.2018.
 *
 * The class will do the comparison of the files imported from ChooseFiles and do the representation in a Matrix
 */
public class Matrix {

    /**
     *
     * @param files
     */
    public static void compare(File[] files) {

        int size= files.length;
        float[][]results= new float[size][size];                                    // array saves the results of the algorithm

        try {
            for(int i=0; i<size-1;i++){
                for(int j=i+1; j<size;j++){
                    // compare each two files
                    FileInputStream f= new FileInputStream(files[i]);
                    FileInputStream f2= new FileInputStream(files[j]);
                    ImportData i1 = new ImportData();
                    ImportData i2 = new ImportData();
                    InputParser parser1 = new InputParser();
                    InputParser parser2 = new InputParser();
                    i1.importData(f);
                    i2.importData(f2);
                    Node<NodeData> t1 = parser1.createTree(i1, 1);
                    Node<NodeData> t2 = parser2.createTree(i2, 1);

                    // Initialise APTED.
                    APTED<CostModel, NodeData> apted = new APTED<>(new CostModel());
                    // Execute APTED.
                    float result = apted.computeEditDistance(t1, t2);
                    results[i][j]=result;
                    results[j][i]=result;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix.show(results);
    }


    /**
     *      Methode muss noch definiert werden um die Matrix in einer Frame anzeigen zu lassen.
     *
     */
    public static void show(float[][] results){


        int size=results.length;
        //Ausgabe der results Array formattieren---------------Tabelle oder Graph plotten !!!!!

        String placeholder="|%8s|";
        String name="%8s |";
        System.out.printf(name,"");
        for(int i=0; i<size;i++){                       // printe die kopfzeiele....anstatt k kann der name angezeigt werden
            int k=i+1;
            System.out.printf(placeholder, "file "+k);
        }
        System.out.print("\n");
        System.out.print("  -------|");
        //System.out.printf(name,"");
        for(int i=0; i<size;i++){                       // trenne die kopfzeile von den resultaten
            System.out.printf(placeholder,"--------");
        }
        System.out.print("\n");
        for(int i=0; i<size;i++){                       // printe die resultaten
            int k=i+1;
            System.out.printf(name, "file "+k);
            for(int j=0; j<size;j++){
                System.out.printf(placeholder,results[i][j]);
            }
            System.out.print("\n");
            System.out.print("  -------|");
            //System.out.printf(name,"");
            for(int l=0; l<size;l++){                       // trenne die zeilen der resultaten
                System.out.printf(placeholder,"--------");
            }
            System.out.print("\n");
        }
    }
}
