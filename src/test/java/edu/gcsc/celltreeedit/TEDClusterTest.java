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
package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.TEDCalculation.CellTreeEditDistance;
import edu.gcsc.celltreeedit.TEDCalculation.TEDClusterResult;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TEDClusterTest {

    private static File tedClusterTestDirectory = new File(BaseDirectory.getTestDirectory().getPath() + "/TEDClusterTest/");
    private static File workingDirectory = BaseDirectory.getWorkingDirectory();

    @Test
    public void checkReassembledMatrix() throws IOException {
        File[] workingFiles = workingDirectory.listFiles();
        if (workingFiles != null) { //some JVMs return null for empty dirs
            for (File f : workingFiles) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    FileUtils.deleteDirectory(f);
                }
            }
        }

        // calculate DistanceMatrices for swcFiles_14.json and swcFiles_15.json
        CellTreeEditDistance cellTreeEditDistance = new CellTreeEditDistance();
        File[] files14 = JsonUtils.parseJsonToFiles(new File(tedClusterTestDirectory + "/swcFiles_14.json"));
        for (int i = 0; i < files14.length; i++) {
            files14[i] = new File(tedClusterTestDirectory + "/" + files14[i].getPath());
        }
        TEDResult result14 = cellTreeEditDistance.compareFilesFromFiles(files14, 18);
        cellTreeEditDistance = new CellTreeEditDistance();
        File[] files15 = JsonUtils.parseJsonToFiles(new File(tedClusterTestDirectory + "/swcFiles_15.json"));
        for (int i = 0; i < files15.length; i++) {
            files15[i] = new File(tedClusterTestDirectory + "/" + files15[i].getPath());
        }
        TEDResult result15 = cellTreeEditDistance.compareFilesFromFiles(files15, 18);

        for (int i = 0; i < files14.length; i++) {
            files14[i] = new File(files14[i].getName());
        }
        for (int i = 0; i < files15.length; i++) {
            files15[i] = new File(files15[i].getName());
        }

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        File matrixFiles14_01Directory = new File(workingDirectory.getPath() + "/Matrix14_01");
        matrixFiles14_01Directory.mkdir();
        for (int iteration = 1; iteration <= 5; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 3, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), matrixFiles14_01Directory, "Matrix14_01_" + iteration);
        }
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_01Directory, files14, workingDirectory, "ReassembledMatrix14_01");
        TEDResult reassembledResult14_01 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_01.txt"));
        checkResultEquality(result14, reassembledResult14_01);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        File matrixFiles14_02Directory = new File(workingDirectory.getPath() + "/Matrix14_02");
        matrixFiles14_02Directory.mkdir();
        for (int iteration = 1; iteration <= 14; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 1, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), matrixFiles14_02Directory, "Matrix14_02_" + iteration);
        }
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_02Directory, files14, workingDirectory, "ReassembledMatrix14_02");
        TEDResult reassembledResult14_02 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_02.txt"));
        checkResultEquality(result14, reassembledResult14_02);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        File matrixFiles14_03Directory = new File(workingDirectory.getPath() + "/Matrix14_03");
        matrixFiles14_03Directory.mkdir();
        for (int iteration = 1; iteration <= 1; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 14, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), matrixFiles14_03Directory, "Matrix14_03_" + iteration);
        }
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_03Directory, files14, workingDirectory, "ReassembledMatrix14_03");
        TEDResult reassembledResult14_03 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_03.txt"));
        checkResultEquality(result14, reassembledResult14_03);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        File matrixFiles14_04Directory = new File(workingDirectory.getPath() + "/Matrix14_04");
        matrixFiles14_04Directory.mkdir();
        for (int iteration = 1; iteration <= 7; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 2, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), matrixFiles14_04Directory, "Matrix14_04_" + iteration);
        }
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_04Directory, files14, workingDirectory, "ReassembledMatrix14_04");
        TEDResult reassembledResult14_04 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_04.txt"));
        checkResultEquality(result14, reassembledResult14_04);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        File matrixFiles15_01Directory = new File(workingDirectory.getPath() + "/Matrix15_01");
        matrixFiles15_01Directory.mkdir();
        for (int iteration = 1; iteration <= 5; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files15, 3, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_15.json"), matrixFiles15_01Directory, "Matrix15_01_" + iteration);
        }
        Utils.reassembleClusterMatrixToTxt(matrixFiles15_01Directory, files15, workingDirectory, "ReassembledMatrix15_01");
        TEDResult reassembledResult15_01 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix15_01.txt"));
        checkResultEquality(result15, reassembledResult15_01);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        File matrixFiles15_02Directory = new File(workingDirectory.getPath() + "/Matrix15_02");
        matrixFiles15_02Directory.mkdir();
        for (int iteration = 1; iteration <= 2; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files15, 11, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_15.json"), matrixFiles15_02Directory, "Matrix15_02_" + iteration);
        }
        Utils.reassembleClusterMatrixToTxt(matrixFiles15_02Directory, files15, workingDirectory, "ReassembledMatrix15_02");
        TEDResult reassembledResult15_02 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix15_02.txt"));
        checkResultEquality(result15, reassembledResult15_02);

        workingFiles = workingDirectory.listFiles();
        if (workingFiles != null) { //some JVMs return null for empty dirs
            for (File f : workingFiles) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    FileUtils.deleteDirectory(f);
                }
            }
        }
    }

    private void checkResultEquality(TEDResult tedResult1, TEDResult tedResult2) {
        float[][] matrix1 = tedResult1.getDistanceMatrix();
        float[][] matrix2 = tedResult2.getDistanceMatrix();
        String[] filenames1 = tedResult1.getFileNames();
        String[] filenames2 = tedResult2.getFileNames();
        assertEquals(matrix1.length, matrix2.length);
        assertEquals(matrix1[0].length, matrix2[0].length);
        assertEquals(filenames1.length, filenames2.length);
        int size = matrix1.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assertEquals(matrix1[i][j], matrix2[i][j], 0d);
            }
        }
    }
}
