package edu.gcsc.celltreeedit;

import distance.APTED;
import node.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Erid on 16.02.2018.
 *
 * The class will do the comparison of the files imported from ChooseFiles and save the results in a 2d array
 */
public class Matrix {

    private float[][] results;

    public float[][] getResults(){
        return this.results;
    }

    public static File[] choose(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\Erid\\Dropbox\\Dokumente\\Informatik-UNI\\SoSe2017\\Bachelorarbeit"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); //accept files and directories as input
        FileNameExtensionFilter swc=new FileNameExtensionFilter("SWC","SWC");
        fileChooser.addChoosableFileFilter(swc);                            // filter on swc files
        fileChooser.setAcceptAllFileFilterUsed(false);                     // show only swc files
        fileChooser.setMultiSelectionEnabled(true);                       // accept multiple files as input
        fileChooser.showOpenDialog(null);

        if(fileChooser.getSelectedFile().isFile())
            return fileChooser.getSelectedFiles();
        else{
            File folder= new File(fileChooser.getSelectedFile().getAbsolutePath());
            File[] selectedFiles= folder.listFiles(new FilenameFilter() {              // return only swc files
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".swc");
                }
            });
            return selectedFiles;
        }
    }

    public static String showLabels(){

        StringBuilder sb= new StringBuilder();
        String labels=null;
        sb.append("Choose a label of the followings by inserting the number\n");
        sb.append("1.  1\n");
        sb.append("2.  1/n\n");
        sb.append("4.  length of t[i]\n");
        sb.append("5.  surface of t[i]\n");
        sb.append("6.  volume of t[i]\n");
        sb.append("7.  legnth of t[i]\n");
        sb.append("8.  legnth of t[i]\n");
        sb.append("9.  legnth of t[i]\n");
        sb.append("10. legnth of t[i]\n");
        sb.append("11. legnth of t[i]\n");

        labels=sb.toString();
        return labels;
    }
    /**
     *
     * @param files
     */
    public static float[][] compare(File[] files) {

        int size= files.length;
        System.out.println(size+" Files were imported!");
        int gesamtBerechnung= ((size*size)-size)/2;
        int progress=0;
        float[][]results= new float[size][size];                         // array saves the results of the algorithm
        String[] names=new String[size];
        for(int i=0;i<size;i++){
            names[i]=files[i].getName();
        }
        System.out.printf("********************************************************************************\n");
        System.out.println("* Select one of the following labels for the comparison (type the number)");
        System.out.println("*");
        String tableformat="*|%13s|%6s|%13s|%42s|%1s";
        System.out.print("*-------------------------------------------------------------------------------\n");
        System.out.format(tableformat," ","k","Abbreviation","Label (t[i])","  \n");
        System.out.print("*-------------------------------------------------------------------------------\n");
        System.out.format(tableformat,"Topology","1","top1","1","\n");
        System.out.format(tableformat," ","2","top2","1 / |T|","\n");
        System.out.format(tableformat,"Length","3","lsec","Lenght of t[i]","\n");
        System.out.format(tableformat,"","4","lsoma","Length from t[i] to soma","\n");
        System.out.format(tableformat,"","5","ltree","Length of T[i]","\n");
        System.out.format(tableformat,"","6","Lsec","length of t[i] / length of T","\n");
        System.out.format(tableformat,"","7","Lsoma","length from t[i] to soma / length of T","\n");
        System.out.format(tableformat,"","8","Ltree","length of T[i] / length of T","\n");
        System.out.format(tableformat,"Volume","9","vsec","Volume of t[i]","\n");
        System.out.format(tableformat,"","10","vsoma","Volume from t[i] to soma","\n");
        System.out.format(tableformat,"","11","vtree","Volume of T[i]","\n");
        System.out.format(tableformat,"","12","Vsec","volume of t[i] / volume of T","\n");
        System.out.format(tableformat,"","13","Vsoma","volume from t[i] to soma / volume of T","\n");
        System.out.format(tableformat,"","14","Vtree","volume of T[i] / volume of T","\n");
        System.out.format(tableformat,"Surface","15","ssec","Surface of t[i]","\n");
        System.out.format(tableformat,"","16","ssoma","Surface from t[i] to soma","\n");
        System.out.format(tableformat,"","17","stree","Surface of T[i]","\n");
        System.out.format(tableformat,"","18","Ssec","surface of t[i] / surface of T","\n");
        System.out.format(tableformat,"","19","Ssoma","surface from t[i] to soma / surface of T","\n");
        System.out.format(tableformat,"","20","Stree","surface of T[i] / surface of T","\n");
        System.out.format(tableformat,"","21","vSsec","volume of t[i] / surface of T","\n");
        System.out.format(tableformat,"Angle","22","asec","Angle between children of t[i]","\n");
        System.out.print("*-------------------------------------------------------------------------------\n");

        Scanner slabel= new Scanner(System.in);
        String labelchoice=slabel.next();
        int choice= Integer.parseInt(labelchoice);

        try {
            System.out.println("****************************** Progress ****************************************");
            System.out.println("*");
            for(int i=0; i<size-1;i++){
                for(int j=i+1; j<size;j++){
                    // compare each two files
                    FileInputStream f= new FileInputStream(files[i]);
                    FileInputStream f2= new FileInputStream(files[j]);
                //    ImportData i1 = new ImportData();
                 //   ImportData i2 = new ImportData();
                  //  InputParser parser1 = new InputParser();
                    //InputParser parser2 = new InputParser();
                    //i1.importData(f, choice);
                    //i2.importData(f2,choice);
                    //Node<NodeData> t1 = parser1.createTree(i1, 1);
             //       System.out.println("Baum T1:"+t1.getNodeCount());
                    //Node<NodeData> t2 = parser2.createTree(i2, 1);
              //      System.out.println("Baum T2:"+t2.getNodeCount());

                     TreeCreator one = new TreeCreator();
                     TreeCreator two = new TreeCreator();

                    Node<NodeData> t1 = one.createTree(f,choice);
                    Node<NodeData> t2 = two.createTree(f2,choice);

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
            System.out.printf("********************************************************************************\n");
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix.print(results, names);
        System.out.println("");
        System.out.println("* Do you want to save the matrix in a txt file? (yes/no) ");
        System.out.println("");
        System.out.printf("********************************************************************************\n");

        Scanner s= new Scanner(System.in);
        String respond=s.next();
        if(respond.equals("yes")){
            Matrix.printToTxt(results);
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



    public float[][] compareFile(int choice) {
        File[] files=Matrix.choose();
        int size= files.length;
        System.out.println(size+" Files were imported!");
        int gesamtBerechnung= ((size*size)-size)/2;
        int progress=0;
        results= new float[size][size];                         // array saves the results of the algorithm
        String[] names=new String[size];
        for(int i=0;i<size;i++){
            names[i]=files[i].getName();
        }

        try {
            System.out.println("****************************** Progress ****************************************");
            System.out.println("*");
            for(int i=0; i<size-1;i++){
                for(int j=i+1; j<size;j++){
                    // compare each two files
                    FileInputStream f= new FileInputStream(files[i]);
                    FileInputStream f2= new FileInputStream(files[j]);

                    TreeCreator one = new TreeCreator();
                    TreeCreator two = new TreeCreator();

                    Node<NodeData> t1 = one.createTree(f,choice);
                    Node<NodeData> t2 = two.createTree(f2,choice);

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
            System.out.printf("********************************************************************************\n");
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    /**
     *
     * @param results
     */
    public static void print(float[][] results, String[] names){


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

    /**
     *
     * @param results
     */
    public static void printToTxt(float[][] results){
        System.out.printf("********************************************************************************\n");
        System.out.println("");
        System.out.println("Please choose the directory where you want to save the file");
        System.out.println("");
        System.out.printf("********************************************************************************\n");

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
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
