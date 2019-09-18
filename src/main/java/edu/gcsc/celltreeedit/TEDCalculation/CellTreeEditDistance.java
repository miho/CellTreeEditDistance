package edu.gcsc.celltreeedit.TEDCalculation;

import edu.gcsc.celltreeedit.Utils;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;

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

    public TEDResult compareFilesFromFiles(File[] files, int labelId) {
        this.files = files;
        String[] fileNames = Utils.getNeuronnamesForFiles(files);
        this.compareFiles(labelId);
        return new TEDResult(results, fileNames);
    }

    public TEDResult compareFilesForCluster(File[] rowFiles, File[] colFiles, int labelId) {
        return null;
    }

    private void compareFiles(@ParamInfo(name="Label", style="load-folder-dialog")int choice) {

        long runtimeInS;
        final long start = System.nanoTime();

        int size = files.length;
        // results is global variable
        results = new double[size][size];
        // resultsFinal is a local reference to global variable results. it will always reference the original one no matter what happens
        final double[][] resultsFinal = results;

        // creating trees is also multithreaded. therefore nodeList must be synchronized. (really necessary? nodeList is initialized with null and every thread only writes to one specific entry)
        List<Node<NodeData>> nodeList = Collections.synchronizedList(new ArrayList<>(size));
        for(int i = 0; i < size;i++) {
            nodeList.add(null);
        }

        try {

            System.out.println("> Reading files");

            // create Threadpool for reading files. newWorkStealingPool takes as much CPU, RAM as it can
            ExecutorService filePool = Executors.newWorkStealingPool();

            // fill nodeList with nodes (trees created with label)
            for (int i = 0; i < size; i++) {

                // make sure finalI never changes
                final int finalI = i;

                filePool.execute(() -> {
                    Node<NodeData> t1 = nodeList.get(finalI);

                    if (t1 == null) {
                        TreeCreator one = null;
                        try {
                            one = new TreeCreator(new FileInputStream(files[finalI]));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        t1 = one.createTree(choice);
                        nodeList.set(finalI, t1);
                    }
                });
            }
            // prevent further adding of tasks to threadpool
            filePool.shutdown();

            // wait until reading is finished (all threads in threadpool are completed)
            while(!filePool.awaitTermination(10L, TimeUnit.SECONDS)) {
                System.out.println("Still waiting for file readers: " + new Date());
            }
            final long runtimeInNanos = System.nanoTime() - start;
            runtimeInS = TimeUnit.NANOSECONDS.toSeconds(runtimeInNanos);
            System.out.println("Runtime reading files in seconds: " + runtimeInS);

            // instantiate new threadpool for calculation
            ExecutorService pool = Executors.newWorkStealingPool();

            System.out.println("> Calculation started");

            // compare each two files
            for (int i = 0; i < size - 1; i++) {
                for (int j = i + 1; j < size; j++) {

                    // Initialise APTED.
                    APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());

                    // Execute APTED.
                    Runnable myTask = new MyTask(i, j, resultsFinal, apted, nodeList);
                    pool.execute(myTask);
                }
            }

            pool.shutdown();

            while(!pool.awaitTermination(10L,TimeUnit.SECONDS)) {
                System.out.println("Still waiting for results: " + new Date());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final long runtimeInNanos = System.nanoTime() - start;
        runtimeInS = TimeUnit.NANOSECONDS.toSeconds(runtimeInNanos);
        System.out.println("Runtime overall calculation in seconds: " + runtimeInS);
    }

    static class MyTask implements Runnable {

        private final List<Node<NodeData>> nodeList;
        private int i;
        private int j;
        private double[][] results;
        private APTED apted;

        MyTask(int i, int j, double[][] results, APTED apted, List<Node<NodeData>> nodeList) {
            this.i = i;
            this.j = j;
            this.results = results;
            this.apted = apted;
            this.nodeList = nodeList;
        }

        @Override
        public void run() {

            float result = apted.computeEditDistance(nodeList.get(i), nodeList.get(j));

            // java arrays are threadsafe if indexes written to are different which is the case here
            results[i][j] = result;
//            results[j][i] = result;
        }
    }
}
