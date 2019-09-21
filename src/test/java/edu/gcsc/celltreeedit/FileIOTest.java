package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileIOTest {

    // MatrixIOTest
    @Test
    public void matrixIOTest() throws IOException {

        float[][] matrixToWrite = new float[][]{
                {0.0f, 27760.734375f, 25667.421875f, 26355.29296875f, 26340.6640625f, 26353.046875f, 24850.39453125f, 28356.73046875f, 28400.90625f, 29496.58203125f},
                {27760.734375f, 0.0f, 4262.7666015625f, 5091.51953125f, 5143.15625f, 5309.9638671875f, 6164.9072265625f, 6063.5986328125f, 5849.37451171875f, 9376.83984375f},
                {25667.421875f, 4262.7666015625f, 0.0f, 2635.501220703125f, 2852.80126953125f, 3338.3427734375f, 3373.124267578125f, 6427.46875f, 6265.37548828125f, 9428.61328125f},
                {26355.29296875f, 5091.51953125f, 2635.501220703125f, 0.0f, 1725.7205810546875f, 2516.42333984375f, 3627.61767578125f, 7206.86083984375f, 6736.67138671875f, 10387.2421875f},
                {26340.6640625f, 5143.15625f, 2852.80126953125f, 1725.7205810546875f, 0.0f, 2527.0927734375f, 3460.4794921875f, 7135.541015625f, 6629.53955078125f, 10573.814453125f},
                {26353.046875f, 5309.9638671875f, 3338.3427734375f, 2516.42333984375f, 2527.0927734375f, 0.0f, 2651.50732421875f, 7458.63525390625f, 6461.8203125f, 10710.970703125f},
                {24850.39453125f, 6164.9072265625f, 3373.124267578125f, 3627.61767578125f, 3460.4794921875f, 2651.50732421875f, 0.0f, 7686.9033203125f, 7286.6474609375f, 10182.31640625f},
                {28356.73046875f, 6063.5986328125f, 6427.46875f, 7206.86083984375f, 7135.541015625f, 7458.63525390625f, 7686.9033203125f, 0.0f, 5492.912109375f, 8929.791015625f},
                {28400.90625f, 5849.37451171875f, 6265.37548828125f, 6736.67138671875f, 6629.53955078125f, 6461.8203125f, 7286.6474609375f, 5492.912109375f, 0.0f, 9126.326171875f},
                {29496.58203125f, 9376.83984375f, 9428.61328125f, 10387.2421875f, 10573.814453125f, 10710.970703125f, 10182.31640625f, 8929.791015625f, 9126.326171875f, 0.0f}
        };

        String[] fileNamesToWrite = new String[]{
                "cell1-5b-CA1",
                "cell15-CA1",
                "GranuleCell-Nr1-Septal",
                "GranuleCell-Nr10-Septal",
                "GranuleCell-Nr11-Septal",
                "GranuleCell-Nr12-Septal",
                "GranuleCell-Nr2",
                "ri04",
                "ri05",
                "ri06"
        };

        File workingDirectory = BaseDirectory.getWorkingDirectory();
        String matrixName = "matrixTest";
        Utils.printMatrixToTxt(matrixToWrite, fileNamesToWrite, workingDirectory, matrixName);
        File savedFile = new File(workingDirectory.getPath() + "/" + matrixName + ".txt");
        TEDResult result = Utils.readMatrixFromTxt(savedFile);

        float[][] matrixRead = result.getDistanceMatrix();
        for (int i = 0; i < matrixToWrite.length; i++) {
            for (int j = 0; j < matrixToWrite.length; j++) {
                assertEquals(matrixToWrite[i][j], matrixRead[i][j], 0d);
            }
        }

        String[] fileNamesRead = result.getFileNames();
        for (int i = 0; i < fileNamesToWrite.length; i++) {
            assertEquals(fileNamesToWrite[i], fileNamesRead[i]);
        }

        assertEquals(matrixToWrite.length, matrixRead.length);
        assertEquals(fileNamesToWrite.length, fileNamesRead.length);
        assertEquals(matrixRead.length, fileNamesRead.length);
        savedFile.delete();
    }


    // JsonIOTest
    @Test
    public void jsonIOTest() throws IOException  {
        List<File> fileDirectoriesToWrite = new ArrayList<>(Arrays.asList(
                new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/kisvarday/CNG version/oi33lpy1-1.CNG.swc"),
                new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/markram/CNG version/C050800E2.CNG.swc"),
                new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/yuste/CNG version/JM090103-10-1.CNG.swc"),
                new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/yuste/CNG version/JM090103-10-2.CNG.swc"),
                new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/yuste/CNG version/JM072303.CNG.swc"),
                new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/yuste/CNG version/JM092903-20-1.CNG.swc")
        ));
        File swcFileDirectory = new File(BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/");
        File workingDirectory = BaseDirectory.getWorkingDirectory();
        String jsonName = "jsonTest";
        JsonUtils.writeToJSON(fileDirectoriesToWrite, "", swcFileDirectory, workingDirectory, jsonName);
        File savedFile = new File(BaseDirectory.getWorkingDirectory().getPath() + "/jsonTest.json");
        File[] fileDirectoriesRead = JsonUtils.parseJsonToFiles(savedFile);

        assertEquals(fileDirectoriesToWrite.size(), fileDirectoriesRead.length);
        for (int i = 0; i < fileDirectoriesRead.length; i++) {
            assertEquals(fileDirectoriesToWrite.get(i).getPath(), BaseDirectory.getTestDirectory().getPath() + "/TestDirectory/" + fileDirectoriesRead[i].getPath());
        }
        savedFile.delete();
    }
}
