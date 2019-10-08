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

import eu.mihosoft.vswcreader.SWCSegment;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Erid on 23.04.2018.
 */
public class SWCSegmentTest {

    private int[] index = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134};
    private int[] type = {1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    private double[] x = {0, -4.11, 4.11, -0.99, -5.06, -6.99, -6.94, -9.41, -12.21, -13.49, -13.57, -15.12, -15.21, -16.02, -16.11, -21.48, -23.85, -25.02, -26.34, -27.78, -29.31, -31.1, -32.3, -35.87, -37.87, -37.87, -39.68, -41, -41.09, -42.23, -46.65, -46.65, -49.56, -50.6, -53.08, -57.09, -61.51, -63.57, -66.03, -67.9, -69.73, -71.61, -72.79, -77.01, -79.06, -82.13, -84.97, -73.45, -73.61, -74.15, -75.73, -70.16, -72.45, -74.79, -44.42, -46.66, -49.27, -50.53, -50.5, -50.64, -50.11, -50.26, -34.96, -36.83, -39.05, -40.07, -41.2, -42.21, -44.18, -44.18, -44.82, -45.73, -46.65, -46.65, -48.4, -49.18, -50.21, -51.88, -54.07, -54.07, -55.88, -57.59, -58.78, -54.02, -55.14, -55.1, -56.26, -56.22, -22.26, -22.34, -23.68, -23.84, -25.05, -27.09, -27.18, -28.15, -28.43, -29.11, -29.76, -29.81, -31.37, -31.82, -32.26, -32.34, -34.04, -35.19, -35.75, -35.75, -36.74, -37.41, -38.82, -39.54, -41, -41.84, -42.45, -42.82, -43.33, -43.38, -43.88, -43.88, -44.8, -45.36, -46.21, -46.27, -47.25, -48.93, -48.93, -43.78, -43.54, -40.42, -40.38, -40.92, -40.86, -40.82};
    private double[] y = {0, 2.11, -2.11, 5.29, 12.86, 18.15, 18.23, 21.08, 23.24, 23.89, 23.93, 26.01, 26.05, 27.38, 27.43, 30.67, 31.73, 31.43, 30.6, 30.13, 29.72, 29.93, 29.75, 28.29, 28.31, 28.31, 28.14, 26.9, 26.95, 27.06, 26.51, 26.51, 26.71, 26.84, 25.91, 25.35, 24.32, 22.68, 21.03, 19.9, 20.64, 20.5, 20.2, 16.86, 16.2, 15.5, 15.44, 22.44, 22.52, 23.41, 23.31, 17.85, 15.33, 14.72, 28.86, 30.21, 32.75, 35.4, 35.47, 37.15, 39.58, 39.66, 32.21, 33.28, 35.21, 36.02, 36.72, 38.12, 39.74, 39.74, 40.86, 41.24, 42.4, 42.4, 44.3, 45.1, 46.44, 48.29, 48.72, 48.72, 49.14, 49.52, 49.51, 50.59, 51.36, 51.44, 52.12, 52.01, 33.88, 33.92, 35.61, 35.69, 37.09, 39.95, 39.99, 42, 43.04, 43.88, 45.72, 46.84, 51.04, 53.27, 54.8, 54.84, 57, 59.3, 61.31, 61.31, 62.93, 64.37, 67.09, 68.65, 70.11, 72.22, 74.34, 76.35, 78.89, 79.01, 80.38, 80.38, 80.55, 82.24, 83.68, 83.8, 85.61, 89.38, 89.38, 83.12, 84.8, 73.6, 76.59, 79.27, 83.52, 83.6};
    private double[] z = {0, 0, 0, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.33, 0.34, 0.49, 0.49, 0.43, 0.37, 0.1, -2.57, -2.57, -2.57, -2.57, -2.57, -2.57, -2.57, -1.82, -1.95, -2.05, -2.67, -3.27, -3.29, -3.71, -4.11, -4, -3.27, -3.21, -3.21, -4.53, -3.5, -3.5, -3.5, -3.5, -2.44, -2.65, -2.34, -4.8, -4.8, -4.8, -4.76, -2.98, -2.98, -3.46, -3.79, -2.77, -5.44, -5.34, -3.8, -3.8, -3.88, -4.46, -4.59, -4.86, -5.15, -5.11, -2.54, -2.54, -2.4, -2.4, -2.4, -2.4, -3.05, -2.96, -2.73, -2.65, -2.77, -2.9, -3.69, -4.4, -4.71, -4.76, -4.44, -4.23, -3.84, -3.54, -3.44, -4.32, -4.4, -4.48, -4.59, -4.65, -2.03, -2.03, -2.03, -2.03, -2.13, -2.38, -2.38, -1.74, -1.7, -1.59, -1.11, -3.4, -2.9, -3.25, -3.44, -3.5, -3.57, -3.57, -2.65, -2.75, -3, -3.21, -3.75, -4.4, -3.94, -3.82, -3.82, -3.82, -3.79, -3.75, -4.19, -4.36, -4.36, -4.36, -4.36, -4.36, -4.36, -5.21, -4.55, -3.38, -3.38, -4.15, -4.26, -4.3, -4.3, -4.3};
    private double[] r = {4.63719, 4.63719, 4.63719, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18, 0.18};
    private int[] parent = {-1, 1, 1, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 42, 48, 49, 50, 40, 52, 53, 30, 55, 56, 57, 58, 59, 60, 61, 23, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 78, 84, 85, 86, 87, 16, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 120, 128, 113, 130, 131, 132, 133};


    @Test
    public void readFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(BaseDirectory.getTestDirectory().getPath() + "/SWCSegmentTest/testSegments.swc"));
        List<SWCSegment> swcSegments = SWCSegment.fromStream(fileInputStream);
        assertEquals(134, swcSegments.size());
        for (int i = 0; i < swcSegments.size(); i++) {
            assertEquals(index[i], swcSegments.get(i).getIndex());
            assertEquals(type[i], swcSegments.get(i).getType());
            assertEquals(x[i], swcSegments.get(i).getPos().getX(), 0d);
            assertEquals(y[i], swcSegments.get(i).getPos().getY(), 0d);
            assertEquals(z[i], swcSegments.get(i).getPos().getZ(), 0d);
            assertEquals(r[i], swcSegments.get(i).getR(), 0d);
            assertEquals(parent[i], swcSegments.get(i).getParent());
        }
    }
}
