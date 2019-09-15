package edu.gcsc.celltreeedit.TEDCalculation;

public class TEDResult {

    private double[][] distanceMatrix;
    private String[] fileNames;
    private String name;

    public TEDResult(double[][] distanceMatrix, String[] fileNames, String name) {
        this.distanceMatrix = distanceMatrix;
        this.fileNames = fileNames;
        this.name = name;
    }

    public TEDResult(double[][] distanceMatrix, String[] fileNames) {
        this.distanceMatrix = distanceMatrix;
        this.fileNames = fileNames;
        this.name = "";
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
