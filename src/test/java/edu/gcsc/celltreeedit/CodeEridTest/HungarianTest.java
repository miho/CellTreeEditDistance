package edu.gcsc.celltreeedit.CodeEridTest;

import edu.gcsc.celltreeedit.ClusterAnalysis.HungarianAlgorithm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HungarianTest {

    @Test
    public void hungarianTest() {
        int length = partitioningErrorMatrix.length;
        int[][] partitioningErrorMatrixCopy = new int[length][];
        for (int i = 0; i < length; i++) {
            partitioningErrorMatrixCopy[i] = partitioningErrorMatrix[i].clone();
        }
        HungarianAlgorithm hungarian = new HungarianAlgorithm(partitioningErrorMatrixCopy);
        int[][] optimalAssignment = hungarian.findOptimalAssignment();

        int minPartitioningError = 0;
        for (int i = 0; i < optimalAssignment.length; i++) {
            int col = optimalAssignment[i][1];
            int partitioningError = partitioningErrorMatrix[i][col];
            minPartitioningError = minPartitioningError + partitioningError;
        }
        assertEquals(22, minPartitioningError);

        assertEquals(4, partitioningErrorMatrix[0][optimalAssignment[0][1]]);
        assertEquals(2, partitioningErrorMatrix[1][optimalAssignment[1][1]]);
        assertEquals(6, partitioningErrorMatrix[2][optimalAssignment[2][1]]);
        assertEquals(5, partitioningErrorMatrix[3][optimalAssignment[3][1]]);
        assertEquals(5, partitioningErrorMatrix[4][optimalAssignment[4][1]]);

    }

    private int[][] partitioningErrorMatrix = new int[][]{
            {0, 4, 4, 3, 8},
            {2, 10, 10, 10, 10},
            {8, 7, 6, 9, 7},
            {5, 5, 5, 5, 5},
            {4, 8, 10, 5, 10}
    };
}
