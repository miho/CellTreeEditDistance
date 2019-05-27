package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.List;
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

public class NeuronMetadata {

    public Integer neuronId;
    @JsonProperty("neuron_name")
    public String neuronName;
    @JsonProperty("archive")
    public String archive;
    @JsonProperty("note")
    public String note;
    @JsonProperty("age_scale")
    public String ageScale;
    @JsonProperty("gender")
    public String gender;
    @JsonProperty("age_classification")
    public String ageClassification;
    @JsonProperty("brain_region")
    public List<String> brainRegion = null;
    @JsonProperty("cell_type")
    public List<String> cellType = null;
    @JsonProperty("species")
    public String species;
    @JsonProperty("strain")
    public String strain;
    @JsonProperty("scientific_name")
    public String scientificName;
    @JsonProperty("stain")
    public String stain;
    @JsonProperty("experiment_condition")
    public List<String> experimentCondition = null;
    @JsonProperty("protocol")
    public String protocol;
    @JsonProperty("slicing_direction")
    public String slicingDirection;
    @JsonProperty("reconstruction_software")
    public String reconstructionSoftware;
    @JsonProperty("objective_type")
    public String objectiveType;
    @JsonProperty("original_format")
    public String originalFormat;
    @JsonProperty("domain")
    public String domain;
    @JsonProperty("attributes")
    public String attributes;
    @JsonProperty("magnification")
    public String magnification;
    @JsonProperty("upload_date")
    public String uploadDate;
    @JsonProperty("deposition_date")
    public String depositionDate;
    @JsonProperty("shrinkage_reported")
    public String shrinkageReported;
    @JsonProperty("shrinkage_corrected")
    public String shrinkageCorrected;
    @JsonProperty("reported_value")
    public Object reportedValue;
    @JsonProperty("reported_xy")
    public Object reportedXy;
    @JsonProperty("reported_z")
    public Object reportedZ;
    @JsonProperty("corrected_value")
    public Object correctedValue;
    @JsonProperty("corrected_xy")
    public Object correctedXy;
    @JsonProperty("corrected_z")
    public Object correctedZ;
    @JsonProperty("soma_surface")
    public String somaSurface;
    @JsonProperty("surface")
    public String surface;
    @JsonProperty("volume")
    public String volume;
    @JsonProperty("slicing_thickness")
    public String slicingThickness;
    @JsonProperty("min_age")
    public String minAge;
    @JsonProperty("max_age")
    public String maxAge;
    @JsonProperty("min_weight")
    public String minWeight;
    @JsonProperty("max_weight")
    public String maxWeight;
    @JsonProperty("png_url")
    public String pngUrl;
    @JsonProperty("reference_pmid")
    public List<String> referencePmid = null;
    @JsonProperty("reference_doi")
    public List<String> referenceDoi = null;
    @JsonProperty("physical_Integrity")
    public String physicalIntegrity;
    @JsonProperty("_links")
    public Links links;

}
