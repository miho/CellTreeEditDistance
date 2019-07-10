package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.List;

/**
 * Interface for read and write neuronMetadata
 */
public interface NeuronMetadataW extends NeuronMetadataR {

    void setNeuronId(Integer neuronId);
    void setNeuronName(String neuronName);
    void setArchive(String archive);
    void setNote(String note);
    void setAgeScale(String ageScale);
    void setGender(String gender);
    void setAgeClassification(String ageClassification);
    void setBrainRegion(List<String> brainRegion);
    void setCellType(List<String> cellType);
    void setSpecies(String species);
    void setStrain(String strain);
    void setScientificName(String scientificName);
    void setStain(String stain);
    void setExperimentCondition(List<String> experimentCondition);
    void setProtocol(String protocol);
    void setSlicingDirection(String slicingDirection);
    void setReconstructionSoftware(String reconstructionSoftware);
    void setObjectiveType(String objectiveType);
    void setOriginalFormat(String originalFormat);
    void setDomain(String domain);
    void setAttributes(String attributes);
    void setMagnification(String magnification);
    void setUploadDate(String uploadDate);
    void setDepositionDate(String depositionDate);
    void setShrinkageReported(String shrinkageReported);
    void setShrinkageCorrected(String shrinkageCorrected);
    void setReportedValue(Float reportedValue);
    void setReportedXy(Float reportedXy);
    void setReportedZ(Float reportedZ);
    void setCorrectedValue(Float correctedValue);
    void setCorrectedXy(Float correctedXy);
    void setCorrectedZ(Float correctedZ);
    void setSomaSurface(Float somaSurface);
    void setSurface(Float surface);
    void setVolume(Float volume);
    void setSlicingThickness(String sclicingThickness);
    void setMinAge(String minAge);
    void setMaxAge(String maxAge);
    void setMinWeight(String minWeight);
    void setMaxWeight(String maxWeight);
    void setPngUrl(String pngUrl);
    void setReferencePmid(List<String> referencePmid);
    void setReferenceDoi(List<String> referenceDoi);
    void setPhysicalIntegrity(String physicalIntegrity);
    void setLinks(Links links);



}
