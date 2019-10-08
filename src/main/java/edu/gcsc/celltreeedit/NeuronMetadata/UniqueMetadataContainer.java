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
package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.*;


/**
 * Outer class provides logic for adding UniqueMetadata and makes them accessible
 */
public class UniqueMetadataContainer {

    // stores all existing UniqueMetadata-Objects. used to check existence
    private Map<UniqueMetadata, UniqueMetadata> uniqueMetadataMap = new HashMap<>();
    private Map<String, UniqueMetadata> fileNameToUniqueMetadataMap = new HashMap<>();
    private int noOfUniqueMetadata = 0;

    public Map<UniqueMetadata, UniqueMetadata> getUniqueMetadataMap() {
        return this.uniqueMetadataMap;
    }
    public Map<String, UniqueMetadata> getFileNameToUniqueMetadataMap() {
        return this.fileNameToUniqueMetadataMap;
    }
    public int getNoOfUniqueMetadata() {
        return this.noOfUniqueMetadata;
    }

    public UniqueMetadata addNeuronMetadata(NeuronMetadataR neuronMetadataR) {
        UniqueMetadata oldUniqueMetadata;
        UniqueMetadata newUniqueMetadata;
        
        // create new UniqueMetadata object
        newUniqueMetadata = new UniqueMetadata(
                neuronMetadataR.getNeuronName(), neuronMetadataR.getArchive(), neuronMetadataR.getSpecies(), (neuronMetadataR.getBrainRegion() != null) ? new HashSet<>(neuronMetadataR.getBrainRegion()): new HashSet<>(), (neuronMetadataR.getCellType() != null) ? new HashSet<>(neuronMetadataR.getCellType()): new HashSet<>()
        );
        
        // check if newUniqueMetadata already exists
        if (this.uniqueMetadataMap.containsKey(newUniqueMetadata)) {
            // if already existing get already existing uniqueMetadata and add information
            oldUniqueMetadata = this.uniqueMetadataMap.get(newUniqueMetadata);
            oldUniqueMetadata.neuronNames.add(neuronMetadataR.getNeuronName());
            oldUniqueMetadata.archives.add(neuronMetadataR.getArchive());
            oldUniqueMetadata.noOfNeurons += 1;
            this.fileNameToUniqueMetadataMap.put(neuronMetadataR.getNeuronName(), oldUniqueMetadata);
            return oldUniqueMetadata;
        } else {
            // if not existing add newUniqueMetadata to Map
            this.uniqueMetadataMap.put(newUniqueMetadata, newUniqueMetadata);
            this.noOfUniqueMetadata += 1;
            newUniqueMetadata.uniqueMetadataId = noOfUniqueMetadata;
            this.fileNameToUniqueMetadataMap.put(neuronMetadataR.getNeuronName(), newUniqueMetadata);
            return newUniqueMetadata;
        }
    }

    public UniqueMetadata createUniqueMetadataObject(String neuronName, String archive, String species, Set<String> brainRegions, Set<String> cellTypes) {
        return new UniqueMetadata(neuronName, archive, species, brainRegions, cellTypes);
    }


    /**
     * Class to get unique metadata combinations. metadata is considered unique if species, brainRegions and cellTypes are equal
     * equals()-method implements this constraint
     * compareTo()-method is not consistent with equals() as it sorts by number of Neurons having this unique metadata combination
     * */
    public class UniqueMetadata implements Comparable {
        private String species;
        private Set<String> brainRegions;
        private Set<String> cellTypes;

        private List<String> neuronNames;
        private Set<String> archives;
        private int noOfNeurons;
        private int uniqueMetadataId;

        private UniqueMetadata(String neuronName, String archive, String species, Set<String> brainRegions, Set<String> cellTypes) {
            this.species = species;
            this.brainRegions = brainRegions;
            this.cellTypes = cellTypes;
            this.neuronNames = new ArrayList<>(Arrays.asList(neuronName));
            this.archives = new HashSet<>(Arrays.asList(archive));
            this.noOfNeurons = 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UniqueMetadata)) return false;
            UniqueMetadata that = (UniqueMetadata) o;
            return Objects.equals(getCellTypes(), that.getCellTypes()) &&
                    Objects.equals(getBrainRegions(), that.getBrainRegions()) &&
                    Objects.equals(getSpecies(), that.getSpecies());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCellTypes(), getBrainRegions(), getSpecies());
        }

        @Override
        public int compareTo(Object o) {
            UniqueMetadata that = (UniqueMetadata) o;
            return Integer.compare(this.getNoOfNeurons(), that.getNoOfNeurons())*(-1);
        }

        public Set<String> getCellTypes() {
            return cellTypes;
        }

        public Set<String> getBrainRegions() {
            return brainRegions;
        }

        public String getSpecies() {
            return species;
        }

        public List<String> getNeuronNames() {
            return neuronNames;
        }

        public int getNoOfNeurons() {
            return noOfNeurons;
        }

        public Set<String> getArchives() {
            return archives;
        }

        public int getUniqueMetadataId() {
            return uniqueMetadataId;
        }

    }
}
