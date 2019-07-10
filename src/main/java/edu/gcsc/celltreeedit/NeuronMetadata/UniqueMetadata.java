package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.*;


/**
 * Class to get unique metadata combinations. metadata is considered unique if cellTypes, brainRegions and species are equal
 * equals()-method implements this constraint
 * compareTo()-method is not consistent with equals() as it sorts by number of Neurons having this unique metadata combination
 * */
public class UniqueMetadata implements Comparable {

    // stores all existing UniqueMetadata-Objects. used to check existence
    public static Map<UniqueMetadata, UniqueMetadata> uniqueMetadataMap = new HashMap<>();
    private static int count = 0;

    private Set<String> cellTypes;
    private Set<String> brainRegion;
    private String species;

    private List<String> neuronNames;
    private Set<String> archives;
    private int noOfNeurons;
    private int uniqueMetadataId;

    public UniqueMetadata(Set<String> cellTypes, Set<String> brainRegion, String species, String neuronName, String archive) {
        this.cellTypes = cellTypes;
        this.brainRegion = brainRegion;
        this.species = species;
        this.neuronNames = new ArrayList<>(Arrays.asList(neuronName));
        this.archives = new HashSet<>(Arrays.asList(archive));
        this.noOfNeurons = 1;
    }

    public static UniqueMetadata addNeuronMetadata(NeuronMetadataR neuronMetadataR) {
        UniqueMetadata oldUniqueMetadata;
        UniqueMetadata newUniqueMetadata;
        // create new UniqueMetadata object
        newUniqueMetadata = new UniqueMetadata(
                (neuronMetadataR.getCellType() != null) ? new HashSet<>(neuronMetadataR.getCellType()): new HashSet<>(),
                (neuronMetadataR.getBrainRegion() != null) ? new HashSet<>(neuronMetadataR.getBrainRegion()): new HashSet<>(),
                neuronMetadataR.getSpecies(),
                neuronMetadataR.getNeuronName(),
                neuronMetadataR.getArchive());
        // check if uniqueMetadata already exists
        if (uniqueMetadataMap.containsKey(newUniqueMetadata)) {
            oldUniqueMetadata = uniqueMetadataMap.get(newUniqueMetadata);
            oldUniqueMetadata.neuronNames.add(neuronMetadataR.getNeuronName());
            oldUniqueMetadata.archives.add(neuronMetadataR.getArchive());
            oldUniqueMetadata.noOfNeurons += 1;
            return oldUniqueMetadata;
        } else {
            uniqueMetadataMap.put(newUniqueMetadata, newUniqueMetadata);
            count += 1;
            newUniqueMetadata.uniqueMetadataId = count;
            return newUniqueMetadata;
        }
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


    public static Map<UniqueMetadata, UniqueMetadata> getUniqueMetadataMap() {
        return uniqueMetadataMap;
    }
}
