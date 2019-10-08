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