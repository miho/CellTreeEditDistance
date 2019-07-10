package edu.gcsc.celltreeedit.NeuronMetadata;

import java.util.List;

/**
 * Interface for read-only neuronMetadata
 */
public interface NeuronMetadataR {

    Integer getNeuronId();
    String getNeuronName();
    String getArchive();
    String getNote();
    String getAgeScale();
    String getGender();
    String getAgeClassification();
    List<String> getBrainRegion();
    List<String> getCellType();
    String getSpecies();
    String getStrain();
    String getScientificName();
    String getStain();
    List<String> getExperimentCondition();
    String getProtocol();
    String getSlicingDirection();
    String getReconstructionSoftware();
    String getObjectiveType();
    String getOriginalFormat();
    String getDomain();
    String getAttributes();
    String getMagnification();
    String getUploadDate();
    String getDepositionDate();
    String getShrinkageReported();
    String getShrinkageCorrected();
    Float getReportedValue();
    Float getReportedXy();
    Float getReportedZ();
    Float getCorrectedValue();
    Float getCorrectedXy();
    Float getCorrectedZ();
    Float getSomaSurface();
    Float getSurface();
    Float getVolume();
    String getSlicingThickness();
    String getMinAge();
    String getMaxAge();
    String getMinWeight();
    String getMaxWeight();
    String getPngUrl();
    List<String> getReferencePmid();
    List<String> getReferenceDoi();
    String getPhysicalIntegrity();
    Links getLinks();

}