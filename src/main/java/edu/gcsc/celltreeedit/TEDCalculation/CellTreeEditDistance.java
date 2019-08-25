package edu.gcsc.celltreeedit.TEDCalculation;

import edu.gcsc.celltreeedit.TEDResult;
import edu.gcsc.celltreeedit.Utils;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import javafx.util.Pair;

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
        String[] fileNames = Utils.getNeuronNamesForFiles(files);
        this.compareFiles(labelId);
        return new TEDResult(results, fileNames);
    }

    static class MyTask implements Runnable {

        private final List<Node<NodeData>> nodeData;
        private int i;
        private int j;
        private double[][] results;
        private APTED apted;

        public MyTask(int i, int j, double[][] results, APTED apted, List<Node<NodeData>> nodeData) {
            this.i = i;
            this.j = j;
            this.results = results;
            this.apted = apted;
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

    private void compareFiles(@ParamInfo(name="Label", style="load-folder-dialog")int choice) {

        long runtimeInS;
        final long start = System.nanoTime();

        int size = files.length;
        results = new double[size][size];
        final double[][] resultsFinal = results;

        List<Node<NodeData>> nodeData = Collections.synchronizedList(new ArrayList<>(size));
        for(int i = 0; i < size;i++) {
            nodeData.add(null);
        }

        try {

            System.out.println("> Reading files");

            ExecutorService filePool = Executors.newWorkStealingPool();


            for (int i = 0; i < size; i++) {
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

            System.out.println("> Calculation started");
            for (int i = 0; i < size - 1; i++) {
                for (int j = i + 1; j < size; j++) {
                    // compare each two files

                    // Initialise APTED.
                    APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());

                    // Execute APTED.
                    Runnable myTask = new MyTask(i, j, resultsFinal, apted, nodeData);
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
        System.out.println("Runtime in seconds: " + runtimeInS);
    }
}
