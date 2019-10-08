/*******************************************************************************
 * Copyright 2013 Lars Behnke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.apporiented.algorithm.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HierarchyBuilder {

    private DistanceMap distances;
    private List<Cluster> clusters;

    public DistanceMap getDistances() {
        return distances;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public HierarchyBuilder(List<Cluster> clusters, DistanceMap distances) {
        this.clusters = clusters;
        this.distances = distances;
    }

    /**
     * Returns Flattened clusters, i.e. clusters that are at least apart by a given threshold
     * @param linkageStrategy
     * @param threshold
     * @return
     */
    public List<Cluster> flatAgg(LinkageStrategy linkageStrategy, Float threshold)
    {
        while((!isTreeComplete()) && (distances.minDist() != null) && (distances.minDist() <= threshold))
        {
            //System.out.println("Cluster Distances: " + distances.toString());
            //System.out.println("Cluster Size: " + clusters.size());
            agglomerate(linkageStrategy);
        }

        //System.out.println("Final MinDistance: " + distances.minDist());
        //System.out.println("Tree complete: " + isTreeComplete());
        return clusters;
    }

    public void agglomerate(LinkageStrategy linkageStrategy) {
        ClusterPair minDistLink = distances.removeFirst();
        if (minDistLink != null) {
            clusters.remove(minDistLink.getrCluster());
            clusters.remove(minDistLink.getlCluster());

            Cluster oldClusterL = minDistLink.getlCluster();
            Cluster oldClusterR = minDistLink.getrCluster();
            Cluster newCluster = minDistLink.agglomerate(null);
            Integer[] numberOfLeafsValues = new Integer[3];
            numberOfLeafsValues[0] = oldClusterL.getLeafNames().size();
            numberOfLeafsValues[1] = oldClusterR.getLeafNames().size();

            for (Cluster iClust : clusters) {
                ClusterPair link1 = findByClusters(iClust, oldClusterL);
                ClusterPair link2 = findByClusters(iClust, oldClusterR);
                ClusterPair newLinkage = new ClusterPair();
                newLinkage.setlCluster(iClust);
                newLinkage.setrCluster(newCluster);
                Distance[] distanceValues = new Distance[3];
                numberOfLeafsValues[2] = iClust.getLeafNames().size();

                if (link1 != null) {
					Float distVal = link1.getLinkageDistance();
                    Float weightVal = link1.getOtherCluster(iClust).getWeightValue();
                    distanceValues[0] = new Distance(distVal, weightVal);
                    distances.remove(link1);
                }
                if (link2 != null) {
					Float distVal = link2.getLinkageDistance();
					Float weightVal = link2.getOtherCluster(iClust).getWeightValue();
                    distanceValues[1] = new Distance(distVal, weightVal);
                    distances.remove(link2);
                }

                distanceValues[2] = new Distance(minDistLink.getLinkageDistance(), 0f);

                Distance newDistance = linkageStrategy.calculateDistance(distanceValues, numberOfLeafsValues);

				newLinkage.setLinkageDistance(newDistance.getDistance());
                distances.add(newLinkage);

            }
            clusters.add(newCluster);
        }
    }

    private ClusterPair findByClusters(Cluster c1, Cluster c2) {
        return distances.findByCodePair(c1, c2);
    }

    public boolean isTreeComplete() {
        return clusters.size() == 1;
    }

    public Cluster getRootCluster() {
        if (!isTreeComplete()) {
            throw new RuntimeException("No root available");
        }
        return clusters.get(0);
    }

}
