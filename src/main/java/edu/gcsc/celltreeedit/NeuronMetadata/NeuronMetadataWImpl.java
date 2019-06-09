package edu.gcsc.celltreeedit.NeuronMetadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({
        "neuron_id",
        "neuron_name",
        "archive",
        "note",
        "age_scale",
        "gender",
        "age_classification",
        "brain_region",
        "cell_type",
        "species",
        "strain",
        "scientific_name",
        "stain",
        "experiment_condition",
        "protocol",
        "slicing_direction",
        "reconstruction_software",
        "objective_type",
        "original_format",
        "domain",
        "attributes",
        "magnification",
        "upload_date",
        "deposition_date",
        "shrinkage_reported",
        "shrinkage_corrected",
        "reported_value",
        "reported_xy",
        "reported_z",
        "corrected_value",
        "corrected_xy",
        "corrected_z",
        "soma_surface",
        "surface",
        "volume",
        "slicing_thickness",
        "min_age",
        "max_age",
        "min_weight",
        "max_weight",
        "png_url",
        "reference_pmid",
        "reference_doi",
        "physical_Integrity",
        "_links"
})

public class NeuronMetadataWImpl implements NeuronMetadataW {

    @JsonProperty("neuron_id")
    private Integer neuronId;
    @JsonProperty("neuron_name")
    private String neuronName;
    @JsonProperty("archive")
    private String archive;
    @JsonProperty("note")
    private String note;
    @JsonProperty("age_scale")
    private String ageScale;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("age_classification")
    private String ageClassification;
    @JsonProperty("brain_region")
    private List<String> brainRegion = null;
    @JsonProperty("cell_type")
    private List<String> cellType = null;
    @JsonProperty("species")
    private String species;
    @JsonProperty("strain")
    private String strain;
    @JsonProperty("scientific_name")
    private String scientificName;
    @JsonProperty("stain")
    private String stain;
    @JsonProperty("experiment_condition")
    private List<String> experimentCondition = null;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("slicing_direction")
    private String slicingDirection;
    @JsonProperty("reconstruction_software")
    private String reconstructionSoftware;
    @JsonProperty("objective_type")
    private String objectiveType;
    @JsonProperty("original_format")
    private String originalFormat;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("attributes")
    private String attributes;
    @JsonProperty("magnification")
    private String magnification;
    @JsonProperty("upload_date")
    private String uploadDate;
    @JsonProperty("deposition_date")
    private String depositionDate;
    @JsonProperty("shrinkage_reported")
    private String shrinkageReported;
    @JsonProperty("shrinkage_corrected")
    private String shrinkageCorrected;
    @JsonProperty("reported_value")
    private Float reportedValue;
    @JsonProperty("reported_xy")
    private Float reportedXy;
    @JsonProperty("reported_z")
    private Float reportedZ;
    @JsonProperty("corrected_value")
    private Float correctedValue;
    @JsonProperty("corrected_xy")
    private Float correctedXy;
    @JsonProperty("corrected_z")
    private Float correctedZ;
    @JsonProperty("soma_surface")
    private Float somaSurface;
    @JsonProperty("surface")
    private Float surface;
    @JsonProperty("volume")
    private Float volume;
    @JsonProperty("slicing_thickness")
    private String slicingThickness;
    @JsonProperty("min_age")
    private String minAge;
    @JsonProperty("max_age")
    private String maxAge;
    @JsonProperty("min_weight")
    private String minWeight;
    @JsonProperty("max_weight")
    private String maxWeight;
    @JsonProperty("png_url")
    private String pngUrl;
    @JsonProperty("reference_pmid")
    private List<String> referencePmid = null;
    @JsonProperty("reference_doi")
    private List<String> referenceDoi = null;
    @JsonProperty("physical_Integrity")
    private String physicalIntegrity;
    @JsonProperty("_links")
    private Links links;

    // if subclasses should not be equal -> replace instanceof by getClass() Comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NeuronMetadataW)) return false;
        NeuronMetadataWImpl that = (NeuronMetadataWImpl) o;
        return Objects.equals(neuronName, that.neuronName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neuronName);
    }

    @Override
    public Integer getNeuronId() {
        return neuronId;
    }

    public void setNeuronId(Integer neuronId) {
        this.neuronId = neuronId;
    }

    @Override
    public String getNeuronName() {
        return neuronName;
    }

    @Override
    public void setNeuronName(String neuronName) {
        this.neuronName = neuronName;
    }

    @Override
    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    @Override
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getAgeScale() {
        return ageScale;
    }

    public void setAgeScale(String ageScale) {
        this.ageScale = ageScale;
    }

    @Override
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String getAgeClassification() {
        return ageClassification;
    }

    public void setAgeClassification(String ageClassification) {
        this.ageClassification = ageClassification;
    }

    @Override
    public List<String> getBrainRegion() {
        return brainRegion;
    }

    @Override
    public void setBrainRegion(List<String> brainRegion) {
        this.brainRegion = brainRegion;
    }

    @Override
    public List<String> getCellType() {
        return cellType;
    }

    public void setCellType(List<String> cellType) {
        this.cellType = cellType;
    }

    @Override
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Override
    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    @Override
    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    @Override
    public String getStain() {
        return stain;
    }

    public void setStain(String stain) {
        this.stain = stain;
    }

    @Override
    public List<String> getExperimentCondition() {
        return experimentCondition;
    }

    public void setExperimentCondition(List<String> experimentCondition) {
        this.experimentCondition = experimentCondition;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getSlicingDirection() {
        return slicingDirection;
    }

    public void setSlicingDirection(String slicingDirection) {
        this.slicingDirection = slicingDirection;
    }

    @Override
    public String getReconstructionSoftware() {
        return reconstructionSoftware;
    }

    public void setReconstructionSoftware(String reconstructionSoftware) {
        this.reconstructionSoftware = reconstructionSoftware;
    }

    @Override
    public String getObjectiveType() {
        return objectiveType;
    }

    public void setObjectiveType(String objectiveType) {
        this.objectiveType = objectiveType;
    }

    @Override
    public String getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getMagnification() {
        return magnification;
    }

    public void setMagnification(String magnification) {
        this.magnification = magnification;
    }

    @Override
    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String getDepositionDate() {
        return depositionDate;
    }

    public void setDepositionDate(String depositionDate) {
        this.depositionDate = depositionDate;
    }

    @Override
    public String getShrinkageReported() {
        return shrinkageReported;
    }

    public void setShrinkageReported(String shrinkageReported) {
        this.shrinkageReported = shrinkageReported;
    }

    @Override
    public String getShrinkageCorrected() {
        return shrinkageCorrected;
    }

    public void setShrinkageCorrected(String shrinkageCorrected) {
        this.shrinkageCorrected = shrinkageCorrected;
    }

    @Override
    public Float getReportedValue() {
        return reportedValue;
    }

    public void setReportedValue(Float reportedValue) {
        this.reportedValue = reportedValue;
    }

    @Override
    public Float getReportedXy() {
        return reportedXy;
    }

    public void setReportedXy(Float reportedXy) {
        this.reportedXy = reportedXy;
    }

    @Override
    public Float getReportedZ() {
        return reportedZ;
    }

    public void setReportedZ(Float reportedZ) {
        this.reportedZ = reportedZ;
    }

    @Override
    public Float getCorrectedValue() {
        return correctedValue;
    }

    public void setCorrectedValue(Float correctedValue) {
        this.correctedValue = correctedValue;
    }

    @Override
    public Float getCorrectedXy() {
        return correctedXy;
    }

    public void setCorrectedXy(Float correctedXy) {
        this.correctedXy = correctedXy;
    }

    @Override
    public Float getCorrectedZ() {
        return correctedZ;
    }

    public void setCorrectedZ(Float correctedZ) {
        this.correctedZ = correctedZ;
    }

    @Override
    public Float getSomaSurface() {
        return somaSurface;
    }

    public void setSomaSurface(Float somaSurface) {
        this.somaSurface = somaSurface;
    }

    @Override
    public Float getSurface() {
        return surface;
    }

    public void setSurface(Float surface) {
        this.surface = surface;
    }

    @Override
    public Float getVolume() {
        return volume;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    @Override
    public String getSlicingThickness() {
        return slicingThickness;
    }

    public void setSlicingThickness(String slicingThickness) {
        this.slicingThickness = slicingThickness;
    }

    @Override
    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    @Override
    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public String getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(String minWeight) {
        this.minWeight = minWeight;
    }

    @Override
    public String getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(String maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public String getPngUrl() {
        return pngUrl;
    }

    public void setPngUrl(String pngUrl) {
        this.pngUrl = pngUrl;
    }

    @Override
    public List<String> getReferencePmid() {
        return referencePmid;
    }

    public void setReferencePmid(List<String> referencePmid) {
        this.referencePmid = referencePmid;
    }

    @Override
    public List<String> getReferenceDoi() {
        return referenceDoi;
    }

    public void setReferenceDoi(List<String> referenceDoi) {
        this.referenceDoi = referenceDoi;
    }

    @Override
    public String getPhysicalIntegrity() {
        return physicalIntegrity;
    }

    public void setPhysicalIntegrity(String physicalIntegrity) {
        this.physicalIntegrity = physicalIntegrity;
    }

    @Override
    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
