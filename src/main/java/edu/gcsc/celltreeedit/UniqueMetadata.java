package edu.gcsc.celltreeedit;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;

import java.util.*;


/**
 * Class to get unique metadata combinations. metadata is considered unique if cellTypes, brainRegion and species are equal
 * equals()-method implements this constraint
 * compareTo()-method is not consistent with equals() as it sorts by number of Neurons having this unique metadata combination
 * */
public class UniqueMetadata implements Comparable {

    public static Map<UniqueMetadata, UniqueMetadata> uniqueMetadataMap = new HashMap<>();

    private Set<String> cellTypes;
    private Set<String> brainRegion;
    private String species;

    private List<String> neuronNames;
    private Set<String> archives;
    private int noOfNeurons;

    public UniqueMetadata(Set<String> cellTypes, Set<String> brainRegion, String species, String neuronName, String archive) {
        this.cellTypes = cellTypes;
        this.brainRegion = brainRegion;
        this.species = species;
        this.neuronNames = new ArrayList<>(Arrays.asList(neuronName));
        this.archives = new HashSet<>(Arrays.asList(archive));
        this.noOfNeurons = 1;
    }

    public static void addNeuronMetadata(NeuronMetadataR neuronMetadataR) {
        UniqueMetadata oldUniqueMetadata;
        UniqueMetadata newUniqueMetadata;
        newUniqueMetadata = new UniqueMetadata(
                (neuronMetadataR.getCellType() != null) ? new HashSet<>(neuronMetadataR.getCellType()): new HashSet<>(),
                (neuronMetadataR.getBrainRegion() != null) ? new HashSet<>(neuronMetadataR.getBrainRegion()): new HashSet<>(),
                neuronMetadataR.getSpecies(),
                neuronMetadataR.getNeuronName(),
                neuronMetadataR.getArchive());
        if (uniqueMetadataMap.containsKey(newUniqueMetadata)) {
            oldUniqueMetadata = uniqueMetadataMap.get(newUniqueMetadata);
            oldUniqueMetadata.neuronNames.add(neuronMetadataR.getNeuronName());
            oldUniqueMetadata.archives.add(neuronMetadataR.getArchive());
            oldUniqueMetadata.noOfNeurons += 1;
        } else {
            uniqueMetadataMap.put(newUniqueMetadata, newUniqueMetadata);
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

    public void setCellTypes(Set<String> cellTypes) {
        this.cellTypes = cellTypes;
    }

    public Set<String> getBrainRegion() {
        return brainRegion;
    }

    public void setBrainRegion(Set<String> brainRegion) {
        this.brainRegion = brainRegion;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public List<String> getNeuronNames() {
        return neuronNames;
    }

    public void setNeuronNames(List<String> neuronNames) {
        this.neuronNames = neuronNames;
    }

    public int getNoOfNeurons() {
        return noOfNeurons;
    }

    public void setNoOfNeurons(int noOfNeurons) {
        this.noOfNeurons = noOfNeurons;
    }

    public Set<String> getArchives() {
        return archives;
    }

    public void setArchives(Set<String> archives) {
        this.archives = archives;
    }

    public static Map<UniqueMetadata, UniqueMetadata> getUniqueMetadataMap() {
        return uniqueMetadataMap;
    }
}
