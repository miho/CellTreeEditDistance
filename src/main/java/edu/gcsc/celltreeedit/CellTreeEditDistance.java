package edu.gcsc.celltreeedit;

import distance.APTED;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import node.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
/**
 * Created by Erid on 16.02.2018.
 *
 * The class will do the comparison of the files imported from Utils and save the results in a 2d array
 */
@ComponentInfo(name = "CellTreeEditDistance", category = "CellTreeEditDistance")
public class CellTreeEditDistance implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private float[][] results;
    private File[] files;
    private String[] fileNames;

    public void showLabels(){
        JFrame frame = new JFrame();
        Tables labels= new Tables();
        frame.add(labels);
        //labels.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,300);
        frame.setVisible(true);
        frame.setTitle("Labels");
    }

    public void compareFiles(@ParamInfo(name="Label", style="load-folder-dialog")int choice) {
        files= Utils.choose();
        int size= files.length;
        fileNames=new String[size];
        for(int i=0;i<size;i++){
            fileNames[i]=files[i].getName();
        }
        System.out.println(size+" Files were imported!");    // loggen?
        int nrofCalculations= ((size*size)-size)/2;
        int progress=0;
        results= new float[size][size];
        String[] names=new String[size];
        for(int i=0;i<size;i++){
            names[i]=files[i].getName();
        }
        try {
            System.out.println("* Progress *");
            for(int i=0; i<size-1;i++){
                for(int j=i+1; j<size;j++){
                    // compare each two files
                    FileInputStream f= new FileInputStream(files[i]);
                    FileInputStream f2= new FileInputStream(files[j]);

                    TreeCreator one = new TreeCreator(f);
                    TreeCreator two = new TreeCreator(f2);

                    Node<NodeData> t1 = one.createTree(choice,0);
                    Node<NodeData> t2 = two.createTree(choice,0);

                    // Initialise APTED.
                    APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
                    // Execute APTED.
                    float result = apted.computeEditDistance(t1, t2);
                    results[i][j]=result;
                    results[j][i]=result;
                    progress++;
                    System.out.println("* Progress....:"+progress+"/"+nrofCalculations);
                }
            }
            System.out.println("*");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame= new JFrame();
        Tables tables = new Tables(fileNames,results);
        JButton export=new JButton("Export");
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.printToTxt(results);
            }
        });
        tables.add(export);
        frame.add(tables);

        frame.setSize(500,300);
        frame.setVisible(true);
        frame.setTitle("Comparison Results");
    }
}
