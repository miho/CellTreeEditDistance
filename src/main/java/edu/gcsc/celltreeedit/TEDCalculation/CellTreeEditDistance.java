/*
 * Copyright 2018-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 * Copyright 2018-2019 Goethe Center for Scientific Computing, University Frankfurt. All rights reserved.
 * Copyright 2018 Erid Guga. All rights reserved.
 * Copyright 2019 Lukas Maurer. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
package edu.gcsc.celltreeedit.TEDCalculation;

import edu.gcsc.celltreeedit.Utils;
import eu.mihosoft.ext.apted.distance.APTED;
import eu.mihosoft.ext.apted.node.Node;
import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Erid on 16.02.2018.
 *
 * The class will do the comparison of the files imported from Utils and save the results in a 2d array
 */
@ComponentInfo(name = "CellTreeEditDistance", category = "CellTreeEditDistance")
public class CellTreeEditDistance implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    private static AtomicLong noOfFinishedTasks = new AtomicLong(0);
    private static Semaphore semaphore = new Semaphore(5000);
    private float[][] results;
    private File[] files;

    public TEDResult compareFilesFromFiles(File[] files, int labelId) {
        this.files = files;
        String[] fileNames = Utils.getNeuronnamesForFiles(files);
        this.compareFiles(labelId);
        return new TEDResult(results, fileNames);
    }

    public TEDClusterResult compareFilesForCluster(File[] files, int noOfRows, int iteration, int labelId, File swcFileDirectory) {
        System.out.println("maxMemory: " + (Runtime.getRuntime().maxMemory()/(1024.0*1024.0*1024.0)) + " freeMemory: " + (Runtime.getRuntime().freeMemory()/(1024*1024*1024)) + " usedMemory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024.0*1024.0*1024.0));
        int filesLength = files.length;
        // logic to get noOfRows and noOfColsPerRow-Array
        // row using matrix index
        int row = (iteration - 1) * noOfRows;
        int maxRow = row + (noOfRows - 1);
        // overflow == 0 is fine if overflow > 0 end of matrix reached
        int rowOverflow = maxRow + 1 - filesLength;
        File firstRowFile = files[row];
        Integer[] noOfColsPerRow;
        if (rowOverflow > 0) {
            noOfColsPerRow = new Integer[noOfRows - rowOverflow];
            noOfRows -= rowOverflow;
            maxRow -= rowOverflow;
        } else {
            noOfColsPerRow = new Integer[noOfRows];
        }
        for (int i = 0; i < noOfRows; i++) {
            noOfColsPerRow[i] = calculateNumberOfColsForRow(filesLength, row + i);
        }

        // logic for creating subFiles
        // col using matrix index
        int col = row + 1;
        int noOfCols = calculateNumberOfCols(filesLength, noOfRows, maxRow);
        File[] subFiles = new File[noOfCols + 1];
        subFiles[0] = firstRowFile;
        for (int i = 0; i < noOfCols; i++) {
            if (col + i > filesLength - 1) {
                // write files from first columns into array
                subFiles[i + 1] = files[col + i - filesLength];
            } else {
                subFiles[i + 1] = files[col + i];
            }
        }
        for (int i = 0; i < subFiles.length; i++) {
            subFiles[i] = new File(swcFileDirectory + "/" + subFiles[i].getPath());
        }

        compareFilesForCluster(subFiles, noOfColsPerRow, labelId);
        return new TEDClusterResult(iteration, row, col, results);
    }

    private Integer calculateNumberOfCols(Integer filesLength, Integer noOfRows, Integer maxRow) {
        if (filesLength % 2 == 0) {
            // even
            // maxRow is matrix index
            if (maxRow >= filesLength / 2) {
                return filesLength / 2 + noOfRows - 2;
            } else {
                return filesLength / 2 + noOfRows - 1;
            }
        } else {
            // odd
            return (filesLength - 1) / 2 + noOfRows - 1;
        }
    }

    private Integer calculateNumberOfColsForRow(Integer filesLength, Integer row) {
        if (filesLength % 2 == 0) {
            // even
            // row is matrix index
            if (row >= filesLength / 2) {
                return filesLength / 2 - 1;
            } else {
                return filesLength / 2;
            }
        } else {
            // odd
            return (filesLength - 1) / 2;
        }
    }

    private void compareFilesForCluster(File[] files, Integer[] noOfColsPerRow, int labelId) {
        int noOfRows = noOfColsPerRow.length;
        this.files = files;

        long runtimeInS;
        final long start = System.nanoTime();

        int size = files.length;
        // results is global variable
        results = new float[noOfRows][size];
        // resultsFinal is a local reference to global variable results. it will always reference the original one no matter what happens
        final float[][] resultsFinal = results;

        // creating trees is also multithreaded. therefore nodeList must be synchronized. (really necessary? nodeList is initialized with null and every thread only writes to one specific entry)
        List<Node<NodeData>> nodeList = Collections.synchronizedList(new ArrayList<>(size));
        for(int i = 0; i < size;i++) {
            nodeList.add(null);
        }

        try {
            // read files and create trees with label
            readFiles(nodeList, size, labelId);
            final long runtimeInNanos = System.nanoTime() - start;
            runtimeInS = TimeUnit.NANOSECONDS.toSeconds(runtimeInNanos);
            System.out.println("Runtime reading files in seconds: " + runtimeInS);

            // instantiate new threadpool for calculation
            ExecutorService pool = Executors.newWorkStealingPool();

            System.out.println("> Calculation started");
            long currentNoOfFinishedTasks = 0L;
            // compare each row file with fitting colFile
            for (int i = 0; i < noOfRows; i++) {
                for (int j = i + 1; j < noOfColsPerRow[i] + i + 1; j++) {
                    semaphore.acquire();

                    // Execute APTED.
                    Runnable myTask = new MyTask(i, j, resultsFinal, nodeList);
                    pool.execute(myTask);
                    currentNoOfFinishedTasks = noOfFinishedTasks.get();
                    if (currentNoOfFinishedTasks !=0 && currentNoOfFinishedTasks % 500000 == 0) {
                        System.out.println("Still waiting for results: " + new Date() + " | Number of finished Tasks: " + currentNoOfFinishedTasks );
                        System.out.println("maxMemory: " + (Runtime.getRuntime().maxMemory()/(1024.0*1024.0*1024.0)) + " freeMemory: " + (Runtime.getRuntime().freeMemory()/(1024*1024*1024)) + " usedMemory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024.0*1024.0*1024.0));
                    }
                }
            }

            pool.shutdown();

            while(!pool.awaitTermination(300L,TimeUnit.SECONDS)) {
                System.out.println("Still waiting for results: " + new Date());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final long runtimeInNanos = System.nanoTime() - start;
        runtimeInS = TimeUnit.NANOSECONDS.toSeconds(runtimeInNanos);
        System.out.println("Runtime overall calculation in seconds: " + runtimeInS);
    }

    private void compareFiles(@ParamInfo(name="Label", style="load-folder-dialog")int labelId) {

        long runtimeInS;
        final long start = System.nanoTime();

        int size = files.length;
        // results is global variable
        results = new float[size][size];
        // resultsFinal is a local reference to global variable results. it will always reference the original one no matter what happens
        final float[][] resultsFinal = results;

        // creating trees is also multithreaded. therefore nodeList must be synchronized. (really necessary? nodeList is initialized with null and every thread only writes to one specific entry)
        List<Node<NodeData>> nodeList = Collections.synchronizedList(new ArrayList<>(size));
        for(int i = 0; i < size;i++) {
            nodeList.add(null);
        }

        try {
            // read files and create trees with label
            readFiles(nodeList, size, labelId);
            final long runtimeInNanos = System.nanoTime() - start;
            runtimeInS = TimeUnit.NANOSECONDS.toSeconds(runtimeInNanos);
            System.out.println("Runtime reading files in seconds: " + runtimeInS);

            // instantiate new threadpool for calculation
            ExecutorService pool = Executors.newWorkStealingPool();

            System.out.println("> Calculation started");
            long currentNoOfFinishedTasks = 0L;
            // compare each two files
            for (int i = 0; i < size - 1; i++) {
                for (int j = i + 1; j < size; j++) {
                    semaphore.acquire();

                    // Execute APTED.
                    Runnable myTask = new MyTask(i, j, resultsFinal, nodeList);
                    pool.execute(myTask);
                    currentNoOfFinishedTasks = noOfFinishedTasks.get();
                    if (currentNoOfFinishedTasks !=0 && currentNoOfFinishedTasks % 50000 == 0) {
                        System.out.println("Still waiting for results: " + new Date() + " | Number of finished Tasks: " + currentNoOfFinishedTasks );
                    }
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

    private void readFiles(List<Node<NodeData>> nodeList, int size, int labelId) throws InterruptedException {
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
                    t1 = one.createTree(labelId);
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
    }

    static class MyTask implements Runnable {

        private final List<Node<NodeData>> nodeList;
        private int i;
        private int j;
        private float[][] results;

        MyTask(int i, int j, float[][] results, List<Node<NodeData>> nodeList) {
            this.i = i;
            this.j = j;
            this.results = results;
            this.nodeList = nodeList;
        }

        @Override
        public void run() {
            // Initialise APTED.
            APTED<TreeCostModel, NodeData> apted = new APTED<>(new TreeCostModel());
            float result = apted.computeEditDistance(nodeList.get(i), nodeList.get(j));

            // java arrays are threadsafe if indexes written to are different which is the case here
            results[i][j] = result;
            noOfFinishedTasks.getAndIncrement();
            semaphore.release();
        }
    }
}
