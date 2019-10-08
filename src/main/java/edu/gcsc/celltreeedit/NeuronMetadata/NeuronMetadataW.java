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
