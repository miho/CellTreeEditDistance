package edu.gcsc.celltreeedit;

import distance.APTED;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import node.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
/**
 * Created by Erid on 16.02.2018.
 *
 * The class will do the comparison of the files imported from ChooseFiles and save the results in a 2d array
 */
@ComponentInfo(name = "CellTreeEditDistance", category = "CellTreeEditDistance")
public class CellTreeEditDistance implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private float[][] results;
    private File[] files;
    private String[] fileNames;

    public String[] getFileNames(){
        return this.fileNames;
    }

    public float[][] getResults(){
        return this.results;
    }

    public File[] getFiles(){
        return this.files;
    }

    public void showLabels(){
        JFrame frame = new JFrame();
        TableView labels= new TableView();
        frame.add(labels);
        //labels.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,300);
        frame.setVisible(true);
        frame.setTitle("Labels");
    }

    public static float[][] compare(File[] files, int choice) {

        int size= files.length;
        System.out.println(size+" Files were imported!");
        int gesamtBerechnung= ((size*size)-size)/2;
        int progress=0;
        float[][]results= new float[size][size];                         // array saves the results of the algorithm
        String[] names=new String[size];
        for(int i=0;i<size;i++){
            names[i]=files[i].getName();
        }
        try {
            System.out.println("****************************** Progress ****************************************");
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
                    System.out.println("* Progress.........:"+progress+"/"+gesamtBerechnung);
                }
            }
            System.out.println("*");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CellTreeEditDistance.printtoConsole(results, names);
        System.out.println("");
        System.out.println("* Do you want to save the matrix in a txt file? (yes/no) ");
        System.out.println("");
        System.out.printf("********************************************************************************\n");

        Scanner s= new Scanner(System.in);
        String respond=s.next();
        if(respond.equals("yes")){
            CellTreeEditDistance.printToTxt(results);
        }else if(respond.equals("no")){
            System.out.printf("********************************************************************************\n");
            System.out.println("");
            System.out.println("Thank you! The program will now close!");
        }else{
            System.out.printf("********************************************************************************\n");
            System.out.println("");
            System.out.println("Input not recognized. Save is canceled");
        }
        return results;
    }

    public void compareFiles(@ParamInfo(name="Label", style="load-folder-dialog")int choice) {
        files=ChooseFiles.choose();
        int size= files.length;
        fileNames=new String[size];
        for(int i=0;i<size;i++){
            fileNames[i]=files[i].getName();
        }
        System.out.println(size+" Files were imported!");    // loggen?
        int nrofCalculations= ((size*size)-size)/2;
        int progress=0;
        results= new float[size][size];                         // array saves the results of the algorithm
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
        TableView tableView= new TableView(fileNames,results);
        JButton export=new JButton("Export");
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CellTreeEditDistance.printToTxt(results);
            }
        });
        tableView.add(export);
        frame.add(tableView);

        frame.setSize(500,300);
        frame.setVisible(true);
        frame.setTitle("Comparison Results");
    }

    public static void printtoConsole(float[][] results, String[] names){
        int size=results.length;
        //Ausgabe der results Array formattieren---------------Tabelle oder Graph plotten !!!!!
        String filename="*%10s -> ";
        System.out.printf("****************************** Filenames ***************************************\n");
        System.out.print("*\n");
        for(int i=0;i<size;i++){
            int z=i+1;
            System.out.printf(filename,"file "+z);
            System.out.printf(names[i]+"\n");
        }
        System.out.print("*\n");
        System.out.printf("********************************************************************************\n");



        String placeholder="|%8s|";
        String name="%8s |";
        System.out.print("\n");
        System.out.printf("******************************* Results ****************************************\n");
        System.out.print("\n");
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
        System.out.printf("\n");
        System.out.printf("********************************************************************************\n");

    }

    public static void printToTxt(float[][] results){
        System.out.println("Please choose the directory where you want to save the file");
        JFileChooser save= new JFileChooser();
        save.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        save.showSaveDialog(null);
        String path=save.getSelectedFile().getAbsolutePath();
        try {
            FileWriter export=new FileWriter(path+"/export.txt");
            BufferedWriter br=new BufferedWriter(export);
            br.write("#;");
            for(int i=0;i<results.length;i++){                              //kopfzeile
                int a=i+1;
                br.write(a+";");
            }
            br.newLine();
            for(int i=0;i<results.length;i++){
                int a=i+1;
                br.write(a+";");
                for(int j=0;j<results.length;j++){
                    br.write(results[i][j]+";");
                }
                br.newLine();
            }

            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }
}
