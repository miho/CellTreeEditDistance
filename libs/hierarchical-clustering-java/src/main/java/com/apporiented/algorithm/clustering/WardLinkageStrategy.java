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

public class WardLinkageStrategy implements LinkageStrategy {

	@Override
	public Distance calculateDistance(Distance[] distances, Integer[] noOfLeafs) {
		float sum = 0;
		float result;
		float noOfLeafsSum = noOfLeafs[0] + noOfLeafs[1] + noOfLeafs[2];

		result = (float)Math.sqrt(
				(noOfLeafs[0] + noOfLeafs[2]) / noOfLeafsSum * distances[0].getDistance() * distances[0].getDistance()
				+ (noOfLeafs[1] + noOfLeafs[2]) / noOfLeafsSum * distances[1].getDistance() * distances[1].getDistance()
				- noOfLeafs[2] / noOfLeafsSum * distances[2].getDistance() * distances[2].getDistance()
		);
		return new Distance(result);
	}
}
