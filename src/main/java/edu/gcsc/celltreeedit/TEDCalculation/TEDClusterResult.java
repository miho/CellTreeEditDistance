package edu.gcsc.celltreeedit.TEDCalculation;

public class TEDClusterResult {
    int iteration;
    int row;
    int col;
    float[][] matrix;


    public TEDClusterResult(int iteration, int row, int col, float[][] matrix) {
        this.iteration = iteration;
        this.row = row;
        this.col = col;
        this.matrix = matrix;
    }

    public int getIteration() {
        return iteration;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public float[][] getMatrix() {
        return matrix;
    }
}
