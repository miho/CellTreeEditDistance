package com.apporiented.algorithm.clustering;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WardTest {

    private static float[][] distanceMatrix = new float[][]{
            {0.0000000f,0.8333607f,0.8471952f,0.4335730f,0.6040070f,0.6457721f,0.8158463f,0.9324100f,0.7002394f,0.7697922f,0.3832023f,0.7385950f,0.9104691f,0.6806722f,0.5530230f,0.7060232f,0.7806748f,0.4811467f,1.0543074f,1.0312414f},
            {0.8333607f,0.0000000f,0.9349784f,0.5617406f,0.7251291f,0.6669092f,0.7150473f,1.1283738f,0.5796361f,0.2664122f,1.1842056f,1.0238666f,0.8596584f,0.9697268f,0.5426308f,0.8283289f,0.6035924f,0.4251583f,0.6910158f,0.5428669f},
            {0.8471952f,0.9349784f,0.0000000f,0.8298117f,0.3589344f,0.7280070f,0.3089397f,0.6247395f,0.9884054f,1.0508882f,1.0733952f,0.5339310f,0.4706486f,1.0041705f,0.9041373f,0.7529083f,0.3833401f,0.7093129f,0.6287134f,0.8928541f},
            {0.4335730f,0.5617406f,0.8298117f,0.0000000f,0.5347728f,0.5416865f,0.6695029f,1.0815867f,0.6040087f,0.6157560f,0.8076615f,0.9303136f,0.8641996f,0.9566362f,0.3426582f,0.7662734f,0.6458395f,0.3030634f,0.9552274f,0.6369529f},
            {0.6040070f,0.7251291f,0.3589344f,0.5347728f,0.0000000f,0.3941484f,0.3785290f,0.6371315f,0.8676809f,0.8290206f,0.9067956f,0.5363400f,0.6499376f,0.8811544f,0.7146311f,0.5081763f,0.4034689f,0.4009520f,0.6224170f,0.7427165f},
            {0.6457721f,0.6669092f,0.7280070f,0.5416865f,0.3941484f,0.0000000f,0.7143346f,0.7237315f,0.9807866f,0.7302145f,0.9680031f,0.6882020f,0.9850156f,0.8625799f,0.8123592f,0.3083869f,0.6877911f,0.3376008f,0.6867560f,0.8155691f},
            {0.8158463f,0.7150473f,0.3089397f,0.6695029f,0.3785290f,0.7143346f,0.0000000f,0.8494834f,0.7736701f,0.8721382f,1.1088296f,0.7318844f,0.3508891f,1.0521348f,0.6857653f,0.8308755f,0.1573864f,0.5888679f,0.6103338f,0.6175606f},
            {0.9324100f,1.1283738f,0.6247395f,1.0815867f,0.6371315f,0.7237315f,0.8494834f,0.0000000f,1.2709967f,1.1385104f,1.0541236f,0.2422061f,0.9533462f,0.7433551f,1.2210426f,0.4927307f,0.8237029f,0.8516698f,0.6766628f,1.3124129f},
            {0.7002394f,0.5796361f,0.9884054f,0.6040087f,0.8676809f,0.9807866f,0.7736701f,1.2709967f,0.0000000f,0.5389099f,0.9443087f,1.0734749f,0.7179833f,0.9257427f,0.2901741f,1.0924303f,0.6753616f,0.6453234f,1.0199451f,0.8198550f},
            {0.7697922f,0.2664122f,1.0508882f,0.6157560f,0.8290206f,0.7302145f,0.8721382f,1.1385104f,0.5389099f,0.0000000f,1.0751716f,1.0153171f,0.9581947f,0.8083520f,0.5656115f,0.8257174f,0.7435230f,0.4724331f,0.8008330f,0.7928708f},
            {0.3832023f,1.1842056f,1.0733952f,0.8076615f,0.9067956f,0.9680031f,1.1088296f,1.0541236f,0.9443087f,1.0751716f,0.0000000f,0.8503205f,1.1254082f,0.7087456f,0.8641246f,0.9385024f,1.0783715f,0.8493488f,1.3354061f,1.4025768f},
            {0.7385950f,1.0238666f,0.5339310f,0.9303136f,0.5363400f,0.6882020f,0.7318844f,0.2422061f,1.0734749f,1.0153171f,0.8503205f,0.0000000f,0.7936035f,0.5949831f,1.0340350f,0.5006767f,0.6961796f,0.7283688f,0.6898096f,1.2229591f},
            {0.9104691f,0.8596584f,0.4706486f,0.8641996f,0.6499376f,0.9850156f,0.3508891f,0.9533462f,0.7179833f,0.9581947f,1.1254082f,0.7936035f,0.0000000f,1.0373259f,0.7485366f,1.0358990f,0.3306476f,0.7957141f,0.7494652f,0.8560543f},
            {0.6806722f,0.9697268f,1.0041705f,0.9566362f,0.8811544f,0.8625799f,1.0521348f,0.7433551f,0.9257427f,0.8083520f,0.7087456f,0.5949831f,1.0373259f,0.0000000f,0.9753047f,0.6825322f,0.9466313f,0.7695743f,0.9351805f,1.3764536f},
            {0.5530230f,0.5426308f,0.9041373f,0.3426582f,0.7146311f,0.8123592f,0.6857653f,1.2210426f,0.2901741f,0.5656115f,0.8641246f,1.0340350f,0.7485366f,0.9753047f,0.0000000f,0.9845246f,0.6283209f,0.4900173f,1.0132281f,0.6671061f},
            {0.7060232f,0.8283289f,0.7529083f,0.7662734f,0.5081763f,0.3083869f,0.8308755f,0.4927307f,1.0924303f,0.8257174f,0.9385024f,0.5006767f,1.0358990f,0.6825322f,0.9845246f,0.0000000f,0.7832806f,0.5184526f,0.6585511f,1.0703120f},
            {0.7806748f,0.6035924f,0.3833401f,0.6458395f,0.4034689f,0.6877911f,0.1573864f,0.8237029f,0.6753616f,0.7435230f,1.0783715f,0.6961796f,0.3306476f,0.9466313f,0.6283209f,0.7832806f,0.0000000f,0.5215267f,0.5222923f,0.6203367f},
            {0.4811467f,0.4251583f,0.7093129f,0.3030634f,0.4009520f,0.3376008f,0.5888679f,0.8516698f,0.6453234f,0.4724331f,0.8493488f,0.7283688f,0.7957141f,0.7695743f,0.4900173f,0.5184526f,0.5215267f,0.0000000f,0.6890130f,0.6620760f},
            {1.0543074f,0.6910158f,0.6287134f,0.9552274f,0.6224170f,0.6867560f,0.6103338f,0.6766628f,1.0199451f,0.8008330f,1.3354061f,0.6898096f,0.7494652f,0.9351805f,1.0132281f,0.6585511f,0.5222923f,0.6890130f,0.0000000f,0.8742235f},
            {1.0312414f,0.5428669f,0.8928541f,0.6369529f,0.7427165f,0.8155691f,0.6175606f,1.3124129f,0.8198550f,0.7928708f,1.4025768f,1.2229591f,0.8560543f,1.3764536f,0.6671061f,1.0703120f,0.6203367f,0.6620760f,0.8742235f,0.0000000f}
    };

    private static String[] fileNames = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private static List<Cluster> clusterList = new ArrayList<>();
    private static final List<Float> correctHeights = new ArrayList<>(Arrays.asList(1.8584163f, 1.5665053f, 1.2043397f, 0.8751259f,	0.8042245f,	0.7647439f,	0.7469914f,	0.7258152f,	0.6840459f,	0.5753823f,	0.3832023f,	0.3830281f,	0.3589344f,	0.3083869f,	0.3030634f,	0.2901741f,	0.2664122f,	0.2422061f,	0.1573864f));

    @Test
    public void clusteringWardTest() {

        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        Cluster rootCluster = alg.performClustering(distanceMatrix, fileNames,
                new WardLinkageStrategy());

        // check height of clusters from top to bottom
        checkClusterHeightRec(rootCluster);

        // morphology of cluster the ones without '-' before it are already agglomerated clusters
//                      [,1] [,2]
//                [1,]   -7  -17
//                [2,]   -8  -12
//                [3,]   -2  -10
//                [4,]   -9  -15
//                [5,]   -4  -18
//                [6,]   -6  -16
//                [7,]   -3   -5
//                [8,]  -13    1
//                [9,]   -1  -11
//                [10,]    7    8
//                [11,]    3    5
//                [12,]    4   11
//                [13,]  -19   10
//                [14,]  -14    2
//                [15,]  -20   12
//                [16,]    6   14
//                [17,]    9   16
//                [18,]   13   15
//                [19,]   17   18
    }

    private static void checkClusterHeightRec(Cluster rootCluster) {
        clusterList.add(rootCluster);
        addClusterToListRec(rootCluster);
        clusterList.sort(new Comparator<Cluster>() {
            @Override
            public int compare(Cluster cluster, Cluster t1) {
                return (t1.getDistance().compareTo(cluster.getDistance()));
            }
        });
        for (int i = 0; i < correctHeights.size(); i++) {
            assertEquals(clusterList.remove(0).getDistance().getDistance(), correctHeights.get(i), 1e-6);
        }

    }

    private static void addClusterToListRec(Cluster cluster) {
        clusterList.addAll(cluster.getChildren());
        for (Cluster cluster1 : cluster.getChildren()) {
            addClusterToListRec(cluster1);
        }
    }
}