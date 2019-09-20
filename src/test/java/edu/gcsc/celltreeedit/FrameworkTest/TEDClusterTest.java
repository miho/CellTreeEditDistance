package edu.gcsc.celltreeedit.FrameworkTest;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.TEDCalculation.CellTreeEditDistance;
import edu.gcsc.celltreeedit.TEDCalculation.TEDClusterResult;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
import edu.gcsc.celltreeedit.Utils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TEDClusterTest {

    private static File tedClusterTestDirectory = new File(BaseDirectory.getTestDirectory().getPath() + "/TEDClusterTest");
    private static File workingDirectory = BaseDirectory.getWorkingDirectory();

    @Test
    public void checkReassembledMatrix() throws IOException {
        File[] workingFiles = BaseDirectory.getWorkingDirectory().listFiles();
        if(workingFiles!=null) { //some JVMs return null for empty dirs
            for(File f: workingFiles) {
                f.delete();
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
        for (int iteration = 1; iteration <= 5; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 3, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), workingDirectory, "Matrix14_01_" + iteration);
        }
        File[] matrixFiles14_01 = workingDirectory.listFiles((dir, name) -> {
            if (name.startsWith("Matrix14_01_")) {
                return true;
            } else {
                return false;
            }
        });
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_01, files14, workingDirectory, "ReassembledMatrix14_01");
        TEDResult reassembledResult14_01 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_01.txt"));
        checkResultEquality(result14, reassembledResult14_01);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        for (int iteration = 1; iteration <= 14; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 1, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), workingDirectory, "Matrix14_02_" + iteration);
        }
        File[] matrixFiles14_02 = workingDirectory.listFiles((dir, name) -> {
            if (name.startsWith("Matrix14_02_")) {
                return true;
            } else {
                return false;
            }
        });
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_02, files14, workingDirectory, "ReassembledMatrix14_02");
        TEDResult reassembledResult14_02 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_02.txt"));
        checkResultEquality(result14, reassembledResult14_02);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        for (int iteration = 1; iteration <= 1; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 14, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), workingDirectory, "Matrix14_03_" + iteration);
        }
        File[] matrixFiles14_03 = workingDirectory.listFiles((dir, name) -> {
            if (name.startsWith("Matrix14_03_")) {
                return true;
            } else {
                return false;
            }
        });
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_03, files14, workingDirectory, "ReassembledMatrix14_03");
        TEDResult reassembledResult14_03 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_03.txt"));
        checkResultEquality(result14, reassembledResult14_03);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        for (int iteration = 1; iteration <= 7; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files14, 2, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_14.json"), workingDirectory, "Matrix14_04_" + iteration);
        }
        File[] matrixFiles14_04 = workingDirectory.listFiles((dir, name) -> {
            if (name.startsWith("Matrix14_04_")) {
                return true;
            } else {
                return false;
            }
        });
        Utils.reassembleClusterMatrixToTxt(matrixFiles14_04, files14, workingDirectory, "ReassembledMatrix14_04");
        TEDResult reassembledResult14_04 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix14_04.txt"));
        checkResultEquality(result14, reassembledResult14_04);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        for (int iteration = 1; iteration <= 5; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files15, 3, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_15.json"), workingDirectory, "Matrix15_01_" + iteration);
        }
        File[] matrixFiles15_01 = workingDirectory.listFiles((dir, name) -> {
            if (name.startsWith("Matrix15_01_")) {
                return true;
            } else {
                return false;
            }
        });
        Utils.reassembleClusterMatrixToTxt(matrixFiles15_01, files15, workingDirectory, "ReassembledMatrix15_01");
        TEDResult reassembledResult15_01 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix15_01.txt"));
        checkResultEquality(result15, reassembledResult15_01);

        // ------------------------------------------------------
        // calculate DistanceMatrices with Cluster calculation
        for (int iteration = 1; iteration <= 2; iteration++) {
            cellTreeEditDistance = new CellTreeEditDistance();
            TEDClusterResult tedClusterResult = cellTreeEditDistance.compareFilesForCluster(files15, 11, iteration, 18, tedClusterTestDirectory);
            Utils.printClusterMatrixToTxt(tedClusterResult, new File("swcFiles_15.json"), workingDirectory, "Matrix15_02_" + iteration);
        }
        File[] matrixFiles15_02 = workingDirectory.listFiles((dir, name) -> {
            if (name.startsWith("Matrix15_02_")) {
                return true;
            } else {
                return false;
            }
        });
        Utils.reassembleClusterMatrixToTxt(matrixFiles15_02, files15, workingDirectory, "ReassembledMatrix15_02");
        TEDResult reassembledResult15_02 = Utils.readMatrixFromTxt(new File(workingDirectory + "/ReassembledMatrix15_02.txt"));
        checkResultEquality(result15, reassembledResult15_02);

        workingFiles = workingDirectory.listFiles();
        if(workingFiles!=null) { //some JVMs return null for empty dirs
            for(File f: workingFiles) {
                f.delete();
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
