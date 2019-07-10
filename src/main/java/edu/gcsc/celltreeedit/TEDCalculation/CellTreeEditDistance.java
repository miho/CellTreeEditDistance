package edu.gcsc.celltreeedit.TEDCalculation;

import edu.gcsc.celltreeedit.Tables;
import edu.gcsc.celltreeedit.Utils;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import javafx.util.Pair;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Erid on 16.02.2018.
 *
 * The class will do the comparison of the files imported from Utils and save the results in a 2d array
 */
@ComponentInfo(name = "CellTreeEditDistance", category = "CellTreeEditDistance")
public class CellTreeEditDistance implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private double[][] results;
    private File[] files;
    private String[] fileNames;
    private Set<String> swcFilenames;
    private List<String> foundFileNames = new ArrayList<>();
    private List<File> foundFiles = new ArrayList<>();

    public static void showLabels(){
        JFrame frame = new JFrame();
        Tables labels= new Tables();
        frame.add(labels);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,350);
        frame.setVisible(true);
        frame.setTitle("Labels");
    }

    public void compareFilesFromFileDialog(int choice) {
        this.files= Utils.choose();
        int size= files.length;
        fileNames=new String[size];
        for(int i=0;i<size;i++){
            fileNames[i]=files[i].getName();
        }
        System.out.println(size + " Files were imported!");    // loggen?
        this.compareFiles(choice);
    }

    public Pair<double[][], String[]> compareFilesFromDirectory(File swcDirectory, int choice) {

        this.findSWCFiles(swcDirectory);
        this.files = this.foundFiles.toArray(new File[this.foundFiles.size()]);
        this.fileNames = this.foundFileNames.toArray(new String[this.foundFileNames.size()]);
        this.compareFiles(choice);
        return new Pair<>(results, fileNames);
    }

    private void findSWCFiles(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                this.foundFiles.add(subFile);
                this.foundFileNames.add(Utils.removeSWCFileExtensions(subFile.getName()));
            } else {
                if (subFile.getName().equals("00_Ignore")) {
                    continue;
                }
                this.findSWCFiles(subFile);
            }
        }
    }

    public Pair<double[][], String[]> compareFilesFromFilenames(Set<String> swcFilenames, File swcDirectory, int choice) {
        this.swcFilenames = swcFilenames;
        this.findSWCFilesWithFilenames(swcDirectory);
        this.files = this.foundFiles.toArray(new File[this.foundFiles.size()]);
        this.fileNames = this.foundFileNames.toArray(new String[this.foundFileNames.size()]);
        this.compareFiles(choice);
        return new Pair<>(results, fileNames);
    }

    private void findSWCFilesWithFilenames(File directory) {
        File[] subFiles = directory.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File subFile: subFiles) {
            if (subFile.isFile()) {
                if (this.swcFilenames.contains(Utils.removeSWCFileExtensions(subFile.getName()))) {
                    this.foundFiles.add(subFile);
                    this.foundFileNames.add(Utils.removeSWCFileExtensions(subFile.getName()));
                }
            } else {
                if (subFile.getName().equals("00_Ignore")) {
                    continue;
                }
                this.findSWCFilesWithFilenames(subFile);
            }
        }
    }

    static class MyTask implements Runnable {

        private final List<Node<NodeData>> nodeData;
        private int i;
        private int j;
        private double[][] results;
        private APTED apted;
        private int choice;

        public MyTask(int i, int j, double[][] results, APTED apted, int choice, List<Node<NodeData>> nodeData) {
            this.i = i;
            this.j = j;
            this.results = results;
            this.apted = apted;
            this.choice = choice;
            this.nodeData = nodeData;
        }

        @Override
        public void run() {

            float result = apted.computeEditDistance(nodeData.get(i), nodeData.get(j));

            // TODO sync
            results[i][j] = result;
            results[j][i] = result;
        }
    }

    public void compareFiles(@ParamInfo(name="Label", style="load-folder-dialog")int choice) {

        long runtimeInS = 0;
        final long start = System.nanoTime();

        int size = files.length;
        int nrofCalculations = ((size*size)-size)/2;
        int progress = 0;
        results = new double[size][size];
        String[] names = new String[size];
        for(int i=0;i<size;i++){
            names[i]=files[i].getName();
        }
        final double[][] resultsFinal = results;

        List<Node<NodeData>> nodeData = Collections.synchronizedList(new ArrayList<Node<NodeData>>(size));
        for(int i = 0; i < size;i++) {
            nodeData.add(null);
        }

        try {

            System.out.println("> Reading files");

            ExecutorService filePool = Executors.newWorkStealingPool();


            for(int i=0; i<size;i++){

                // compare each two files

                final int finalI = i;

                filePool.execute(() -> {
                    Node<NodeData> t1 = nodeData.get(finalI);

                    if (t1 == null) {
                        TreeCreator one = null;
                        try {
                            one = new TreeCreator(new FileInputStream(files[finalI]));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        t1 = one.createTree(choice);
                        nodeData.set(finalI, t1);
                    }
                });
            }

            filePool.shutdown();

            while(!filePool.awaitTermination(10L, TimeUnit.SECONDS)) {
                System.out.println("Still waiting for file readers: " + new Date());
            }

            ExecutorService pool = Executors.newWorkStealingPool();

//            System.out.println("* Progress *");
            for(int i=0; i<size-1;i++) {

                for(int j=i+1; j<size;j++){
                    // compare each two files

                    // Initialise APTED.
                    APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
                    // Execute APTED.

                    Runnable myTask = new MyTask(i,j,resultsFinal, apted, choice, nodeData);
                    pool.execute(myTask);

                    progress++;
//                    System.out.println("* Progress....:"+progress+"/"+nrofCalculations);
                }
            }
            System.out.println("*");


            pool.shutdown();

            while(!pool.awaitTermination(10L,TimeUnit.SECONDS)) {
                System.out.println("Still waiting for results: " + new Date());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final long runtimeInNanos = System.nanoTime() - start;
        runtimeInS = TimeUnit.NANOSECONDS.toSeconds(runtimeInNanos);
        System.out.println("Runtime in seconds: " + runtimeInS);
    }
}
