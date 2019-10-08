/*******************************************************************************
 * Copyright 2015 Lars Behnke
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

public class Distance implements Comparable<Distance>, Cloneable {

    private Float distance;
	private Float weight;

    public Distance() {
		this(0.0f);
    }

    public Distance(Float distance) {
		this(distance, 1.0f);
    }

    public Distance(Float distance, Float weight) {
        this.distance = distance;
        this.weight = weight;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public boolean isNaN() {
        return distance == null || distance.isNaN();
    }

    @Override
    public int compareTo(Distance distance) {
        return distance == null ? 1 : getDistance().compareTo(distance.getDistance());
    }

	@Override
	public String toString() {
		return String.format("distance : %.2f, weight : %.2f", distance, weight);
	}
}
