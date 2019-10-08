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
package edu.gcsc.celltreeedit.Lucene;

import edu.gcsc.celltreeedit.NeuronMetadata.NeuronMetadataR;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class used for writing the lucene index. the index is needed to enable lucene to query neuronMetadata fast.
 */
public class LuceneIndexWriter {

    private File indexDirectory;
    private IndexWriter indexWriter = null;

    public LuceneIndexWriter(File indexDirectory) {
        this.indexDirectory = indexDirectory;
    }

    public void createIndex(Map<String, NeuronMetadataR> neuronMetadataRMap) {
        this.openIndex();
        this.addDocuments(neuronMetadataRMap);
        this.finish();
    }

    /**
     * opens Lucene index in FileSystem. also defines IndexWriter (how lucene parses data)
     * @return
     */
    private void openIndex() {
        try {
            Directory dir = FSDirectory.open(indexDirectory.toPath());
            Analyzer analyzer = new CaseInsensitiveKeywordAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            //Always overwrite the directory
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);
        } catch (Exception e) {
            System.err.println("Error opening the index. " + e.getMessage());
        }
    }

    /**
     * Add documents to the index. one document per neuron.
     */
    private void addDocuments(Map<String, NeuronMetadataR> neuronMetadataRMap) {
        NeuronMetadataR neuronMetadataR;
        for (String neuronMetadataKey: neuronMetadataRMap.keySet()) {
            Document doc = new Document();
            neuronMetadataR = neuronMetadataRMap.get(neuronMetadataKey);
            this.addDocumentsForNeuronMetadataObject(doc, neuronMetadataR);
            try {
                indexWriter.addDocument(doc);
            } catch (IOException ex) {
                System.err.println("Error adding documents to the index. " + ex.getMessage());
            }
        }
    }

    private void addStringToDoc(Document doc, String stringFieldName, String value) {
        if (value == null) {
            doc.add(new TextField(stringFieldName, "", Field.Store.YES));
        } else {
            doc.add(new TextField(stringFieldName, value.toLowerCase(), Field.Store.YES));
        }
    }

    private void addFloatToDoc(Document doc, String stringFieldName, Float value) {
        if (value == null) {
            doc.add(new TextField(stringFieldName, "", Field.Store.YES));
        } else {
            doc.add(new TextField(stringFieldName, value.toString(), Field.Store.YES));
        }
    }

    private void addListToDoc(Document doc, String stringFieldName, List<String> list) {
        if (list == null) {
            doc.add(new TextField(stringFieldName, "", Field.Store.YES));
            return;
        }
        for (String entry: list) {
            doc.add(new TextField(stringFieldName, entry.toLowerCase(), Field.Store.YES));
        }
    }

    /**
     * adds neuronMetadata of one neuron to one document
     * @param doc
     * @param neuronMetadataR
     */
    private void addDocumentsForNeuronMetadataObject(Document doc, NeuronMetadataR neuronMetadataR) {
        // not allowed to be null values
        doc.add(new TextField("neuronId", neuronMetadataR.getNeuronId().toString(), Field.Store.YES));
        doc.add(new TextField("neuronName", neuronMetadataR.getNeuronName(), Field.Store.YES));
        doc.add(new TextField("archive", neuronMetadataR.getArchive(), Field.Store.YES));

        this.addStringToDoc(doc, "note", neuronMetadataR.getNote());
        this.addStringToDoc(doc, "ageScale", neuronMetadataR.getAgeScale());
        this.addStringToDoc(doc, "gender", neuronMetadataR.getGender());
        this.addStringToDoc(doc, "ageClassification", neuronMetadataR.getAgeClassification());

        this.addListToDoc(doc, "brainRegion", neuronMetadataR.getBrainRegion());
        this.addListToDoc(doc, "cellType", neuronMetadataR.getCellType());

        this.addStringToDoc(doc, "species", neuronMetadataR.getSpecies());
        this.addStringToDoc(doc, "strain", neuronMetadataR.getStrain());
        this.addStringToDoc(doc, "scientificName", neuronMetadataR.getScientificName());
        this.addStringToDoc(doc, "stain", neuronMetadataR.getStain());

        this.addListToDoc(doc, "experimentCondition", neuronMetadataR.getExperimentCondition());

        this.addStringToDoc(doc, "protocol", neuronMetadataR.getProtocol());
        this.addStringToDoc(doc, "slicingDirection", neuronMetadataR.getSlicingDirection());
        this.addStringToDoc(doc, "reconstructionSoftware", neuronMetadataR.getReconstructionSoftware());
        this.addStringToDoc(doc, "objectiveType", neuronMetadataR.getObjectiveType());
        this.addStringToDoc(doc, "originalFormat", neuronMetadataR.getOriginalFormat());
        this.addStringToDoc(doc, "domain", neuronMetadataR.getDomain());
        this.addStringToDoc(doc, "attributes", neuronMetadataR.getAttributes());
        this.addStringToDoc(doc, "magnification", neuronMetadataR.getMagnification());
        this.addStringToDoc(doc, "uploadDate", neuronMetadataR.getUploadDate());
        this.addStringToDoc(doc, "depositionDate", neuronMetadataR.getDepositionDate());
        this.addStringToDoc(doc, "shrinkageReported", neuronMetadataR.getShrinkageReported());
        this.addStringToDoc(doc, "shrinkageCorrected", neuronMetadataR.getShrinkageCorrected());

        this.addFloatToDoc(doc, "reportedValue", neuronMetadataR.getReportedValue());
        this.addFloatToDoc(doc, "reportedXy", neuronMetadataR.getReportedXy());
        this.addFloatToDoc(doc, "reportedZ", neuronMetadataR.getReportedZ());
        this.addFloatToDoc(doc, "correctedValue", neuronMetadataR.getCorrectedValue());
        this.addFloatToDoc(doc, "correctedXy", neuronMetadataR.getCorrectedXy());
        this.addFloatToDoc(doc, "correctedZ", neuronMetadataR.getCorrectedZ());
        this.addFloatToDoc(doc, "somaSurface", neuronMetadataR.getSomaSurface());
        this.addFloatToDoc(doc, "surface", neuronMetadataR.getSurface());
        this.addFloatToDoc(doc, "volume", neuronMetadataR.getVolume());

        this.addStringToDoc(doc, "slicingThickness", neuronMetadataR.getSlicingThickness());
        this.addStringToDoc(doc, "minAge", neuronMetadataR.getMinAge());
        this.addStringToDoc(doc, "maxAge", neuronMetadataR.getMaxAge());
        this.addStringToDoc(doc, "minWeight", neuronMetadataR.getMinWeight());
        this.addStringToDoc(doc, "maxWeight", neuronMetadataR.getMaxWeight());
        this.addStringToDoc(doc, "pngUrl", neuronMetadataR.getPngUrl());

        this.addListToDoc(doc, "referencePmid", neuronMetadataR.getReferencePmid());
        this.addListToDoc(doc, "referenceDoi", neuronMetadataR.getReferenceDoi());

        this.addStringToDoc(doc, "physicalIntegrity", neuronMetadataR.getPhysicalIntegrity());

        // TODO: implement Links
    }

    /**
     * Write the document to the index and close it
     */
    private void finish() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }
}
