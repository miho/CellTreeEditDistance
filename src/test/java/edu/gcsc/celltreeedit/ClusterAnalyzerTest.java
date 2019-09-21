package edu.gcsc.celltreeedit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClusterAnalyzerTest {

    // TODO: fails because order of result matrix is not correct. needs to be changed anyhow.
//    @Test
//    public void clusterAnalyzerTest() throws IOException {
//        TEDResult result = new TEDResult(distanceMatrix, fileNames);
//
//        // put metadata in hashMap
//        NeuronMetadataMapper neuronMetadataMapper = new NeuronMetadataMapper();
//        Map<String, NeuronMetadataR> neuronMetadata = neuronMetadataMapper.mapAllFromMetadataDirectory(new File("/media/exdisk/Sem06/BA/ProgramData/Data/Metadata"));
//
//        Pair<double[][], List<UniqueMetadataContainer.UniqueMetadata>> analyzerResult = ClusteringAnalyzer.analyzeClusteringOfTEDResult(result, neuronMetadata);
//        double[][] relPartitioningErrors = analyzerResult.getKey();
//        List<UniqueMetadataContainer.UniqueMetadata> uniqueMetadataObjects = analyzerResult.getValue();
//
//        // check all relPartitioningErrors not lower left for 0
//        for (int i = 0; i < relPartitioningErrors.length; i++) {
//            for (int j = i + 1; j < relPartitioningErrors.length; j++) {
//                assertEquals(0d, relPartitioningErrors[i][j], 0d);
//            }
//        }
//
//        // get uniqueMetadataId of the three categories --> relPartitioningErrors is sorted after these
//        int uniqueMetadata1 = 0;
//        int uniqueMetadata2 = 0;
//        int uniqueMetadata3 = 0;
//        for (UniqueMetadataContainer.UniqueMetadata uniqueMetadataObject : uniqueMetadataObjects) {
//            if (uniqueMetadataObject.getNeuronNames().contains("Chat-IRES-Cre-neo_Ai14-280699-05-02-01_570681325_m")) {
//                uniqueMetadata1 = uniqueMetadataObject.getUniqueMetadataId();
//            } else if (uniqueMetadataObject.getNeuronNames().contains("1CL-IVxAnk2-IR_ddaC")) {
//                uniqueMetadata2 = uniqueMetadataObject.getUniqueMetadataId();
//            } else if (uniqueMetadataObject.getNeuronNames().contains("cell-2-trace")) {
//                uniqueMetadata3 = uniqueMetadataObject.getUniqueMetadataId();
//            } else {
//                assertTrue(false);
//            }
//        }
//        double result00 = 0;
//        double result10 = 0;
//        double result11 = 0;
//        if (uniqueMetadata1 == 1 && uniqueMetadata2 == 2 && uniqueMetadata3 == 3) {
//            result00 = 1d / 4d;
//            result10 = 1d / 3d;
//            result11 = 1d / 8d;
//        } else if (uniqueMetadata1 == 1 && uniqueMetadata2 == 3 && uniqueMetadata3 == 2) {
//            result00 = 1d / 3d;
//            result10 = 1d / 4d;
//            result11 = 1d / 8d;
//        } else if (uniqueMetadata1 == 2 && uniqueMetadata2 == 1 && uniqueMetadata3 == 3) {
//            result00 = 1d / 4d;
//            result10 = 1d / 8d;
//            result11 = 1d / 3d;
//        } else if (uniqueMetadata1 == 2 && uniqueMetadata2 == 3 && uniqueMetadata3 == 1) {
//            result00 = 1d / 8d;
//            result10 = 1d / 4d;
//            result11 = 1d / 3d;
//        } else if (uniqueMetadata1 == 3 && uniqueMetadata2 == 1 && uniqueMetadata3 == 2) {
//            result00 = 1d / 3d;
//            result10 = 1d / 8d;
//            result11 = 1d / 4d;
//        } else if (uniqueMetadata1 == 3 && uniqueMetadata2 == 2 && uniqueMetadata3 == 1) {
//            result00 = 1d / 8d;
//            result10 = 1d / 3d;
//            result11 = 1d / 4d;
//        } else {
//            assertTrue(false);
//        }
//        System.out.println(relPartitioningErrors[0][0]);
//        System.out.println(relPartitioningErrors[1][0]);
//        System.out.println(relPartitioningErrors[1][1]);
//        // check all relPartitioningErrors lower left entries
//        assertEquals(result00, relPartitioningErrors[0][0], 0d);
//        assertEquals(result10, relPartitioningErrors[1][0], 0d);
//        assertEquals(result11, relPartitioningErrors[1][1], 0d);
//        // check entries relating to uniqueMetadata1
//    }

    private float[][] distanceMatrix = new float[][]{
            {0f, 1f, 9f, 9f, 1f, 9f, 9f, 9f, 1f},
            {1f, 0f, 9f, 9f, 1f, 9f, 9f, 9f, 1f},
            {9f, 9f, 0f, 1f, 9f, 1f, 9f, 9f, 9f},
            {9f, 9f, 1f, 0f, 9f, 1f, 9f, 9f, 9f},
            {1f, 1f, 9f, 9f, 0f, 9f, 9f, 9f, 1f},
            {9f, 9f, 1f, 1f, 9f, 0f, 9f, 9f, 9f},
            {9f, 9f, 9f, 9f, 9f, 9f, 0f, 1f, 9f},
            {9f, 9f, 9f, 9f, 9f, 9f, 1f, 0f, 9f},
            {1f, 1f, 9f, 9f, 1f, 9f, 9f, 9f, 0f}
    };

    private String[] fileNames = new String[]{
            "Chat-IRES-Cre-neo_Ai14-280699-05-02-01_570681325_m",
            "1CL-IVxAnk2-IR_ddaC",
            "1CL-IVxRhoGAP18B-IR_ddaC",
            "1CL-IVxRpL24_ddaC",
            "cell-2-trace",
            "cell-6-trace",
            "cell-7-trace",
            "cell-8-trace",
            "cell-9-trace"
    };
}
