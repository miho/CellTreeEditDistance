package edu.gcsc.celltreeedit.Lucene;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DocExport {

    void exportNamesToJson(IndexSearcher indexSearcher, TopDocs topDocs, File outputDirectory) {
        NeuronNames neuronNames = new NeuronNames();
        List<String> neuronNamesList = neuronNames.getNeuronNames();
        try {
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                neuronNamesList.add(indexSearcher.doc(scoreDoc.doc).getField("neuronName").stringValue());
            }
            this.writeJSON(neuronNames, outputDirectory);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeJSON(NeuronNames neuronNames, File outputDirectory) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outputDirectory.getPath() + "/swcFiles.json"), neuronNames);
    }
}
