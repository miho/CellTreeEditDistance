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
