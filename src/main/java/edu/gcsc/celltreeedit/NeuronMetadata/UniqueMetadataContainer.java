package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.*;


/** TODO: maybe rename class?
 * Outer class provides logic for adding UniqueMetadata and makes them accessable
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
                (neuronMetadataR.getCellType() != null) ? new HashSet<>(neuronMetadataR.getCellType()): new HashSet<>(),
                (neuronMetadataR.getBrainRegion() != null) ? new HashSet<>(neuronMetadataR.getBrainRegion()): new HashSet<>(),
                neuronMetadataR.getSpecies(),
                neuronMetadataR.getNeuronName(),
                neuronMetadataR.getArchive());
        
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

    /**
     * Class to get unique metadata combinations. metadata is considered unique if cellTypes, brainRegion and species are equal
     * equals()-method implements this constraint
     * compareTo()-method is not consistent with equals() as it sorts by number of Neurons having this unique metadata combination
     * */
    private class UniqueMetadata implements Comparable {
        private Set<String> cellTypes;
        private Set<String> brainRegion;
        private String species;

        private List<String> neuronNames;
        private Set<String> archives;
        private int noOfNeurons;
        private int uniqueMetadataId;

        private UniqueMetadata(Set<String> cellTypes, Set<String> brainRegion, String species, String neuronName, String archive) {
            this.cellTypes = cellTypes;
            this.brainRegion = brainRegion;
            this.species = species;
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
                    Objects.equals(getBrainRegion(), that.getBrainRegion()) &&
                    Objects.equals(getSpecies(), that.getSpecies());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCellTypes(), getBrainRegion(), getSpecies());
        }

        @Override
        public int compareTo(Object o) {
            UniqueMetadata that = (UniqueMetadata) o;
            return Integer.compare(this.getNoOfNeurons(), that.getNoOfNeurons())*(-1);
        }

        public Set<String> getCellTypes() {
            return cellTypes;
        }

        public Set<String> getBrainRegion() {
            return brainRegion;
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
