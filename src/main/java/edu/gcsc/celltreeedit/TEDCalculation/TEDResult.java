package edu.gcsc.celltreeedit.TEDCalculation;

public class TEDResult {

    private float[][] distanceMatrix;
    private String[] fileNames;
    private String name;

    public TEDResult(float[][] distanceMatrix, String[] fileNames, String name) {
        this.distanceMatrix = distanceMatrix;
        this.fileNames = fileNames;
        this.name = name;
    }

    public TEDResult(float[][] distanceMatrix, String[] fileNames) {
        this.distanceMatrix = distanceMatrix;
        this.fileNames = fileNames;
        this.name = "";
    }

    public float[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(float[][] distanceMatrix) {
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
