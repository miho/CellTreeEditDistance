package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.JsonIO.JsonUtils;
import edu.gcsc.celltreeedit.TEDCalculation.TEDResult;
import edu.gcsc.celltreeedit.Utils;
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

        double[][] matrixToWrite = new double[][]{
                {0.0, 27760.734375, 25667.421875, 26355.29296875, 26340.6640625, 26353.046875, 24850.39453125, 28356.73046875, 28400.90625, 29496.58203125},
                {27760.734375, 0.0, 4262.7666015625, 5091.51953125, 5143.15625, 5309.9638671875, 6164.9072265625, 6063.5986328125, 5849.37451171875, 9376.83984375},
                {25667.421875, 4262.7666015625, 0.0, 2635.501220703125, 2852.80126953125, 3338.3427734375, 3373.124267578125, 6427.46875, 6265.37548828125, 9428.61328125},
                {26355.29296875, 5091.51953125, 2635.501220703125, 0.0, 1725.7205810546875, 2516.42333984375, 3627.61767578125, 7206.86083984375, 6736.67138671875, 10387.2421875},
                {26340.6640625, 5143.15625, 2852.80126953125, 1725.7205810546875, 0.0, 2527.0927734375, 3460.4794921875, 7135.541015625, 6629.53955078125, 10573.814453125},
                {26353.046875, 5309.9638671875, 3338.3427734375, 2516.42333984375, 2527.0927734375, 0.0, 2651.50732421875, 7458.63525390625, 6461.8203125, 10710.970703125},
                {24850.39453125, 6164.9072265625, 3373.124267578125, 3627.61767578125, 3460.4794921875, 2651.50732421875, 0.0, 7686.9033203125, 7286.6474609375, 10182.31640625},
                {28356.73046875, 6063.5986328125, 6427.46875, 7206.86083984375, 7135.541015625, 7458.63525390625, 7686.9033203125, 0.0, 5492.912109375, 8929.791015625},
                {28400.90625, 5849.37451171875, 6265.37548828125, 6736.67138671875, 6629.53955078125, 6461.8203125, 7286.6474609375, 5492.912109375, 0.0, 9126.326171875},
                {29496.58203125, 9376.83984375, 9428.61328125, 10387.2421875, 10573.814453125, 10710.970703125, 10182.31640625, 8929.791015625, 9126.326171875, 0.0}
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

        File outputDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/Test/TestWorkingDir/");
        String matrixName = "matrixTest";
        Utils.printMatrixToTxt(matrixToWrite, fileNamesToWrite, outputDirectory, matrixName);
        File savedFile = new File(outputDirectory.getPath() + "/" + matrixName + ".txt");
        TEDResult result = Utils.readMatrixFromTxt(savedFile);

        double[][] matrixRead = result.getDistanceMatrix();
        for (int i = 0; i < matrixToWrite.length; i++) {
            for (int j = 0; j < matrixToWrite.length; j++) {
                assertEquals(matrixToWrite[i][j], matrixRead[i][j], 0d);
                System.out.println("expected: " + matrixToWrite[i][j]);
                System.out.println("actual  : " + matrixRead[i][j]);
            }
        }

        String[] fileNamesRead = result.getFileNames();
        for (int i = 0; i < fileNamesToWrite.length; i++) {
            assertEquals(fileNamesToWrite[i], fileNamesRead[i]);
            System.out.println("expected: " + fileNamesToWrite[i]);
            System.out.println("actual  : " + fileNamesRead[i]);
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
                new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/kisvarday/CNG version/oi33lpy1-1.CNG.swc"),
                new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/markram/CNG version/C050800E2.CNG.swc"),
                new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/yuste/CNG version/JM090103-10-1.CNG.swc"),
                new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/yuste/CNG version/JM090103-10-2.CNG.swc"),
                new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/yuste/CNG version/JM072303.CNG.swc"),
                new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/yuste/CNG version/JM092903-20-1.CNG.swc")
        ));
        File swcFileDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/TestDirectory/");
        File outputDirectory = new File(BaseDirectory.baseDirectory.getPath() + "/Test/TestWorkingDir");
        String jsonName = "jsonTest";
        JsonUtils.writeToJSON(fileDirectoriesToWrite, "", swcFileDirectory, outputDirectory, jsonName);
        File savedFile = new File(BaseDirectory.baseDirectory.getPath() + "/Test/TestWorkingDir/jsonTest.json");
        File[] fileDirectoriesRead = JsonUtils.parseJsonToFiles(savedFile);

        assertEquals(fileDirectoriesToWrite.size(), fileDirectoriesRead.length);
        for (int i = 0; i < fileDirectoriesRead.length; i++) {
            System.out.println("expected: " + fileDirectoriesToWrite.get(i).getPath());
            System.out.println("actual:   " + BaseDirectory.baseDirectory.getPath() + "/TestDirectory/" + fileDirectoriesRead[i].getPath());
            assertEquals(fileDirectoriesToWrite.get(i).getPath(), BaseDirectory.baseDirectory.getPath() + "/TestDirectory/" + fileDirectoriesRead[i].getPath());
        }
        savedFile.delete();
    }
}
