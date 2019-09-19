package edu.gcsc.celltreeedit.TEDCalculation;

public class TEDClusterResult {
    int iteration;
    int row;
    int col;
    double[][] matrix;


    public TEDClusterResult(int iteration, int row, int col, double[][] matrix) {
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

    public double[][] getMatrix() {
        return matrix;
    }
}
