package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

public class NeuronMetadataRO implements NeuronMetadata{

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
    private Object reportedValue;
    @JsonProperty("reported_xy")
    private Object reportedXy;
    @JsonProperty("reported_z")
    private Object reportedZ;
    @JsonProperty("corrected_value")
    private Object correctedValue;
    @JsonProperty("corrected_xy")
    private Object correctedXy;
    @JsonProperty("corrected_z")
    private Object correctedZ;
    @JsonProperty("soma_surface")
    private String somaSurface;
    @JsonProperty("surface")
    private String surface;
    @JsonProperty("volume")
    private String volume;
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
        if (!(o instanceof NeuronMetadataRO)) return false;
        NeuronMetadataRO that = (NeuronMetadataRO) o;
        return Objects.equals(neuronName, that.neuronName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neuronName);
    }

    public Integer getNeuronId() {
        return neuronId;
    }

    public String getNeuronName() {
        return neuronName;
    }

    public String getArchive() {
        return archive;
    }

    public String getNote() {
        return note;
    }

    public String getAgeScale() {
        return ageScale;
    }

    public String getGender() {
        return gender;
    }

    public String getAgeClassification() {
        return ageClassification;
    }

    public List<String> getBrainRegion() {
        return brainRegion;
    }

    public List<String> getCellType() {
        return cellType;
    }

    public String getSpecies() {
        return species;
    }

    public String getStrain() {
        return strain;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getStain() {
        return stain;
    }

    public List<String> getExperimentCondition() {
        return experimentCondition;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSlicingDirection() {
        return slicingDirection;
    }

    public String getReconstructionSoftware() {
        return reconstructionSoftware;
    }

    public String getObjectiveType() {
        return objectiveType;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public String getDomain() {
        return domain;
    }

    public String getAttributes() {
        return attributes;
    }

    public String getMagnification() {
        return magnification;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public String getDepositionDate() {
        return depositionDate;
    }

    public String getShrinkageReported() {
        return shrinkageReported;
    }

    public String getShrinkageCorrected() {
        return shrinkageCorrected;
    }

    public Object getReportedValue() {
        return reportedValue;
    }

    public Object getReportedXy() {
        return reportedXy;
    }

    public Object getReportedZ() {
        return reportedZ;
    }

    public Object getCorrectedValue() {
        return correctedValue;
    }

    public Object getCorrectedXy() {
        return correctedXy;
    }

    public Object getCorrectedZ() {
        return correctedZ;
    }

    public String getSomaSurface() {
        return somaSurface;
    }

    public String getSurface() {
        return surface;
    }

    public String getVolume() {
        return volume;
    }

    public String getSlicingThickness() {
        return slicingThickness;
    }

    public String getMinAge() {
        return minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public String getMinWeight() {
        return minWeight;
    }

    public String getMaxWeight() {
        return maxWeight;
    }

    public String getPngUrl() {
        return pngUrl;
    }

    public List<String> getReferencePmid() {
        return referencePmid;
    }

    public List<String> getReferenceDoi() {
        return referenceDoi;
    }

    public String getPhysicalIntegrity() {
        return physicalIntegrity;
    }

    public Links getLinks() {
        return links;
    }
}